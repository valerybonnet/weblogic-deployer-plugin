/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic.task;

import org.apache.commons.collections.Predicate;
import org.jenkinsci.plugins.deploy.weblogic.data.DeploymentTaskResult;
import org.jenkinsci.plugins.deploy.weblogic.data.WebLogicPreRequisteStatus;

/**
 * @author Raphael
 *
 */
public class PreRequisiteStatusUnSuccesfullPredicate implements Predicate {

	/* (non-Javadoc)
	 * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
	 */
	public boolean evaluate(Object arg0) {
		DeploymentTaskResult elt = (DeploymentTaskResult)  arg0;
		return ! WebLogicPreRequisteStatus.OK.equals(elt.getCheck());
	}

}
