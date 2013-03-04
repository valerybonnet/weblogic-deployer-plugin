/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic.action;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.jenkinsci.plugins.deploy.weblogic.WatchingWeblogicDeploymentAction;
import org.jenkinsci.plugins.deploy.weblogic.task.TaskStatusUnSuccesfullPredicate;

/**
 * @author Raphael
 *
 */
public class DeploymentActionNotSucceededPredicate implements Predicate {

	/* (non-Javadoc)
	 * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
	 */
	public boolean evaluate(Object arg0) {
		WatchingWeblogicDeploymentAction elt = (WatchingWeblogicDeploymentAction)  arg0;
		return CollectionUtils.exists(elt.getResults(), new TaskStatusUnSuccesfullPredicate());
	}

}
