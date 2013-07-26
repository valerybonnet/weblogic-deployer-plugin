/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic.deployer;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author RaphaelC
 *
 */
public class WebLogicDeployerTokenResolverTestCase {

	
	@Test
	public void resolveKey() {
		WebLogicDeployerTokenResolver resolver = new WebLogicDeployerTokenResolverImpl();
		
		WebLogicDeployerParameters parameters = new WebLogicDeployerParameters();
		parameters.setSource("toto");
		parameters.setDeploymentName("deploymentName2");
		String keySource = "wl.source";
		Assert.assertEquals("toto",resolver.resolveKey(keySource, parameters));
		String keyDeplName = "wl.deployment_name";
		Assert.assertEquals("deploymentName2",resolver.resolveKey(keyDeplName, parameters));
	}
}
