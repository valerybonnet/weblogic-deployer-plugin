/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic.util;

import org.apache.commons.io.FilenameUtils;
import org.jenkinsci.plugins.deploy.weblogic.properties.WebLogicDeploymentPluginConstantes;

import java.io.File;

/**
 * @author rchaumie
 *
 */
public class DeployerClassPathUtils {

	/**
	 * 
	 * @return
	 */
	public static boolean checkDefaultPathToWebLogicJar() {
		return new File(getDefaultPathToWebLogicJar()).exists();
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getDefaultPathToWebLogicJar() {
		String envWlHome = System.getenv(WebLogicDeploymentPluginConstantes.WL_HOME_ENV_VAR_NAME);
		return FilenameUtils.normalize(envWlHome+WebLogicDeploymentPluginConstantes.WL_HOME_LIB_DIR+WebLogicDeploymentPluginConstantes.WL_WEBLOGIC_LIBRARY_NAME);
	}
}
