/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic;

import hudson.FilePath;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

/**
 * @author rchaumie
 *
 */
public class FreeStyleJobArtifactSelectorImpl implements ArtifactSelector {
	
	/*
	 * (non-Javadoc)
	 * @see org.jenkinsci.plugins.deploy.weblogic.ArtifactSelector#selectArtifactRecorded(hudson.model.AbstractBuild, hudson.model.BuildListener, java.lang.String, java.lang.String)
	 */
	public FilePath selectArtifactRecorded(AbstractBuild<?, ?> build, BuildListener listener, String filteredResource, String baseDirectory) throws IOException, XmlPullParserException, InterruptedException  {
		
		FilePath selectedArtifact = null;
		
        listener.getLogger().println("[WeblogicDeploymentPlugin] - Retrieving artifacts recorded [filtered resources on "+filteredResource+"]...");
        List<FilePath> artifactsRecorded = new ArrayList<FilePath>();
        
        // On parcours le workspace si aucun repertoire de base specifie à la recherche d'un fichier correspondant à l'expression régulière
        if(baseDirectory == null || ! (new File(baseDirectory)).exists()){
	        FilePath workspace = build.getWorkspace();
	        List<FilePath> filesInWorkspace = workspace.list();
	        for(FilePath file : filesInWorkspace){
	        	if(! file.isDirectory() && Pattern.matches(filteredResource, file.getName())){
	    			listener.getLogger().println("[WeblogicDeploymentPlugin] - the following artifact recorded "+file.getName()+" is eligible.");
	    			artifactsRecorded.add(file);
	    		} else {
	    			listener.getLogger().println("[WeblogicDeploymentPlugin] - the following artifact "+file.getName()+" doesn't match "+filteredResource);
	    		}
	        }
        } else {
        	Collection<?> files = FileUtils.listFiles(new File(baseDirectory), null, true);
        	for(File file : (Collection<File>) files){
        		if(! file.isDirectory() && Pattern.matches(filteredResource, file.getName())){
	    			listener.getLogger().println("[WeblogicDeploymentPlugin] - the following file recorded "+file.getName()+" is eligible.");
	    			artifactsRecorded.add(new FilePath(file));
	    		} else {
	    			listener.getLogger().println("[WeblogicDeploymentPlugin] - the following file "+file.getName()+" doesn't match "+filteredResource);
	    		}
        	}
        }
        
        if(artifactsRecorded.size() < 1){
        	throw new RuntimeException("[WeblogicDeploymentPlugin] - No artifact to deploy ["+filteredResource+"] found.");
        }
        
        if(artifactsRecorded.size() > 1){
        	listener.getLogger().println("[WeblogicDeploymentPlugin] - More than 1 artifact found : The first one "+artifactsRecorded.get(0)+ " will be deployed!!!");
        }
        
        selectedArtifact = artifactsRecorded.get(0);
		
		// Erreur si l'artifact n'existe pas
		if(selectedArtifact == null){
			throw new RuntimeException("No artifact to deploy found.");
		}
        
		return selectedArtifact;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jenkinsci.plugins.deploy.weblogic.ArtifactSelector#getName()
	 */
	public String getName() {
		return "FreeStyleProject";
	}

}
