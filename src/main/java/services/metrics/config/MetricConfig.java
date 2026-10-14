/*
 * MetricConfig
 *
 * Top-level configuration for the metrics collector: which services to poll, how
 * often to poll, and how many samples to keep per metric (the CircularQueue capacity).
 */
package services.metrics.config;

import java.util.List;

public class MetricConfig {

    private List<Service> services;

    private int freqOfCollection;

    private int numberOfCollections;

    /** Returns the services to poll. */
    public List<Service> getServices() {
        return services;
    }

    /** Sets the services to poll. */
    public void setServices(List<Service> services) {
        this.services = services;
    }

    /** Returns the polling interval. */
    public int getFreqOfCollection() {
        return freqOfCollection;
    }

    /** Sets the polling interval. */
    public void setFreqOfCollection(int freqOfCollection) {
        this.freqOfCollection = freqOfCollection;
    }

    /** Returns the number of samples kept per metric. */
    public int getNumberOfCollections() {
        return numberOfCollections;
    }

    /** Sets the number of samples kept per metric. */
    public void setNumberOfCollections(int numberOfCollections) {
        this.numberOfCollections = numberOfCollections;
    }
}
