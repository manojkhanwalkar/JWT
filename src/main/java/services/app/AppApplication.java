package services.app;


import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


public class AppApplication extends Application<AppConfiguration> {
    public static void main(String[] args) throws Exception {
        new AppApplication().run(args);
    }



    @Override
    public String getName() {
        return "Login Application";
    }

    @Override
    public void initialize(Bootstrap<AppConfiguration> bootstrap) {




    }

    @Override
    public void run(AppConfiguration configuration,
                    Environment environment) {
        final AppResource resource = new AppResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
         environment.jersey().register(resource);
     //   environment.healthChecks().register("APIHealthCheck", new AppHealthCheck());


    }

}