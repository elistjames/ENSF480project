/*
 * Author(s): Eli, Manjot
 * Documented by: Ryan Sommerville
 * Date created: Nov 28, 2021
 * Last Edited: Dec 6, 2021
 */

package Model.User;

/**
 * A subclass of User that represents a Manager who
 * is using the program.
 */
public class Manager extends User{

    public Manager(int userID, String name, String username, String password, String email, String type) {
        super(userID, name, username, password, email, type);

    }
}
