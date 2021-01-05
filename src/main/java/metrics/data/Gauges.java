
package metrics.data;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "jvm.gc.G1-Young-Generation.count",
    "jvm.memory.heap.usage",
    "jvm.threads.count",
    "jvm.threads.daemon.count"
})
public class Gauges {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jvm.gc.G1-Young-Generation.count")
    private JvmGcG1YoungGenerationCount jvmGcG1YoungGenerationCount;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jvm.memory.heap.usage")
    private JvmMemoryHeapUsage jvmMemoryHeapUsage;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jvm.threads.count")
    private JvmThreadsCount jvmThreadsCount;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jvm.threads.daemon.count")
    private JvmThreadsDaemonCount jvmThreadsDaemonCount;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jvm.gc.G1-Young-Generation.count")
    public JvmGcG1YoungGenerationCount getJvmGcG1YoungGenerationCount() {
        return jvmGcG1YoungGenerationCount;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jvm.gc.G1-Young-Generation.count")
    public void setJvmGcG1YoungGenerationCount(JvmGcG1YoungGenerationCount jvmGcG1YoungGenerationCount) {
        this.jvmGcG1YoungGenerationCount = jvmGcG1YoungGenerationCount;
    }

    public Gauges withJvmGcG1YoungGenerationCount(JvmGcG1YoungGenerationCount jvmGcG1YoungGenerationCount) {
        this.jvmGcG1YoungGenerationCount = jvmGcG1YoungGenerationCount;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jvm.memory.heap.usage")
    public JvmMemoryHeapUsage getJvmMemoryHeapUsage() {
        return jvmMemoryHeapUsage;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jvm.memory.heap.usage")
    public void setJvmMemoryHeapUsage(JvmMemoryHeapUsage jvmMemoryHeapUsage) {
        this.jvmMemoryHeapUsage = jvmMemoryHeapUsage;
    }

    public Gauges withJvmMemoryHeapUsage(JvmMemoryHeapUsage jvmMemoryHeapUsage) {
        this.jvmMemoryHeapUsage = jvmMemoryHeapUsage;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jvm.threads.count")
    public JvmThreadsCount getJvmThreadsCount() {
        return jvmThreadsCount;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jvm.threads.count")
    public void setJvmThreadsCount(JvmThreadsCount jvmThreadsCount) {
        this.jvmThreadsCount = jvmThreadsCount;
    }

    public Gauges withJvmThreadsCount(JvmThreadsCount jvmThreadsCount) {
        this.jvmThreadsCount = jvmThreadsCount;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jvm.threads.daemon.count")
    public JvmThreadsDaemonCount getJvmThreadsDaemonCount() {
        return jvmThreadsDaemonCount;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("jvm.threads.daemon.count")
    public void setJvmThreadsDaemonCount(JvmThreadsDaemonCount jvmThreadsDaemonCount) {
        this.jvmThreadsDaemonCount = jvmThreadsDaemonCount;
    }

    public Gauges withJvmThreadsDaemonCount(JvmThreadsDaemonCount jvmThreadsDaemonCount) {
        this.jvmThreadsDaemonCount = jvmThreadsDaemonCount;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Gauges withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Gauges.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("jvmGcG1YoungGenerationCount");
        sb.append('=');
        sb.append(((this.jvmGcG1YoungGenerationCount == null)?"<null>":this.jvmGcG1YoungGenerationCount));
        sb.append(',');
        sb.append("jvmMemoryHeapUsage");
        sb.append('=');
        sb.append(((this.jvmMemoryHeapUsage == null)?"<null>":this.jvmMemoryHeapUsage));
        sb.append(',');
        sb.append("jvmThreadsCount");
        sb.append('=');
        sb.append(((this.jvmThreadsCount == null)?"<null>":this.jvmThreadsCount));
        sb.append(',');
        sb.append("jvmThreadsDaemonCount");
        sb.append('=');
        sb.append(((this.jvmThreadsDaemonCount == null)?"<null>":this.jvmThreadsDaemonCount));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null)?"<null>":this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.jvmMemoryHeapUsage == null)? 0 :this.jvmMemoryHeapUsage.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.jvmGcG1YoungGenerationCount == null)? 0 :this.jvmGcG1YoungGenerationCount.hashCode()));
        result = ((result* 31)+((this.jvmThreadsDaemonCount == null)? 0 :this.jvmThreadsDaemonCount.hashCode()));
        result = ((result* 31)+((this.jvmThreadsCount == null)? 0 :this.jvmThreadsCount.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Gauges) == false) {
            return false;
        }
        Gauges rhs = ((Gauges) other);
        return ((((((this.jvmMemoryHeapUsage == rhs.jvmMemoryHeapUsage)||((this.jvmMemoryHeapUsage!= null)&&this.jvmMemoryHeapUsage.equals(rhs.jvmMemoryHeapUsage)))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.jvmGcG1YoungGenerationCount == rhs.jvmGcG1YoungGenerationCount)||((this.jvmGcG1YoungGenerationCount!= null)&&this.jvmGcG1YoungGenerationCount.equals(rhs.jvmGcG1YoungGenerationCount))))&&((this.jvmThreadsDaemonCount == rhs.jvmThreadsDaemonCount)||((this.jvmThreadsDaemonCount!= null)&&this.jvmThreadsDaemonCount.equals(rhs.jvmThreadsDaemonCount))))&&((this.jvmThreadsCount == rhs.jvmThreadsCount)||((this.jvmThreadsCount!= null)&&this.jvmThreadsCount.equals(rhs.jvmThreadsCount))));
    }

}
