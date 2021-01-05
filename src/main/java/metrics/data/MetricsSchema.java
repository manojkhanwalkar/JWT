
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
    "version",
    "gauges",
    "meters",
    "timers"
})
public class MetricsSchema {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("version")
    private String version;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("gauges")
    private Gauges gauges;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("meters")
    private Meters meters;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("timers")
    private Timers timers;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    public MetricsSchema withVersion(String version) {
        this.version = version;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("gauges")
    public Gauges getGauges() {
        return gauges;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("gauges")
    public void setGauges(Gauges gauges) {
        this.gauges = gauges;
    }

    public MetricsSchema withGauges(Gauges gauges) {
        this.gauges = gauges;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("meters")
    public Meters getMeters() {
        return meters;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("meters")
    public void setMeters(Meters meters) {
        this.meters = meters;
    }

    public MetricsSchema withMeters(Meters meters) {
        this.meters = meters;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("timers")
    public Timers getTimers() {
        return timers;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("timers")
    public void setTimers(Timers timers) {
        this.timers = timers;
    }

    public MetricsSchema withTimers(Timers timers) {
        this.timers = timers;
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

    public MetricsSchema withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MetricsSchema.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("version");
        sb.append('=');
        sb.append(((this.version == null)?"<null>":this.version));
        sb.append(',');
        sb.append("gauges");
        sb.append('=');
        sb.append(((this.gauges == null)?"<null>":this.gauges));
        sb.append(',');
        sb.append("meters");
        sb.append('=');
        sb.append(((this.meters == null)?"<null>":this.meters));
        sb.append(',');
        sb.append("timers");
        sb.append('=');
        sb.append(((this.timers == null)?"<null>":this.timers));
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
        result = ((result* 31)+((this.gauges == null)? 0 :this.gauges.hashCode()));
        result = ((result* 31)+((this.timers == null)? 0 :this.timers.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.version == null)? 0 :this.version.hashCode()));
        result = ((result* 31)+((this.meters == null)? 0 :this.meters.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof MetricsSchema) == false) {
            return false;
        }
        MetricsSchema rhs = ((MetricsSchema) other);
        return ((((((this.gauges == rhs.gauges)||((this.gauges!= null)&&this.gauges.equals(rhs.gauges)))&&((this.timers == rhs.timers)||((this.timers!= null)&&this.timers.equals(rhs.timers))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.version == rhs.version)||((this.version!= null)&&this.version.equals(rhs.version))))&&((this.meters == rhs.meters)||((this.meters!= null)&&this.meters.equals(rhs.meters))));
    }

}
