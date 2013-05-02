package org.jenkinsci.plugins.deploy.weblogic.task;

import hudson.model.AutoCompletionCandidates;
import hudson.model.Descriptor;
import hudson.model.JDK;
import net.sf.json.JSONObject;

import org.jenkinsci.plugins.deploy.weblogic.data.DeploymentTask;
import org.jenkinsci.plugins.deploy.weblogic.jdk.JdkToolService;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.bind.JavaScriptMethod;

public class DeploymentTaskDescriptor extends Descriptor<DeploymentTask> {

	@Override
	public String getDisplayName() {
		return "Toto";
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see hudson.model.Descriptor#configure(org.kohsuke.stapler.StaplerRequest, net.sf.json.JSONObject)
	 */
	@Override
	public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
		//Sauvegarde de la configuration du plugin
		System.out.println("Je sauvegarde ma tache");
		save();
		return true;
	}
	
    /**
     * This method provides auto-completion items for the 'jdkName' field.
     * Stapler finds this method via the naming convention.
     *
     * @param value
     *      The text that the user entered.
     */
    public AutoCompletionCandidates doAutoCompleteJdkName(@QueryParameter String value) {
        AutoCompletionCandidates c = new AutoCompletionCandidates();
        for (JDK jdk : JdkToolService.getJdkToolAvailables()) {
            if (jdk.getName().contains(value.toLowerCase())) {
            	c.add(jdk.getName());
            }
        }
        return c;
    }

    @JavaScriptMethod
    public String completeJdkHome(String jdkName) {
    	JDK jdk = JdkToolService.getJDKByName(jdkName);
    	if(jdk != null){
    		 return jdk.getHome();
    	}
    	return "";
    }

}
