
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
    "ch.qos.logback.core.Appender.all",
    "io.dropwizard.jetty.MutableServletContextHandler.2xx-responses",
    "io.dropwizard.jetty.MutableServletContextHandler.4xx-responses",
    "io.dropwizard.jetty.MutableServletContextHandler.5xx-responses"
})
public class Meters {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("ch.qos.logback.core.Appender.all")
    private ChQosLogbackCoreAppenderAll chQosLogbackCoreAppenderAll;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("io.dropwizard.jetty.MutableServletContextHandler.2xx-responses")
    private IoDropwizardJettyMutableServletContextHandler2xxResponses ioDropwizardJettyMutableServletContextHandler2xxResponses;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("io.dropwizard.jetty.MutableServletContextHandler.4xx-responses")
    private IoDropwizardJettyMutableServletContextHandler4xxResponses ioDropwizardJettyMutableServletContextHandler4xxResponses;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("io.dropwizard.jetty.MutableServletContextHandler.5xx-responses")
    private IoDropwizardJettyMutableServletContextHandler5xxResponses ioDropwizardJettyMutableServletContextHandler5xxResponses;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("ch.qos.logback.core.Appender.all")
    public ChQosLogbackCoreAppenderAll getChQosLogbackCoreAppenderAll() {
        return chQosLogbackCoreAppenderAll;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("ch.qos.logback.core.Appender.all")
    public void setChQosLogbackCoreAppenderAll(ChQosLogbackCoreAppenderAll chQosLogbackCoreAppenderAll) {
        this.chQosLogbackCoreAppenderAll = chQosLogbackCoreAppenderAll;
    }

    public Meters withChQosLogbackCoreAppenderAll(ChQosLogbackCoreAppenderAll chQosLogbackCoreAppenderAll) {
        this.chQosLogbackCoreAppenderAll = chQosLogbackCoreAppenderAll;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("io.dropwizard.jetty.MutableServletContextHandler.2xx-responses")
    public IoDropwizardJettyMutableServletContextHandler2xxResponses getIoDropwizardJettyMutableServletContextHandler2xxResponses() {
        return ioDropwizardJettyMutableServletContextHandler2xxResponses;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("io.dropwizard.jetty.MutableServletContextHandler.2xx-responses")
    public void setIoDropwizardJettyMutableServletContextHandler2xxResponses(IoDropwizardJettyMutableServletContextHandler2xxResponses ioDropwizardJettyMutableServletContextHandler2xxResponses) {
        this.ioDropwizardJettyMutableServletContextHandler2xxResponses = ioDropwizardJettyMutableServletContextHandler2xxResponses;
    }

    public Meters withIoDropwizardJettyMutableServletContextHandler2xxResponses(IoDropwizardJettyMutableServletContextHandler2xxResponses ioDropwizardJettyMutableServletContextHandler2xxResponses) {
        this.ioDropwizardJettyMutableServletContextHandler2xxResponses = ioDropwizardJettyMutableServletContextHandler2xxResponses;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("io.dropwizard.jetty.MutableServletContextHandler.4xx-responses")
    public IoDropwizardJettyMutableServletContextHandler4xxResponses getIoDropwizardJettyMutableServletContextHandler4xxResponses() {
        return ioDropwizardJettyMutableServletContextHandler4xxResponses;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("io.dropwizard.jetty.MutableServletContextHandler.4xx-responses")
    public void setIoDropwizardJettyMutableServletContextHandler4xxResponses(IoDropwizardJettyMutableServletContextHandler4xxResponses ioDropwizardJettyMutableServletContextHandler4xxResponses) {
        this.ioDropwizardJettyMutableServletContextHandler4xxResponses = ioDropwizardJettyMutableServletContextHandler4xxResponses;
    }

    public Meters withIoDropwizardJettyMutableServletContextHandler4xxResponses(IoDropwizardJettyMutableServletContextHandler4xxResponses ioDropwizardJettyMutableServletContextHandler4xxResponses) {
        this.ioDropwizardJettyMutableServletContextHandler4xxResponses = ioDropwizardJettyMutableServletContextHandler4xxResponses;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("io.dropwizard.jetty.MutableServletContextHandler.5xx-responses")
    public IoDropwizardJettyMutableServletContextHandler5xxResponses getIoDropwizardJettyMutableServletContextHandler5xxResponses() {
        return ioDropwizardJettyMutableServletContextHandler5xxResponses;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("io.dropwizard.jetty.MutableServletContextHandler.5xx-responses")
    public void setIoDropwizardJettyMutableServletContextHandler5xxResponses(IoDropwizardJettyMutableServletContextHandler5xxResponses ioDropwizardJettyMutableServletContextHandler5xxResponses) {
        this.ioDropwizardJettyMutableServletContextHandler5xxResponses = ioDropwizardJettyMutableServletContextHandler5xxResponses;
    }

    public Meters withIoDropwizardJettyMutableServletContextHandler5xxResponses(IoDropwizardJettyMutableServletContextHandler5xxResponses ioDropwizardJettyMutableServletContextHandler5xxResponses) {
        this.ioDropwizardJettyMutableServletContextHandler5xxResponses = ioDropwizardJettyMutableServletContextHandler5xxResponses;
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

    public Meters withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Meters.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("chQosLogbackCoreAppenderAll");
        sb.append('=');
        sb.append(((this.chQosLogbackCoreAppenderAll == null)?"<null>":this.chQosLogbackCoreAppenderAll));
        sb.append(',');
        sb.append("ioDropwizardJettyMutableServletContextHandler2xxResponses");
        sb.append('=');
        sb.append(((this.ioDropwizardJettyMutableServletContextHandler2xxResponses == null)?"<null>":this.ioDropwizardJettyMutableServletContextHandler2xxResponses));
        sb.append(',');
        sb.append("ioDropwizardJettyMutableServletContextHandler4xxResponses");
        sb.append('=');
        sb.append(((this.ioDropwizardJettyMutableServletContextHandler4xxResponses == null)?"<null>":this.ioDropwizardJettyMutableServletContextHandler4xxResponses));
        sb.append(',');
        sb.append("ioDropwizardJettyMutableServletContextHandler5xxResponses");
        sb.append('=');
        sb.append(((this.ioDropwizardJettyMutableServletContextHandler5xxResponses == null)?"<null>":this.ioDropwizardJettyMutableServletContextHandler5xxResponses));
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
        result = ((result* 31)+((this.ioDropwizardJettyMutableServletContextHandler2xxResponses == null)? 0 :this.ioDropwizardJettyMutableServletContextHandler2xxResponses.hashCode()));
        result = ((result* 31)+((this.ioDropwizardJettyMutableServletContextHandler5xxResponses == null)? 0 :this.ioDropwizardJettyMutableServletContextHandler5xxResponses.hashCode()));
        result = ((result* 31)+((this.chQosLogbackCoreAppenderAll == null)? 0 :this.chQosLogbackCoreAppenderAll.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.ioDropwizardJettyMutableServletContextHandler4xxResponses == null)? 0 :this.ioDropwizardJettyMutableServletContextHandler4xxResponses.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Meters) == false) {
            return false;
        }
        Meters rhs = ((Meters) other);
        return ((((((this.ioDropwizardJettyMutableServletContextHandler2xxResponses == rhs.ioDropwizardJettyMutableServletContextHandler2xxResponses)||((this.ioDropwizardJettyMutableServletContextHandler2xxResponses!= null)&&this.ioDropwizardJettyMutableServletContextHandler2xxResponses.equals(rhs.ioDropwizardJettyMutableServletContextHandler2xxResponses)))&&((this.ioDropwizardJettyMutableServletContextHandler5xxResponses == rhs.ioDropwizardJettyMutableServletContextHandler5xxResponses)||((this.ioDropwizardJettyMutableServletContextHandler5xxResponses!= null)&&this.ioDropwizardJettyMutableServletContextHandler5xxResponses.equals(rhs.ioDropwizardJettyMutableServletContextHandler5xxResponses))))&&((this.chQosLogbackCoreAppenderAll == rhs.chQosLogbackCoreAppenderAll)||((this.chQosLogbackCoreAppenderAll!= null)&&this.chQosLogbackCoreAppenderAll.equals(rhs.chQosLogbackCoreAppenderAll))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.ioDropwizardJettyMutableServletContextHandler4xxResponses == rhs.ioDropwizardJettyMutableServletContextHandler4xxResponses)||((this.ioDropwizardJettyMutableServletContextHandler4xxResponses!= null)&&this.ioDropwizardJettyMutableServletContextHandler4xxResponses.equals(rhs.ioDropwizardJettyMutableServletContextHandler4xxResponses))));
    }

}
