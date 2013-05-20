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
package org.opencastproject.util;

/**
 * This exception is thrown by the {@link ProcessExecutor} and indicates that the process failed.
 */
public class ProcessExcecutorException extends Exception {

  /** Serial version uid */
  private static final long serialVersionUID = -9194578448624904231L;

  /**
   * Creates a new exception.
   * 
   * @param msg
   *          the error message
   * @param cause
   *          the original cause
   */
  public ProcessExcecutorException(String msg, Throwable cause) {
    super(msg, cause);
  }

}
