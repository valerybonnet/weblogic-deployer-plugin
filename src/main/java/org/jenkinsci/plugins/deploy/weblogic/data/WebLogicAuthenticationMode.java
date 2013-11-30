/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author RaphaelC
 *
 */
@XStreamAlias("authMode")
public enum WebLogicAuthenticationMode {

	BY_KEY,
	BY_LOGIN;
}
