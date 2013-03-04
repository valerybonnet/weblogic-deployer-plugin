package org.jenkinsci.plugins.deploy.weblogic;

import hudson.model.Action;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.jenkinsci.plugins.deploy.weblogic.action.DeploymentActionNotSucceededPredicate;
import org.jenkinsci.plugins.deploy.weblogic.data.WebLogicDeployment;


public class PrintingWebLogicDeploymentLastSuccessResultAction implements Action  {

	private WebLogicDeployment lastDeploymentSucessfull;
	
	/**
	 * 
	 */
	public PrintingWebLogicDeploymentLastSuccessResultAction(){
		super();
	}
	
	/**
	 * 
	 * @param project
	 */
	public PrintingWebLogicDeploymentLastSuccessResultAction(AbstractProject<?, ?> project){
		super();
		
		List<AbstractBuild<?, ?>> builds = (List<AbstractBuild<?, ?>>) project.getBuilds();
		for (AbstractBuild<?, ?> build : builds) {
			List<WatchingWeblogicDeploymentAction> deploymentAction = build.getActions(WatchingWeblogicDeploymentAction.class);
			
			WatchingWeblogicDeploymentAction found = (WatchingWeblogicDeploymentAction) CollectionUtils.find(deploymentAction, new DeploymentActionNotSucceededPredicate());
			if(found == null){
				lastDeploymentSucessfull = new WebLogicDeployment(build.getNumber(), build.getTime(), null);
				break;
			}
//			for(WatchingWeblogicDeploymentLogsAction action : deploymentAction){
//				// TODO : Comment gerer le last deployment sucessfull ?
//				if(action != null && WebLogicDeploymentStatus.SUCCEEDED.equals(action.deploymentActionStatus)){
//					lastDeploymentSucessfull = new WebLogicDeployment(build.getNumber(), build.getTime(), action.getTarget());
//					break;
//				}
//			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see hudson.model.Action#getDisplayName()
	 */
	public String getDisplayName() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see hudson.model.Action#getIconFileName()
	 */
	public String getIconFileName() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see hudson.model.Action#getUrlName()
	 */
	public String getUrlName() {
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public WebLogicDeployment getLastDeploymentSucessfull() {
		return lastDeploymentSucessfull;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean hasAtLeastOneDeploymentSuccessfull(){
		return lastDeploymentSucessfull != null;
	}

}
