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
package org.opencastproject.composer.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.opencastproject.composer.api.EncodingProfile;
import org.opencastproject.composer.impl.ffmpeg.FFmpegEncoderEngine;
import org.opencastproject.util.FileSupport;
import org.opencastproject.util.IoSupport;
import org.opencastproject.util.StreamHelper;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Map;

/**
 * Test trimming using ffmpeg.
 */
public class TrimmingTest {

  /** the encoding engine */
  private FFmpegEncoderEngine engine;

  /** The temporary directory to store media files */
  private File workingDirectory = null;

  /** Encoding profiles **/
  private Map<String, EncodingProfile> profiles;

  /** True to run the tests */
  private static boolean ffmpegInstalled = true;

  /** Logging facility */
  private static final Logger logger = LoggerFactory.getLogger(TrimmingTest.class);

  @BeforeClass
  public static void testOcropus() {
    StreamHelper stdout = null;
    StreamHelper stderr = null;
    Process p = null;
    try {
      p = new ProcessBuilder(FFmpegEncoderEngine.FFMPEG_BINARY_DEFAULT, "-version").start();
      stdout = new StreamHelper(p.getInputStream());
      stderr = new StreamHelper(p.getErrorStream());
      if (p.waitFor() != 0)
        throw new IllegalStateException();
    } catch (Throwable t) {
      logger.warn("Skipping trimming tests due to unsatisifed ffmpeg installation");
      ffmpegInstalled = false;
    } finally {
      IoSupport.closeQuietly(stdout);
      IoSupport.closeQuietly(stderr);
      IoSupport.closeQuietly(p);
    }
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    engine = new FFmpegEncoderEngine();
    workingDirectory = FileSupport.getTempDirectory("trimtest");
    FileUtils.forceMkdir(workingDirectory);

    URL url = EncodingProfileTest.class.getResource("/encodingprofiles.properties");
    EncodingProfileScanner mgr = new EncodingProfileScanner();
    profiles = mgr.loadFromProperties(new File(url.toURI()));
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception {
    FileUtils.forceDelete(workingDirectory);
  }

  /**
   * Test method for
   * {@link org.opencastproject.composer.impl.ffmpeg.FFmpegEncoderEngine#trim(java.io.File, org.opencastproject.composer.api.EncodingProfile, long, long, java.util.Map)}
   * .
   */
  @Test
  public void testTrim() throws Exception {
    if (!ffmpegInstalled)
      return;
    URL sourceUrl = getClass().getResource("/slidechanges.mov");
    File sourceFile = new File(workingDirectory, "slidechanges.mov");
    FileUtils.copyURLToFile(sourceUrl, sourceFile);
    EncodingProfile trimProfile = profiles.get("trim.work");
    File trimmedMovie = engine.trim(sourceFile, trimProfile, 5000, 10000, null);

    // These are weak assertions, but anything else would require either integration with another 3rd party tool
    // or manual parsing of ffmpeg output. Instead, we keep this test generic (but weak).
    assertTrue(trimmedMovie.exists());
    assertTrue(trimmedMovie.length() < sourceFile.length());

    assertEquals("00:00:05", engine.getCommandlineParameters().get(FFmpegEncoderEngine.PROP_TRIMMING_START_TIME));
    assertEquals("00:00:10", engine.getCommandlineParameters().get(FFmpegEncoderEngine.PROP_TRIMMING_DURATION));

  }

}
