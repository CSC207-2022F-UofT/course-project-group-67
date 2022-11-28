package RegisterUseCase;

import entities.UserManager;

import java.sql.Date;

public class RegisterInteractor {
    private final UserManager userManager;

    /**
     *
     */
    public RegisterInteractor() {
        userManager = UserManager.instance;
    }

    /**
     * @param username
     * @param password
     * @param passwordConfirm
     * @param loginDate
     * @return RegisterError type such that it can be a Username or Password error or no error
     */

    public RegisterError signUpUser(String username, String password, String passwordConfirm, Date loginDate) {

        // Refactored by adding methods to check for errors in order to make the code more readable
        if (usernameInvalid(username)) {
            return RegisterError.USERNAME;
        } else if (!passwordValid(password)) {
            return RegisterError.PASSWORD_INVALID;
        }else if (!passwordMatch(password, passwordConfirm)) {
            return RegisterError.PASSWORD_NOT_MATCH;
        }
        else {
            userManager.createUser(username, password, loginDate);
            return RegisterError.NONE;
        }
    }


    /**
     * @param username
     * @return True if the username is invalid
     * Refactored
     */
    private boolean usernameInvalid(String username) {
        // case 1: username is already taken, case 2: username is empty, case 3: username contains spaces
        // case 4: username is too long (SQL Error)
        return userManager.userExists(username) || username.equals("") || username.contains(" ") || username.length() > 50 || username.contains("\"");
    }

    /**
     * @param password
     * @return True if password length is in range [8, 50] otherwise false
     */
    private boolean passwordValid(String password) {
        int minLength = 8;
        int maxLength = 50;

        return password.length() >= minLength && password.length() <= maxLength;
    }

    /**
     * @param password
     * @param passwordConfirm
     * @return True if password and passwordConfirm match, else false
     */
    private boolean passwordMatch(String password, String passwordConfirm) {
        return password.equals(passwordConfirm);
    }
}