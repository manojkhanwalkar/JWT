/*
 * LoginApplication
 *
 * Dropwizard entry point for the authenticator service. It checks user
 * credentials against DynamoDB and manages login sessions on https://localhost:8480 (Authenticator.yml).
 * Run it with: java -cp JWT.jar services.authenticator.LoginApplication server <config>.yml
 */
package services.authenticator;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Environment;

public class LoginApplication extends Application<LoginConfiguration> {

    /** Starts the service. Dropwizard reads the command, for example "server App.yml", from {@code args}. */
    public static void main(String[] args) throws Exception {
        new LoginApplication().run(args);
    }

    /** Service name shown in logs and on the admin pages. */
    @Override
    public String getName() {
        return "Login Application";
    }

    /** Registers the REST resource with Jersey. */
    @Override
    public void run(LoginConfiguration configuration, Environment environment) {
        environment.jersey().register(new LoginResource());
    }
}
