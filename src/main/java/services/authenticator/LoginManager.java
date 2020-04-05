package services.authenticator;

import data.LoginRequest;
import data.LoginResponse;
import data.LogoutRequest;
import data.LogoutResponse;

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
}
