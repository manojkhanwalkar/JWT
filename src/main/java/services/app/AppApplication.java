/*
 * AppApplication
 *
 * Dropwizard entry point for the app (resource) service. It serves JWT-protected
 * requests, file downloads, and ECDHE-encrypted messages on https://localhost:8380 (App.yml).
 * Run it with: java -cp JWT.jar services.app.AppApplication server <config>.yml
 */
package services.app;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import services.ServiceConfiguration;

public class AppApplication extends Application<AppConfiguration> {

    /** Starts the service. Dropwizard reads the command, for example "server App.yml", from {@code args}. */
    public static void main(String[] args) throws Exception {
        new AppApplication().run(args);
    }

    /** Service name shown in logs and on the admin pages. */
    @Override
    public String getName() {
        return "App Application";
    }

    /** Resolves ${ENV_VAR} references (such as the keystore password) in the YAML config. */
    @Override
    public void initialize(Bootstrap<AppConfiguration> bootstrap) {
        ServiceConfiguration.enableEnvironmentSubstitution(bootstrap);
    }

    /** Registers the REST resource with Jersey. */
    @Override
    public void run(AppConfiguration configuration, Environment environment) {
        environment.jersey().register(new AppResource(configuration.getFilesDirectory()));
    }
}
