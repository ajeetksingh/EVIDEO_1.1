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

import org.opencastproject.mediapackage.MediaPackage;
import org.opencastproject.security.api.Organization;
import org.opencastproject.security.api.User;

import java.util.List;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * An single instance of a running, paused, or stopped workflow. WorkflowInstance objects are snapshots in time for a
 * particular workflow. They are not threadsafe, and will not be updated by other threads.
 */
@XmlJavaTypeAdapter(WorkflowInstanceImpl.Adapter.class)
public interface WorkflowInstance extends Configurable {
  enum WorkflowState {
    INSTANTIATED, RUNNING, STOPPED, PAUSED, SUCCEEDED, FAILED, FAILING
  }

  /**
   * The unique ID of this {@link WorkflowInstance}
   */
  long getId();

  /**
   * Sets the workflow identifier.
   * 
   * @param id
   *          the identifier
   */
  void setId(long id);

  /**
   * The short title of the workflow definition used to create this workflow instance
   */
  String getTitle();

  /**
   * The identifier of the workflow definition used to create this workflow instance
   */
  String getTemplate();

  /**
   * The longer description of the workflow definition used to create this workflow instance
   */
  String getDescription();

  /**
   * The parent workflow instance ID, if any
   */
  Long getParentId();

  /**
   * Returns the user that created this workflow.
   * 
   * @return the workflow's creator
   */
  User getCreator();

  /**
   * Returns the organization that this workflow belongs to.
   * 
   * @return the organization
   */
  Organization getOrganization();

  /**
   * Returns a copy of the {@link WorkflowOperationInstance}s that make up this workflow. In order to modify the
   * operations, call setOperations.
   * 
   * @return the workflow operations
   */
  List<WorkflowOperationInstance> getOperations();

  /**
   * Sets the list of workflow operations
   * 
   * @param operations
   *          the new list of operations
   */
  void setOperations(List<WorkflowOperationInstance> operations);

  /**
   * Returns the {@link WorkflowOperationInstance} that is currently either in {@link WorkflowState#RUNNING} or
   * {@link WorkflowState#PAUSED}.
   * 
   * @return the current operation
   */
  WorkflowOperationInstance getCurrentOperation();

  /**
   * The current {@link WorkflowState} of this {@link WorkflowInstance}
   */
  WorkflowState getState();

  /**
   * Set the state of the workflow
   * 
   * @param state
   */
  void setState(WorkflowState state);

  /**
   * The {@link MediaPackage} being worked on by this workflow instance.
   */
  MediaPackage getMediaPackage();

  /**
   * Returns the next operation, and marks it as current. If there is no next operation, this method will return null.
   * 
   * @return The next operation
   */
  WorkflowOperationInstance next();

  /**
   * Whether there is another operation after the current operation. If there is no next operation, this will return
   * null.
   * 
   * @return Whether there is a next operation.
   */
  boolean hasNext();

  /**
   * @param mp
   *          the mediapackage
   */
  void setMediaPackage(MediaPackage mp);

  /**
   * Gets the error messages describing what went wrong, if anything, with this mediapackage
   * 
   * @return the error messages that occured during processing
   */
  String[] getErrorMessages();

  /**
   * @param localizedMessage
   */
  void addErrorMessage(String localizedMessage);

  /**
   * Appends the operations found in the workflow defintion to the end of this workflow instance.
   * 
   * @param workflow
   *          the workflow defintion
   */
  void extend(WorkflowDefinition workflowDefinition);

}
