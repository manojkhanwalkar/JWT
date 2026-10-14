/*
 * FileMetaResponse
 *
 * Reply from POST /meta: the file's size in bytes and its MD5 checksum (hex).
 */
package data;

public class FileMetaResponse {

    private int fileSize;

    private String checkSum;

    /** Creates a populated response. */
    public FileMetaResponse(int fileSize, String checkSum) {
        this.fileSize = fileSize;
        this.checkSum = checkSum;
    }

    /** No-arg constructor for Jackson. */
    public FileMetaResponse() {
    }

    /** File size in bytes. */
    public int getFileSize() {
        return fileSize;
    }

    /** Sets the file size. */
    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    /** Hex MD5 of the file contents. */
    public String getCheckSum() {
        return checkSum;
    }

    /** Sets the checksum. */
    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    @Override
    public String toString() {
        return "FileMetaResponse{fileSize=" + fileSize + ", checkSum='" + checkSum + "'}";
    }
}
