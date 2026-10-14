/*
 * MetricConfigBuilder
 *
 * Placeholder test for building the metrics collector configuration (services.metrics.config).
 */
import org.junit.Test;
import services.metrics.config.Metric;
import services.metrics.config.Service;

import static org.junit.Assert.assertEquals;

public class MetricConfigBuilder {

    /** A default-constructed Service accepts metrics. It used to throw NullPointerException. */
    @Test
    public void buildMetricConfigForServices() {
        Service service = new Service("app", "https://localhost:8381/");
        service.add(new Metric("jvm.threads.count", "processor.Count"));
        assertEquals(1, service.getMetrics().size());

        new Service().add(new Metric());
    }
}
