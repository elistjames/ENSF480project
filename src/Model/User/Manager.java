/**
 * Author(s):
 * Editted by:
 * Documented by: Ryan Sommerville
 * Date created:
 * Last Editted:
 */

package Model.User;

import Model.Lising.ListingFee;
import Database.Database;

/**
 * A subclass of User that represents a Manager who
 * is using the program.
 */
public class Manager extends User{
    private Database db;

    public Manager(int userID, String name, String username, String password, String email, String type) {
        super(userID, name, username, password, email, type);

    }
}
