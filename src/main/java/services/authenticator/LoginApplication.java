package services.authenticator;


import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;



public class LoginApplication extends Application<LoginConfiguration> {
    public static void main(String[] args) throws Exception {
        new LoginApplication().run(args);
    }



    @Override
    public String getName() {
        return "Login Application";
    }

    @Override
    public void initialize(Bootstrap<LoginConfiguration> bootstrap) {




    }

    @Override
    public void run(LoginConfiguration configuration,
                    Environment environment) {
        final LoginResource resource = new LoginResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
         environment.jersey().register(resource);
     //   environment.healthChecks().register("APIHealthCheck", new AppHealthCheck());


    }

}