/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Proc;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.model.TopLevelItem;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Cause;
import hudson.model.Hudson;
import hudson.model.JDK;
import hudson.model.Job;
import hudson.model.Run.Artifact;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.FormValidation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.util.FileUtils;
import org.jenkinsci.plugins.deploy.weblogic.configuration.WeblogicDeploymentConfiguration;
import org.jenkinsci.plugins.deploy.weblogic.data.TransfertConfiguration;
import org.jenkinsci.plugins.deploy.weblogic.data.WebLogicDeploymentStatus;
import org.jenkinsci.plugins.deploy.weblogic.data.WeblogicEnvironment;
import org.jenkinsci.plugins.deploy.weblogic.deployer.WebLogicCommand;
import org.jenkinsci.plugins.deploy.weblogic.deployer.WebLogicDeployer;
import org.jenkinsci.plugins.deploy.weblogic.deployer.WebLogicDeployerParameters;
import org.jenkinsci.plugins.deploy.weblogic.exception.RequiredJDKNotFoundException;
import org.jenkinsci.plugins.deploy.weblogic.properties.WebLogicDeploymentPluginConstantes;
import org.jenkinsci.plugins.deploy.weblogic.util.DeployerClassPathUtils;
import org.jenkinsci.plugins.deploy.weblogic.util.FTPUtils;
import org.jenkinsci.plugins.deploy.weblogic.util.JdkUtils;
import org.jenkinsci.plugins.deploy.weblogic.util.URLUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;



/**
 * @author rchaumie
 *
 */
public class WeblogicDeploymentPlugin extends Recorder {
	
	public static transient final String NON_DEPLOYMENT_STRATEGY_VALUE_SPECIFIED = "unknown";
	
	public static transient final String DEFAULT_JAVA_OPTIONS_DEPLOYER = "-Xms256M -Xmx256M";
	
	
	/**
     * Identifies {@link WeblogicEnvironment} to be used.
     */
	private final String weblogicEnvironmentTargetedName;
	
	/**
	 * Le nom de deploiement. Si null on n'utilisera le nom de l'artifact
	 */
	private final String deploymentName;
	
	/**
	 * Les targets de deploiement. Par defaut AdminServer
	 */
	private String deploymentTargets = "AdminServer";
	
	/**
	 * L'artifact est une librairie
	 */
	private boolean isLibrary;
	
	/**
	 * 
	 */
	private transient JDK usedJdk = null;
	
	/**
	 * Le build doit se terminer en erreur. Configurable.
	 */
	private boolean mustExitOnFailure = true;
	
	/**
	 * strategies de deploiement (rattache a un trigger de build)
	 */
	private List<String> selectedDeploymentStrategyIds;
	
	/**
	 * le deploiement est effectif uniquement si les sources ont changes
	 */
	private boolean isDeployingOnlyWhenUpdates;
	
	/**
	 * Liste des deploiements dont depends l'execution du projet sur un job
	 */
	private String deployedProjectsDependencies;
	
	/**
	 * Regex permettant de filtrer la ressource à deployer si plusieurs ressources 
	 * correspondantes sont trouvées
	 */
	private String builtResourceRegexToDeploy;
	
