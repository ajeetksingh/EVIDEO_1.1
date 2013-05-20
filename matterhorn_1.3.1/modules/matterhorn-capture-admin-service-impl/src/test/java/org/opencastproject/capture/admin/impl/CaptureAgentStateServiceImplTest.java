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

package org.opencastproject.capture.admin.impl;

import static junit.framework.Assert.fail;
import static org.opencastproject.capture.admin.api.AgentState.IDLE;
import static org.opencastproject.capture.admin.api.AgentState.UNKNOWN;
import static org.opencastproject.capture.admin.api.CaptureAgentStateService.BAD_PARAMETER;
import static org.opencastproject.capture.admin.api.RecordingState.CAPTURING;
import static org.opencastproject.capture.admin.api.RecordingState.UPLOADING;
import static org.opencastproject.capture.admin.api.RecordingState.UPLOAD_FINISHED;
import static org.opencastproject.security.api.SecurityConstants.DEFAULT_ORGANIZATION_ADMIN;
import static org.opencastproject.security.api.SecurityConstants.DEFAULT_ORGANIZATION_ID;

import org.opencastproject.capture.CaptureParameters;
import org.opencastproject.capture.admin.api.Agent;
import org.opencastproject.capture.admin.api.Recording;
import org.opencastproject.security.api.DefaultOrganization;
import org.opencastproject.security.api.SecurityService;
import org.opencastproject.security.api.User;
import org.opencastproject.workflow.api.WorkflowQuery;
import org.opencastproject.workflow.api.WorkflowService;
import org.opencastproject.workflow.api.WorkflowSetImpl;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.eclipse.persistence.jpa.PersistenceProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.service.cm.ConfigurationException;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

public class CaptureAgentStateServiceImplTest {
  private CaptureAgentStateServiceImpl service = null;
  private Properties capabilities;
  private ComboPooledDataSource pooledDataSource = null;
  private long timestamp = -1L;

  @Before
  public void setUp() throws Exception {
    timestamp = System.currentTimeMillis();
    setupService();

    capabilities = new Properties();
    capabilities.setProperty(CaptureParameters.CAPTURE_DEVICE_PREFIX + "CAMERA", "/dev/video0");
    capabilities.setProperty(CaptureParameters.CAPTURE_DEVICE_PREFIX + "SCREEN", "/dev/video1");
    capabilities.setProperty(CaptureParameters.CAPTURE_DEVICE_PREFIX + "AUDIO", "hw:0");
    capabilities.setProperty(CaptureParameters.CAPTURE_DEVICE_NAMES, "CAMERA,SCREEN,AUDIO");
  }

  private void setupService() throws Exception {
    pooledDataSource = new ComboPooledDataSource();
    pooledDataSource.setDriverClass("org.h2.Driver");
    pooledDataSource.setJdbcUrl("jdbc:h2:./target/db" + timestamp);
    pooledDataSource.setUser("sa");
    pooledDataSource.setPassword("sa");

    // Collect the persistence properties
    Map<String, Object> props = new HashMap<String, Object>();
    props.put("javax.persistence.nonJtaDataSource", pooledDataSource);
    props.put("eclipselink.ddl-generation", "create-tables");
    props.put("eclipselink.ddl-generation.output-mode", "database");

    service = new CaptureAgentStateServiceImpl();
    service.setPersistenceProvider(new PersistenceProvider());
    service.setPersistenceProperties(props);

    WorkflowService workflowService = EasyMock.createNiceMock(WorkflowService.class);
    EasyMock.expect(workflowService.getWorkflowInstances((WorkflowQuery) EasyMock.anyObject()))
            .andReturn(new WorkflowSetImpl()).anyTimes();
    EasyMock.replay(workflowService);
    service.setWorkflowService(workflowService);

    User user = new User("testuser", DEFAULT_ORGANIZATION_ID, new String[] { DEFAULT_ORGANIZATION_ADMIN });
    SecurityService securityService = EasyMock.createNiceMock(SecurityService.class);
    EasyMock.expect(securityService.getUser()).andReturn(user).anyTimes();
    EasyMock.expect(securityService.getOrganization()).andReturn(new DefaultOrganization()).anyTimes();
    EasyMock.replay(securityService);
    service.setSecurityService(securityService);

    service.activate(null);
  }

