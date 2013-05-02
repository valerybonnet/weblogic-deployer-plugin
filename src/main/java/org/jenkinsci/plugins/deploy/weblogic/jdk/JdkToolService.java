/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic.jdk;

import hudson.model.JDK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jenkins.model.Jenkins;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.jenkinsci.plugins.deploy.weblogic.util.JdkUtils;

/**
 * @author Raphael
 *
 */
public class JdkToolService {

	Map<String, JDK> jdkAvailables = new HashMap<String, JDK>();
	
	/**
	 * 
	 * @return
	 */
	public static List<JDK> getJdkToolAvailables() {
		List<JDK> jdkList = new ArrayList<JDK>();
		
		//Ajout de la java home system
		if (StringUtils.isNotBlank(SystemUtils.JAVA_HOME)){
			jdkList.add(new JDK(JdkUtils.SYSTEM_JDK, System.getProperty("java.home")));
		}
		//Ajout de la java home avec lequel est demarre Jenkins
		if(StringUtils.isNotBlank(System.getenv("JAVA_HOME"))){
			jdkList.add(new JDK(JdkUtils.EXTERNAL_ENV_JDK, System.getenv("JAVA_HOME")));
		}
		// Ajout de tous les jdk declares dans Jenkins
		jdkList.addAll(Jenkins.getInstance().getJDKs());
		return jdkList;
	}
	
	public static JDK getJDKByName(String name) {
		JDK out = null;
		for(JDK jdk : getJdkToolAvailables()) {
			if(name.equalsIgnoreCase(jdk.getName())){
				out = jdk;
			}
		}
		return out;
	}
	
	public static String getJDKHomeByName(String name) {
		String out = null;
		for(JDK jdk : getJdkToolAvailables()) {
			if(name.equalsIgnoreCase(jdk.getName())){
				out = jdk.getHome();
			}
		}
		return out;
	}
}
