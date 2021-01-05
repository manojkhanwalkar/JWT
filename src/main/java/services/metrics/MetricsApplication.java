package services.metrics;


import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


public class MetricsApplication extends Application<MetricsConfiguration> {
    public static void main(String[] args) throws Exception {
        new MetricsApplication().run(args);
    }



    @Override
    public String getName() {
        return "Login Application";
    }

    @Override
    public void initialize(Bootstrap<MetricsConfiguration> bootstrap) {




    }

    @Override
    public void run(MetricsConfiguration configuration,
                    Environment environment) {
        final MetricsResource resource = new MetricsResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
         environment.jersey().register(resource);
     //   environment.healthChecks().register("APIHealthCheck", new AppHealthCheck());


    }

}