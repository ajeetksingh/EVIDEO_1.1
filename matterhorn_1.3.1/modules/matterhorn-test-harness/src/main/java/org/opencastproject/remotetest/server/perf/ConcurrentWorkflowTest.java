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
package org.opencastproject.remotetest.server.perf;

import static org.opencastproject.remotetest.Main.BASE_URL;

import org.opencastproject.remotetest.Main;
import org.opencastproject.remotetest.util.TrustedHttpClient;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

/**
 * Tests the functionality of a remote workflow service rest endpoint
 */
public class ConcurrentWorkflowTest {
  @Rule
  public ContiPerfRule i = new ContiPerfRule();

  TrustedHttpClient client;

  protected static Set<File> tempFiles = new HashSet<File>();

  @Before
  public void setUp() throws Exception {
    client = Main.getClient();
  }

  @After
  public void tearDown() throws Exception {
    Main.returnClient(client);
  }

  @Test
  @PerfTest(invocations = 100, threads = 100)
  public void testStartAndRetrieveWorkflowInstance() throws Exception {
    // Start a workflow instance via the rest endpoint
    HttpPost postStart = new HttpPost(BASE_URL + "/workflow/start");
    List<NameValuePair> formParams = new ArrayList<NameValuePair>();

    formParams.add(new BasicNameValuePair("definition", getSampleWorkflowDefinition()));
    formParams.add(new BasicNameValuePair("mediapackage", getSampleMediaPackage()));
    formParams.add(new BasicNameValuePair("properties", "this=that"));
    postStart.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));

    // Grab the new workflow instance from the response
    String postResponse = EntityUtils.toString(client.execute(postStart).getEntity());
    String id = getWorkflowInstanceId(postResponse);

    // Ensure we can retrieve the workflow instance from the rest endpoint
    HttpGet getWorkflowMethod = new HttpGet(BASE_URL + "/workflow/instance/" + id + ".xml");
    String getResponse = EntityUtils.toString(client.execute(getWorkflowMethod).getEntity());
    Assert.assertEquals(id, getWorkflowInstanceId(getResponse));

    // Make sure we can retrieve it via json, too
    HttpGet getWorkflowJson = new HttpGet(BASE_URL + "/workflow/instance/" + id + ".json");
    String jsonResponse = EntityUtils.toString(client.execute(getWorkflowJson).getEntity());
    JSONObject json = (JSONObject) JSONValue.parse(jsonResponse);
    if (json == null)
      Assert.fail("JSON response should not be null, but is " + jsonResponse);
    Assert.assertEquals(id, json.get("workflow_id"));

    // Ensure that the workflow finishes successfully
    int attempts = 0;
    while (true) {
      if (++attempts == 1000)
        Assert.fail("workflow rest endpoint test has hung");
      getWorkflowMethod = new HttpGet(BASE_URL + "/workflow/instance/" + id + ".xml");
      getResponse = EntityUtils.toString(client.execute(getWorkflowMethod).getEntity());
      String state = getWorkflowInstanceStatus(getResponse);
      if ("FAILED".equals(state))
        Assert.fail("workflow instance " + id + " failed");
      if ("SUCCEEDED".equals(state))
        break;
      System.out.println("workflow " + id + " is " + state);
      Thread.sleep(5000);
    }
  }

  protected String getWorkflowInstanceId(String xml) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(IOUtils.toInputStream(xml, "UTF-8"));
    return ((Element) XPathFactory.newInstance().newXPath().compile("/*").evaluate(doc, XPathConstants.NODE))
            .getAttribute("id");
  }

  protected String getWorkflowInstanceStatus(String xml) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(IOUtils.toInputStream(xml, "UTF-8"));
    return ((Element) XPathFactory.newInstance().newXPath().compile("/*").evaluate(doc, XPathConstants.NODE))
            .getAttribute("state");
  }

  protected String getSampleMediaPackage() throws Exception {
    String template = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("mediapackage-1.xml"), "UTF-8");
    // Make a copy of the media file
    File mediaFile = copyStreamToTempFile(getClass().getClassLoader().getResourceAsStream("av.mov"), "media", ".mov");
    String xml = template.replaceFirst("@SAMPLES_URL@/screen.mpg", "file:" + mediaFile.getAbsolutePath());

    // Make a copy of the dulin core file
    File dcFile = copyStreamToTempFile(getClass().getClassLoader().getResourceAsStream("dublincore.xml"), "dublincore",
            ".xml");
    xml = xml.replaceFirst("@SAMPLES_URL@/dc-1.xml", "file:" + dcFile.getAbsolutePath());
    System.out.println("Using mediapackage " + xml);
    return xml;
  }

  protected File copyStreamToTempFile(InputStream in, String prefix, String suffix) throws Exception {
    File f = File.createTempFile(prefix, suffix);
    FileOutputStream out = new FileOutputStream(f);
    IOUtils.copy(in, out);
    IOUtils.closeQuietly(in);
    IOUtils.closeQuietly(out);
    tempFiles.add(f);
    return f;
  }

  protected String getSampleWorkflowDefinition() throws Exception {
    return IOUtils.toString(getClass().getClassLoader().getResourceAsStream("workflow-definition-1.xml"));
  }
}
