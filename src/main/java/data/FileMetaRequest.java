/*
 * FileMetaRequest
 *
 * Body of POST /meta: asks for the size and checksum of a downloadable file.
 */
package data;

public class FileMetaRequest {

    private String fileName;

    private String jwt;

    /** Creates a metadata request for {@code fileName}. */
    public FileMetaRequest(String fileName, String jwt) {
        this.fileName = fileName;
        this.jwt = jwt;
    }

    /** No-arg constructor for Jackson. */
    public FileMetaRequest() {
    }

    /** File name, relative to the download directory. */
    public String getFileName() {
        return fileName;
    }

    /** Sets the file name. */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /** JWT authorizing the request. Not checked yet. */
    public String getJwt() {
        return jwt;
    }

    /** Sets the JWT. */
    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public String toString() {
        return "FileMetaRequest{fileName='" + fileName + "'}";
    }
}
