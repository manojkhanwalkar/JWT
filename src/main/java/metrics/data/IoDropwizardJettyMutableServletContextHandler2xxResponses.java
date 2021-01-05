
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
    "count",
    "m15_rate",
    "m1_rate",
    "m5_rate",
    "mean_rate",
    "units"
})
public class IoDropwizardJettyMutableServletContextHandler2xxResponses {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("count")
    private Integer count;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("m15_rate")
    private Double m15Rate;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("m1_rate")
    private Double m1Rate;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("m5_rate")
    private Double m5Rate;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("mean_rate")
    private Double meanRate;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("units")
    private String units;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("count")
    public Integer getCount() {
        return count;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("count")
    public void setCount(Integer count) {
        this.count = count;
    }

    public IoDropwizardJettyMutableServletContextHandler2xxResponses withCount(Integer count) {
        this.count = count;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("m15_rate")
    public Double getM15Rate() {
        return m15Rate;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("m15_rate")
    public void setM15Rate(Double m15Rate) {
        this.m15Rate = m15Rate;
    }

    public IoDropwizardJettyMutableServletContextHandler2xxResponses withM15Rate(Double m15Rate) {
        this.m15Rate = m15Rate;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("m1_rate")
    public Double getM1Rate() {
        return m1Rate;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("m1_rate")
    public void setM1Rate(Double m1Rate) {
        this.m1Rate = m1Rate;
    }

    public IoDropwizardJettyMutableServletContextHandler2xxResponses withM1Rate(Double m1Rate) {
        this.m1Rate = m1Rate;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("m5_rate")
    public Double getM5Rate() {
        return m5Rate;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("m5_rate")
    public void setM5Rate(Double m5Rate) {
        this.m5Rate = m5Rate;
    }

    public IoDropwizardJettyMutableServletContextHandler2xxResponses withM5Rate(Double m5Rate) {
        this.m5Rate = m5Rate;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("mean_rate")
    public Double getMeanRate() {
        return meanRate;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("mean_rate")
    public void setMeanRate(Double meanRate) {
        this.meanRate = meanRate;
    }

    public IoDropwizardJettyMutableServletContextHandler2xxResponses withMeanRate(Double meanRate) {
        this.meanRate = meanRate;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("units")
    public String getUnits() {
        return units;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("units")
    public void setUnits(String units) {
        this.units = units;
    }

    public IoDropwizardJettyMutableServletContextHandler2xxResponses withUnits(String units) {
        this.units = units;
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

    public IoDropwizardJettyMutableServletContextHandler2xxResponses withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(IoDropwizardJettyMutableServletContextHandler2xxResponses.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("count");
        sb.append('=');
        sb.append(((this.count == null)?"<null>":this.count));
        sb.append(',');
        sb.append("m15Rate");
        sb.append('=');
        sb.append(((this.m15Rate == null)?"<null>":this.m15Rate));
        sb.append(',');
        sb.append("m1Rate");
        sb.append('=');
        sb.append(((this.m1Rate == null)?"<null>":this.m1Rate));
        sb.append(',');
        sb.append("m5Rate");
        sb.append('=');
        sb.append(((this.m5Rate == null)?"<null>":this.m5Rate));
        sb.append(',');
        sb.append("meanRate");
        sb.append('=');
        sb.append(((this.meanRate == null)?"<null>":this.meanRate));
        sb.append(',');
        sb.append("units");
        sb.append('=');
        sb.append(((this.units == null)?"<null>":this.units));
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
        result = ((result* 31)+((this.meanRate == null)? 0 :this.meanRate.hashCode()));
        result = ((result* 31)+((this.m15Rate == null)? 0 :this.m15Rate.hashCode()));
        result = ((result* 31)+((this.count == null)? 0 :this.count.hashCode()));
        result = ((result* 31)+((this.m5Rate == null)? 0 :this.m5Rate.hashCode()));
        result = ((result* 31)+((this.units == null)? 0 :this.units.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.m1Rate == null)? 0 :this.m1Rate.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof IoDropwizardJettyMutableServletContextHandler2xxResponses) == false) {
            return false;
        }
        IoDropwizardJettyMutableServletContextHandler2xxResponses rhs = ((IoDropwizardJettyMutableServletContextHandler2xxResponses) other);
        return ((((((((this.meanRate == rhs.meanRate)||((this.meanRate!= null)&&this.meanRate.equals(rhs.meanRate)))&&((this.m15Rate == rhs.m15Rate)||((this.m15Rate!= null)&&this.m15Rate.equals(rhs.m15Rate))))&&((this.count == rhs.count)||((this.count!= null)&&this.count.equals(rhs.count))))&&((this.m5Rate == rhs.m5Rate)||((this.m5Rate!= null)&&this.m5Rate.equals(rhs.m5Rate))))&&((this.units == rhs.units)||((this.units!= null)&&this.units.equals(rhs.units))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.m1Rate == rhs.m1Rate)||((this.m1Rate!= null)&&this.m1Rate.equals(rhs.m1Rate))));
    }

}