  @After
  public void tearDown() {
    service.deactivate();
    pooledDataSource.close();
  }

  @Test
  public void nonExistantAgent() {
    Agent agent = service.getAgentState("doesNotExist");
    Assert.assertNull(agent);
  }

  @Test
  public void noAgents() {
    Assert.assertEquals(0, service.getKnownAgents().size());
  }

  @Test
  public void badAgentStates() {
    service.setAgentState(null, "something");
    Assert.assertEquals(0, service.getKnownAgents().size());
    service.setAgentState("", "something");
    Assert.assertEquals(0, service.getKnownAgents().size());
    service.setAgentState("something", null);
    Assert.assertEquals(0, service.getKnownAgents().size());
  }

  @Test
  public void badAgentCapabilities() {
    Assert.assertEquals(BAD_PARAMETER, service.setAgentConfiguration(null, capabilities));
    Assert.assertEquals(0, service.getKnownAgents().size());

    service.setAgentConfiguration("", capabilities);
    Assert.assertEquals(0, service.getKnownAgents().size());

    service.setAgentState("something", null);
    Assert.assertEquals(0, service.getKnownAgents().size());
  }

  private void verifyAgent(String name, String state, Properties caps) {
    Agent agent = service.getAgentState(name);

    if (agent != null) {
      Assert.assertEquals(name, agent.getName());
      Assert.assertEquals(state, agent.getState());
      Assert.assertEquals(caps, agent.getCapabilities());
    } else if (state != null)
      Assert.fail();
  }

  @Test
  public void oneAgentState() {
    service.setAgentState("agent1", IDLE);
    Assert.assertEquals(1, service.getKnownAgents().size());

    verifyAgent("notAgent1", null, null);
    verifyAgent("agent1", IDLE, new Properties());

    service.setAgentState("agent1", CAPTURING);
    Assert.assertEquals(1, service.getKnownAgents().size());

    verifyAgent("notAgent1", null, null);
    verifyAgent("agent1", CAPTURING, new Properties());
  }

  @Test
  public void oneAgentCapabilities() {
    service.setAgentConfiguration("agent1", capabilities);
    Assert.assertEquals(1, service.getKnownAgents().size());

    verifyAgent("notAgent1", null, null);
    verifyAgent("agent1", UNKNOWN, capabilities);

    service.setAgentState("agent1", IDLE);
    Assert.assertEquals(1, service.getKnownAgents().size());

    verifyAgent("notAgent1", null, null);
    verifyAgent("agent1", IDLE, capabilities);

    service.setAgentConfiguration("agent1", new Properties());
    Assert.assertEquals(1, service.getKnownAgents().size());

    verifyAgent("notAnAgent", null, null);
    verifyAgent("agent1", IDLE, new Properties());
  }

  @Test
  public void removeAgent() {
    service.setAgentConfiguration("agent1", capabilities);
    Assert.assertEquals(1, service.getKnownAgents().size());
    service.setAgentConfiguration("agent2", capabilities);
    service.setAgentState("agent2", UPLOADING);

    verifyAgent("notAnAgent", null, capabilities);
    verifyAgent("agent1", UNKNOWN, capabilities);
    verifyAgent("agent2", UPLOADING, capabilities);

    service.removeAgent("agent1");
    Assert.assertEquals(1, service.getKnownAgents().size());
    verifyAgent("notAnAgent", null, capabilities);
    verifyAgent("agent1", null, capabilities);
    verifyAgent("agent2", UPLOADING, capabilities);

    service.removeAgent("notAnAgent");
    Assert.assertEquals(1, service.getKnownAgents().size());
    verifyAgent("notAnAgent", null, capabilities);
    verifyAgent("agent1", null, capabilities);
    verifyAgent("agent2", UPLOADING, capabilities);
  }

