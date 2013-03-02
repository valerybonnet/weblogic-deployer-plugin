/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic.data;

/**
 * @author Raphael
 *
 */
public class DeploymentTaskResult {

	private WebLogicDeploymentStatus status;
	
	private WeblogicEnvironment environment;
	
	private String artifactName;
	
	/**
	 * 
	 * @param status
	 * @param environment
	 */
	public DeploymentTaskResult(WebLogicDeploymentStatus status, WeblogicEnvironment environment, String artifactName) {
		this.environment = environment;
		this.status = status;
		this.artifactName = artifactName;
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
	 * @return the environment
	 */
	public WeblogicEnvironment getEnvironment() {
		return environment;
	}

	/**
	 * @param environment the environment to set
	 */
	public void setEnvironment(WeblogicEnvironment environment) {
		this.environment = environment;
	}

	/**
	 * @return the artifactName
	 */
	public String getArtifactName() {
		return artifactName;
	}

	/**
	 * @param artifactName the artifactName to set
	 */
	public void setArtifactName(String artifactName) {
		this.artifactName = artifactName;
	}
	
}
