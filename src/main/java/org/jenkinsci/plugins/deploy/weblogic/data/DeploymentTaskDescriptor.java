/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic.data;

import hudson.Extension;
import hudson.model.Descriptor;

import org.jenkinsci.plugins.deploy.weblogic.Messages;

/**
 * @author Raphael
 *
 */
@Extension
public class DeploymentTaskDescriptor extends Descriptor<DeploymentTask> {

	
	/**
	 * 
	 */
	public DeploymentTaskDescriptor() {
		super(DeploymentTask.class);
	}

	/*
	 * (non-Javadoc)
	 * @see hudson.model.Descriptor#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return Messages.DeploymentTaskDescriptor_DisplayName();
	}

	/**
	 * @return the weblogicStageMode
	 */
	public WebLogicStageMode[] getWeblogicStageModes() {
		return WebLogicStageMode.values();
	}
    
    
}
