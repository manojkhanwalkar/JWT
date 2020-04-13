package data;

public class FileMetaRequest {


    String fileName;

    String jwt;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public FileMetaRequest(String fileName, String jwt) {
        this.fileName = fileName;
        this.jwt = jwt;
    }

    public FileMetaRequest() {
    }


    @Override
    public String toString() {
        return "FileMetaRequest{" +
                "fileName='" + fileName + '\'' +
                ", jwt='" + jwt + '\'' +
                '}';
    }
}
