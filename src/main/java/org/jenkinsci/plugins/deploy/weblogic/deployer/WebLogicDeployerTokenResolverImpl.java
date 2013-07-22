/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic.deployer;

/**
 * @author RaphaelC
 *
 */
public class WebLogicDeployerTokenResolverImpl implements WebLogicDeployerTokenResolver {

	/*
	 * (non-Javadoc)
	 * @see org.jenkinsci.plugins.deploy.weblogic.deployer.WebLogicDeployerTokenResolver#resolveKey(java.lang.String, org.jenkinsci.plugins.deploy.weblogic.deployer.WebLogicDeployerParameters)
	 */
	public String resolveKey(String key, WebLogicDeployerParameters parameters) {
		
		if(WebLogicDeployerTokenResolver.WL_DEPLOYMENT_CMD_DEPLOYMENT_NAME_TOKEN.equalsIgnoreCase(key)){
			return parameters.getDeploymentName();
		}
		if(WebLogicDeployerTokenResolver.WL_DEPLOYMENT_CMD_HOST_TOKEN.equalsIgnoreCase(key)){
			return parameters.getEnvironment().getHost();
		}
		if(WebLogicDeployerTokenResolver.WL_DEPLOYMENT_CMD_LOGIN_TOKEN.equalsIgnoreCase(key)){
			return parameters.getEnvironment().getLogin();
		}
		if(WebLogicDeployerTokenResolver.WL_DEPLOYMENT_CMD_PASSWORD_TOKEN.equalsIgnoreCase(key)){
			return parameters.getEnvironment().getPassword();
		}
		if(WebLogicDeployerTokenResolver.WL_DEPLOYMENT_CMD_PORT_TOKEN.equalsIgnoreCase(key)){
			return parameters.getEnvironment().getPort();
		}
		
		if(WebLogicDeployerTokenResolver.WL_DEPLOYMENT_CMD_SOURCE_TOKEN.equalsIgnoreCase(key)){
			return parameters.getSource();
		}
		if(WebLogicDeployerTokenResolver.WL_DEPLOYMENT_CMD_TARGETS_TOKEN.equalsIgnoreCase(key)){
			return parameters.getDeploymentTargets();
		}
		if(WebLogicDeployerTokenResolver.WL_DEPLOYMENT_CMD_USER_CONFIGFILE_TOKEN.equalsIgnoreCase(key)){
			return parameters.getEnvironment().getUserconfigfile();
		}
		if(WebLogicDeployerTokenResolver.WL_DEPLOYMENT_CMD_USER_KEYFILE_TOKEN.equalsIgnoreCase(key)){
			return parameters.getEnvironment().getUserkeyfile();
		}
		return null;
	}

}
