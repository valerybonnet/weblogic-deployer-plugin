/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic;

import hudson.model.Action;
import hudson.model.AbstractBuild;

import java.io.Serializable;

import org.codehaus.plexus.util.StringUtils;
import org.jenkinsci.plugins.deploy.weblogic.Messages;
import org.jenkinsci.plugins.deploy.weblogic.data.DeploymentTaskResult;
import org.jenkinsci.plugins.deploy.weblogic.data.WebLogicDeploymentStatus;
import org.jenkinsci.plugins.deploy.weblogic.data.WeblogicEnvironment;
import org.jenkinsci.plugins.deploy.weblogic.properties.WebLogicDeploymentPluginConstantes;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

/**
 * @author rchaumie
 *
 */
@ExportedBean(defaultVisibility = 999)
public class WatchingWeblogicDeploymentLogsAction implements Action, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5479554061667005120L;

	private static transient final String iconFileName = WebLogicDeploymentPluginConstantes.PLUGIN_RESOURCES_PATH + "/icons/48x48/BEA.jpg";
	
	private static transient final String urlName = "deploymentLogs";
	
	@Exported(name="status")
	@Deprecated
	public WebLogicDeploymentStatus deploymentActionStatus;
	
	private AbstractBuild<?, ?> build;
	
	@Exported(name="result")
	public DeploymentTaskResult result;
	
	@Exported(name="target")
	@Deprecated
	public WeblogicEnvironment target;
	
	private transient boolean isLogsAvailable = false;
	
	/**
	 * 
	 */
	public WatchingWeblogicDeploymentLogsAction(){
		super();
	}
	
	/**
	 * 
	 * @param deploymentActionStatus
	 * @param b
	 */
	public WatchingWeblogicDeploymentLogsAction(DeploymentTaskResult result, AbstractBuild<?, ?> b){
		this.build = b;
//		this.deploymentActionStatus = deploymentActionStatus;
//		this.target = target;
		this.result = result;
		
		//lien vers les logs uniquement si pas d'execution
		switch(result.getStatus()){
			case ABORTED:
			case DISABLED:
				this.isLogsAvailable = false;
				break;
			default :
				this.isLogsAvailable = true;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see hudson.model.Action#getDisplayName()
	 */
	public String getDisplayName() {
		return this.isLogsAvailable ? Messages.WatchingWeblogicDeploymentLogsAction_DisplayName() : Messages.WatchingWeblogicDeploymentLogsAction_MissingLogs();
	}

	/*
	 * (non-Javadoc)
	 * @see hudson.model.Action#getIconFileName()
	 */
	public String getIconFileName() {
		return iconFileName;
	}
	
	/*
	 * (non-Javadoc)
	 * @see hudson.model.Action#getUrlName()
	 */
	public String getUrlName() {
		return this.isLogsAvailable ? urlName : "#";
	}
	
	/**
	 * 
	 * @return
	 */
	public WebLogicDeploymentStatus getDeploymentActionStatus() {
		return this.result != null ? this.result.getStatus() : this.deploymentActionStatus;
	}
	
	/**
	 * 
	 * @return
	 */
	public AbstractBuild<?, ?> getBuild() {
        return build;
    }

	/**
	 * @return the target
	 * @deprecated
	 */
	public WeblogicEnvironment getTarget() {
		return target;
	}

	/**
	 * @return the task result label
	 */
	public String getActionLabel() {
		String actionLabel = "";
		if(StringUtils.isNotBlank(this.result.getResourceName())) {
			actionLabel = this.result.getResourceName();
		} else if(StringUtils.isNotBlank(this.result.getTask().getTaskName())) {
			actionLabel = this.result.getTask().getTaskName();
		} else {
			actionLabel = this.result.getTask().getId();
		}
		
		return StringUtils.defaultString(actionLabel,"").concat("#").concat(StringUtils.defaultString(this.result.getTask().getWeblogicEnvironmentTargetedName(),""));
	}
	
}