	@DataBoundConstructor
    public WeblogicDeploymentPlugin(String weblogicEnvironmentTargetedName, String deploymentName, String deploymentTargets, boolean isLibrary, boolean mustExitOnFailure, List<String> selectedDeploymentStrategyIds, String deployedProjectsDependencies, boolean isDeployingOnlyWhenUpdates, String builtResourceRegexToDeploy) {
        this.weblogicEnvironmentTargetedName = weblogicEnvironmentTargetedName;
        this.deploymentName = deploymentName;
        this.deploymentTargets = deploymentTargets;
        this.isLibrary = isLibrary;
        this.mustExitOnFailure = mustExitOnFailure;
        this.selectedDeploymentStrategyIds = selectedDeploymentStrategyIds;
        this.deployedProjectsDependencies = deployedProjectsDependencies;
        this.isDeployingOnlyWhenUpdates = isDeployingOnlyWhenUpdates;
        this.builtResourceRegexToDeploy = builtResourceRegexToDeploy;
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
	 * 
	 * @return
	 */
	public boolean getMustExitOnFailure() {
		return mustExitOnFailure;
	}
	
	/**
	 * @return the selectedDeploymentStrategyIds
	 */
	public List<String> getSelectedDeploymentStrategyIds() {
		return selectedDeploymentStrategyIds;
	}
	
	/**
	 * @return the deployedProjectsDependencies
	 */
	public String getDeployedProjectsDependencies() {
		return deployedProjectsDependencies;
	}
	
	/**
	 * @return the isDeployingOnlyWhenUpdates
	 */
	public boolean getIsDeployingOnlyWhenUpdates() {
		return isDeployingOnlyWhenUpdates;
	}

	/**
	 * @param isDeployingOnlyWhenUpdates the isDeployingOnlyWhenUpdates to set
	 */
	public void setDeployingOnlyWhenUpdates(boolean isDeployingOnlyWhenUpdates) {
		this.isDeployingOnlyWhenUpdates = isDeployingOnlyWhenUpdates;
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

	/*
	 * (non-Javadoc)
	 * @see hudson.tasks.BuildStepCompatibilityLayer#getProjectAction(hudson.model.AbstractProject)
	 */
	@Override
	public Action getProjectAction(AbstractProject<?, ?> project) {
		return new PrintingWebLogicDeploymentLastSuccessResultAction(project);
	}
	
	/*
	 * (non-Javadoc)
	 * @see hudson.tasks.BuildStepCompatibilityLayer#perform(hudson.model.AbstractBuild, hudson.Launcher, hudson.model.BuildListener)
	 */
	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
		
		// write out the log
        FileOutputStream deploymentLogOut = new FileOutputStream(getDeploymentLogFile(build));
//        deploymentLogOut.write("------------------------------------  DEPLOYMENT PRE-REQUISITES ----------------------------------------------\r\n".getBytes());
        
        //Pre-requis ko , arret du traitement
        if(! checkPreRequisites( build, launcher, listener)){
        	return exitPerformAction(build, listener, WebLogicDeploymentStatus.DISABLED, null);
        }
        
		// Identification de la ressource a deployer
        FilePath archivedArtifact = null;
		String artifactName = null;
		String fullArtifactFinalName = null;
		try {
			ArtifactSelector artifactSelector = new MavenJobArtifactSelectorImpl();
			Artifact selectedArtifact = artifactSelector.selectArtifactRecorded(build, listener, builtResourceRegexToDeploy);
			// Ne devrait pas etre le nom mais la valeur finale du artifact.name (sans l'extension)
			artifactName = StringUtils.substringBeforeLast(selectedArtifact.getFileName(), ".");
			archivedArtifact = new FilePath(selectedArtifact.getFile());
			fullArtifactFinalName = selectedArtifact.getFileName();
		} catch (Throwable e) {
            listener.error("[WeblogicDeploymentPlugin] - Failed to get artifact from archive directory : " + e.getMessage());
            return exitPerformAction(build, listener, WebLogicDeploymentStatus.ABORTED, null);
        }
		
		//Deploiement
		String sourceFile = null;
		String remoteFilePath = null;
		//Recuperation du parametrage
		WeblogicEnvironment weblogicEnvironmentTargeted = null;
		try {
            
			//Gestion de liste d'exclusions
			Pattern pattern = Pattern.compile(getDescriptor().getExcludedArtifactNamePattern());
			Matcher matcher = pattern.matcher(artifactName);
			if(matcher.matches()){
				listener.error("[WeblogicDeploymentPlugin] - The artifact Name " +artifactName+ " is excluded from deployment (see exclusion list).");
				return exitPerformAction(build, listener, WebLogicDeploymentStatus.ABORTED, weblogicEnvironmentTargeted);
			}
			
			//Recuperation du parametrage
			weblogicEnvironmentTargeted = getWeblogicEnvironmentTargeted(listener);
			
			if(weblogicEnvironmentTargeted == null){
				listener.error("[WeblogicDeploymentPlugin] - WebLogic environment Name " +weblogicEnvironmentTargetedName+ " not found in the list. Please check the configuration file.");
				return exitPerformAction(build, listener, WebLogicDeploymentStatus.ABORTED, weblogicEnvironmentTargeted);
			}
			listener.getLogger().println("[WeblogicDeploymentPlugin] - Deploying the artifact on the following target : (name="+weblogicEnvironmentTargetedName+") (host=" + weblogicEnvironmentTargeted.getHost() + ") (port=" +weblogicEnvironmentTargeted.getPort()+ ")");
			
			//Execution commande undeploy
			WebLogicDeployerParameters undeployWebLogicDeployerParameters = new WebLogicDeployerParameters(build, launcher, listener, usedJdk, deploymentName, isLibrary, deploymentTargets, weblogicEnvironmentTargeted, artifactName, null, WebLogicCommand.UNDEPLOY, true, getDescriptor().getJavaOpts(), getDescriptor().getExtraClasspath());
			String[] undeployCommand = WebLogicDeployer.getWebLogicCommandLine(undeployWebLogicDeployerParameters);
	        
	        deploymentLogOut.write("------------------------------------  ARTIFACT UNDEPLOYMENT ------------------------------------------------\r\n".getBytes());
	        listener.getLogger().println("[WeblogicDeploymentPlugin] - UNDEPLOYING ARTIFACT...");
	        final Proc undeploymentProc = launcher.launch().cmds(undeployCommand).stdout(deploymentLogOut).start();
	        undeploymentProc.join();
	        listener.getLogger().println("[WeblogicDeploymentPlugin] - ARTIFACT UNDEPLOYED SUCCESSFULLY.");
	        
	        //Transfert FTP pour les librairies (contrainte weblogic)
	        if(isLibrary){
	        	//Par defaut si ftp n'est pas renseigne on prend le host
	        	String ftpHost = StringUtils.isBlank(weblogicEnvironmentTargeted.getFtpHost()) ? weblogicEnvironmentTargeted.getHost() : weblogicEnvironmentTargeted.getFtpHost();
	        	// path to remote resource
	            remoteFilePath = weblogicEnvironmentTargeted.getRemoteDir() + "/" + fullArtifactFinalName;
	            String localFilePath = archivedArtifact.getRemote();
	            listener.getLogger().println("[WeblogicDeploymentPlugin] - TRANSFERING LIBRARY : (local=" +fullArtifactFinalName+ ") (remote=" + remoteFilePath + ") to (ftp=" +ftpHost + "@" +weblogicEnvironmentTargeted.getFtpUser()+ ") ...");
	            FTPUtils.transfertFile(new TransfertConfiguration(ftpHost, weblogicEnvironmentTargeted.getFtpUser(), weblogicEnvironmentTargeted.getFtpPassowrd(), localFilePath, remoteFilePath),listener.getLogger());
	        	listener.getLogger().println("[WeblogicDeploymentPlugin] - LIBRARY TRANSFERED SUCCESSFULLY.");
	        }
	        
	        //Execution commande deploy
			//source file correspond au remote file pour les librairies
	        if(isLibrary){
	        	sourceFile = remoteFilePath;
	        } else {
	        	sourceFile = archivedArtifact.getRemote();
	        }
	        
	        WebLogicDeployerParameters deployWebLogicDeployerParameters = new WebLogicDeployerParameters(build,launcher,listener, usedJdk, deploymentName, isLibrary, deploymentTargets, weblogicEnvironmentTargeted, artifactName, sourceFile, WebLogicCommand.DEPLOY, false,getDescriptor().getJavaOpts(),getDescriptor().getExtraClasspath());
	        String[] deployCommand = WebLogicDeployer.getWebLogicCommandLine(deployWebLogicDeployerParameters);
	        listener.getLogger().println("[WeblogicDeploymentPlugin] - DEPLOYING ARTIFACT...");
	        deploymentLogOut.write("------------------------------------  ARTIFACT DEPLOYMENT ------------------------------------------------\r\n".getBytes());
	        final Proc deploymentProc = launcher.launch().cmds(deployCommand).stdout(deploymentLogOut).start();
	        int exitStatus = deploymentProc.join();
	        if(exitStatus != 0){
	        	listener.error("[WeblogicDeploymentPlugin] - Command " +StringUtils.join(deployCommand, '|')+" completed abnormally (exit code = "+exitStatus+")");
	        	throw new RuntimeException("Command " +StringUtils.join(deployCommand, '|')+" completed abnormally (exit code = "+exitStatus+")");
	        }
	        listener.getLogger().println("[WeblogicDeploymentPlugin] - ARTIFACT DEPLOYED SUCCESSFULLY.");
			
        } catch (Throwable e) {
        	e.printStackTrace(listener.getLogger());
        	listener.error("[WeblogicDeploymentPlugin] - Failed to deploy.");
            return exitPerformAction(build, listener, WebLogicDeploymentStatus.FAILED, weblogicEnvironmentTargeted);
        } finally {
        	IOUtils.closeQuietly(deploymentLogOut);
        }

		
        return exitPerformAction(build, listener, WebLogicDeploymentStatus.SUCCEEDED, weblogicEnvironmentTargeted);
	}
	
	/**
	 * 
	 * @param build
	 * @param launcher
	 * @param listener
	 * @return
	 */
	private boolean checkPreRequisites(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener){
		
		//Verification desactivation plugin
		if(getDescriptor().isPluginDisabled()){
			listener.getLogger().println("[WeblogicDeploymentPlugin] - The plugin execution is disabled.");
			return false;
		}
				
		//Verification coherence du (des) declencheur(s)
		boolean isSpecifiedDeploymentStrategyValue = true;
		if(CollectionUtils.isEmpty(selectedDeploymentStrategyIds) || 
				(selectedDeploymentStrategyIds.size() == 1 && selectedDeploymentStrategyIds.contains(NON_DEPLOYMENT_STRATEGY_VALUE_SPECIFIED))){
			isSpecifiedDeploymentStrategyValue = false;
		}
		
		if(isSpecifiedDeploymentStrategyValue && !hasAtLeastOneBuildCauseChecked(build, selectedDeploymentStrategyIds)){
			listener.getLogger().println("[WeblogicDeploymentPlugin] - Not properly build causes expected (configured=" +StringUtils.join(selectedDeploymentStrategyIds,';')+ ") (currents=" +StringUtils.join(build.getCauses(),';')+ ") : The plugin execution is disabled.");
			return false;
		}
		
		//Verification strategie relative a la gestion des sources (systematique (par defaut) / uniquement sur modification(actif) )
		if(isDeployingOnlyWhenUpdates && build.getChangeSet().isEmptySet()) {
			listener.getLogger().println("[WeblogicDeploymentPlugin] - No changes : The plugin execution is disabled.");
			return false;
		}
		
		
		// Verification condition de dependance remplie
		boolean satisfiedDependenciesDeployments = true;
		if(StringUtils.isNotBlank(deployedProjectsDependencies)){
			String[] listeDependances = StringUtils.split(StringUtils.trim(deployedProjectsDependencies), ',');
			for(int i = 0; i<listeDependances.length; i++){
				TopLevelItem item = Hudson.getInstance().getItem(listeDependances[i]);
				if(item instanceof Job){
					WatchingWeblogicDeploymentLogsAction deploymentAction = ((Job<?,?>) item).getLastBuild().getAction(WatchingWeblogicDeploymentLogsAction.class);
					listener.getLogger().println("[WeblogicDeploymentPlugin] - satisfying dependencies project involved : " + item.getName()+ " deploymentAction : "+ deploymentAction.getDeploymentActionStatus());
					if(deploymentAction != null && WebLogicDeploymentStatus.FAILED.equals(deploymentAction.getDeploymentActionStatus())){
						satisfiedDependenciesDeployments = false;
					}
				}
			}
			
			if(!satisfiedDependenciesDeployments){
				listener.getLogger().println("[WeblogicDeploymentPlugin] - Not satisfied project dependencies deployment : The plugin execution is disabled.");
				return false;
			}
		}
				
		// Verification build SUCCESS
		if (build.getResult().isWorseThan(Result.SUCCESS)) {
			listener.getLogger().println("[WeblogicDeploymentPlugin] - build didn't finished successfully. The plugin execution is disabled.");
			return false;
		}
				
		// Recuperation du JDK
		try {
			usedJdk = JdkUtils.getSelectedJDK(getDescriptor().getJdkSelected(), listener.getLogger());
		} catch (RequiredJDKNotFoundException rjnfe) {
			listener.getLogger().println("[WeblogicDeploymentPlugin] - No JDK found. The plugin execution is disabled.");
			return false;
		}
		String valueJdk = usedJdk != null ? "the JDK " +usedJdk.getHome()+ " will be used." : "no JDK retrieved.";
		listener.getLogger().println("[WeblogicDeploymentPlugin] - " +valueJdk);
		return true;		
	}
	
	/**
	 * 
	 * @param listener
	 * @return
	 */
	private WeblogicEnvironment getWeblogicEnvironmentTargeted(BuildListener listener) {
		
		WeblogicEnvironment out = null;
		WeblogicEnvironment[] targets = getDescriptor().getWeblogicEnvironments();
		
		if(targets == null){
			return out;
		}
		
		for (int i = 0;i< targets.length;i++) {
			if(weblogicEnvironmentTargetedName.equalsIgnoreCase(targets[i].getName())){
				out = targets[i];
				break;
			}
		}
		
		return out;
	}

	/*
	 * 	(non-Javadoc)
	 * @see hudson.model.AbstractDescribableImpl#getDescriptor()
	 */
	@Override
	public WeblogicDeploymentPluginDescriptor getDescriptor() {
		return (WeblogicDeploymentPluginDescriptor) super.getDescriptor();
	}
	
	/*
	 * (non-Javadoc)
	 * @see hudson.tasks.BuildStep#getRequiredMonitorService()
	 */
	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}
	
