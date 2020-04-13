package data;

public class ChunkRequest {


    String fileName;

    String jwt;

    int start;

    int end;

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

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }


    public ChunkRequest(String fileName, String jwt, int start, int end) {
        this.fileName = fileName;
        this.jwt = jwt;
        this.start = start;
        this.end = end;
    }


    public ChunkRequest() {
    }
}
