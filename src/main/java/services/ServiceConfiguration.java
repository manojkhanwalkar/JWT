/*
 * ServiceConfiguration
 *
 * Base Dropwizard configuration that all four services share (app, authenticator,
 * authorizer, metrics). It maps the "template" and "defaultName" keys in each
 * service's YAML file (App.yml, Auth.yml, Authenticator.yml). Server ports and TLS
 * keystores live in the inherited "server" section.
 */
package services;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;
import jakarta.validation.constraints.NotEmpty;

public class ServiceConfiguration extends Configuration {

    @NotEmpty
    private String template;

    @NotEmpty
    private String defaultName = "Stranger";

    /** Greeting template from the YAML file, for example "Hello, %s!". */
    @JsonProperty
    public String getTemplate() {
        return template;
    }

    /** Sets the greeting template. */
    @JsonProperty
    public void setTemplate(String template) {
        this.template = template;
    }

    /** Fallback name used with the template. */
    @JsonProperty
    public String getDefaultName() {
        return defaultName;
    }

    /** Sets the fallback name. */
    @JsonProperty
    public void setDefaultName(String name) {
        this.defaultName = name;
    }
}
