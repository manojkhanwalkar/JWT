package services.authenticator;

import dynamodb.UserCreator;

public class UserManager {

    static class Holder {

        static UserManager  manager = new UserManager();
    }

    private UserManager()
    {

    }

    public static UserManager getInstance() {

        return Holder.manager;
    }

    UserCreator creator = new UserCreator();


    public boolean validate(String userId,String password)
    {
        return creator.validate(userId,password);


    }
}