	@Extension
	public static final class WeblogicDeploymentPluginDescriptor extends BuildStepDescriptor<Publisher> {
		
		public static transient final String PLUGIN_XSD_SCHEMA_CONFIG_FILE_PATH = WebLogicDeploymentPluginConstantes.PLUGIN_RESOURCES_PATH + "/defaultConfig/plugin-configuration.xsd";
		
		private String configurationFilePath;
		
		private boolean pluginDisabled;
		
		private transient WeblogicEnvironment[] weblogicEnvironments;
		
		/**
		 * Pattern des artifacts a exclure
		 */
		private String excludedArtifactNamePattern;
		
		/**
		 * 
		 */
		private String jdkSelected;
		
		/**
		 * classpath additionnel (librairie weblogic a utiliser)
		 */
		private String extraClasspath;
		
		/**
		 * 
		 */
		private String javaOpts;
		
		/**
		 * 
		 */
		public WeblogicDeploymentPluginDescriptor(){
			super(WeblogicDeploymentPlugin.class);
			
			//on charge les annotations XStream
			Hudson.XSTREAM.processAnnotations(
	        		new Class[]{org.jenkinsci.plugins.deploy.weblogic.configuration.WeblogicDeploymentConfiguration.class, org.jenkinsci.plugins.deploy.weblogic.data.WeblogicEnvironment.class});
			
			//charge les donnees de configuration du plugin dans l'instance
			load();
			
			//initialisation specifique
			init();
			
		}
		
