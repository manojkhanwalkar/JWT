package data;

public class FileMetaResponse {

    int fileSize;

    String checkSum;

    public FileMetaResponse(int fileSize, String checkSum) {
        this.fileSize = fileSize;
        this.checkSum = checkSum;
    }

    public FileMetaResponse() {
    }


    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    @Override
    public String toString() {
        return "FileMetaResponse{" +
                "fileSize=" + fileSize +
                ", checkSum='" + checkSum + '\'' +
                '}';
    }
}
