/*
 * Metric
 *
 * Configuration for one metric to collect from a service: the metric's name in the
 * Dropwizard /metrics JSON, and the class that turns the raw value into a number.
 */
package services.metrics.config;

public class Metric {

    private String name;

    /** Fully qualified name of the class that extracts a number from the raw metric JSON. */
    private String processorName;

    /** Creates a metric definition. */
    public Metric(String name, String processorName) {
        this.name = name;
        this.processorName = processorName;
    }

    /** No-arg constructor for Jackson. */
    public Metric() {
    }

    /** Metric key, for example "jvm.threads.count". */
    public String getName() {
        return name;
    }

    /** Sets the metric key. */
    public void setName(String name) {
        this.name = name;
    }

    /** Returns the processor class name. */
    public String getProcessorName() {
        return processorName;
    }

    /** Sets the processor class name. */
    public void setProcessorName(String processorName) {
        this.processorName = processorName;
    }
}