		/**
		 * customisation des donnees de conf
		 */
		private void init(){
			//gestion java option utilise par defaut si non charge
			if(StringUtils.isBlank(javaOpts)){
				javaOpts = DEFAULT_JAVA_OPTIONS_DEPLOYER;
			}
		}
		
		/**
		 * 
		 * @return
		 */
		public WeblogicEnvironment[] getWeblogicEnvironments() {
			
			if(weblogicEnvironments == null){
				loadWeblogicEnvironments();
			}
			
			return weblogicEnvironments;
		}
		
		/*
		 * (non-Javadoc)
		 * @see hudson.model.Descriptor#getDisplayName()
		 */
		@Override
		public String getDisplayName() {
			return Messages.WeblogicDeploymentPluginDescriptor_DisplayName();
		}

		/**
		 * 
		 * @return
		 */
		public String getConfigurationFilePath() {
			return configurationFilePath;
		}
		
		/**
		 * 
		 * @param configurationFilePath
		 */
		public void setConfigurationFilePath(String configurationFilePath) {
			this.configurationFilePath = configurationFilePath;
		}
		
		/**
		 * 		
		 * @return
		 */
		public boolean isPluginDisabled() {
			return pluginDisabled;
		}
		
		/**
		 * 
		 * @param pluginDisabled
		 */
		public void setPluginDisabled(boolean pluginDisabled) {
			this.pluginDisabled = pluginDisabled;
		}
		
