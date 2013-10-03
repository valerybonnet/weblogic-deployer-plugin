/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic.util;

import hudson.EnvVars;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author RaphaelC
 *
 */
public class ParameterValueResolver {

	private static final Pattern ENV_VAR_PATTERN = Pattern.compile("\\$\\{?([\\S&&[^}]]*)\\}?");
	
	
	/**
	 * 
	 * @param label
	 * @param envars
	 * @return
	 */
	public static String resolveEnvVar(String label, EnvVars envars) {
		String key = "";
		Matcher matcher = ENV_VAR_PATTERN.matcher(label);
		if(matcher.matches()){
			key = matcher.group(1);
		}
			
		return envars.get(key, label);
		
	}
}
