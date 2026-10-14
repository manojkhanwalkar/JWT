/*
 * LoginRequest
 *
 * Body of POST /login on the authenticator: a userid and password.
 */
package data;

public class LoginRequest {

    private String userid;

    private String password;

    /** No-arg constructor for Jackson. */
    public LoginRequest() {
    }

    /** Creates a login request. */
    public LoginRequest(String userid, String password) {
        this.userid = userid;
        this.password = password;
    }

    /** Returns the user id. */
    public String getUserid() {
        return userid;
    }

    /** Sets the user id. */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    /** Returns the plaintext password. */
    public String getPassword() {
        return password;
    }

    /** Sets the password. */
    public void setPassword(String password) {
        this.password = password;
    }

    /** Password is masked so it never ends up in logs. */
    @Override
    public String toString() {
        return "LoginRequest{userid='" + userid + "', password='****'}";
    }
}
