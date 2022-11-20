package entities;

import db.UserDSRequest;
import db.UserDSResponse;
import db.iEntityDBGateway;

import java.sql.Date;

public class UserManager {
    private User user;
    private final iEntityDBGateway dbGateway;
    private final UserFactory userFactory = new UserFactory();

    public UserManager(iEntityDBGateway dbGateway) {
        this.dbGateway = dbGateway;
    }

    /**
     * @param username non-empty string
     * @param password non-empty string
     * @return the user from the system that has the given username password pair
     * or null if it doesn't exist
     */
    public User getUser(String username, String password, Date loginDate) {
        User user = convertUserDSResponse(dbGateway.findUserPortfolios(username, password));

        if (user != null) {
            this.user = user;
            user.updateLoginDate(loginDate);
        }

        return user;
    }

    /**
     * @param username non-empty string
     * @return true if a user in the system has a matching username, false otherwise
     */
    public boolean userExists(String username) {
        if (user != null && user.getUsername().equals(username)) {
            return true;
        }

        return dbGateway.findUser(username);
    }

    /**
     * <p>
     * Creates new user that doesn't exist in the system already
     * and adds it to the database
     * </p>
     *
     * @param username    unique username that does not exist in the system already
     * @param password    non-empty string satisfying valid password parameters
     * @param dateCreated String describing a date
     */
    public void createUser(String username, String password, Date dateCreated) {
        dbGateway.addUser(new UserDSRequest(username, password, dateCreated));
    }

    private User convertUserDSResponse(UserDSResponse userDSResponse) {
        if (userDSResponse == null) {
            return null;
        }

        return userFactory.createUser(userDSResponse.getUsername(), userDSResponse.getPassword(), userDSResponse.getLastLogin(), userDSResponse.getPortfolios(), dbGateway);
    }

    public User getUser() {
        return user;
    }
}