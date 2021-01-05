package services.metrics.config;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;

public class MetricConfig {


    List<Service> services;

    int freqOfCollection;

    int numberOfCollections;

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public int getFreqOfCollection() {
        return freqOfCollection;
    }

    public void setFreqOfCollection(int freqOfCollection) {
        this.freqOfCollection = freqOfCollection;
    }

    public int getNumberOfCollections() {
        return numberOfCollections;
    }

    public void setNumberOfCollections(int numberOfCollections) {
        this.numberOfCollections = numberOfCollections;
    }


}
