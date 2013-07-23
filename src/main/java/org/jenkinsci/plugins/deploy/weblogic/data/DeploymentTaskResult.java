/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic.data;

import org.codehaus.plexus.util.StringUtils;

/**
 * @author Raphael
 *
 */
public class DeploymentTaskResult {

	private WebLogicDeploymentStatus status;
	
	private WebLogicPreRequisteStatus check;
	
	private DeploymentTask task;
	
	private String resourceName;
	
	private static final String PLUGIN_EXECUTION_CHECK_FAILED = "PLUGIN_EXECUTION_CHECK_FAILED";
	
	/**
	 * 
	 * @param status
	 * @param environment
	 */
	public DeploymentTaskResult(WebLogicPreRequisteStatus check, WebLogicDeploymentStatus status, DeploymentTask task, String resourceName) {
		this.check = check;
		this.task = task;
		this.status = status;
		this.resourceName = resourceName;
	}

	/**
	 * @return the task result label
	 */
	public String getLabel() {
		
		String actionLabel = null;
		
		if(this.check != null && this.check != WebLogicPreRequisteStatus.OK){
			return PLUGIN_EXECUTION_CHECK_FAILED;
		}
		
		if(StringUtils.isNotBlank(this.getResourceName())) {
			actionLabel = this.getResourceName();
		} else if(this.getTask() != null){
			if(StringUtils.isNotBlank(this.getTask().getTaskName())) {
				actionLabel = this.getTask().getTaskName();
			} else {
				actionLabel = this.getTask().getId();
			}
		}
		
		if(this.getTask() != null){
			return StringUtils.defaultString(actionLabel,"").concat("#").concat(StringUtils.defaultString(this.getTask().getWeblogicEnvironmentTargetedName(),""));
		} else {
			return StringUtils.defaultString(actionLabel,"");
		}
	}
	
	/**
	 * @return the status
	 */
	public WebLogicDeploymentStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(WebLogicDeploymentStatus status) {
		this.status = status;
	}

	/**
	 * @return the task
	 */
	public DeploymentTask getTask() {
		return task;
	}

	/**
	 * @param task the task to set
	 */
	public void setTask(DeploymentTask task) {
		this.task = task;
	}

	/**
	 * @return the resourceName
	 */
	public String getResourceName() {
		return resourceName;
	}

	/**
	 * @param resourceName the resourceName to set
	 */
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	/**
	 * 
	 * @return
	 */
	public WebLogicPreRequisteStatus getCheck() {
		return check;
	}

	/**
	 * 
	 * @param check
	 */
	public void setCheck(WebLogicPreRequisteStatus check) {
		this.check = check;
	}
	
}
