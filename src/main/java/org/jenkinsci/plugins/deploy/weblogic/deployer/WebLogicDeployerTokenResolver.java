/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic.deployer;

/**
 * @author RaphaelC
 *
 */
public interface WebLogicDeployerTokenResolver {

	static final String WL_DEPLOYMENT_CMD_TOKEN_PREF = "wl.";
	
	static final String WL_DEPLOYMENT_CMD_DEPLOYMENT_NAME_TOKEN = WL_DEPLOYMENT_CMD_TOKEN_PREF + "deployment_name";
	
	static final String WL_DEPLOYMENT_CMD_SOURCE_TOKEN = WL_DEPLOYMENT_CMD_TOKEN_PREF + "source";
	
	static final String WL_DEPLOYMENT_CMD_TARGETS_TOKEN = WL_DEPLOYMENT_CMD_TOKEN_PREF + "targets";
	
	static final String WL_DEPLOYMENT_CMD_HOST_TOKEN = WL_DEPLOYMENT_CMD_TOKEN_PREF + "host";
	
	static final String WL_DEPLOYMENT_CMD_PORT_TOKEN = WL_DEPLOYMENT_CMD_TOKEN_PREF + "port";
	
	static final String WL_DEPLOYMENT_CMD_LOGIN_TOKEN = WL_DEPLOYMENT_CMD_TOKEN_PREF + "login";
	
	static final String WL_DEPLOYMENT_CMD_PASSWORD_TOKEN = WL_DEPLOYMENT_CMD_TOKEN_PREF + "password";
	
	static final String WL_DEPLOYMENT_CMD_USER_CONFIGFILE_TOKEN = WL_DEPLOYMENT_CMD_TOKEN_PREF + "user_configfile";
	
	static final String WL_DEPLOYMENT_CMD_USER_KEYFILE_TOKEN = WL_DEPLOYMENT_CMD_TOKEN_PREF + "user_keyfile";
	
	static final String WL_DEPLOYMENT_CMD_DEPLOYMENT_PLAN_TOKEN = WL_DEPLOYMENT_CMD_TOKEN_PREF + "deployment_plan";
	
	/**
	 * 
	 * @param key
	 * @param parameters
	 * @return
	 */
	String resolveKey(String key, WebLogicDeployerParameters parameters);
}
