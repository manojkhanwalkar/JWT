
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
    "io.dropwizard.jetty.MutableServletContextHandler.dispatches",
    "io.dropwizard.jetty.MutableServletContextHandler.post-requests",
    "io.dropwizard.jetty.MutableServletContextHandler.requests",
    "org.eclipse.jetty.server.HttpConnectionFactory.8380.connections",
    "org.eclipse.jetty.server.HttpConnectionFactory.8381.connections",
    "services.app.AppResource.claims"
})
public class Timers {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("io.dropwizard.jetty.MutableServletContextHandler.dispatches")
    private IoDropwizardJettyMutableServletContextHandlerDispatches ioDropwizardJettyMutableServletContextHandlerDispatches;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("io.dropwizard.jetty.MutableServletContextHandler.post-requests")
    private IoDropwizardJettyMutableServletContextHandlerPostRequests ioDropwizardJettyMutableServletContextHandlerPostRequests;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("io.dropwizard.jetty.MutableServletContextHandler.requests")
    private IoDropwizardJettyMutableServletContextHandlerRequests ioDropwizardJettyMutableServletContextHandlerRequests;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("org.eclipse.jetty.server.HttpConnectionFactory.8380.connections")
    private OrgEclipseJettyServerHttpConnectionFactory8380Connections orgEclipseJettyServerHttpConnectionFactory8380Connections;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("org.eclipse.jetty.server.HttpConnectionFactory.8381.connections")
    private OrgEclipseJettyServerHttpConnectionFactory8381Connections orgEclipseJettyServerHttpConnectionFactory8381Connections;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("services.app.AppResource.claims")
    private ServicesAppAppResourceClaims servicesAppAppResourceClaims;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("io.dropwizard.jetty.MutableServletContextHandler.dispatches")
    public IoDropwizardJettyMutableServletContextHandlerDispatches getIoDropwizardJettyMutableServletContextHandlerDispatches() {
        return ioDropwizardJettyMutableServletContextHandlerDispatches;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("io.dropwizard.jetty.MutableServletContextHandler.dispatches")
    public void setIoDropwizardJettyMutableServletContextHandlerDispatches(IoDropwizardJettyMutableServletContextHandlerDispatches ioDropwizardJettyMutableServletContextHandlerDispatches) {
        this.ioDropwizardJettyMutableServletContextHandlerDispatches = ioDropwizardJettyMutableServletContextHandlerDispatches;
    }

    public Timers withIoDropwizardJettyMutableServletContextHandlerDispatches(IoDropwizardJettyMutableServletContextHandlerDispatches ioDropwizardJettyMutableServletContextHandlerDispatches) {
        this.ioDropwizardJettyMutableServletContextHandlerDispatches = ioDropwizardJettyMutableServletContextHandlerDispatches;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("io.dropwizard.jetty.MutableServletContextHandler.post-requests")
    public IoDropwizardJettyMutableServletContextHandlerPostRequests getIoDropwizardJettyMutableServletContextHandlerPostRequests() {
        return ioDropwizardJettyMutableServletContextHandlerPostRequests;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("io.dropwizard.jetty.MutableServletContextHandler.post-requests")
    public void setIoDropwizardJettyMutableServletContextHandlerPostRequests(IoDropwizardJettyMutableServletContextHandlerPostRequests ioDropwizardJettyMutableServletContextHandlerPostRequests) {
        this.ioDropwizardJettyMutableServletContextHandlerPostRequests = ioDropwizardJettyMutableServletContextHandlerPostRequests;
    }

    public Timers withIoDropwizardJettyMutableServletContextHandlerPostRequests(IoDropwizardJettyMutableServletContextHandlerPostRequests ioDropwizardJettyMutableServletContextHandlerPostRequests) {
        this.ioDropwizardJettyMutableServletContextHandlerPostRequests = ioDropwizardJettyMutableServletContextHandlerPostRequests;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("io.dropwizard.jetty.MutableServletContextHandler.requests")
    public IoDropwizardJettyMutableServletContextHandlerRequests getIoDropwizardJettyMutableServletContextHandlerRequests() {
        return ioDropwizardJettyMutableServletContextHandlerRequests;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("io.dropwizard.jetty.MutableServletContextHandler.requests")
    public void setIoDropwizardJettyMutableServletContextHandlerRequests(IoDropwizardJettyMutableServletContextHandlerRequests ioDropwizardJettyMutableServletContextHandlerRequests) {
        this.ioDropwizardJettyMutableServletContextHandlerRequests = ioDropwizardJettyMutableServletContextHandlerRequests;
    }

    public Timers withIoDropwizardJettyMutableServletContextHandlerRequests(IoDropwizardJettyMutableServletContextHandlerRequests ioDropwizardJettyMutableServletContextHandlerRequests) {
        this.ioDropwizardJettyMutableServletContextHandlerRequests = ioDropwizardJettyMutableServletContextHandlerRequests;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("org.eclipse.jetty.server.HttpConnectionFactory.8380.connections")
    public OrgEclipseJettyServerHttpConnectionFactory8380Connections getOrgEclipseJettyServerHttpConnectionFactory8380Connections() {
        return orgEclipseJettyServerHttpConnectionFactory8380Connections;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("org.eclipse.jetty.server.HttpConnectionFactory.8380.connections")
    public void setOrgEclipseJettyServerHttpConnectionFactory8380Connections(OrgEclipseJettyServerHttpConnectionFactory8380Connections orgEclipseJettyServerHttpConnectionFactory8380Connections) {
        this.orgEclipseJettyServerHttpConnectionFactory8380Connections = orgEclipseJettyServerHttpConnectionFactory8380Connections;
    }

    public Timers withOrgEclipseJettyServerHttpConnectionFactory8380Connections(OrgEclipseJettyServerHttpConnectionFactory8380Connections orgEclipseJettyServerHttpConnectionFactory8380Connections) {
        this.orgEclipseJettyServerHttpConnectionFactory8380Connections = orgEclipseJettyServerHttpConnectionFactory8380Connections;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("org.eclipse.jetty.server.HttpConnectionFactory.8381.connections")
    public OrgEclipseJettyServerHttpConnectionFactory8381Connections getOrgEclipseJettyServerHttpConnectionFactory8381Connections() {
        return orgEclipseJettyServerHttpConnectionFactory8381Connections;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("org.eclipse.jetty.server.HttpConnectionFactory.8381.connections")
    public void setOrgEclipseJettyServerHttpConnectionFactory8381Connections(OrgEclipseJettyServerHttpConnectionFactory8381Connections orgEclipseJettyServerHttpConnectionFactory8381Connections) {
        this.orgEclipseJettyServerHttpConnectionFactory8381Connections = orgEclipseJettyServerHttpConnectionFactory8381Connections;
    }

    public Timers withOrgEclipseJettyServerHttpConnectionFactory8381Connections(OrgEclipseJettyServerHttpConnectionFactory8381Connections orgEclipseJettyServerHttpConnectionFactory8381Connections) {
        this.orgEclipseJettyServerHttpConnectionFactory8381Connections = orgEclipseJettyServerHttpConnectionFactory8381Connections;
        return this;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("services.app.AppResource.claims")
    public ServicesAppAppResourceClaims getServicesAppAppResourceClaims() {
        return servicesAppAppResourceClaims;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("services.app.AppResource.claims")
    public void setServicesAppAppResourceClaims(ServicesAppAppResourceClaims servicesAppAppResourceClaims) {
        this.servicesAppAppResourceClaims = servicesAppAppResourceClaims;
    }

    public Timers withServicesAppAppResourceClaims(ServicesAppAppResourceClaims servicesAppAppResourceClaims) {
        this.servicesAppAppResourceClaims = servicesAppAppResourceClaims;
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

    public Timers withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Timers.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("ioDropwizardJettyMutableServletContextHandlerDispatches");
        sb.append('=');
        sb.append(((this.ioDropwizardJettyMutableServletContextHandlerDispatches == null)?"<null>":this.ioDropwizardJettyMutableServletContextHandlerDispatches));
        sb.append(',');
        sb.append("ioDropwizardJettyMutableServletContextHandlerPostRequests");
        sb.append('=');
        sb.append(((this.ioDropwizardJettyMutableServletContextHandlerPostRequests == null)?"<null>":this.ioDropwizardJettyMutableServletContextHandlerPostRequests));
        sb.append(',');
        sb.append("ioDropwizardJettyMutableServletContextHandlerRequests");
        sb.append('=');
        sb.append(((this.ioDropwizardJettyMutableServletContextHandlerRequests == null)?"<null>":this.ioDropwizardJettyMutableServletContextHandlerRequests));
        sb.append(',');
        sb.append("orgEclipseJettyServerHttpConnectionFactory8380Connections");
        sb.append('=');
        sb.append(((this.orgEclipseJettyServerHttpConnectionFactory8380Connections == null)?"<null>":this.orgEclipseJettyServerHttpConnectionFactory8380Connections));
        sb.append(',');
        sb.append("orgEclipseJettyServerHttpConnectionFactory8381Connections");
        sb.append('=');
        sb.append(((this.orgEclipseJettyServerHttpConnectionFactory8381Connections == null)?"<null>":this.orgEclipseJettyServerHttpConnectionFactory8381Connections));
        sb.append(',');
        sb.append("servicesAppAppResourceClaims");
        sb.append('=');
        sb.append(((this.servicesAppAppResourceClaims == null)?"<null>":this.servicesAppAppResourceClaims));
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
        result = ((result* 31)+((this.ioDropwizardJettyMutableServletContextHandlerPostRequests == null)? 0 :this.ioDropwizardJettyMutableServletContextHandlerPostRequests.hashCode()));
        result = ((result* 31)+((this.orgEclipseJettyServerHttpConnectionFactory8381Connections == null)? 0 :this.orgEclipseJettyServerHttpConnectionFactory8381Connections.hashCode()));
        result = ((result* 31)+((this.orgEclipseJettyServerHttpConnectionFactory8380Connections == null)? 0 :this.orgEclipseJettyServerHttpConnectionFactory8380Connections.hashCode()));
        result = ((result* 31)+((this.ioDropwizardJettyMutableServletContextHandlerRequests == null)? 0 :this.ioDropwizardJettyMutableServletContextHandlerRequests.hashCode()));
        result = ((result* 31)+((this.ioDropwizardJettyMutableServletContextHandlerDispatches == null)? 0 :this.ioDropwizardJettyMutableServletContextHandlerDispatches.hashCode()));
        result = ((result* 31)+((this.servicesAppAppResourceClaims == null)? 0 :this.servicesAppAppResourceClaims.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Timers) == false) {
            return false;
        }
        Timers rhs = ((Timers) other);
        return ((((((((this.ioDropwizardJettyMutableServletContextHandlerPostRequests == rhs.ioDropwizardJettyMutableServletContextHandlerPostRequests)||((this.ioDropwizardJettyMutableServletContextHandlerPostRequests!= null)&&this.ioDropwizardJettyMutableServletContextHandlerPostRequests.equals(rhs.ioDropwizardJettyMutableServletContextHandlerPostRequests)))&&((this.orgEclipseJettyServerHttpConnectionFactory8381Connections == rhs.orgEclipseJettyServerHttpConnectionFactory8381Connections)||((this.orgEclipseJettyServerHttpConnectionFactory8381Connections!= null)&&this.orgEclipseJettyServerHttpConnectionFactory8381Connections.equals(rhs.orgEclipseJettyServerHttpConnectionFactory8381Connections))))&&((this.orgEclipseJettyServerHttpConnectionFactory8380Connections == rhs.orgEclipseJettyServerHttpConnectionFactory8380Connections)||((this.orgEclipseJettyServerHttpConnectionFactory8380Connections!= null)&&this.orgEclipseJettyServerHttpConnectionFactory8380Connections.equals(rhs.orgEclipseJettyServerHttpConnectionFactory8380Connections))))&&((this.ioDropwizardJettyMutableServletContextHandlerRequests == rhs.ioDropwizardJettyMutableServletContextHandlerRequests)||((this.ioDropwizardJettyMutableServletContextHandlerRequests!= null)&&this.ioDropwizardJettyMutableServletContextHandlerRequests.equals(rhs.ioDropwizardJettyMutableServletContextHandlerRequests))))&&((this.ioDropwizardJettyMutableServletContextHandlerDispatches == rhs.ioDropwizardJettyMutableServletContextHandlerDispatches)||((this.ioDropwizardJettyMutableServletContextHandlerDispatches!= null)&&this.ioDropwizardJettyMutableServletContextHandlerDispatches.equals(rhs.ioDropwizardJettyMutableServletContextHandlerDispatches))))&&((this.servicesAppAppResourceClaims == rhs.servicesAppAppResourceClaims)||((this.servicesAppAppResourceClaims!= null)&&this.servicesAppAppResourceClaims.equals(rhs.servicesAppAppResourceClaims))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))));
    }

}