		/**
		 * 
		 * @return
		 */
		public String getExcludedArtifactNamePattern() {
			return excludedArtifactNamePattern;
		}
		
		/**
		 * @return the extraClasspath
		 */
		public String getExtraClasspath() {
			return extraClasspath;
		}

		/**
		 * @param extraClasspath the extraClasspath to set
		 */
		public void setExtraClasspath(String extraClasspath) {
			this.extraClasspath = extraClasspath;
		}

		/**
		 * @return the javaOpts
		 */
		public String getJavaOpts() {
			return javaOpts;
		}

		/**
		 * @param javaOpts the javaOpts to set
		 */
		public void setJavaOpts(String javaOpts) {
			this.javaOpts = javaOpts;
		}
		
		/**
		 * 
		 * @return
		 */
		public String getJdkSelected() {
			return jdkSelected;
		}

		/**
		 * 
		 * @param jdkHomeSelected
		 */
		public void setJdkSelected(String jdkSelected) {
			this.jdkSelected = jdkSelected;
		}

		/*
		 * (non-Javadoc)
		 * @see hudson.model.Descriptor#configure(org.kohsuke.stapler.StaplerRequest, net.sf.json.JSONObject)
		 */
		@Override
		public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
			
			pluginDisabled = json.getBoolean("pluginDisabled");
			excludedArtifactNamePattern = json.getString("excludedArtifactNamePattern");
			
