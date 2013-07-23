/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic.data;


/**
 * @author rchaumie
 *
 */
public enum WebLogicPreRequisteStatus {

	OK(0),
	PLUGIN_DISABLED(1),
	OTHER_TRIGGER_CAUSE(2),
	NO_CHANGES(3),
	UNSATISFIED_DEPENDENCIES(4),
	BUILD_FAILED(5);
	
	private int value;
	
	/**
	 * @param value
	 */
	private WebLogicPreRequisteStatus(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
}
