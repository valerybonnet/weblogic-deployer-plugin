/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic;

import hudson.FilePath;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;

import java.io.IOException;

import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

/**
 * @author rchaumie
 *
 */
public interface ArtifactSelector {

	/**
	 * 
	 * @param build
	 * @param listener
	 * @param filteredResource
	 * @param baseDirectory : base directory where the filtered resources will be searched.If the directory is not specified, it will search into the workspace.
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 * @throws InterruptedException
	 */
	public FilePath selectArtifactRecorded(AbstractBuild<?, ?> build, BuildListener listener, String filteredResource, String baseDirectory) throws IOException, XmlPullParserException, InterruptedException;
	
	/**
	 * Name of the selector
	 * @return
	 */
	public String getName();
}