			// Sauvegarde de la valeur par defaut
			if(StringUtils.isNotBlank(json.getString("extraClasspath"))){
				extraClasspath = json.getString("extraClasspath");
			} else {
				extraClasspath = DeployerClassPathUtils.getDefaultPathToWebLogicJar();
			}
			
			javaOpts = json.getString("javaOpts");
			
			// Sauvegarde du jdk selectionne
			jdkSelected = json.getString("jdkSelected");
			
			//Chargement des weblogicTargets
			configurationFilePath = json.getString("configurationFilePath");
			loadWeblogicEnvironments();
			
			//Sauvegarde de la configuration du plugin
			save();
			return true;
		}
		
		/**
		 * Charge les environnements weblogic declares dans le fichier de conf
		 */
		private void loadWeblogicEnvironments(){
			try {
		        
				WeblogicDeploymentConfiguration weblogicDeploymentConfiguration =null;
		        
				if(StringUtils.isBlank(configurationFilePath)){
					return;
				}
				
		        if(configurationFilePath.startsWith(URLUtils.HTTP_PROTOCOL_PREFIX)){
		        	URI uri = new URI(configurationFilePath);
		        	URL url = uri.toURL();
		        	weblogicDeploymentConfiguration = (WeblogicDeploymentConfiguration) Hudson.XSTREAM.fromXML(url.openStream());
		        } else if (FileUtils.fileExists(configurationFilePath)) {
		        	weblogicDeploymentConfiguration = (WeblogicDeploymentConfiguration) Hudson.XSTREAM.fromXML(new FileInputStream(FileUtils.getFile(configurationFilePath)));
		        }
		        
		        if(weblogicDeploymentConfiguration != null && ! ArrayUtils.isEmpty(weblogicDeploymentConfiguration.getWeblogicEnvironments())){
		        	weblogicEnvironments = weblogicDeploymentConfiguration.getWeblogicEnvironments();
		        }
		        
	        } catch(Exception e){
	        	e.printStackTrace();
	        }
		}

		/**
		 * Performs on-the-fly validation of the form field 'configurationFilePath'
		 * @param value
		 * @return
		 * @throws IOException
		 * @throws ServletException
		 */
        public FormValidation doCheckConfigurationFilePath(@QueryParameter String value) throws IOException, ServletException {

        	if(value.startsWith(URLUtils.HTTP_PROTOCOL_PREFIX)) {
        		if(! URLUtils.exists(value)){
        			return FormValidation.error("The url " + value + " can't be reached.");
        		}
        		return FormValidation.ok();
        	}
        	
        	if(! FileUtils.fileExists(value)) {
        		return FormValidation.error("The file " + value + " does not exists.");
        	}
            return FormValidation.ok();
        }
        
