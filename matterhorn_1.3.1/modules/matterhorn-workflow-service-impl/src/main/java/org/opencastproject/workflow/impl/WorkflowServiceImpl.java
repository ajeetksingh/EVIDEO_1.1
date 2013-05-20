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
package org.opencastproject.workflow.impl;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.opencastproject.security.api.SecurityConstants.GLOBAL_ADMIN_ROLE;
import static org.opencastproject.workflow.api.WorkflowInstance.WorkflowState.FAILED;
import static org.opencastproject.workflow.api.WorkflowInstance.WorkflowState.FAILING;
import static org.opencastproject.workflow.api.WorkflowInstance.WorkflowState.INSTANTIATED;
import static org.opencastproject.workflow.api.WorkflowInstance.WorkflowState.PAUSED;
import static org.opencastproject.workflow.api.WorkflowInstance.WorkflowState.RUNNING;
import static org.opencastproject.workflow.api.WorkflowInstance.WorkflowState.STOPPED;
import static org.opencastproject.workflow.api.WorkflowInstance.WorkflowState.SUCCEEDED;

import org.opencastproject.job.api.Job;
import org.opencastproject.job.api.Job.Status;
import org.opencastproject.job.api.JobProducer;
import org.opencastproject.mediapackage.MediaPackage;
import org.opencastproject.mediapackage.MediaPackageException;
import org.opencastproject.mediapackage.MediaPackageParser;
import org.opencastproject.metadata.api.MediaPackageMetadata;
import org.opencastproject.metadata.api.MediaPackageMetadataService;
import org.opencastproject.security.api.AccessControlList;
import org.opencastproject.security.api.AuthorizationService;
import org.opencastproject.security.api.Organization;
import org.opencastproject.security.api.OrganizationDirectoryService;
import org.opencastproject.security.api.SecurityService;
import org.opencastproject.security.api.UnauthorizedException;
import org.opencastproject.security.api.User;
import org.opencastproject.security.api.UserDirectoryService;
import org.opencastproject.series.api.SeriesException;
import org.opencastproject.series.api.SeriesService;
import org.opencastproject.serviceregistry.api.ServiceRegistry;
import org.opencastproject.serviceregistry.api.ServiceRegistryException;
import org.opencastproject.util.NotFoundException;
import org.opencastproject.workflow.api.ResumableWorkflowOperationHandler;
import org.opencastproject.workflow.api.WorkflowDatabaseException;
import org.opencastproject.workflow.api.WorkflowDefinition;
import org.opencastproject.workflow.api.WorkflowInstance;
import org.opencastproject.workflow.api.WorkflowInstance.WorkflowState;
import org.opencastproject.workflow.api.WorkflowInstanceImpl;
import org.opencastproject.workflow.api.WorkflowListener;
import org.opencastproject.workflow.api.WorkflowOperationDefinition;
import org.opencastproject.workflow.api.WorkflowOperationException;
import org.opencastproject.workflow.api.WorkflowOperationHandler;
import org.opencastproject.workflow.api.WorkflowOperationInstance;
import org.opencastproject.workflow.api.WorkflowOperationInstance.OperationState;
import org.opencastproject.workflow.api.WorkflowOperationInstanceImpl;
import org.opencastproject.workflow.api.WorkflowOperationResult;
import org.opencastproject.workflow.api.WorkflowOperationResult.Action;
import org.opencastproject.workflow.api.WorkflowOperationResultImpl;
import org.opencastproject.workflow.api.WorkflowParser;
import org.opencastproject.workflow.api.WorkflowParsingException;
import org.opencastproject.workflow.api.WorkflowQuery;
import org.opencastproject.workflow.api.WorkflowService;
import org.opencastproject.workflow.api.WorkflowSet;
import org.opencastproject.workflow.api.WorkflowStatistics;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implements WorkflowService with in-memory data structures to hold WorkflowOperations and WorkflowInstances.
 * WorkflowOperationHandlers are looked up in the OSGi service registry based on the "workflow.operation" property. If
 * the WorkflowOperationHandler's "workflow.operation" service registration property matches
 * WorkflowOperation.getName(), then the factory returns a WorkflowOperationRunner to handle that operation. This allows
 * for custom runners to be added or modified without affecting the workflow service itself.
 */
public class WorkflowServiceImpl implements WorkflowService, JobProducer, ManagedService {

  /** Logging facility */
  private static final Logger logger = LoggerFactory.getLogger(WorkflowServiceImpl.class);

  /** List of available operations on jobs */
  private enum Operation {
    START_WORKFLOW, RESUME, START_OPERATION
  };

  /** The pattern used by workfow operation configuration keys **/
  public static final Pattern PROPERTY_PATTERN = Pattern.compile("\\$\\{.+?\\}");

  /** The set of yes values */
  public static final Set<String> YES;

  /** The set of 'no' values */
  public static final Set<String> NO;

  /** The configuration key for setting {@link #maxConcurrentWorkflows} */
  public static final String MAX_CONCURRENT_CONFIG_KEY = "max.concurrent";

  /** Configuration value for the maximum number of parallel workflows based on the number of cores in the cluster */
  public static final String OPT_NUM_CORES = "cores";

  /** Constant value indicating a <code>null</code> parent id */
  private static final String NULL_PARENT_ID = "-";

  /** Remove references to the component context once felix scr 1.2 becomes available */
  protected ComponentContext componentContext = null;

  /** The maximum number of cluster-wide workflows that will cause this service to stop accepting new jobs */
  protected int maxConcurrentWorkflows = -1;

  /** The collection of workflow definitions */
  protected Map<String, WorkflowDefinition> workflowDefinitions = new HashMap<String, WorkflowDefinition>();

  /** The metadata services */
  private SortedSet<MediaPackageMetadataService> metadataServices;

  /** The data access object responsible for storing and retrieving workflow instances */
  protected WorkflowServiceIndex index;

  /** The list of workflow listeners */
  private List<WorkflowListener> listeners = new CopyOnWriteArrayList<WorkflowListener>();

  /** The thread pool to use for firing listeners and handling dispatched jobs */
  protected ThreadPoolExecutor executorService;

  /** The service registry */
  protected ServiceRegistry serviceRegistry = null;

  /** The security service */
  protected SecurityService securityService = null;

  /** The authorization service */
  protected AuthorizationService authorizationService = null;

  /** The user directory service */
  protected UserDirectoryService userDirectoryService = null;

  /** The organization directory service */
  protected OrganizationDirectoryService organizationDirectoryService = null;

  /** The series service */
  protected SeriesService seriesService;

  static {
    YES = new HashSet<String>(Arrays.asList(new String[] { "yes", "true", "on" }));
    NO = new HashSet<String>(Arrays.asList(new String[] { "no", "false", "off" }));
  }

  /**
   * Constructs a new workflow service impl, with a priority-sorted map of metadata services
   */
  public WorkflowServiceImpl() {
    metadataServices = new TreeSet<MediaPackageMetadataService>(new Comparator<MediaPackageMetadataService>() {
      @Override
      public int compare(MediaPackageMetadataService o1, MediaPackageMetadataService o2) {
        return o1.getPriority() - o2.getPriority();
      }
    });
  }

