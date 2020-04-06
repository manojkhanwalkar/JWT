package data;

public class AppRequest {

    String token;

    String dummy;

    public AppRequest(String token, String dummy) {
        this.token = token;
        this.dummy = dummy;
    }

    public AppRequest() {
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }
}
