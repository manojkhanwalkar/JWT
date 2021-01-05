
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
    "max",
    "mean",
    "min",
    "p50",
    "p75",
    "p95",
    "p98",
    "p99",
    "p999",
    "stddev",
    "m15_rate",
    "m1_rate",
    "m5_rate",
    "mean_rate",
    "duration_units",
    "rate_units"
})
public class OrgEclipseJettyServerHttpConnectionFactory8381Connections {

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
    @JsonProperty("max")
    private Double max;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("mean")
    private Double mean;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("min")
    private Double min;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("p50")
    private Double p50;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("p75")
    private Double p75;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("p95")
    private Double p95;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("p98")
    private Double p98;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("p99")
    private Double p99;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("p999")
    private Double p999;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("stddev")
    private Double stddev;
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
    @JsonProperty("duration_units")
    private String durationUnits;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("rate_units")
    private String rateUnits;
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

    public OrgEclipseJettyServerHttpConnectionFactory8381Connections withCount(Integer count) {
        this.count = count;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("max")
    public Double getMax() {
        return max;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("max")
    public void setMax(Double max) {
        this.max = max;
    }

    public OrgEclipseJettyServerHttpConnectionFactory8381Connections withMax(Double max) {
        this.max = max;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("mean")
    public Double getMean() {
        return mean;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("mean")
    public void setMean(Double mean) {
        this.mean = mean;
    }

    public OrgEclipseJettyServerHttpConnectionFactory8381Connections withMean(Double mean) {
        this.mean = mean;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("min")
    public Double getMin() {
        return min;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("min")
    public void setMin(Double min) {
        this.min = min;
    }

    public OrgEclipseJettyServerHttpConnectionFactory8381Connections withMin(Double min) {
        this.min = min;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("p50")
    public Double getP50() {
        return p50;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("p50")
    public void setP50(Double p50) {
        this.p50 = p50;
    }

    public OrgEclipseJettyServerHttpConnectionFactory8381Connections withP50(Double p50) {
        this.p50 = p50;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("p75")
    public Double getP75() {
        return p75;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("p75")
    public void setP75(Double p75) {
        this.p75 = p75;
    }

    public OrgEclipseJettyServerHttpConnectionFactory8381Connections withP75(Double p75) {
        this.p75 = p75;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("p95")
    public Double getP95() {
        return p95;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("p95")
    public void setP95(Double p95) {
        this.p95 = p95;
    }

    public OrgEclipseJettyServerHttpConnectionFactory8381Connections withP95(Double p95) {
        this.p95 = p95;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("p98")
    public Double getP98() {
        return p98;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("p98")
    public void setP98(Double p98) {
        this.p98 = p98;
    }

    public OrgEclipseJettyServerHttpConnectionFactory8381Connections withP98(Double p98) {
        this.p98 = p98;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("p99")
    public Double getP99() {
        return p99;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("p99")
    public void setP99(Double p99) {
        this.p99 = p99;
    }

    public OrgEclipseJettyServerHttpConnectionFactory8381Connections withP99(Double p99) {
        this.p99 = p99;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("p999")
    public Double getP999() {
        return p999;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("p999")
    public void setP999(Double p999) {
        this.p999 = p999;
    }

    public OrgEclipseJettyServerHttpConnectionFactory8381Connections withP999(Double p999) {
        this.p999 = p999;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("stddev")
    public Double getStddev() {
        return stddev;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("stddev")
    public void setStddev(Double stddev) {
        this.stddev = stddev;
    }

    public OrgEclipseJettyServerHttpConnectionFactory8381Connections withStddev(Double stddev) {
        this.stddev = stddev;
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

    public OrgEclipseJettyServerHttpConnectionFactory8381Connections withM15Rate(Double m15Rate) {
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

    public OrgEclipseJettyServerHttpConnectionFactory8381Connections withM1Rate(Double m1Rate) {
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

    public OrgEclipseJettyServerHttpConnectionFactory8381Connections withM5Rate(Double m5Rate) {
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

    public OrgEclipseJettyServerHttpConnectionFactory8381Connections withMeanRate(Double meanRate) {
        this.meanRate = meanRate;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("duration_units")
    public String getDurationUnits() {
        return durationUnits;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("duration_units")
    public void setDurationUnits(String durationUnits) {
        this.durationUnits = durationUnits;
    }

    public OrgEclipseJettyServerHttpConnectionFactory8381Connections withDurationUnits(String durationUnits) {
        this.durationUnits = durationUnits;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("rate_units")
    public String getRateUnits() {
        return rateUnits;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("rate_units")
    public void setRateUnits(String rateUnits) {
        this.rateUnits = rateUnits;
    }

    public OrgEclipseJettyServerHttpConnectionFactory8381Connections withRateUnits(String rateUnits) {
        this.rateUnits = rateUnits;
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

    public OrgEclipseJettyServerHttpConnectionFactory8381Connections withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(OrgEclipseJettyServerHttpConnectionFactory8381Connections.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("count");
        sb.append('=');
        sb.append(((this.count == null)?"<null>":this.count));
        sb.append(',');
        sb.append("max");
        sb.append('=');
        sb.append(((this.max == null)?"<null>":this.max));
        sb.append(',');
        sb.append("mean");
        sb.append('=');
        sb.append(((this.mean == null)?"<null>":this.mean));
        sb.append(',');
        sb.append("min");
        sb.append('=');
        sb.append(((this.min == null)?"<null>":this.min));
        sb.append(',');
        sb.append("p50");
        sb.append('=');
        sb.append(((this.p50 == null)?"<null>":this.p50));
        sb.append(',');
        sb.append("p75");
        sb.append('=');
        sb.append(((this.p75 == null)?"<null>":this.p75));
        sb.append(',');
        sb.append("p95");
        sb.append('=');
        sb.append(((this.p95 == null)?"<null>":this.p95));
        sb.append(',');
        sb.append("p98");
        sb.append('=');
        sb.append(((this.p98 == null)?"<null>":this.p98));
        sb.append(',');
        sb.append("p99");
        sb.append('=');
        sb.append(((this.p99 == null)?"<null>":this.p99));
        sb.append(',');
        sb.append("p999");
        sb.append('=');
        sb.append(((this.p999 == null)?"<null>":this.p999));
        sb.append(',');
        sb.append("stddev");
        sb.append('=');
        sb.append(((this.stddev == null)?"<null>":this.stddev));
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
        sb.append("durationUnits");
        sb.append('=');
        sb.append(((this.durationUnits == null)?"<null>":this.durationUnits));
        sb.append(',');
        sb.append("rateUnits");
        sb.append('=');
        sb.append(((this.rateUnits == null)?"<null>":this.rateUnits));
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
        result = ((result* 31)+((this.max == null)? 0 :this.max.hashCode()));
        result = ((result* 31)+((this.m15Rate == null)? 0 :this.m15Rate.hashCode()));
        result = ((result* 31)+((this.count == null)? 0 :this.count.hashCode()));
        result = ((result* 31)+((this.durationUnits == null)? 0 :this.durationUnits.hashCode()));
        result = ((result* 31)+((this.p50 == null)? 0 :this.p50 .hashCode()));
        result = ((result* 31)+((this.p95 == null)? 0 :this.p95 .hashCode()));
        result = ((result* 31)+((this.p98 == null)? 0 :this.p98 .hashCode()));
        result = ((result* 31)+((this.p75 == null)? 0 :this.p75 .hashCode()));
        result = ((result* 31)+((this.p99 == null)? 0 :this.p99 .hashCode()));
        result = ((result* 31)+((this.min == null)? 0 :this.min.hashCode()));
        result = ((result* 31)+((this.meanRate == null)? 0 :this.meanRate.hashCode()));
        result = ((result* 31)+((this.mean == null)? 0 :this.mean.hashCode()));
        result = ((result* 31)+((this.rateUnits == null)? 0 :this.rateUnits.hashCode()));
        result = ((result* 31)+((this.m5Rate == null)? 0 :this.m5Rate.hashCode()));
        result = ((result* 31)+((this.p999 == null)? 0 :this.p999 .hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.stddev == null)? 0 :this.stddev.hashCode()));
        result = ((result* 31)+((this.m1Rate == null)? 0 :this.m1Rate.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OrgEclipseJettyServerHttpConnectionFactory8381Connections) == false) {
            return false;
        }
        OrgEclipseJettyServerHttpConnectionFactory8381Connections rhs = ((OrgEclipseJettyServerHttpConnectionFactory8381Connections) other);
        return (((((((((((((((((((this.max == rhs.max)||((this.max!= null)&&this.max.equals(rhs.max)))&&((this.m15Rate == rhs.m15Rate)||((this.m15Rate!= null)&&this.m15Rate.equals(rhs.m15Rate))))&&((this.count == rhs.count)||((this.count!= null)&&this.count.equals(rhs.count))))&&((this.durationUnits == rhs.durationUnits)||((this.durationUnits!= null)&&this.durationUnits.equals(rhs.durationUnits))))&&((this.p50 == rhs.p50)||((this.p50 != null)&&this.p50 .equals(rhs.p50))))&&((this.p95 == rhs.p95)||((this.p95 != null)&&this.p95 .equals(rhs.p95))))&&((this.p98 == rhs.p98)||((this.p98 != null)&&this.p98 .equals(rhs.p98))))&&((this.p75 == rhs.p75)||((this.p75 != null)&&this.p75 .equals(rhs.p75))))&&((this.p99 == rhs.p99)||((this.p99 != null)&&this.p99 .equals(rhs.p99))))&&((this.min == rhs.min)||((this.min!= null)&&this.min.equals(rhs.min))))&&((this.meanRate == rhs.meanRate)||((this.meanRate!= null)&&this.meanRate.equals(rhs.meanRate))))&&((this.mean == rhs.mean)||((this.mean!= null)&&this.mean.equals(rhs.mean))))&&((this.rateUnits == rhs.rateUnits)||((this.rateUnits!= null)&&this.rateUnits.equals(rhs.rateUnits))))&&((this.m5Rate == rhs.m5Rate)||((this.m5Rate!= null)&&this.m5Rate.equals(rhs.m5Rate))))&&((this.p999 == rhs.p999)||((this.p999 != null)&&this.p999 .equals(rhs.p999))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.stddev == rhs.stddev)||((this.stddev!= null)&&this.stddev.equals(rhs.stddev))))&&((this.m1Rate == rhs.m1Rate)||((this.m1Rate!= null)&&this.m1Rate.equals(rhs.m1Rate))));
    }

}