  @Test
  public void agentCapabilities() {
    Assert.assertNull(service.getAgentCapabilities("agent"));
    Assert.assertNull(service.getAgentCapabilities("NotAgent"));
    service.setAgentConfiguration("agent", capabilities);
    Assert.assertEquals(service.getAgentCapabilities("agent"), capabilities);
    Assert.assertNull(service.getAgentCapabilities("NotAgent"));
  }

  @Test
  public void stickyAgents() throws Exception {
    Assert.assertEquals(0, service.getKnownAgents().size());

    Properties cap1 = new Properties();
    cap1.put(CaptureParameters.CAPTURE_DEVICE_PREFIX + "key", "value");
    cap1.put(CaptureParameters.CAPTURE_DEVICE_NAMES, "key");
    Properties cap2 = new Properties();
    cap2.put(CaptureParameters.CAPTURE_DEVICE_PREFIX + "foo", "bar");
    cap2.put(CaptureParameters.CAPTURE_DEVICE_NAMES, "foo");
    Properties cap3 = new Properties();
    cap3.put(CaptureParameters.CAPTURE_DEVICE_PREFIX + "bam", "bam");
    cap3.put(CaptureParameters.CAPTURE_DEVICE_NAMES, "bam");

    // Setup the two agents and persist them
    service.setAgentState("sticky1", IDLE);
    service.setAgentConfiguration("sticky1", cap1);
    service.setAgentState("sticky2", CAPTURING);
    service.setAgentConfiguration("sticky2", cap2);
    service.setAgentState("sticky3", UPLOADING);
    service.setAgentConfiguration("sticky3", cap3);

    // Make sure they're set right
    Assert.assertEquals(cap1, service.getAgentCapabilities("sticky1"));
    Assert.assertEquals(IDLE, service.getAgentState("sticky1").getState());
    Assert.assertEquals(cap2, service.getAgentCapabilities("sticky2"));
    Assert.assertEquals(CAPTURING, service.getAgentState("sticky2").getState());
    Assert.assertEquals(cap3, service.getAgentCapabilities("sticky3"));
    Assert.assertEquals(UPLOADING, service.getAgentState("sticky3").getState());
    Assert.assertNull(service.getAgentCapabilities("sticky4"));
    Assert.assertNull(service.getAgentState("sticky4"));

    // Shut down the service completely
    service.deactivate();
    service = null;

    // Restart the service with the same configuration as before
    setupService();

    Assert.assertEquals(3, service.getKnownAgents().size());

    // The agents should still be there
    Assert.assertEquals(cap1, service.getAgentCapabilities("sticky1"));
    Assert.assertEquals(IDLE, service.getAgentState("sticky1").getState());
    Assert.assertEquals(cap2, service.getAgentCapabilities("sticky2"));
    Assert.assertEquals(CAPTURING, service.getAgentState("sticky2").getState());
    Assert.assertEquals(cap3, service.getAgentCapabilities("sticky3"));
    Assert.assertEquals(UPLOADING, service.getAgentState("sticky3").getState());
    Assert.assertNull(service.getAgentCapabilities("sticky4"));
    Assert.assertNull(service.getAgentState("sticky4"));
  }

  @Test
  public void nonExistantRecording() {
    Recording recording = service.getRecordingState("doesNotExist");
    Assert.assertNull(recording);
  }

  @Test
  public void badRecordingData() {
    service.setRecordingState(null, CAPTURING);
    Assert.assertEquals(0, service.getKnownRecordings().size());
    service.setRecordingState("", IDLE);
    Assert.assertEquals(0, service.getKnownRecordings().size());
    service.setRecordingState("something", "bad_state");
    Assert.assertEquals(0, service.getKnownRecordings().size());
  }

