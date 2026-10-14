/*
 * UserManager
 *
 * Lazily created singleton in front of the DynamoDB user store, used to check login
 * credentials. Sharing one instance means one DynamoDB client is reused for every
 * request.
 */
package services.authenticator;

import dynamodb.UserCreator;

public class UserManager {

    private static final class Holder {
        static final UserManager MANAGER = new UserManager();
    }

    private final UserCreator creator = new UserCreator();

    private UserManager() {
    }

    /** Returns the shared instance. The DynamoDB client is created on the first call. */
    public static UserManager getInstance() {
        return Holder.MANAGER;
    }

    /** Returns true if a user with this id exists and the password matches. */
    public boolean validate(String userId, String password) {
        return creator.validate(userId, password);
    }
}
