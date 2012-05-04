/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic.util;

import hudson.Launcher;
import hudson.model.TaskListener;
import hudson.model.Hudson;
import hudson.model.JDK;
import hudson.util.StreamTaskListener;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.jenkinsci.plugins.deploy.weblogic.exception.RequiredJDKNotFoundException;


/**
 * @author rchaumie
 *
 */
public class JdkUtils {
	
	public static final String DEFAULT_JDK = "default";
	
	public static final String HOME_JDK = "home";
	
	public static final String JAVA_VERSION_COMMAND_VERSION_LINE_REGEX = ".*\\r*\\n*(java version )(\")(.+)(\").*\\r*\\n*.*\\r*\\n*.*\\r*\\n*";
	
	/**
	 * 
	 * @param jdk
	 * @param logger
	 * @return
	 */
	public static boolean checkJdkVersion(JDK jdk, PrintStream logger){
		
		if(jdk == null || ! jdk.getExists()){
			return false;
		}
		
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			TaskListener listener = new StreamTaskListener(out);
			Launcher launcher = Hudson.getInstance().createLauncher(listener);
			String cmd = jdk.getBinDir().getAbsolutePath().concat("/java");
			int result = launcher.launch().cmds(cmd,"-version").stdout(out).join();
//			L'executable n'existe pas
			if(result  != 0){
				logger.println("[HudsonWeblogicDeploymentPlugin] - Unable to detect JDK version");
				return false;
			}
			
			Pattern pattern = Pattern.compile(JAVA_VERSION_COMMAND_VERSION_LINE_REGEX);
			Matcher matcher = pattern.matcher(out.toString());
			if(matcher.matches()){
				logger.println("[HudsonWeblogicDeploymentPlugin] - Pay attention of Jdk version {selected version is "+matcher.group(3)+"} compatibility with Weblogic Deployer API (see Oracle documentation).");
			}
			
		} catch (IOException e) {
            return false;
        } catch (InterruptedException e) {
            return false;
        } catch(IllegalStateException ise){
        	return false;
        } catch(IndexOutOfBoundsException ioobe) {
        	return false;
        }
		
		return true;
	}
	
	/**
	 * Check only if the JDK PATH is reachable
	 * @param name
	 * @return
	 */
	public static JDK getSelectedJDK(String name, PrintStream logger) throws RequiredJDKNotFoundException {
		JDK selectedJdk = null;
		
		logger.println("[HudsonWeblogicDeploymentPlugin] - jdk selected : "+name);
		
		if (DEFAULT_JDK.equals(name) && StringUtils.isNotBlank(SystemUtils.JAVA_HOME)){ // find embedded JDK
			logger.println("[HudsonWeblogicDeploymentPlugin] - java.home=" +  SystemUtils.JAVA_HOME);
			String embeddedHome = System.getProperty("java.home");
			selectedJdk = new JDK("default", embeddedHome);
		} else if(HOME_JDK.equals(name)){
			// JDK utiliser pour demarrer le produit
			try {
				Process java = Runtime.getRuntime().exec("java -version");
				BufferedReader in = new BufferedReader(new InputStreamReader(java.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
					System.out.println("ligne commande java -version : "+ line);
				}
			} catch(Exception e) {
				
			}
		
		} else {
			// Else lookup JDK referenced
			selectedJdk = Hudson.getInstance().getJDK(name);
		}
		
		// Check exists
		if(selectedJdk == null || ! selectedJdk.getExists()){
			String execu = selectedJdk != null ? selectedJdk.getHome(): "";
			throw new RequiredJDKNotFoundException("Unable to find PATH to the JDK's executable ["+execu+"]");
		}
		
		// Check version
		checkJdkVersion(selectedJdk, logger);
		return selectedJdk;
	}
	

}
