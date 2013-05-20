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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.opencastproject.job.api.Job;
import org.opencastproject.job.api.JobBarrier;
import org.opencastproject.mediapackage.MediaPackageElementParser;
import org.opencastproject.mediapackage.Track;
import org.opencastproject.security.api.DefaultOrganization;
import org.opencastproject.security.api.Organization;
import org.opencastproject.security.api.OrganizationDirectoryService;
import org.opencastproject.security.api.SecurityConstants;
import org.opencastproject.security.api.SecurityService;
import org.opencastproject.security.api.User;
import org.opencastproject.security.api.UserDirectoryService;
import org.opencastproject.serviceregistry.api.ServiceRegistry;
import org.opencastproject.serviceregistry.api.ServiceRegistryInMemoryImpl;
import org.opencastproject.util.IoSupport;
import org.opencastproject.util.StreamHelper;
import org.opencastproject.workspace.api.Workspace;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests the {@link ComposerServiceImpl}.
 */
public class ComposerServiceTest {
  /** The source file to test with */
  private File source = null;

  /** The composer service to test */
  private ComposerServiceImpl composerService = null;

  /** The service registry for job dispatching */
  private ServiceRegistry serviceRegistry = null;

  /** FFmpeg binary location */
  private static final String FFMPEG_BINARY = "ffmpeg";

  /** True to run the tests */
  private static boolean ffmpegInstalled = true;

  /** Logging facility */
  private static final Logger logger = LoggerFactory.getLogger(ComposerServiceTest.class);

  @BeforeClass
  public static void testForFFmpeg() {
    StreamHelper stdout = null;
    StreamHelper stderr = null;
    Process p = null;
    try {
      p = new ProcessBuilder(FFMPEG_BINARY, "-version").start();
      stdout = new StreamHelper(p.getInputStream());
      stderr = new StreamHelper(p.getErrorStream());
      if (p.waitFor() != 0)
        throw new IllegalStateException();
    } catch (Throwable t) {
      logger.warn("Skipping image composer service tests due to unsatisifed or erroneus ffmpeg installation");
      ffmpegInstalled = false;
    } finally {
      IoSupport.closeQuietly(stdout);
      IoSupport.closeQuietly(stderr);
      IoSupport.closeQuietly(p);
    }
  }

  @Before
  public void setUp() throws Exception {
    if (!ffmpegInstalled)
      return;

    // Copy an existing media file to a temp file
    File f = new File("src/test/resources/slidechanges.mov");
    source = File.createTempFile(FilenameUtils.getBaseName(f.getName()), ".mov");
    FileUtils.copyFile(f, source);
    f = null;

    // create the needed mocks
    BundleContext bc = EasyMock.createNiceMock(BundleContext.class);
    EasyMock.expect(bc.getProperty((String) EasyMock.anyObject())).andReturn(FFMPEG_BINARY);

    ComponentContext cc = EasyMock.createNiceMock(ComponentContext.class);
    EasyMock.expect(cc.getBundleContext()).andReturn(bc).anyTimes();

    Organization org = new DefaultOrganization();
    User user = new User("admin", org.getId(), new String[] { SecurityConstants.DEFAULT_ORGANIZATION_ADMIN });
    OrganizationDirectoryService orgDirectory = EasyMock.createNiceMock(OrganizationDirectoryService.class);
    EasyMock.expect(orgDirectory.getOrganization((String) EasyMock.anyObject())).andReturn(org).anyTimes();

    UserDirectoryService userDirectory = EasyMock.createNiceMock(UserDirectoryService.class);
    EasyMock.expect(userDirectory.loadUser("admin")).andReturn(user).anyTimes();

    SecurityService securityService = EasyMock.createNiceMock(SecurityService.class);
    EasyMock.expect(securityService.getOrganization()).andReturn(org).anyTimes();
    EasyMock.expect(securityService.getUser()).andReturn(user).anyTimes();

    Workspace workspace = EasyMock.createNiceMock(Workspace.class);
    EasyMock.expect(workspace.get((URI) EasyMock.anyObject())).andReturn(source).anyTimes();

    EncodingProfileScanner profileScanner = new EncodingProfileScanner();
    File encodingProfile = new File("src/test/resources/encodingprofiles.properties");
    assertNotNull("Encoding profile must exist", encodingProfile);
    profileScanner.install(encodingProfile);

    // Finish setting up the mocks
    EasyMock.replay(bc, cc, orgDirectory, userDirectory, securityService, workspace);

    // Create an encoding engine factory
    EncoderEngineFactoryImpl encoderEngineFactory = new EncoderEngineFactoryImpl();
    encoderEngineFactory.activate(cc);

    // Create and populate the composer service
    composerService = new ComposerServiceImpl();
    serviceRegistry = new ServiceRegistryInMemoryImpl(composerService, securityService, userDirectory, orgDirectory);
    composerService.setEncoderEngineFactory(encoderEngineFactory);
    composerService.setOrganizationDirectoryService(orgDirectory);
    composerService.setSecurityService(securityService);
    composerService.setServiceRegistry(serviceRegistry);
    composerService.setUserDirectoryService(userDirectory);
    composerService.setProfileScanner(profileScanner);
    composerService.setWorkspace(workspace);

  }

  @After
  public void tearDown() throws Exception {
    FileUtils.deleteQuietly(source);
  }

  @Test
  public void testConcurrentExecutionWithSameSource() throws Exception {
    if (!ffmpegInstalled)
      return;

    assertTrue(source.isFile());
    String sourceTrackXml = "<track id=\"track-1\" type=\"presentation/source\"><mimetype>video/quicktime</mimetype>"
            + "<url>http://localhost:8080/workflow/samples/camera.mpg</url>"
            + "<checksum type=\"md5\">43b7d843b02c4a429b2f547a4f230d31</checksum><duration>14546</duration>"
            + "<video><device type=\"UFG03\" version=\"30112007\" vendor=\"Unigraf\" />"
            + "<encoder type=\"H.264\" version=\"7.4\" vendor=\"Apple Inc\" /><resolution>640x480</resolution>"
            + "<scanType type=\"progressive\" /><bitrate>540520</bitrate><frameRate>2</frameRate></video></track>";
    Track sourceTrack = (Track) MediaPackageElementParser.getFromXml(sourceTrackXml);
    List<Job> jobs = new ArrayList<Job>();
    for (int i = 0; i < 10; i++) {
      jobs.add(composerService.image(sourceTrack, "player-preview.http", 1L));
    }
    boolean success = new JobBarrier(serviceRegistry, jobs.toArray(new Job[jobs.size()])).waitForJobs().isSuccess();
    assertTrue(success);
    for (Job j : jobs) {
      // Always check the service registry for the latest version of the job
      Job job = serviceRegistry.getJob(j.getId());
      assertEquals(Job.Status.FINISHED, job.getStatus());
    }
  }
}
