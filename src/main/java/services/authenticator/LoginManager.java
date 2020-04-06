package services.authenticator;

import data.*;

import java.util.UUID;

public class LoginManager {


    UserSessionCache cache = UserSessionCache.getInstance();

    UserManager manager = UserManager.getInstance();

    public LoginResponse login(LoginRequest request) {

        // validate userid and password against UserManager
        // if valid generate a session id , else send an error

        if (manager.validate(request.getUserid(), request.getPassword())) {

            String sessionId = UUID.randomUUID().toString();

            cache.put(request.getUserid(), sessionId);

            LoginResponse response = new LoginResponse(sessionId);


            return response;
        }
        else
        {
            LoginResponse response = new LoginResponse();
            response.setErrorMessage("User login failed");
            return response;
        }

    }





    public LogoutResponse logout(LogoutRequest request) {

        cache.remove(request.getUserid());
        return new LogoutResponse(LogoutResponse.Status.sucess);
    }


    public static void main(String[] args) throws Exception {
        LoginManager loginManager = new LoginManager();

        LoginRequest request = new LoginRequest("user1","password1");

        LoginResponse response = loginManager.login(request);

        System.out.println(response);

        if (response.getStatus()== LoginResponse.Status.sucess) {

            SessionVerifyRequest vrequest = new SessionVerifyRequest(response.getSessionId());

            SessionVerifyResponse vresponse = loginManager.verify(vrequest);

            System.out.println(vresponse);

            Thread.sleep(10000);

            vresponse = loginManager.verify(vrequest);

            System.out.println(vresponse);


        }
    }

    public SessionVerifyResponse verify(SessionVerifyRequest request) {

            boolean valid = cache.contains(request.getSessionId());
            SessionVerifyResponse response = new SessionVerifyResponse(valid? SessionVerifyResponse.Status.Valid: SessionVerifyResponse.Status.Invalid,request.getSessionId());
            return response;
    }
}
