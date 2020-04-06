package data;

public class AppResponse {

    public enum Status { Valid , Invalid};

    Status status;

    String dumyecho;


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDumyecho() {
        return dumyecho;
    }

    public void setDumyecho(String dumyecho) {
        this.dumyecho = dumyecho;
    }

    public AppResponse() {
    }

    public AppResponse(Status status, String dumyecho) {
        this.status = status;
        this.dumyecho = dumyecho;
    }
}
