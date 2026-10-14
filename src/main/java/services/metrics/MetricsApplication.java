/*
 * MetricsApplication
 *
 * Dropwizard entry point for the metrics service. This is work in progress:
 * its resource is currently a copy of the authorizer's endpoints.
 * Run it with: java -cp JWT.jar services.metrics.MetricsApplication server <config>.yml
 */
package services.metrics;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Environment;

public class MetricsApplication extends Application<MetricsConfiguration> {

    /** Starts the service. Dropwizard reads the command, for example "server App.yml", from {@code args}. */
    public static void main(String[] args) throws Exception {
        new MetricsApplication().run(args);
    }

    /** Service name shown in logs and on the admin pages. */
    @Override
    public String getName() {
        return "Metrics Application";
    }

    /** Registers the REST resource with Jersey. */
    @Override
    public void run(MetricsConfiguration configuration, Environment environment) {
        environment.jersey().register(new MetricsResource());
    }
}