        /**
         * Controle a la volee du champ 'extraClasspath'
         * @param value
         * @return
         * @throws IOException
         * @throws ServletException
         */
        public FormValidation doCheckExtraClasspath(@QueryParameter String value) throws IOException, ServletException {
        	
        	if(value.length() == 0){
        		
        		// Si aucun jar specifie. On tente le WL_HOME.
        		// On verifie que la librairie existe bien
        		if(DeployerClassPathUtils.checkDefaultPathToWebLogicJar()){
        			return FormValidation.warning("By default, the "+WebLogicDeploymentPluginConstantes.WL_WEBLOGIC_LIBRARY_NAME+" library found into "+System.getenv(WebLogicDeploymentPluginConstantes.WL_HOME_ENV_VAR_NAME)+WebLogicDeploymentPluginConstantes.WL_HOME_LIB_DIR+" will be used.");
        		}
        		
        		return FormValidation.error("The weblogic library has to be filled in.");
        	}
        	
        	if(! FileUtils.fileExists(value)) {
        		return FormValidation.error("The file " + value + " does not exists.");
        	}
        	
        	return FormValidation.ok();
        }
        
        /**
         * 
         * @param value
         * @return
         * @throws IOException
         * @throws ServletException
         */
        public FormValidation doCheckJavaOpts(@QueryParameter String value) throws IOException, ServletException {
        	
        	if(value.length() == 0){
        		return FormValidation.warning("The default options -Xms256M -Xmx256M will be used.");
        	}
        	
        	return FormValidation.ok();
        }

		/*
		 * (non-Javadoc)
		 * @see hudson.tasks.BuildStepDescriptor#isApplicable(java.lang.Class)
		 */
		@Override
		public boolean isApplicable(Class<? extends AbstractProject> jobType) {
			// indicates that this builder can be used with all kinds of project types
			//Verification projet Maven
			if(hudson.maven.AbstractMavenProject.class.isAssignableFrom(jobType)){
				return true;
			}
			return false;
		}
	}
	
	
	/**
	 * 
	 * @param build
	 * @param deploymentStrategies
	 * @return
	 */
	private boolean hasAtLeastOneBuildCauseChecked(AbstractBuild<?, ?> build, List<String> deploymentStrategies) {
		boolean isProperlyBuildCause = false;
		//On ne controle la desactivation que si la strategie a ete definie
		//gestion des classes privees : les tokens \$ sont transformees en $
		List<String> searchedCauseIds = new ArrayList<String>();
		for(String elt : deploymentStrategies){
			searchedCauseIds.add(StringUtils.remove(elt, '\\'));
		}
		
		List<Cause> causes = build.getCauses();
		for(Cause cause : causes){
			if(searchedCauseIds.contains(cause.getClass().getName())) {
				isProperlyBuildCause = true;
			}
		}
		return isProperlyBuildCause;
	}
	
	/**
	 * 
	 * @param build
	 * @param listener
	 * @param status
	 * @param weblogicEnvironment
	 * @return
	 */
	private boolean exitPerformAction(AbstractBuild<?, ?> build, BuildListener listener, WebLogicDeploymentStatus status, WeblogicEnvironment weblogicEnvironment){
		
		if(! WebLogicDeploymentStatus.SUCCEEDED.equals(status)){
			if(mustExitOnFailure){
				build.setResult(Result.FAILURE);
			} else {
				build.setResult(Result.SUCCESS);
			}
		} else {
			build.setResult(Result.SUCCESS);
		}
		
		//Ajout de la build action
		build.addAction(new WatchingWeblogicDeploymentLogsAction(status, build, weblogicEnvironment));
		
		listener.getLogger().println("[INFO] ------------------------------------------------------------------------");
		listener.getLogger().println("[INFO] DEPLOYMENT "+status.name());
		listener.getLogger().println("[INFO] ------------------------------------------------------------------------");
		return true;
	}
	
	/**
	 * 
	 * @param build
	 * @return
	 */
	public static File getDeploymentLogFile(AbstractBuild<?,?> build) {
		return new File(build.getRootDir(),"deploymentLog.txt");
	}
	
}
