/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic.deployer;

/**
 * @author RaphaelC
 *
 */
public interface WebLogicDeployerTokenResolver {

	public static final String WL_DEPLOYMENT_CMD_TOKEN_PREF = "wl.";
	
	public static final String WL_DEPLOYMENT_CMD_DEPLOYMENT_NAME_TOKEN = WL_DEPLOYMENT_CMD_TOKEN_PREF + "deployment_name";
	
	public static final String WL_DEPLOYMENT_CMD_SOURCE_TOKEN = WL_DEPLOYMENT_CMD_TOKEN_PREF + "source";
	
	public static final String WL_DEPLOYMENT_CMD_TARGETS_TOKEN = WL_DEPLOYMENT_CMD_TOKEN_PREF + "targets";
	
	public static final String WL_DEPLOYMENT_CMD_HOST_TOKEN = WL_DEPLOYMENT_CMD_TOKEN_PREF + "host";
	
	public static final String WL_DEPLOYMENT_CMD_PORT_TOKEN = WL_DEPLOYMENT_CMD_TOKEN_PREF + "port";
	
	public static final String WL_DEPLOYMENT_CMD_LOGIN_TOKEN = WL_DEPLOYMENT_CMD_TOKEN_PREF + "login";
	
	public static final String WL_DEPLOYMENT_CMD_PASSWORD_TOKEN = WL_DEPLOYMENT_CMD_TOKEN_PREF + "password";
	
	public static final String WL_DEPLOYMENT_CMD_USER_CONFIGFILE_TOKEN = WL_DEPLOYMENT_CMD_TOKEN_PREF + "user_configfile";
	
	public static final String WL_DEPLOYMENT_CMD_USER_KEYFILE_TOKEN = WL_DEPLOYMENT_CMD_TOKEN_PREF + "user_keyfile";
	
	/**
	 * 
	 * @param key
	 * @param parameters
	 * @return
	 */
	public String resolveKey(String key, WebLogicDeployerParameters parameters);
}
