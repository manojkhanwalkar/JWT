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
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.core.Configuration;
import io.dropwizard.core.setup.Bootstrap;
import jakarta.validation.constraints.NotEmpty;

public class ServiceConfiguration extends Configuration {

    /**
     * Lets the YAML files reference environment variables, e.g. ${JWT_KEYSTORE_PASSWORD},
     * so secrets are not committed. Strict: a referenced variable that is not set stops startup.
     */
    public static void enableEnvironmentSubstitution(Bootstrap<?> bootstrap) {
        bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(
                bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor(true)));
    }

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
