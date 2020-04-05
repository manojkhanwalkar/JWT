package data;

public class LoginRequest {


    public LoginRequest()
    {

    }

    public LoginRequest(String userid, String password) {
        this.userid = userid;
        this.password = password;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "userid='" + userid + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    String userid;

    String password;


}
