package services.metrics.config;

public class Metric {

    String name;


    String processorName;  // name of the java object that will process the raw string from the service and return to the caller a relevant number.

    public Metric(String name,String processorName) {
        this.name = name;

        this.processorName = processorName;
    }

    public Metric() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getProcessorName() {
        return processorName;
    }

    public void setProcessorName(String processorName) {
        this.processorName = processorName;
    }
}