  /**
   * Activate this service implementation via the OSGI service component runtime.
   * 
   * @param componentContext
   *          the component context
   */
  public void activate(ComponentContext componentContext) {
    this.componentContext = componentContext;
    executorService = (ThreadPoolExecutor) Executors.newCachedThreadPool();
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.workflow.api.WorkflowService#addWorkflowListener(org.opencastproject.workflow.api.WorkflowListener)
   */
  @Override
  public void addWorkflowListener(WorkflowListener listener) {
    listeners.add(listener);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.workflow.api.WorkflowService#removeWorkflowListener(org.opencastproject.workflow.api.WorkflowListener)
   */
  @Override
  public void removeWorkflowListener(WorkflowListener listener) {
    listeners.remove(listener);
  }

  /**
   * Fires the workflow listeners on workflow updates.
   */
  protected void fireListeners(final WorkflowInstance oldWorkflowInstance, final WorkflowInstance newWorkflowInstance) {
    final User currentUser = securityService.getUser();
    final Organization currentOrganization = securityService.getOrganization();
    for (final WorkflowListener listener : listeners) {
      if (oldWorkflowInstance == null || !oldWorkflowInstance.getState().equals(newWorkflowInstance.getState())) {
        Runnable runnable = new Runnable() {
          @Override
          public void run() {
            try {
              securityService.setUser(currentUser);
              securityService.setOrganization(currentOrganization);
              listener.stateChanged(newWorkflowInstance);
            } finally {
              securityService.setUser(null);
              securityService.setOrganization(null);
            }
          }
        };
        executorService.execute(runnable);
      } else {
        logger.debug("Not notifying {} because the workflow state has not changed", listener);
      }

      if (newWorkflowInstance.getCurrentOperation() != null) {
        if (oldWorkflowInstance == null || oldWorkflowInstance.getCurrentOperation() == null
                || !oldWorkflowInstance.getCurrentOperation().equals(newWorkflowInstance.getCurrentOperation())) {
          Runnable runnable = new Runnable() {
            @Override
            public void run() {
              try {
                securityService.setUser(currentUser);
                securityService.setOrganization(currentOrganization);
                listener.operationChanged(newWorkflowInstance);
              } finally {
                securityService.setUser(null);
                securityService.setOrganization(null);
              }
            }
          };
          executorService.execute(runnable);
        }
      } else {
        logger.debug("Not notifying {} because the workflow operation has not changed", listener);
      }
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.workflow.api.WorkflowService#listAvailableWorkflowDefinitions()
   */
  public List<WorkflowDefinition> listAvailableWorkflowDefinitions() {
    List<WorkflowDefinition> list = new ArrayList<WorkflowDefinition>();
    for (Entry<String, WorkflowDefinition> entry : workflowDefinitions.entrySet()) {
      list.add(entry.getValue());
    }
    Collections.sort(list, new Comparator<WorkflowDefinition>() {
      public int compare(WorkflowDefinition o1, WorkflowDefinition o2) {
        return o1.getId().compareTo(o2.getId());
      }
    });
    return list;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.workflow.api.WorkflowService#isRunnable(org.opencastproject.workflow.api.WorkflowDefinition)
   */
  public boolean isRunnable(WorkflowDefinition workflowDefinition) {
    List<String> availableOperations = listAvailableOperationNames();
    List<WorkflowDefinition> checkedWorkflows = new ArrayList<WorkflowDefinition>();
    boolean runnable = isRunnable(workflowDefinition, availableOperations, checkedWorkflows);
    int wfCount = checkedWorkflows.size() - 1;
    if (runnable)
      logger.info("Workflow {}, containing {} derived workflows, is runnable", workflowDefinition, wfCount);
    else
      logger.warn("Workflow {}, containing {} derived workflows, is not runnable", workflowDefinition, wfCount);
    return runnable;
  }

  /**
   * Tests the workflow definition for its runnability. This method is a helper for
   * {@link #isRunnable(WorkflowDefinition)} that is suited for recursive calling.
   * 
   * @param workflowDefinition
   *          the definition to test
   * @param availableOperations
   *          list of currently available operation handlers
   * @param checkedWorkflows
   *          list of checked workflows, used to avoid circular checking
   * @return <code>true</code> if all bits and pieces used for executing <code>workflowDefinition</code> are in place
   */
  private boolean isRunnable(WorkflowDefinition workflowDefinition, List<String> availableOperations,
          List<WorkflowDefinition> checkedWorkflows) {
    if (checkedWorkflows.contains(workflowDefinition))
      return true;

    // Test availability of operation handler and catch workflows
    for (WorkflowOperationDefinition op : workflowDefinition.getOperations()) {
      if (!availableOperations.contains(op.getId())) {
        logger.info("{} is not runnable due to missing operation {}", workflowDefinition, op);
        return false;
      }
      String catchWorkflow = op.getExceptionHandlingWorkflow();
      if (catchWorkflow != null) {
        WorkflowDefinition catchWorkflowDefinition;
        try {
          catchWorkflowDefinition = getWorkflowDefinitionById(catchWorkflow);
        } catch (NotFoundException e) {
          logger.info("{} is not runnable due to missing catch workflow {} on operation {}", new Object[] {
                  workflowDefinition, catchWorkflow, op });
          return false;
        } catch (WorkflowDatabaseException e) {
          logger.info("{} is not runnable because we can not load the catch workflow {} on operation {}", new Object[] {
                  workflowDefinition, catchWorkflow, op });
          return false;
        }
        if (!isRunnable(catchWorkflowDefinition, availableOperations, checkedWorkflows))
          return false;
      }
    }

    // Add the workflow to the list of checked workflows
    if (!checkedWorkflows.contains(workflowDefinition))
      checkedWorkflows.add(workflowDefinition);
    return true;
  }

  /**
   * Gets the currently registered workflow operation handlers.
   * 
   * @return All currently registered handlers
   */
  public Set<HandlerRegistration> getRegisteredHandlers() {
    Set<HandlerRegistration> set = new HashSet<HandlerRegistration>();
    ServiceReference[] refs;
    try {
      refs = componentContext.getBundleContext().getServiceReferences(WorkflowOperationHandler.class.getName(), null);
    } catch (InvalidSyntaxException e) {
      throw new IllegalStateException(e);
    }
    for (ServiceReference ref : refs) {
      WorkflowOperationHandler handler = (WorkflowOperationHandler) componentContext.getBundleContext().getService(ref);
      set.add(new HandlerRegistration((String) ref.getProperty(WORKFLOW_OPERATION_PROPERTY), handler));
    }
    return set;
  }

  protected WorkflowOperationHandler getWorkflowOperationHandler(String operationId) {
    for (HandlerRegistration reg : getRegisteredHandlers()) {
      if (reg.operationName.equals(operationId))
        return reg.handler;
    }
    return null;
  }

  /**
   * Lists the names of each workflow operation. Operation names are availalbe for use if there is a registered
   * {@link WorkflowOperationHandler} with an equal {@link WorkflowServiceImpl#WORKFLOW_OPERATION_PROPERTY} property.
   * 
   * @return The {@link List} of available workflow operation names
   */
  protected List<String> listAvailableOperationNames() {
    List<String> list = new ArrayList<String>();
    for (HandlerRegistration reg : getRegisteredHandlers()) {
      list.add(reg.operationName);
    }
    return list;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.workflow.api.WorkflowService#registerWorkflowDefinition(org.opencastproject.workflow.api.WorkflowDefinition)
   */
  public void registerWorkflowDefinition(WorkflowDefinition workflow) {
    if (workflow == null || workflow.getId() == null) {
      throw new IllegalArgumentException("Workflow must not be null, and must contain an ID");
    }
    String id = workflow.getId();
    if (workflowDefinitions.containsKey(id)) {
      throw new IllegalStateException("A workflow definition with ID '" + id + "' is already registered.");
    }
    workflowDefinitions.put(id, workflow);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.workflow.api.WorkflowService#unregisterWorkflowDefinition(java.lang.String)
   */
  public void unregisterWorkflowDefinition(String workflowDefinitionId) {
    workflowDefinitions.remove(workflowDefinitionId);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.workflow.api.WorkflowService#getWorkflowById(long)
   */
  public WorkflowInstanceImpl getWorkflowById(long id) throws WorkflowDatabaseException, NotFoundException,
          UnauthorizedException {
    try {
      Job job = serviceRegistry.getJob(id);
      if (Status.DELETED.equals(job.getStatus())) {
        throw new NotFoundException("Workflow '" + id + "' has been deleted");
      }
      if (JOB_TYPE.equals(job.getJobType()) && Operation.START_WORKFLOW.toString().equals(job.getOperation())) {
        WorkflowInstanceImpl workflow = WorkflowParser.parseWorkflowInstance(job.getPayload());
        assertPermission(workflow, READ_PERMISSION);
        return workflow;
      } else {
        throw new NotFoundException("'" + id + "' is a job identifier, but it is not a workflow identifier");
      }
    } catch (WorkflowParsingException e) {
      throw new IllegalStateException("The workflow job payload is malformed");
    } catch (ServiceRegistryException e) {
      throw new IllegalStateException("Error loading workflow job from the service registry");
    } catch (MediaPackageException e) {
      throw new IllegalStateException("Unable to read mediapackage from workflow " + id, e);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.workflow.api.WorkflowService#start(org.opencastproject.workflow.api.WorkflowDefinition,
   *      org.opencastproject.mediapackage.MediaPackage)
   */
  @Override
  public WorkflowInstance start(WorkflowDefinition workflowDefinition, MediaPackage mediaPackage)
          throws WorkflowDatabaseException, WorkflowParsingException {
    if (workflowDefinition == null)
      throw new IllegalArgumentException("workflow definition must not be null");
    if (mediaPackage == null)
      throw new IllegalArgumentException("mediapackage must not be null");
    Map<String, String> properties = new HashMap<String, String>();
    return start(workflowDefinition, mediaPackage, properties);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.workflow.api.WorkflowService#start(org.opencastproject.workflow.api.WorkflowDefinition,
   *      org.opencastproject.mediapackage.MediaPackage)
   */
  public WorkflowInstance start(WorkflowDefinition workflowDefinition, MediaPackage mediaPackage,
          Map<String, String> properties) throws WorkflowDatabaseException, WorkflowParsingException {
    try {
      return start(workflowDefinition, mediaPackage, null, properties);
    } catch (NotFoundException e) {
      // should never happen
      throw new IllegalStateException("a null workflow ID caused a NotFoundException.  This is a programming error.");
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.workflow.api.WorkflowService#start(org.opencastproject.workflow.api.WorkflowDefinition,
   *      org.opencastproject.mediapackage.MediaPackage, Long, java.util.Map)
   */
  @Override
  public WorkflowInstance start(WorkflowDefinition workflowDefinition, MediaPackage sourceMediaPackage,
          Long parentWorkflowId, Map<String, String> properties) throws WorkflowDatabaseException,
          WorkflowParsingException, NotFoundException {

    if (workflowDefinition == null)
      throw new IllegalArgumentException("workflow definition must not be null");
    if (sourceMediaPackage == null)
      throw new IllegalArgumentException("mediapackage must not be null");
    if (parentWorkflowId != null) {
      try {
        getWorkflowById(parentWorkflowId); // Let NotFoundException bubble up
      } catch (UnauthorizedException e) {
        throw new IllegalArgumentException("Parent workflow " + parentWorkflowId + " not visible to this user");
      }
    }

    // Get the current user
    User currentUser = securityService.getUser();
    if (currentUser == null)
      throw new SecurityException("Current user is unknown");

    // Get the current organization
    Organization organization = securityService.getOrganization();
    if (organization == null)
      throw new SecurityException("Current organization is unknown");
    
    WorkflowInstance workflowInstance = new WorkflowInstanceImpl(workflowDefinition, sourceMediaPackage,
            parentWorkflowId, currentUser, organization, properties);
    workflowInstance = updateConfiguration(workflowInstance, properties);

    // Create and configure the workflow instance
    try {
      // Create a new job for this workflow instance
      String workflowDefinitionXml = WorkflowParser.toXml(workflowDefinition);
      String workflowInstanceXml = WorkflowParser.toXml(workflowInstance);
      String mediaPackageXml = MediaPackageParser.getAsXml(sourceMediaPackage);

      List<String> arguments = new ArrayList<String>();
      arguments.add(workflowDefinitionXml);
      arguments.add(mediaPackageXml);
      if (parentWorkflowId != null || properties != null) {
        String parentWorkflowIdString = (parentWorkflowId != null) ? parentWorkflowId.toString() : NULL_PARENT_ID;
        arguments.add(parentWorkflowIdString);
      }
      if (properties != null) {
        arguments.add(mapToString(properties));
      }

      Job job = serviceRegistry.createJob(JOB_TYPE, Operation.START_WORKFLOW.toString(), arguments,
              workflowInstanceXml, false);

      // Have the workflow take on the job's identity
      workflowInstance.setId(job.getId());

      // Add the workflow to the search index and have the job enqueued for dispatch.
      // Update also sets ACL and mediapackage metadata
      update(workflowInstance);

      return workflowInstance;
    } catch (Throwable t) {
      try {
        workflowInstance.setState(FAILED);
        update(workflowInstance);
      } catch (Exception failureToFail) {
        logger.warn("Unable to update workflow to failed state", failureToFail);
      }
      throw new WorkflowDatabaseException(t);
    }
  }

  protected WorkflowInstance updateConfiguration(WorkflowInstance instance, Map<String, String> properties) {
    try {
      String xml = replaceVariables(WorkflowParser.toXml(instance), properties);
      WorkflowInstanceImpl workflow = WorkflowParser.parseWorkflowInstance(xml);
      return workflow;
    } catch (Exception e) {
      throw new IllegalStateException("Unable to replace workflow instance variables", e);
    }
  }

  /**
   * Replaces all occurrences of <code>${.*+}</code> with the property in the provided map, or if not available in the
   * map, from the bundle context properties, if available.
   * 
   * @param source
   *          The source string
   * @param properties
   *          The map of properties to replace
   * @return The resulting string
   */
  protected String replaceVariables(String source, Map<String, String> properties) {
    Matcher matcher = PROPERTY_PATTERN.matcher(source);
    StringBuilder result = new StringBuilder();
    int cursor = 0;
    boolean matchFound = matcher.find();
    if (!matchFound)
      return source;
    while (matchFound) {
      int matchStart = matcher.start();
      int matchEnd = matcher.end();
      result.append(source.substring(cursor, matchStart)); // add the content before the match
      String key = source.substring(matchStart + 2, matchEnd - 1);
      String systemProperty = componentContext == null ? null : componentContext.getBundleContext().getProperty(key);
      String providedProperty = null;
      if (properties != null) {
        providedProperty = properties.get(key);
      }
      if (isNotBlank(providedProperty)) {
        result.append(providedProperty);
      } else if (isNotBlank(systemProperty)) {
        result.append(systemProperty);
      } else {
        result.append(source.substring(matchStart, matchEnd)); // retain the original matched value
      }
      cursor = matchEnd;
      matchFound = matcher.find();
      if (!matchFound)
        result.append(source.substring(matchEnd, source.length()));
    }
    return result.toString();
  }

  /**
   * Does a lookup of available operation handlers for the given workflow operation.
   * 
   * @param operation
   *          the operation definition
   * @return the handler or <code>null</code>
   */
  protected WorkflowOperationHandler selectOperationHandler(WorkflowOperationInstance operation) {
    List<WorkflowOperationHandler> handlerList = new ArrayList<WorkflowOperationHandler>();
    for (HandlerRegistration handlerReg : getRegisteredHandlers()) {
      if (handlerReg.operationName != null && handlerReg.operationName.equals(operation.getTemplate())) {
        handlerList.add(handlerReg.handler);
      }
    }
    if (handlerList.size() > 1) {
      throw new IllegalStateException("Multiple operation handlers found for operation '" + operation.getTemplate()
              + "'");
    } else if (handlerList.size() == 1) {
      return handlerList.get(0);
    }
    logger.warn("No workflow operation handlers found for operation '{}'", operation.getTemplate());
    return null;
  }

  /**
   * Executes the workflow.
   * 
   * @param workflow
   *          the workflow instance
   * @throws WorkflowDatabaseException
   *           if the workflow instance can't be updated in the database
   * @throws WorkflowParsingException
   *           if the workflow instance can't be parsed
   */
  protected Job runWorkflow(WorkflowInstance workflow) throws WorkflowDatabaseException, WorkflowParsingException,
          UnauthorizedException {
    if (!INSTANTIATED.equals(workflow.getState()))
      throw new IllegalStateException("Cannot start a workflow in state '" + workflow.getState() + "'");

    // If this is a new workflow, move to the first operation
    workflow.setState(RUNNING);
    update(workflow);

    WorkflowOperationInstance operation = workflow.getCurrentOperation();

    if (operation == null)
      throw new IllegalStateException("Cannot start a workflow without a current operation");

    if (operation.getPosition() != 0)
      throw new IllegalStateException("Current operation expected to be first");

    try {
      Job job = serviceRegistry.createJob(JOB_TYPE, Operation.START_OPERATION.toString(),
              Arrays.asList(Long.toString(workflow.getId())), null, false);
      operation.setId(job.getId());
      update(workflow);
      job.setStatus(Status.QUEUED);
      job.setDispatchable(true);
      return serviceRegistry.updateJob(job);
    } catch (ServiceRegistryException e) {
      throw new WorkflowDatabaseException(e);
    } catch (NotFoundException e) {
      // this should be impossible
      throw new IllegalStateException("Unable to find a job that was just created");
    }

  }

  /**
   * Executes the workflow's current operation.
   * 
   * @param workflow
   *          the workflow
   * @param properties
   *          the properties that are passed in on resume
   * @return the processed workflow operation
   * @throws WorkflowDatabaseException
   *           if the workflow can't be updated in the database
   * @throws WorkflowParsingException
   *           if the workflow can't be parsed
   */
  protected WorkflowOperationInstance runWorkflowOperation(WorkflowInstance workflow, Map<String, String> properties)
          throws WorkflowDatabaseException, WorkflowParsingException, UnauthorizedException {
    WorkflowOperationInstance processingOperation = workflow.getCurrentOperation();
    if (processingOperation == null)
      throw new IllegalStateException("No operation to run, workflow is " + workflow.getState());

    // Keep the current state for later reference, it might have been changed from the outside
    WorkflowState initialState = workflow.getState();

    // Execute the operation handler
    WorkflowOperationHandler operationHandler = selectOperationHandler(processingOperation);
    WorkflowOperationWorker worker = new WorkflowOperationWorker(operationHandler, workflow, properties, this);
    workflow = worker.execute();

    // The workflow has been serialized/deserialized in between, so we need to refresh the reference
    int currentOperationPosition = processingOperation.getPosition();
    processingOperation = workflow.getOperations().get(currentOperationPosition);

    Long currentOperationJobId = processingOperation.getId();
    try {
      updateOperationJob(currentOperationJobId, processingOperation.getState());
    } catch (NotFoundException e) {
      throw new IllegalStateException("Unable to find a job that has already been running");
    } catch (ServiceRegistryException e) {
      throw new WorkflowDatabaseException(e);
    }

    // Move on to the next workflow operation
    WorkflowOperationInstance currentOperation = workflow.getCurrentOperation();

    // Is the workflow done?
    if (currentOperation == null) {

      // If we are in failing mode, we were simply working off an error handling workflow
      if (FAILING.equals(workflow.getState())) {
        workflow.setState(FAILED);
      }

      // Otherwise, let's make sure we didn't miss any failed operation, since the workflow state could have been
      // switched to paused while processing the error handling workflow extension
      else if (!FAILED.equals(workflow.getState())) {
        workflow.setState(SUCCEEDED);
        for (WorkflowOperationInstance op : workflow.getOperations()) {
          if (op.getState().equals(WorkflowOperationInstance.OperationState.FAILED)) {
            if (op.isFailWorkflowOnException()) {
              workflow.setState(FAILED);
              break;
            }
          }
        }
      }

      // Save the updated workflow to the database
      logger.debug("{} has {}", workflow, workflow.getState());
      update(workflow);

    } else {

      // Somebody might have set the workflow to "paused" from the outside, so take a look a the database first
      WorkflowState dbWorkflowState = null;
      try {
        dbWorkflowState = getWorkflowById(workflow.getId()).getState();
      } catch (WorkflowDatabaseException e) {
        throw new IllegalStateException("The workflow with ID " + workflow.getId()
                + " can not be accessed in the database", e);
      } catch (NotFoundException e) {
        throw new IllegalStateException("The workflow with ID " + workflow.getId()
                + " can not be found in the database", e);
      } catch (UnauthorizedException e) {
        throw new IllegalStateException("The workflow with ID " + workflow.getId() + " can not be read", e);
      }

      // If somebody changed the workflow state from the outside, that state should take precedence
      if (!dbWorkflowState.equals(initialState)) {
        logger.info("Workflow state for {} was changed to '{}' from the outside", workflow, dbWorkflowState);
        workflow.setState(dbWorkflowState);
      }

      // Save the updated workflow to the database

      Job job = null;
      switch (workflow.getState()) {
      case FAILED:
        update(workflow);
        break;
      case FAILING:
      case RUNNING:
        try {
          job = serviceRegistry.createJob(JOB_TYPE, Operation.START_OPERATION.toString(),
                  Arrays.asList(Long.toString(workflow.getId())), null, false);
          currentOperation.setId(job.getId());
          update(workflow);
          job.setStatus(Status.QUEUED);
          job.setDispatchable(true);
          serviceRegistry.updateJob(job);
        } catch (ServiceRegistryException e) {
          throw new WorkflowDatabaseException(e);
        } catch (NotFoundException e) {
          // this should be impossible
          throw new IllegalStateException("Unable to find a job that was just created");
        }
        break;
      case PAUSED:
      case STOPPED:
      case SUCCEEDED:
        update(workflow);
        break;
      case INSTANTIATED:
        update(workflow);
        throw new IllegalStateException("Impossible workflow state found during processing");
      default:
        throw new IllegalStateException("Unkown workflow state found during processing");
      }

    }

    return processingOperation;
  }

  /**
   * Returns the workflow identified by <code>id</code> or <code>null</code> if no such definition was found.
   * 
   * @param id
   *          the workflow definition id
   * @return the workflow
   */
  public WorkflowDefinition getWorkflowDefinitionById(String id) throws NotFoundException, WorkflowDatabaseException {
    WorkflowDefinition def = workflowDefinitions.get(id);
    if (def == null)
      throw new NotFoundException("Workflow definition '" + id + "' not found");
    return def;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.workflow.api.WorkflowService#stop(long)
   */
  public WorkflowInstance stop(long workflowInstanceId) throws WorkflowDatabaseException, WorkflowParsingException,
          NotFoundException, UnauthorizedException {
    WorkflowInstanceImpl instance = getWorkflowById(workflowInstanceId);
    instance.setState(STOPPED);
    update(instance);
    return instance;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.workflow.api.WorkflowService#remove(long)
   */
  public void remove(long workflowInstanceId) throws WorkflowDatabaseException, NotFoundException,
          UnauthorizedException, WorkflowParsingException {
    WorkflowQuery query = new WorkflowQuery();
    query.withId(Long.toString(workflowInstanceId));
    WorkflowSet workflows = index.getWorkflowInstances(query, READ_PERMISSION, false);
    if (workflows.size() == 1) {
      WorkflowInstance[] w = workflows.getItems();
      try {
        assertPermission(w[0], WRITE_PERMISSION);
      } catch (MediaPackageException e) {
        throw new WorkflowParsingException(e);
      }
      index.remove(workflowInstanceId);
    } else if (workflows.size() == 0) {
      throw new NotFoundException();
    } else {
      throw new WorkflowDatabaseException("More than one workflow found with id: " + Long.toString(workflowInstanceId));
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.workflow.api.WorkflowService#suspend(long)
   */
  public WorkflowInstance suspend(long workflowInstanceId) throws WorkflowDatabaseException, WorkflowParsingException,
          NotFoundException, UnauthorizedException {
    WorkflowInstanceImpl instance = getWorkflowById(workflowInstanceId);
    instance.setState(PAUSED);
    update(instance);
    return instance;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.workflow.api.WorkflowService#resume(long)
   */
  @Override
  public WorkflowInstance resume(long id) throws WorkflowDatabaseException, WorkflowParsingException,
          NotFoundException, UnauthorizedException {
    return resume(id, null);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.workflow.api.WorkflowService#resume(long, Map)
   */
  public WorkflowInstance resume(long workflowInstanceId, Map<String, String> properties)
          throws WorkflowDatabaseException, WorkflowParsingException, NotFoundException, UnauthorizedException {

    WorkflowInstance workflowInstance = getWorkflowById(workflowInstanceId);
    workflowInstance = updateConfiguration(workflowInstance, properties);
    update(workflowInstance);

    WorkflowOperationInstance currentOperation = workflowInstance.getCurrentOperation();

    // We can resume workflows when they are in either the paused state, or they are being advanced manually passed
    // certain operations. In the latter case, there is no current paused operation.
    if (OperationState.INSTANTIATED.equals(currentOperation.getState())) {
      try {
        // the operation has its own job. Update that too.
        Job operationJob = serviceRegistry.createJob(JOB_TYPE, Operation.START_OPERATION.toString(),
                Arrays.asList(Long.toString(workflowInstanceId)), null, false);

        // this method call is publicly visible, so it doesn't necessarily go through the accept method. Set the
        // workflow state manually.
        workflowInstance.setState(RUNNING);
        currentOperation.setId(operationJob.getId());

        // update the workflow and its associated job
        update(workflowInstance);

        // Now set this job to be queued so it can be dispatched
        operationJob.setStatus(Status.QUEUED);
        operationJob.setDispatchable(true);
        serviceRegistry.updateJob(operationJob);

        return workflowInstance;
      } catch (ServiceRegistryException e) {
        throw new WorkflowDatabaseException(e);
      }
    }

    Long operationJobId = workflowInstance.getCurrentOperation().getId();
    if (operationJobId == null) {
      throw new IllegalStateException("Can not resume a workflow where the current operation has no associated id");
    }

    // Set the current operation's job to queued, so it gets picked up again
    Job workflowJob;
    try {
      workflowJob = serviceRegistry.getJob(workflowInstanceId);
      workflowJob.setStatus(Status.RUNNING);
      workflowJob.setPayload(WorkflowParser.toXml(workflowInstance));
      serviceRegistry.updateJob(workflowJob);

      Job operationJob = serviceRegistry.getJob(operationJobId);
      operationJob.setStatus(Status.QUEUED);
      operationJob.setDispatchable(true);
      if (properties != null) {
        Properties props = new Properties();
        props.putAll(properties);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        props.store(out, null);
        List<String> newArguments = new ArrayList<String>(operationJob.getArguments());
        newArguments.add(new String(out.toByteArray(), "ISO-8859-1"));
        operationJob.setArguments(newArguments);
      }
      serviceRegistry.updateJob(operationJob);
    } catch (ServiceRegistryException e) {
      throw new WorkflowDatabaseException(e);
    } catch (IOException e) {
      throw new WorkflowParsingException("Unable to parse workflow and/or workflow properties");
    }

    return workflowInstance;
  }

  /**
   * Asserts that the current user has permission to take the provided action on a workflow instance.
   * 
   * @param workflow
   *          the workflow instance
   * @param action
   *          the action to ensure is permitted
   * @throws UnauthorizedException
   *           if the action is not authorized
   * @throws MediaPackageException
   *           if there is an error accessing the workflow's security policy in its mediapackage
   */
  protected void assertPermission(WorkflowInstance workflow, String action) throws UnauthorizedException,
          MediaPackageException {
    User currentUser = securityService.getUser();
    Organization currentOrg = securityService.getOrganization();
    String currentOrgAdminRole = currentOrg.getAdminRole();
    String currentOrgId = currentOrg.getId();

    MediaPackage mediapackage = workflow.getMediaPackage();
    User workflowCreator = workflow.getCreator();
    String workflowOrgId = workflowCreator.getOrganization();

    boolean authorized = currentUser.hasRole(GLOBAL_ADMIN_ROLE)
            || (currentUser.hasRole(currentOrgAdminRole) && currentOrgId.equals(workflowOrgId))
            || currentUser.equals(workflowCreator)
            || (authorizationService.hasPermission(mediapackage, WRITE_PERMISSION) && currentOrgId
                    .equals(workflowOrgId));

    if (!authorized) {
      throw new UnauthorizedException(currentUser, action);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.workflow.api.WorkflowService#update(org.opencastproject.workflow.api.WorkflowInstance)
   */
  public void update(final WorkflowInstance workflowInstance) throws WorkflowDatabaseException,
          WorkflowParsingException, UnauthorizedException {
    WorkflowInstance originalWorkflowInstance = null;
    try {
      originalWorkflowInstance = getWorkflowById(workflowInstance.getId());
    } catch (NotFoundException e) {
      // That's fine, it's a new workflow instance
    }

    if (originalWorkflowInstance != null) {
      try {
        assertPermission(originalWorkflowInstance, WRITE_PERMISSION);
      } catch (MediaPackageException e) {
        throw new WorkflowParsingException(e);
      }
    }

    // Before we persist this, extract the metadata
    MediaPackage updatedMediaPackage = workflowInstance.getMediaPackage();
    populateMediaPackageMetadata(updatedMediaPackage);
    String seriesId = updatedMediaPackage.getSeries();
    if (seriesId != null) {
      // If the mediapackage contains a series, find the series ACLs and add the security information to the
      // mediapackage
      try {
        AccessControlList acl = seriesService.getSeriesAccessControl(seriesId);
        authorizationService.setAccessControl(updatedMediaPackage, acl);
      } catch (SeriesException e) {
        throw new WorkflowDatabaseException(e);
      } catch (MediaPackageException e) {
        throw new WorkflowDatabaseException(e);
      } catch (NotFoundException e) {
        logger.warn("Series {} not found, unable to set ACLs", seriesId);
      }
    }

    // Synchronize the job status with the workflow
    WorkflowState workflowState = workflowInstance.getState();
    String xml;
    try {
      xml = WorkflowParser.toXml(workflowInstance);
    } catch (Exception e) {
      // Can't happen, since we are converting from an in-memory object
      throw new IllegalStateException("In-memory workflow instance could not be serialized", e);
    }

    Job job = null;
    try {
      job = serviceRegistry.getJob(workflowInstance.getId());
      job.setPayload(xml);

      // Synchronize workflow and job state
      switch (workflowState) {
      case FAILED:
        job.setStatus(Status.FAILED);
        break;
      case FAILING:
        break;
      case INSTANTIATED:
        job.setDispatchable(true);
        job.setStatus(Status.QUEUED);
        break;
      case PAUSED:
        job.setStatus(Status.PAUSED);
        break;
      case RUNNING:
        job.setStatus(Status.RUNNING);
        break;
      case STOPPED:
        job.setStatus(Status.DELETED);
        break;
      case SUCCEEDED:
        job.setStatus(Status.FINISHED);
        break;
      default:
        throw new IllegalStateException("Found a workflow state that is not handled");
      }

      index(workflowInstance);

      // Update the service registry
      serviceRegistry.updateJob(job);

    } catch (ServiceRegistryException e) {
      throw new WorkflowDatabaseException(e);
    } catch (NotFoundException e) {
      // this should never happen, since we create the job if it doesn't already exist
      throw new WorkflowDatabaseException(e);
    }

    try {
      WorkflowInstance clone = WorkflowParser.parseWorkflowInstance(WorkflowParser.toXml(workflowInstance));
      fireListeners(originalWorkflowInstance, clone);
    } catch (Exception e) {
      // Can't happen, since we are converting from an in-memory object
      throw new IllegalStateException("In-memory workflow instance could not be serialized", e);
    }
  }

  /**
   * Updates the search index entries for this workflow instance.
   * 
   * @param workflowInstance
   *          the workflow
   * @throws WorkflowDatabaseException
   *           if there is a problem storing the workflow instance
   */
  protected void index(final WorkflowInstance workflowInstance) throws WorkflowDatabaseException {
    // Update the search index
    index.update(workflowInstance);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.workflow.api.WorkflowService#countWorkflowInstances()
   */
  @Override
  public long countWorkflowInstances() throws WorkflowDatabaseException {
    return index.countWorkflowInstances(null, null);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.workflow.api.WorkflowService#countWorkflowInstances(org.opencastproject.workflow.api.WorkflowInstance.WorkflowState,
   *      java.lang.String)
   */
  @Override
  public long countWorkflowInstances(WorkflowState state, String operation) throws WorkflowDatabaseException {
    return index.countWorkflowInstances(state, operation);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.workflow.api.WorkflowService#getStatistics()
   */
  @Override
  public WorkflowStatistics getStatistics() throws WorkflowDatabaseException {
    return index.getStatistics();
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.workflow.api.WorkflowService#getWorkflowInstances(org.opencastproject.workflow.api.WorkflowQuery)
   */
  public WorkflowSet getWorkflowInstances(WorkflowQuery query) throws WorkflowDatabaseException {
    return index.getWorkflowInstances(query, READ_PERMISSION, true);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.workflow.api.WorkflowService#getWorkflowInstancesForAdministrativeRead(org.opencastproject.workflow.api.WorkflowQuery)
   */
  public WorkflowSet getWorkflowInstancesForAdministrativeRead(WorkflowQuery query) throws WorkflowDatabaseException,
          UnauthorizedException {
    User user = securityService.getUser();
    if (!user.hasRole(GLOBAL_ADMIN_ROLE)) {
      throw new UnauthorizedException(user, getClass().getName() + ".getForAdministrativeRead");
    }
    return index.getWorkflowInstances(query, READ_PERMISSION, false);
  }

  /**
   * Callback for workflow operations that were throwing an exception. This implementation assumes that the operation
   * worker has already adjusted the current operation's state appropriately.
   * 
   * @param workflow
   *          the workflow instance
   * @param e
   *          the exception
   * @return the workflow instance
   * @throws WorkflowParsingException
   */
  protected WorkflowInstance handleOperationException(WorkflowInstance workflow, WorkflowOperationException e)
          throws WorkflowDatabaseException, WorkflowParsingException, UnauthorizedException {
    // Add the exception's localized message to the workflow instance
    workflow.addErrorMessage(e.getLocalizedMessage());

    WorkflowOperationInstanceImpl currentOperation = (WorkflowOperationInstanceImpl) e.getOperation();
    int failedAttempt = currentOperation.getFailedAttempts() + 1;
    currentOperation.setFailedAttempts(failedAttempt);

    if (failedAttempt == currentOperation.getMaxAttempts()) {
      String errorDefId = currentOperation.getExceptionHandlingWorkflow();

      // Adjust the workflow state according to the setting on the operation
      if (currentOperation.isFailWorkflowOnException()) {
        if (StringUtils.isBlank(errorDefId)) {
          workflow.setState(FAILED);
        } else {
          workflow.setState(FAILING);

          // Remove the rest of the original workflow
          int currentOperationPosition = workflow.getOperations().indexOf(currentOperation);
          List<WorkflowOperationInstance> operations = new ArrayList<WorkflowOperationInstance>();
          operations.addAll(workflow.getOperations().subList(0, currentOperationPosition + 1));
          workflow.setOperations(operations);

          // Append the operations
          WorkflowDefinition errorDef = null;
          try {
            errorDef = getWorkflowDefinitionById(errorDefId);
            workflow.extend(errorDef);
          } catch (NotFoundException notFoundException) {
            throw new IllegalStateException("Unable to find the error workflow definition '" + errorDefId + "'");
          }
        }
      }

      // Fail the current operation
      currentOperation.setState(OperationState.FAILED);
    } else {
      // We're going to try again, so set the current operation to instantiated
      currentOperation.setState(OperationState.INSTANTIATED);
      runWorkflowOperation(workflow, null); // I *think* we don't need properties here, since this isn't a resume (jmh)
    }
    return workflow;
  }

  /**
   * Callback for workflow operation handlers that executed and finished without exception. This implementation assumes
   * that the operation worker has already adjusted the current operation's state appropriately.
   * 
   * @param workflow
   *          the workflow instance
   * @param result
   *          the workflow operation result
   * @return the workflow instance
   * @throws WorkflowDatabaseException
   *           if updating the workflow fails
   */
  protected WorkflowInstance handleOperationResult(WorkflowInstance workflow, WorkflowOperationResult result)
          throws WorkflowDatabaseException {

    // Get the operation and its handler
    WorkflowOperationInstanceImpl currentOperation = (WorkflowOperationInstanceImpl) workflow.getCurrentOperation();
    WorkflowOperationHandler handler = getWorkflowOperationHandler(currentOperation.getTemplate());

    // Create an operation result for the lazy or else update the workflow's media package
    if (result == null) {
      logger.warn("Handling a null operation result for workflow {} in operation {}", workflow.getId(),
              currentOperation.getTemplate());
      result = new WorkflowOperationResultImpl(workflow.getMediaPackage(), null, Action.CONTINUE, 0);
    } else {
      MediaPackage mp = result.getMediaPackage();
      if (mp != null) {
        workflow.setMediaPackage(mp);
      }
    }

    // The action to take
    Action action = result.getAction();

    // Update the workflow configuration. Update the reference to the current operation as well, since the workflow has
    // been serialized and deserialized in the meantime.
    int currentOperationPosition = currentOperation.getPosition();
    workflow = updateConfiguration(workflow, result.getProperties());
    currentOperation = (WorkflowOperationInstanceImpl) workflow.getOperations().get(currentOperationPosition);

    // Adjust workflow statistics
    currentOperation.setTimeInQueue(result.getTimeInQueue());

    // Adjust the operation state
    switch (action) {
    case CONTINUE:
      currentOperation.setState(OperationState.SUCCEEDED);
      break;
    case PAUSE:
      if (!(handler instanceof ResumableWorkflowOperationHandler)) {
        throw new IllegalStateException("Operation " + currentOperation.getTemplate() + " is not resumable");
      }

      // Set abortable and continuable to default values
      currentOperation.setContinuable(result.allowsContinue());
      currentOperation.setAbortable(result.allowsAbort());

      ResumableWorkflowOperationHandler resumableHandler = (ResumableWorkflowOperationHandler) handler;
      try {
        String url = resumableHandler.getHoldStateUserInterfaceURL(workflow);
        if (url != null) {
          String holdActionTitle = resumableHandler.getHoldActionTitle();
          currentOperation.setHoldActionTitle(holdActionTitle);
          currentOperation.setHoldStateUserInterfaceUrl(url);
        }
      } catch (WorkflowOperationException e) {
        logger.warn("unable to replace workflow ID in the hold state URL", e);
      }

      workflow.setState(PAUSED);
      currentOperation.setState(OperationState.PAUSED);
      break;
    case SKIP:
      currentOperation.setState(OperationState.SKIPPED);
      break;
    default:
      throw new IllegalStateException("Unknown action '" + action + "' returned");
    }

    return workflow;
  }

  /**
   * Reads the available metadata from the dublin core catalog (if there is one) and updates the mediapackage.
   * 
   * @param mp
   *          the media package
   */
  protected void populateMediaPackageMetadata(MediaPackage mp) {
    if (metadataServices.size() == 0) {
      logger.warn("No metadata services are registered, so no mediapackage metadata can be extracted from catalogs");
      return;
    }
    for (MediaPackageMetadataService metadataService : metadataServices) {
      MediaPackageMetadata metadata = metadataService.getMetadata(mp);
      if (metadata != null) {

        // Series identifier
        if (isNotBlank(metadata.getSeriesIdentifier())) {
          mp.setSeries(metadata.getSeriesIdentifier());
        }

        // Series title
        if (isNotBlank(metadata.getSeriesTitle())) {
          mp.setSeriesTitle(metadata.getSeriesTitle());
        }

        // Episode title
        if (isNotBlank(metadata.getTitle())) {
          mp.setTitle(metadata.getTitle());
        }

        // Episode date
        if (metadata.getDate() != null) {
          mp.setDate(metadata.getDate());
        }

        // Episode subjects
        if (metadata.getSubjects().length > 0) {
          if (mp.getSubjects() != null) {
            for (String subject : mp.getSubjects()) {
              mp.removeSubject(subject);
            }
          }
          for (String subject : metadata.getSubjects()) {
            mp.addSubject(subject);
          }
        }

        // Episode contributers
        if (metadata.getContributors().length > 0) {
          if (mp.getContributors() != null) {
            for (String contributor : mp.getContributors()) {
              mp.removeContributor(contributor);
            }
          }
          for (String contributor : metadata.getContributors()) {
            mp.addContributor(contributor);
          }
        }

        // Episode creators
        if (mp.getCreators().length == 0 && metadata.getCreators().length > 0) {
          if (mp.getCreators() != null) {
            for (String creator : mp.getCreators()) {
              mp.removeCreator(creator);
            }
          }
          for (String creator : metadata.getCreators()) {
            mp.addCreator(creator);
          }
        }

        // Episode license
        if (isNotBlank(metadata.getLicense())) {
          mp.setLicense(metadata.getLicense());
        }

        // Episode language
        if (isNotBlank(metadata.getLanguage())) {
          mp.setLanguage(metadata.getLanguage());
        }

      }
    }
  }

  /**
   * {@inheritDoc}
   * 
   * If we are already running the maximum number of workflows, don't accept another START_WORKFLOW job
   * 
   * @see org.opencastproject.job.api.AbstractJobProducer#isReadyToAccept(org.opencastproject.job.api.Job)
   */
  @Override
  public boolean isReadyToAccept(Job job) throws ServiceRegistryException {
    String operation = job.getOperation();
    if (Operation.START_WORKFLOW.toString().equals(operation)) {

      // If the first operation is guaranteed to pause, run the job.
      if (job.getArguments().size() > 1 && job.getArguments().get(0) != null) {
        try {
          WorkflowDefinition workflowDef = WorkflowParser.parseWorkflowDefinition(job.getArguments().get(0));
          String firstOperationId = workflowDef.getOperations().get(0).getId();
          WorkflowOperationHandler handler = getWorkflowOperationHandler(firstOperationId);
          if (handler instanceof ResumableWorkflowOperationHandler) {
            if (((ResumableWorkflowOperationHandler) handler).isAlwaysPause()) {
              return true;
            }
          }
        } catch (WorkflowParsingException e) {
          throw new IllegalStateException(job + " is not a proper job to start a workflow");
        }
      }

      long runningWorkflows;
      try {
        runningWorkflows = serviceRegistry.countByOperation(JOB_TYPE, Operation.START_WORKFLOW.toString(),
                Job.Status.RUNNING);
      } catch (ServiceRegistryException e) {
        logger.warn("Unable to determine the number of running workflows", e);
        return false;
      }

      // If no hard maximum has been configured, ask the service registry for the number of cores in the system
      int maxWorkflows = maxConcurrentWorkflows;
      if (maxWorkflows < 1) {
        maxWorkflows = serviceRegistry.getMaxConcurrentJobs();
      }

      // Reject if there's enough going on already.
      if (runningWorkflows >= maxWorkflows) {
        logger.debug("Refused to accept dispatched job '{}'. This server is already running {} workflows.", job,
                runningWorkflows);
        return false;
      }
    }
    return true;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.job.api.AbstractJobProducer#acceptJob(org.opencastproject.job.api.Job)
   */
  @Override
  public synchronized boolean acceptJob(Job job) throws ServiceRegistryException {
    if (isReadyToAccept(job)) {
      User originalUser = securityService.getUser();
      Organization originalOrg = securityService.getOrganization();
      try {
        Organization organization = organizationDirectoryService.getOrganization(job.getOrganization());
        securityService.setOrganization(organization);
        User user = userDirectoryService.loadUser(job.getCreator());
        securityService.setUser(user);
        job.setStatus(Job.Status.RUNNING);
        serviceRegistry.updateJob(job);
        executorService.submit(new JobRunner(job));
        return true;
      } catch (Exception e) {
        if (e instanceof ServiceRegistryException)
          throw (ServiceRegistryException) e;
        throw new ServiceRegistryException(e);
      } finally {
        securityService.setUser(originalUser);
        securityService.setOrganization(originalOrg);
      }
    } else {
      return false;
    }
  }

  /**
   * Processes the workflow job.
   * 
   * @param job
   *          the job
   * @return the job payload
   * @throws Exception
   *           if job processing fails
   */
  protected String process(Job job) throws Exception {
    List<String> arguments = job.getArguments();
    Operation op = null;
    WorkflowInstance workflowInstance = null;
    WorkflowOperationInstance wfo = null;
    String operation = job.getOperation();
    try {
      try {
        op = Operation.valueOf(operation);
        switch (op) {
        case START_WORKFLOW:
          workflowInstance = WorkflowParser.parseWorkflowInstance(job.getPayload());
          logger.debug("Starting new workflow {}", workflowInstance);
          runWorkflow(workflowInstance);
          break;
        case RESUME:
          workflowInstance = getWorkflowById(Long.parseLong(arguments.get(0)));
          wfo = workflowInstance.getCurrentOperation();
          Map<String, String> properties = null;
          if (arguments.size() > 1) {
            Properties props = new Properties();
            props.load(IOUtils.toInputStream(arguments.get(arguments.size() - 1)));
            properties = new HashMap<String, String>();
            for (Entry<Object, Object> entry : props.entrySet()) {
              properties.put(entry.getKey().toString(), entry.getValue().toString());
            }
          }
          logger.debug("Resuming {} at {}", workflowInstance, workflowInstance.getCurrentOperation());
          workflowInstance.setState(RUNNING);
          update(workflowInstance);
          wfo = runWorkflowOperation(workflowInstance, properties);
          break;
        case START_OPERATION:
          workflowInstance = getWorkflowById(Long.parseLong(arguments.get(0)));
          wfo = workflowInstance.getCurrentOperation();
          logger.debug("Running {} {}", workflowInstance, wfo);
          wfo = runWorkflowOperation(workflowInstance, null);
          updateOperationJob(job.getId(), wfo.getState());
          break;
        default:
          throw new IllegalStateException("Don't know how to handle operation '" + operation + "'");
        }
      } catch (IllegalArgumentException e) {
        throw new ServiceRegistryException("This service can't handle operations of type '" + op + "'", e);
      } catch (IndexOutOfBoundsException e) {
        throw new ServiceRegistryException("This argument list for operation '" + op + "' does not meet expectations",
                e);
      }
      return null;
    } catch (Exception e) {
      logger.warn("Exception while accepting job " + job, e);
      try {
        if (workflowInstance != null) {
          workflowInstance.setState(FAILED);
          update(workflowInstance);
        } else {
          logger.warn("Unable to parse workflow instance", e);
        }
      } catch (WorkflowDatabaseException e1) {
        throw new ServiceRegistryException(e1);
      }
      if (e instanceof ServiceRegistryException)
        throw (ServiceRegistryException) e;
      throw new ServiceRegistryException("Error handling operation '" + op + "'", e);
    }
  }

  /**
   * Synchronizes the workflow operation's job with the operation status if the operation has a job associated with it,
   * which is determined by looking at the operation's job id.
   * 
   * @param state
   *          the operation state
   * @param job
   *          the associated job
   * @return the updated job or <code>null</code> if there is no job for this operation
   * @throws ServiceRegistryException
   *           if the job can't be updated in the service registry
   * @throws NotFoundException
   *           if the job can't be found
   */
  private Job updateOperationJob(Long jobId, OperationState state) throws NotFoundException, ServiceRegistryException {
    if (jobId == null)
      return null;
    Job job = serviceRegistry.getJob(jobId);
    switch (state) {
    case FAILED:
      job.setStatus(Status.FAILED);
      break;
    case PAUSED:
      job.setStatus(Status.PAUSED);
      job.setOperation(Operation.RESUME.toString());
      break;
    case SKIPPED:
    case SUCCEEDED:
      job.setStatus(Status.FINISHED);
      break;
    default:
      throw new IllegalStateException("Unexpected state '" + state + "' found");
    }
    return serviceRegistry.updateJob(job);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.job.api.JobProducer#countJobs(org.opencastproject.job.api.Job.Status)
   */
  @Override
  public long countJobs(Status status) throws ServiceRegistryException {
    return serviceRegistry.count(JOB_TYPE, status);
  }

  /**
   * Converts a Map<String, String> to s key=value\n string, suitable for the properties form parameter expected by the
   * workflow rest endpoint.
   * 
   * @param props
   *          The map of strings
   * @return the string representation
   */
  private String mapToString(Map<String, String> props) {
    if (props == null)
      return null;
    StringBuilder sb = new StringBuilder();
    for (Entry<String, String> entry : props.entrySet()) {
      sb.append(entry.getKey());
      sb.append("=");
      sb.append(entry.getValue());
      sb.append("\n");
    }
    return sb.toString();
  }

  /**
   * Callback for the OSGi environment to register with the <code>ServiceRegistry</code>.
   * 
   * @param registry
   *          the service registry
   */
  protected void setServiceRegistry(ServiceRegistry registry) {
    this.serviceRegistry = registry;
  }

  /**
   * Callback for setting the security service.
   * 
   * @param securityService
   *          the securityService to set
   */
  public void setSecurityService(SecurityService securityService) {
    this.securityService = securityService;
  }

  /**
   * Callback for setting the authorization service.
   * 
   * @param authorizationService
   *          the authorizationService to set
   */
  public void setAuthorizationService(AuthorizationService authorizationService) {
    this.authorizationService = authorizationService;
  }

  /**
   * Callback for setting the user directory service
   * 
   * @param userDirectoryService
   *          the userDirectoryService to set
   */
  public void setUserDirectoryService(UserDirectoryService userDirectoryService) {
    this.userDirectoryService = userDirectoryService;
  }

  /**
   * Sets a reference to the organization directory service.
   * 
   * @param organizationDirectory
   *          the organization directory
   */
  public void setOrganizationDirectoryService(OrganizationDirectoryService organizationDirectory) {
    this.organizationDirectoryService = organizationDirectory;
  }

  /**
   * Sets the search indexer to use in this service.
   * 
   * @param index
   *          The search index
   */
  protected void setDao(WorkflowServiceIndex index) {
    this.index = index;
  }

  /**
   * Sets the series service
   * 
   * @param seriesService
   *          the seriesService to set
   */
  public void setSeriesService(SeriesService seriesService) {
    this.seriesService = seriesService;
  }

  /**
   * Callback to set the metadata service
   * 
   * @param service
   *          the metadata service
   */
  protected void addMetadataService(MediaPackageMetadataService service) {
    metadataServices.add(service);
  }

  /**
   * Callback to remove a mediapackage metadata service.
   * 
   * @param service
   *          the mediapackage metadata service to remove
   */
  protected void removeMetadataService(MediaPackageMetadataService service) {
    metadataServices.remove(service);
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.opencastproject.job.api.JobProducer#getJobType()
   */
  @Override
  public String getJobType() {
    return JOB_TYPE;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.osgi.service.cm.ManagedService#updated(java.util.Dictionary)
   */
  @Override
  @SuppressWarnings("rawtypes")
  public void updated(Dictionary properties) throws ConfigurationException {
    String maxConfiguration = StringUtils.trimToNull((String) properties.get(MAX_CONCURRENT_CONFIG_KEY));
    if (maxConfiguration != null) {
      try {
        maxConcurrentWorkflows = Integer.parseInt(maxConfiguration);
        logger.info("Set maximum concurrent workflows to {}", maxConcurrentWorkflows);
      } catch (NumberFormatException e) {
        logger.warn("Can not set max concurrent workflows to {}. {} must be an integer", maxConfiguration,
                MAX_CONCURRENT_CONFIG_KEY);
      }
    }
  }

  /**
   * A tuple of a workflow operation handler and the name of the operation it handles
   */
  public static class HandlerRegistration {

    protected WorkflowOperationHandler handler;
    protected String operationName;

    public HandlerRegistration(String operationName, WorkflowOperationHandler handler) {
      if (operationName == null)
        throw new IllegalArgumentException("Operation name cannot be null");
      if (handler == null)
        throw new IllegalArgumentException("Handler cannot be null");
      this.operationName = operationName;
      this.handler = handler;
    }

    public WorkflowOperationHandler getHandler() {
      return handler;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + handler.hashCode();
      result = prime * result + operationName.hashCode();
      return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      HandlerRegistration other = (HandlerRegistration) obj;
      if (!handler.equals(other.handler))
        return false;
      if (!operationName.equals(other.operationName))
        return false;
      return true;
    }
  }

  /**
   * A utility class to run jobs
   */
  class JobRunner implements Callable<Void> {

    /** The job */
    private Job job = null;

    /**
     * Constructs a new job runner
     * 
     * @param job
     *          the job to run
     */
    JobRunner(Job job) {
      this.job = job;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.concurrent.Callable#call()
     */
    @Override
    public Void call() throws Exception {
      Organization jobOrganization = organizationDirectoryService.getOrganization(job.getOrganization());
      try {
        securityService.setOrganization(jobOrganization);
        User jobUser = userDirectoryService.loadUser(job.getCreator());
        securityService.setUser(jobUser);
        process(job);
      } finally {
        securityService.setUser(null);
        securityService.setOrganization(null);
      }
      return null;
    }
  }

}
