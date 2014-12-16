/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic;

import hudson.model.Action;
import hudson.model.AbstractBuild;

import java.io.Serializable;
import java.util.List;

import org.jenkinsci.plugins.deploy.weblogic.data.DeploymentTaskResult;
import org.jenkinsci.plugins.deploy.weblogic.properties.WebLogicDeploymentPluginConstantes;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

/**
 * @author rchaumie
 *
 */
@ExportedBean(defaultVisibility = 999)
public class WatchingWeblogicDeploymentAction implements Action, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5479554061667005120L;

	private static transient final String iconFileName = WebLogicDeploymentPluginConstantes.PLUGIN_RESOURCES_PATH + "/icons/48x48/BEA.png";
	
	private static transient final String urlName = "deployment";
	
	private AbstractBuild<?, ?> build;
	
	@Exported(name="results")
	public List<DeploymentTaskResult> results;
	
	/**
	 * 
	 */
	public WatchingWeblogicDeploymentAction(){
		super();
	}
	
	/**
	 * 
	 * @param results the results of deployment tasks
	 * @param b
	 */
	public WatchingWeblogicDeploymentAction(List<DeploymentTaskResult> results, AbstractBuild<?, ?> b){
		this.build = b;
		this.results = results;
	}
	
	/*
	 * (non-Javadoc)
	 * @see hudson.model.Action#getDisplayName()
	 */
	public String getDisplayName() {
		return Messages.WatchingWeblogicDeploymentAction_DisplayName();
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
		return urlName;
	}
	
	
	/**
	 * @return the results
	 */
	public List<DeploymentTaskResult> getResults() {
		return results;
	}

	/**
	 * 
	 * @return the build
	 */
	public AbstractBuild<?, ?> getBuild() {
        return build;
    }
	
	
	
}
