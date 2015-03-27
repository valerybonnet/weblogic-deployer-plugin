package org.jenkinsci.plugins.deploy.weblogic.util;

import hudson.EnvVars;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;

import java.io.IOException;
import java.util.Map;

/**
 * Created by MRC on 27/03/2015.
 */
public final class VarUtils {

    /**
     *
     * @param build
     * @param listener
     * @return
     */
    public static EnvVars getEnvVars(AbstractBuild<?, ?> build, BuildListener listener) {
        try {
            // Added envVars
            EnvVars envVars = build.getEnvironment(listener);
            // on Windows environment variables are converted to all upper case,
            // but no such conversions are done on Unix, so to make this cross-platform,
            // convert variables to all upper cases.
            for(Map.Entry<String,String> e : build.getBuildVariables().entrySet()) {
                envVars.put(e.getKey(),e.getValue());
            }
            return envVars;
        } catch (IOException ioe) {
            // Nothing to do
        } catch (InterruptedException ie) {
            // Nothing to do
        }

        return null;
    }
}
