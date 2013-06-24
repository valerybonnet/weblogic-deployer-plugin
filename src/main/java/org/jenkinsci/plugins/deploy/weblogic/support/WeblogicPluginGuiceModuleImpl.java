/**
 * 
 */
package org.jenkinsci.plugins.deploy.weblogic.support;

import hudson.Extension;

import org.jenkinsci.plugins.deploy.weblogic.task.DeploymentTaskService;
import org.jenkinsci.plugins.deploy.weblogic.task.DeploymentTaskServiceImpl;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * @author Raphael
 *
 */
@Extension
public class WeblogicPluginGuiceModuleImpl extends AbstractModule {

	/* (non-Javadoc)
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {
		bind(DeploymentTaskService.class).to(DeploymentTaskServiceImpl.class);
		//.in(Singleton.class)
	}

}
