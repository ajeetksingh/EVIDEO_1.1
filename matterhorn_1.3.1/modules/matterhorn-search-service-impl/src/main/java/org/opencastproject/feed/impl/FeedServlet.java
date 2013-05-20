/**
 *  Copyright 2009, 2010 The Regents of the University of California
 *  Licensed under the Educational Community License, Version 2.0
 *  (the "License"); you may not use this file except in compliance
 *  with the License. You may obtain a copy of the License at
 *
 *  http://www.osedu.org/licenses/ECL-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an "AS IS"
 *  BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *  or implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 */

package org.opencastproject.feed.impl;

import org.opencastproject.feed.api.Feed;
import org.opencastproject.feed.api.FeedGenerator;

import com.sun.syndication.io.SyndFeedOutput;

import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class is responsible of creating RSS and Atom feeds.
 * <p>
 * The implementation relies on the request uri containing information about the requested feed type and the query used
 * to construct the feed contents.
 * </p>
 * <p>
 * Therefore, assuming that this servlet has been mounted to <code>/feeds/*</code>, a correct uri for this servlet looks
 * like this: <code>/feeds/&lt;feed type&gt;/&lt;version&gt;/&lt;query&gt;</code>, e. g.
 * 
 * <pre>
 *     http://localhost/feeds/Atom/0.3/favorites
 * </pre>
 * 
 * which would indicate a requeste to an atom 0.3 feed with <tt>favourites</tt> being the query.
 */
public class FeedServlet extends HttpServlet {

  /** The serial version uid */
  private static final long serialVersionUID = -4623160106007127801L;

  /** Logging facility */
  private static Logger logger = LoggerFactory.getLogger(FeedServlet.class);

  /** List of feed generators */
  private List<FeedGenerator> feeds = new ArrayList<FeedGenerator>();

  /**
   * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse)
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    ClassLoader originalContextClassLoader = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader(FeedServlet.class.getClassLoader());
      doGetWithBundleClassloader(request, response);
    } finally {
      Thread.currentThread().setContextClassLoader(originalContextClassLoader);
    }
  }

  /**
   * Handles HTTP GET requests after the context classloader has been set.
   * 
   * See https://issues.apache.org/jira/browse/SMX4-510 for details
   * 
   * @param request
   *          the http request
   * @param response
   *          the http response
   * @throws ServletException
   *           if there is a problem handling the http exchange
   * @throws IOException
   *           if there is an exception reading or writing from / to the request and response streams
   */
  protected void doGetWithBundleClassloader(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

    FeedInfo feedInfo = null;

    // Try to extract requested feed type and content
    try {
      feedInfo = extractFeedInfo(request);
    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
      return;
    }

    // Set the content type
    if (feedInfo.getType().equals(Feed.Type.Atom))
      response.setContentType("application/atom+xml");
    else if (feedInfo.getType().equals(Feed.Type.RSS))
      response.setContentType("application/rss+xml");

    // Have a feed generator create the requested feed
    Feed feed = null;
    for (FeedGenerator generator : feeds) {
      if (generator.accept(feedInfo.getQuery())) {
        feed = generator.createFeed(feedInfo.getType(), feedInfo.getQuery());
        if (feed == null) {
          response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
          return;
        }
        break;
      }
    }

    // Have we found a feed generator?
    if (feed == null) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    // Set character encoding
    response.setCharacterEncoding(feed.getEncoding());

    // Write back feed using Romeo
    SyndFeedOutput output = new SyndFeedOutput();
    Writer responseWriter = response.getWriter();
    try {
      output.output(new RomeFeed(feed, feedInfo), responseWriter);
    } catch (Exception e) {
      logger.error("Error serializing feed", e);
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  /**
   * Returns information about the requested feed by extracting all relevant pieces from the servlet request's uri.
   * <p>
   * This method throws an {@link IllegalStateException} if the information cannot be extracted from the uri.
   * </p>
   * 
   * @param request
   *          the servlet request
   * @return the requested feed
   * @throws IllegalStateException
   *           if the uri does not contain sufficient information about the request
   */
  private FeedInfo extractFeedInfo(HttpServletRequest request) throws IllegalStateException {
    String path = request.getPathInfo();
    if (path.startsWith("/"))
      path = path.substring(1);
    String[] pathElements = path.split("/");

    if (pathElements.length < 3)
      throw new IllegalStateException("Cannot extract requested feed parameters.");
    Feed.Type type = null;
    try {
      type = Feed.Type.parseString(pathElements[0]);
    } catch (Exception e) {
      throw new IllegalStateException("Cannot extract requested feed type.");
    }
    float version = 0;
    try {
      version = Float.parseFloat(pathElements[1]);
    } catch (NumberFormatException e) {
      throw new IllegalStateException("Cannot extract requested feed version.");
    }
    int queryLength = pathElements.length - 2;
    String[] query = new String[queryLength];
    for (int i = 0; i < queryLength; i++)
      query[i] = pathElements[i + 2];
    return new FeedInfo(type, version, query);
  }

  /**
   * Sets the http service.
   * 
   * @param httpService
   *          the http service
   */
  public void setHttpService(HttpService httpService) {
    try {
      HttpContext httpContext = httpService.createDefaultHttpContext();
      httpService.registerServlet("/feeds", FeedServlet.this, null, httpContext);
      logger.debug("Feed servlet registered");
    } catch (ServletException e) {
      e.printStackTrace();
    } catch (NamespaceException e) {
      e.printStackTrace();
    }
  }

  /**
   * Adds the feed generator to the list of generators.
   * 
   * @param generator
   *          the generator
   */
  public void addFeedGenerator(FeedGenerator generator) {
    logger.info("Registering '{}' feed", generator.getIdentifier());
    feeds.add(generator);
  }

  /**
   * Removes the generator from the list of feed generators.
   * 
   * @param generator
   *          the feed generator
   */
  public void removeFeedGenerator(FeedGenerator generator) {
    logger.info("Removing '{}' feed", generator.getIdentifier());
    feeds.remove(generator);
  }

}
