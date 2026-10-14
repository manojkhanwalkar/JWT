/*
 * Service
 *
 * Configuration for one monitored service: its name, the URL of its Dropwizard admin
 * endpoint, and the metrics to collect from it.
 */
package services.metrics.config;

import java.util.ArrayList;
import java.util.List;

public class Service {

    private String name;
    private String adminURL;

    private List<Metric> metrics = new ArrayList<>();

    /** No-arg constructor for Jackson. */
    public Service() {
    }

    /** Creates a service definition that has no metrics yet. */
    public Service(String name, String adminURL) {
        this.name = name;
        this.adminURL = adminURL;
    }

    /** Adds a metric to collect. */
    public void add(Metric metric) {
        metrics.add(metric);
    }

    /** Returns the metrics to collect from this service. */
    public List<Metric> getMetrics() {
        return metrics;
    }

    /** Replaces the metric list. */
    public void setMetrics(List<Metric> metrics) {
        this.metrics = metrics;
    }

    /** Returns the service name. */
    public String getName() {
        return name;
    }

    /** Sets the service name. */
    public void setName(String name) {
        this.name = name;
    }

    /** Returns the base URL of the admin endpoint, for example https://localhost:8381/. */
    public String getAdminURL() {
        return adminURL;
    }

    /** Sets the base URL of the admin endpoint. */
    public void setAdminURL(String adminURL) {
        this.adminURL = adminURL;
    }
}
