/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic.data;

import hudson.model.JDK;

import java.io.Serializable;

import org.apache.commons.lang.RandomStringUtils;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author Raphael
 *
 */
public class DeploymentTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3924420945973321189L;

	/**
	 * Identify the task
	 */
	private String id = RandomStringUtils.randomAlphanumeric(10);
	
	/**
     * Identifies {@link WeblogicEnvironment} to be used.
     */
	private String weblogicEnvironmentTargetedName;
	
	/**
	 * Le nom de deploiement. Si null on n'utilisera le nom de l'artifact
	 */
	private String deploymentName;
	
	/**
	 * Les targets de deploiement. Par defaut AdminServer
	 */
	private String deploymentTargets = "AdminServer";
	
	/**
	 * L'artifact est une librairie
	 */
	private boolean isLibrary;
	
	/**
	 * Regex permettant de filtrer la ressource Ã  deployer si plusieurs ressources 
	 * correspondantes sont trouvÃ©es
	 */
	private String builtResourceRegexToDeploy;
	
	/**
	 * Repertoire parent dans lequel la ressource à deployer peut etre localisée.
	 * Utilise principalement pour les job non maven dans le cas où la ressource
	 * à deployer ne se trouve pas dans le workspace
	 */
	private String baseResourcesGeneratedDirectory;
	
	/**
	 * The task name (optional)
	 */
	private String taskName;
	
	/**
	 * The JDK to use
	 */
	private JDK jdk;
	
	@DataBoundConstructor
	public DeploymentTask(String id, String taskName, String weblogicEnvironmentTargetedName, String deploymentName, 
  		String deploymentTargets, boolean isLibrary, String builtResourceRegexToDeploy, String baseResourcesGeneratedDirectory, JDK jdk) {
		if (id != null) {
			this.id = id;
		}
		this.taskName = taskName;
		this.weblogicEnvironmentTargetedName = weblogicEnvironmentTargetedName;
		this.deploymentName = deploymentName;
		this.deploymentTargets = deploymentTargets;
		this.isLibrary = isLibrary;
		this.builtResourceRegexToDeploy = builtResourceRegexToDeploy;
		this.baseResourcesGeneratedDirectory = baseResourcesGeneratedDirectory;
		this.jdk = jdk;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getWeblogicEnvironmentTargetedName() {
		return weblogicEnvironmentTargetedName;
	}
	
	/**
	 * 	
	 * @return
	 */
	public String getDeploymentName() {
		return deploymentName;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDeploymentTargets() {
		return deploymentTargets;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getIsLibrary() {
		return isLibrary;
	}

	/**
	 * @return the builtResourceRegexToDeploy
	 */
	public String getBuiltResourceRegexToDeploy() {
		return builtResourceRegexToDeploy;
	}

	/**
	 * @param builtResourceRegexToDeploy the builtResourceRegexToDeploy to set
	 */
	public void setBuiltResourceRegexToDeploy(String builtResourceRegexToDeploy) {
		this.builtResourceRegexToDeploy = builtResourceRegexToDeploy;
	}
	
	/**
	 * @return the baseResourcesGeneratedDirectory
	 */
	public String getBaseResourcesGeneratedDirectory() {
		return baseResourcesGeneratedDirectory;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the taskName
	 */
	public String getTaskName() {
		return taskName;
	}

	/**
	 * @return the jdk
	 */
	public JDK getJdk() {
		return jdk;
	}
	
}
