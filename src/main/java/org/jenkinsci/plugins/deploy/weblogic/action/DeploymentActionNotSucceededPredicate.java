/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic.action;

import org.apache.commons.collections.Predicate;
import org.jenkinsci.plugins.deploy.weblogic.WatchingWeblogicDeploymentLogsAction;
import org.jenkinsci.plugins.deploy.weblogic.data.WebLogicDeploymentStatus;

/**
 * @author Raphael
 *
 */
public class DeploymentActionNotSucceededPredicate implements Predicate {

	/* (non-Javadoc)
	 * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
	 */
	public boolean evaluate(Object arg0) {
		WatchingWeblogicDeploymentLogsAction elt = (WatchingWeblogicDeploymentLogsAction)  arg0;
		return ! WebLogicDeploymentStatus.SUCCEEDED.equals(elt.getDeploymentActionStatus());
	}

}
