package services.metrics.config;

import java.util.List;

public class Service {

    String name ;
    String adminURL;

    List<Metric> metrics;

    public void add(Metric metric)
    {
        metrics.add(metric);
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<Metric> metrics) {
        this.metrics = metrics;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdminURL() {
        return adminURL;
    }

    public void setAdminURL(String adminURL) {
        this.adminURL = adminURL;
    }

    public Service() {
    }

    public Service(String name, String adminURL) {
        this.name = name;
        this.adminURL = adminURL;
    }
}
