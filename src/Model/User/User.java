/*
 * Author(s): Eli, Manjot
 * Documented by: Ryan Sommerville
 * Date created: Nov 27, 2021
 * Last Edited: Dec 6, 2021
 */

package Model.User;

/**
 * An abstract class that represents a User of the program
 * and holds their login and contact info.
 */
public abstract class User {

    private String name;
    private String username;
    private String password;
    private int userID;
    private String email;
    private String type;

    public User(int userID, String name, String username, String password, String email, String type) {
        this.username = username;
        this.password = password;
        this.userID = userID;
        this.email = email;
        this.name = name;
        this.type = type;
    }

    public User(){}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
