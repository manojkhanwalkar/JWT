package services.authenticator;

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


    public boolean validate(String userId,String password)
    {
        //TODO - add Dynamodb table

        return true;
    }
}