  @Test
  public void noRecordings() {
    Assert.assertEquals(0, service.getKnownRecordings().size());
  }

  private void verifyRecording(String id, String state) {
    Recording recording = service.getRecordingState(id);

    if (state != null) {
      Assert.assertEquals(id, recording.getID());
      Assert.assertEquals(state, recording.getState());
    } else
      Assert.assertNull(recording);
  }

  @Test
  public void oneRecording() {
    service.setRecordingState("Recording1", UPLOAD_FINISHED);
    Assert.assertEquals(1, service.getKnownRecordings().size());

    verifyRecording("notRecording1", null);
    verifyRecording("Recording1", UPLOAD_FINISHED);

    service.setRecordingState("Recording1", CAPTURING);
    Assert.assertEquals(1, service.getKnownRecordings().size());

    verifyRecording("notRecording1", null);
    verifyRecording("Recording1", CAPTURING);
  }

  @Test
  public void removeRecording() {
    service.setRecordingState("Recording1", CAPTURING);
    Assert.assertEquals(1, service.getKnownRecordings().size());
    service.setRecordingState("Recording2", UPLOADING);
    Assert.assertEquals(2, service.getKnownRecordings().size());

    verifyRecording("notAnRecording", null);
    verifyRecording("Recording1", CAPTURING);
    verifyRecording("Recording2", UPLOADING);

    Assert.assertTrue(service.removeRecording("Recording1"));
    Assert.assertFalse(service.removeRecording("asdfasdf"));
    Assert.assertEquals(1, service.getKnownRecordings().size());
    verifyRecording("notAnRecording", null);
    verifyRecording("Recording1", null);
    verifyRecording("Recording2", UPLOADING);
  }

  @Test
  public void testAgentVisibility() throws Exception {
    // Create a new capture agent called "visibility"
    String agentName = "visibility";
    service.setAgentState(agentName, IDLE);

    // Ensure we can see it
    Assert.assertEquals(1, service.getKnownAgents().size());

    // Set the roles allowed to use this agent
    Set<String> roles = new HashSet<String>();
    roles.add("a_role_we_do_not_have");
    AgentImpl agent = service.getAgent(agentName);
    agent.setSchedulerRoles(roles);
    service.updateAgentInDatabase(agent);

    // Since we are an organizational admin, we should still see the agent
    Assert.assertEquals(1, service.getKnownAgents().size());

    // Use a security service that identifies us as a non-administrative user
    User user = new User("testuser", DEFAULT_ORGANIZATION_ID, new String[] { "ROLE_NOT_ADMIN" });
    SecurityService securityService = EasyMock.createNiceMock(SecurityService.class);
    EasyMock.expect(securityService.getUser()).andReturn(user).anyTimes();
    EasyMock.expect(securityService.getOrganization()).andReturn(new DefaultOrganization()).anyTimes();
    EasyMock.replay(securityService);
    service.setSecurityService(securityService);

    // Ensure we can no longer see the agent, since we don't have an administrative role
    Assert.assertEquals(0, service.getKnownAgents().size());

    // TODO: Do we need to enforce access strictly? If someone asks for an agent by name, but they do not have the
    // appropriate scheduler role, should we throw UnauthorizedException?
  }

  @Test
  public void testManagedServiceFactory() throws Exception {
    // Make sure we can register a capture agent with specific scheduler roles
    String pid = UUID.randomUUID().toString();
    Dictionary<String, String> properties = new Hashtable<String, String>();
    properties.put("id", "agent1");
    properties.put("organization", DEFAULT_ORGANIZATION_ID);
    properties.put("url", "http://agent1:8080/");
    properties.put("schedulerRoles", DEFAULT_ORGANIZATION_ADMIN + ", SOME_OTHER_ROLE");
    service.updated(pid, properties);

    // If any of the three values are missing, we should throw
    properties.remove("id");
    try {
      service.updated(pid, properties);
      fail();
    } catch (ConfigurationException e) {
      // expected
    }
  }

}
