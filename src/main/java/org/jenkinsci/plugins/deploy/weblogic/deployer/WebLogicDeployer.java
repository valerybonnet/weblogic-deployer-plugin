/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic.deployer;

import hudson.model.Run.RunnerAbortedException;
import hudson.util.ArgumentListBuilder;

import org.apache.commons.lang.StringUtils;
import org.jenkinsci.plugins.deploy.weblogic.properties.WebLogicDeploymentPluginConstantes;

/**
 * @author rchaumie
 *
 */
public class WebLogicDeployer {

	
	/**
	 * 
	 * @param parameter
	 * @return
	 */
	public static final String[] getWebLogicCommandLine(WebLogicDeployerParameters parameter) {
		ArgumentListBuilder args = new ArgumentListBuilder();
        
		//jdk
		if(parameter.getUsedJdk() == null) {
            parameter.getListener().error("[HudsonWeblogicDeploymentPlugin] - No JDK selected to deploy artifact.");
            throw new RunnerAbortedException();
		}
        args.add(parameter.getUsedJdk().getBinDir().getAbsolutePath().concat("/java"));
        
		//java options specifique
        if(StringUtils.isNotBlank(parameter.getJavaOpts())){
        	//On parse l'ensemble des options et on les rajoute des le args[]
        	String[] javaOptions = StringUtils.split(parameter.getJavaOpts(), ' ');
        	args.add(javaOptions);
        }
		
        //gestion du classpath
        args.add("-cp");
        
        // remoting.jar
        if(StringUtils.isBlank(parameter.getClasspath())){
        	parameter.getListener().error("[HudsonWeblogicDeploymentPlugin] - Classpath is not set. Please configure correctly the plugin.");
            throw new RunnerAbortedException();
        }
        String remotingJar = parameter.getClasspath();
        args.add(remotingJar);
        args.add(WebLogicDeploymentPluginConstantes.WL_WEBLOGIC_API_DEPLOYER_MAIN_CLASS);
        
        // mode debug force
        args.add("-debug");
        
        //Cas d'une application stage uniquement au deploiement
        if(! WebLogicCommand.UNDEPLOY.equals(parameter.getCommand()) && !parameter.isLibrary()){
        	// TODO stage provoque la generation du config....
        	args.add("-stage");
//        	args.add("-nostage");
        }
        
        args.add("-remote");
        args.add("-verbose");
        
        //Cas d'une application
        // Pour une librairie on copie sur le serveur puis on deploie
        if(! WebLogicCommand.UNDEPLOY.equals(parameter.getCommand()) && !parameter.isLibrary()){
        	args.add("-upload");
        }
        
        if(parameter.isSilentMode()){
        	args.add("-noexit");
        }
        
        args.add("-name");
        String targetedDeploymentName = StringUtils.isNotBlank(parameter.getDeploymentName()) ? parameter.getDeploymentName() : parameter.getArtifactName();
        if(StringUtils.isBlank(targetedDeploymentName)){
        	// TODO
        }
        args.add(targetedDeploymentName);
        
        if(StringUtils.isNotBlank(parameter.getSource())) {
        	args.add("-source");
        	args.add(parameter.getSource());
        }

        args.add("-targets");
        args.add(parameter.getDeploymentTargets());
        args.add("-adminurl");
        args.add("t3://" +parameter.getEnvironment().getHost()+":"+parameter.getEnvironment().getPort());
        args.add("-user");
        args.add(parameter.getEnvironment().getLogin());
        args.add("-password");
        args.add(parameter.getEnvironment().getPassword());
        
        args.add("-"+parameter.getCommand().getValue());
        
        if(parameter.isLibrary()) {
        	args.add("-library");
        }
        
        return args.toCommandArray();
	}

}
