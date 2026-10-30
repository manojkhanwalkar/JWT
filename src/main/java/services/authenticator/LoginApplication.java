/*
 * LoginApplication
 *
 * Dropwizard entry point for the authenticator service. It checks user
 * credentials against DynamoDB and manages login sessions on https://localhost:8480 (Authenticator.yml).
 * Run it with: java -cp JWT.jar services.authenticator.LoginApplication server <config>.yml
 */
package services.authenticator;

import io.dropwizard.core.Application;
import jakarta.servlet.DispatcherType;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import services.ServiceConfiguration;

import java.util.EnumSet;

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

    /** Resolves ${ENV_VAR} references (such as the keystore password) in the YAML config. */
    @Override
    public void initialize(Bootstrap<LoginConfiguration> bootstrap) {
        ServiceConfiguration.enableEnvironmentSubstitution(bootstrap);
    }

    /** Registers the REST resource with Jersey, behind the per-IP rate limit on /login. */
    @Override
    public void run(LoginConfiguration configuration, Environment environment) {
        environment.servlets()
                .addFilter("login-rate-limit", new LoginRateLimitFilter())
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/login");
        environment.jersey().register(new LoginResource());
    }
}
