/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic;

import hudson.model.AbstractBuild;

import java.io.File;

/**
 * @author Raphael
 *
 */
public class WeblogicDeploymentPluginLog {

	private static final String WEBLOGIC_DEPLOYMENT_LOG_FILENAME = "deploymentLog.txt";
	
	/**
	 * 
	 * @param build
	 * @return
	 */
	public static File getDeploymentLogFile(AbstractBuild<?,?> build) {
		return new File(build.getRootDir(),WEBLOGIC_DEPLOYMENT_LOG_FILENAME);
	}
}
