package services.authorizer;


import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


public class AuthApplication extends Application<AuthConfiguration> {
    public static void main(String[] args) throws Exception {
        new AuthApplication().run(args);
    }



    @Override
    public String getName() {
        return "Login Application";
    }

    @Override
    public void initialize(Bootstrap<AuthConfiguration> bootstrap) {




    }

    @Override
    public void run(AuthConfiguration configuration,
                    Environment environment) {
        final AuthResource resource = new AuthResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
         environment.jersey().register(resource);
     //   environment.healthChecks().register("APIHealthCheck", new AppHealthCheck());


    }

}