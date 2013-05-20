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

/**
 * Thrown when a {@link WorkflowOperationInstance} fails to run.
 */
public class WorkflowOperationException extends Exception {

  private static final long serialVersionUID = 5840096157653799867L;

  /** The failing workflow operation */
  private WorkflowOperationInstance operation = null;

  /**
   * Constructs a new {@link WorkflowOperationException} with a message and a root cause.
   * 
   * @param message
   *          The message describing what went wrong
   * @param cause
   *          The exception that triggered this problem
   */
  public WorkflowOperationException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new {@link WorkflowOperationException} with a message, but no root cause.
   * 
   * @param message
   *          The message describing what went wrong
   */
  public WorkflowOperationException(String message) {
    super(message);
  }

  /**
   * Constructs a new {@link WorkflowOperationException} with a root cause.
   * 
   * @param cause
   *          The exception that caused this problem
   */
  public WorkflowOperationException(Throwable cause) {
    super(cause);
  }

  /**
   * Constructs a new {@link WorkflowOperationException} with a root cause.
   * 
   * @param cause
   *          The exception that caused this problem
   * @param operation
   *          the failing operation
   */
  public WorkflowOperationException(Throwable cause, WorkflowOperationInstance operation) {
    super(cause);
    this.operation = operation;
  }

  /**
   * Returns the workflow operation instance which caused the failure.
   * 
   * @return the operation
   */
  public WorkflowOperationInstance getOperation() {
    return operation;
  }

}
