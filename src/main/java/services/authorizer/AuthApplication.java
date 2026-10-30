/*
 * AuthApplication
 *
 * Dropwizard entry point for the authorizer service. It swaps a valid login
 * session for a signed JWT on https://localhost:8280 (Auth.yml).
 * Run it with: java -cp JWT.jar services.authorizer.AuthApplication server <config>.yml
 */
package services.authorizer;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import services.ServiceConfiguration;

public class AuthApplication extends Application<AuthConfiguration> {

    /** Starts the service. Dropwizard reads the command, for example "server App.yml", from {@code args}. */
    public static void main(String[] args) throws Exception {
        new AuthApplication().run(args);
    }

    /** Service name shown in logs and on the admin pages. */
    @Override
    public String getName() {
        return "Auth Application";
    }

    /** Resolves ${ENV_VAR} references (such as the keystore password) in the YAML config. */
    @Override
    public void initialize(Bootstrap<AuthConfiguration> bootstrap) {
        ServiceConfiguration.enableEnvironmentSubstitution(bootstrap);
    }

    /** Registers the REST resource with Jersey. */
    @Override
    public void run(AuthConfiguration configuration, Environment environment) {
        environment.jersey().register(new AuthResource());
    }
}
