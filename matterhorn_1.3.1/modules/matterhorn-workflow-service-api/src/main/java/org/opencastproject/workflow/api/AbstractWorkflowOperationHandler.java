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
package org.opencastproject.workflow.api;

import org.opencastproject.job.api.Job;
import org.opencastproject.job.api.JobBarrier;
import org.opencastproject.job.api.JobContext;
import org.opencastproject.mediapackage.MediaPackage;
import org.opencastproject.mediapackage.MediaPackageElement;
import org.opencastproject.serviceregistry.api.ServiceRegistry;
import org.opencastproject.workflow.api.WorkflowOperationResult.Action;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.osgi.framework.Constants;
import org.osgi.service.component.ComponentContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Abstract base implementation for an operation handler, which implements a simple start operation that returns a
 * {@link WorkflowOperationResult} with the current mediapackage and {@link Action#CONTINUE}.
 */
public abstract class AbstractWorkflowOperationHandler implements WorkflowOperationHandler {

  /** The ID of this operation handler */
  protected String id = null;

  /** The description of what this handler actually does */
  protected String description = null;

  /** The configuration options for this operation handler */
  protected SortedMap<String, String> options = new TreeMap<String, String>();

  /** Optional service registry */
  protected ServiceRegistry serviceRegistry = null;

  /**
   * Activates this component with its properties once all of the collaborating services have been set
   * 
   * @param cc
   *          The component's context, containing the properties used for configuration
   */
  protected void activate(ComponentContext cc) {
    this.id = (String) cc.getProperties().get(WorkflowService.WORKFLOW_OPERATION_PROPERTY);
    this.description = (String) cc.getProperties().get(Constants.SERVICE_DESCRIPTION);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.workflow.api.WorkflowOperationHandler#start(org.opencastproject.workflow.api.WorkflowInstance, JobContext)
   */
  @Override
  public abstract WorkflowOperationResult start(WorkflowInstance workflowInstance, JobContext context) throws WorkflowOperationException;

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.workflow.api.WorkflowOperationHandler#skip(org.opencastproject.workflow.api.WorkflowInstance, JobContext)
   */
  @Override
  public WorkflowOperationResult skip(WorkflowInstance workflowInstance, JobContext context) throws WorkflowOperationException {
    return createResult(Action.SKIP);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.workflow.api.WorkflowOperationHandler#destroy(org.opencastproject.workflow.api.WorkflowInstance, JobContext)
   */
  @Override
  public void destroy(WorkflowInstance workflowInstance, JobContext context) throws WorkflowOperationException {
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.workflow.api.WorkflowOperationHandler#getConfigurationOptions()
   */
  @Override
  public SortedMap<String, String> getConfigurationOptions() {
    return options;
  }

  /**
   * Adds a configuration option to the list of possible configuration options.
   * 
   * @param name
   *          the option name
   * @param description
   *          the option description
   */
  public void addConfigurationOption(String name, String description) {
    options.put(name, description);
  }

  /**
   * Removes the configuration option from the list of possible configuration options.
   * 
   * @param name
   *          the option name
   */
  public void removeConfigurationOption(String name) {
    options.remove(name);
  }

  /**
   * Converts a comma separated string into a set of values. Useful for converting operation configuration strings into
   * multi-valued sets.
   * 
   * @param commaSeparated
   *          The comma separated string
   * @return the set of values
   */
  protected List<String> asList(String commaSeparated) {
    commaSeparated = StringUtils.trimToNull(commaSeparated);
    List<String> list = new ArrayList<String>();
    if (commaSeparated != null) {
      for (String s : commaSeparated.split(",")) {
        if (StringUtils.trimToNull(s) != null) {
          list.add(s.trim());
        }
      }
    }
    return list;
  }

  /**
   * Generates a filename using the base name of a source element and the extension of a derived element.
   * 
   * @param source
   *          the source mediapackage element
   * @param derived
   *          the derived mediapackage element
   * @return the filename
   */
  protected String getFileNameFromElements(MediaPackageElement source, MediaPackageElement derived) {
    String fileName = FilenameUtils.getBaseName(source.getURI().toString());
    String fileExtension = FilenameUtils.getExtension(derived.getURI().toString());
    return fileName + "." + fileExtension;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.workflow.api.WorkflowOperationHandler#getId()
   */
  @Override
  public String getId() {
    return id;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.workflow.api.WorkflowOperationHandler#getDescription()
   */
  @Override
  public String getDescription() {
    return description;
  }

  /**
   * Creates a result for the execution of this workflow operation handler.
   * 
   * @param action
   *          the action to take
   * @return the result
   */
  protected WorkflowOperationResult createResult(Action action) {
    return createResult(null, null, action, 0);
  }

  /**
   * Creates a result for the execution of this workflow operation handler.
   * <p>
   * Since there is no way for the workflow service to determine the queuing time (e. g. waiting on services), it needs
   * to be provided by the handler.
   * 
   * @param action
   *          the action to take
   * @param timeInQueue
   *          the amount of time this handle spent waiting for services
   * @return the result
   */
  protected WorkflowOperationResult createResult(Action action, long timeInQueue) {
    return createResult(null, null, action, timeInQueue);
  }

  /**
   * Creates a result for the execution of this workflow operation handler.
   * 
   * @param mediaPackage
   *          the modified mediapackage
   * @param action
   *          the action to take
   * @return the result
   */
  protected WorkflowOperationResult createResult(MediaPackage mediaPackage, Action action) {
    return createResult(mediaPackage, null, action, 0);
  }

  /**
   * Creates a result for the execution of this workflow operation handler.
   * <p>
   * Since there is no way for the workflow service to determine the queuing time (e. g. waiting on services), it needs
   * to be provided by the handler.
   * 
   * @param mediaPackage
   *          the modified mediapackage
   * @param action
   *          the action to take
   * @param timeInQueue
   *          the amount of time this handle spent waiting for services
   * @return the result
   */
  protected WorkflowOperationResult createResult(MediaPackage mediaPackage, Action action, long timeInQueue) {
    return createResult(mediaPackage, null, action, timeInQueue);
  }

  /**
   * Creates a result for the execution of this workflow operation handler.
   * <p>
   * Since there is no way for the workflow service to determine the queuing time (e. g. waiting on services), it needs
   * to be provided by the handler.
   * 
   * @param mediaPackage
   *          the modified mediapackage
   * @param properties
   *          the properties to add to the workflow instance
   * @param action
   *          the action to take
   * @param timeInQueue
   *          the amount of time this handle spent waiting for services
   * @return the result
   */
  protected WorkflowOperationResult createResult(MediaPackage mediaPackage, Map<String, String> properties,
          Action action, long timeInQueue) {
    return new WorkflowOperationResultImpl(mediaPackage, properties, action, timeInQueue);
  }

  /**
   * Sets the service registry. This method is here as a convenience for developers that need the registry to do job
   * waiting.
   * 
   * @param serviceRegistry
   *          the service registry
   */
  public void setServiceRegistry(ServiceRegistry serviceRegistry) {
    this.serviceRegistry = serviceRegistry;
  }

  /**
   * Waits until all of the jobs have reached either one of these statuses:
   * <ul>
   * <li>{@link Job.Status#FINISHED}</li>
   * <li>{@link Job.Status#FAILED}</li>
   * <li>{@link Job.Status#DELETED}</li>
   * </ul>
   * After that, the method returns with the actual outcomes of the jobs.
   * 
   * @param jobs
   *          the jobs
   * @return the jobs and their outcomes
   * @throws IllegalStateException
   *           if the service registry has not been set
   * @throws IllegalArgumentException
   *           if the jobs collecion is either <code>null</code> or empty
   */
  protected JobBarrier.Result waitForStatus(Job... jobs) throws IllegalStateException, IllegalArgumentException {
    return waitForStatus(0, jobs);
  }

  /**
   * Waits until all of the jobs have reached either one of these statuses:
   * <ul>
   * <li>{@link Job.Status#FINISHED}</li>
   * <li>{@link Job.Status#FAILED}</li>
   * <li>{@link Job.Status#DELETED}</li>
   * </ul>
   * After that, the method returns with the actual outcomes of the jobs.
   * 
   * @param timeout
   *          the maximum amount of time in miliseconds to wait
   * @param jobs
   *          the jobs
   * @return the jobs and their outcomes
   * @throws IllegalStateException
   *           if the service registry has not been set
   * @throws IllegalArgumentException
   *           if the jobs collecion is either <code>null</code> or empty
   */
  protected JobBarrier.Result waitForStatus(long timeout, Job... jobs) throws IllegalStateException,
          IllegalArgumentException {
    if (serviceRegistry == null)
      throw new IllegalStateException("Can't wait for job status without providing a service registry first");
    JobBarrier barrier = new JobBarrier(serviceRegistry, jobs);
    return barrier.waitForJobs(timeout);
  }

  /**
   * {@inheritDoc}
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : super.hashCode();
  }

  /**
   * {@inheritDoc}
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof WorkflowOperationHandler) {
      if (id != null)
        return id.equals(((WorkflowOperationHandler) obj).getId());
      else
        return this == obj;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return getId();
  }

}
