/*
 * AppConfiguration
 *
 * Dropwizard configuration for the app (resource) service, read from App.yml.
 * Besides the shared settings it adds "filesDirectory", the directory that the
 * /meta and /chunk download endpoints serve files from.
 */
package services.app;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import services.ServiceConfiguration;

public class AppConfiguration extends ServiceConfiguration {

    static final String DEFAULT_FILES_DIRECTORY = "/home/manoj/IdeaProjects/JWT/src/main/resources/files/";

    @NotEmpty
    private String filesDirectory = DEFAULT_FILES_DIRECTORY;

    /** Directory that holds the files offered for download. */
    @JsonProperty
    public String getFilesDirectory() {
        return filesDirectory;
    }

    /** Sets the download directory. */
    @JsonProperty
    public void setFilesDirectory(String filesDirectory) {
        this.filesDirectory = filesDirectory;
    }
}
