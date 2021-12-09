/**
 * Author(s):
 * Editted by:
 * Documented by: Ryan Sommerville
 * Date created:
 * Last Editted:
 */

package Model.User;

import Model.Lising.Listing;
import Model.Lising.Property;

import java.util.ArrayList;

/**
 * A subclass of User that represents a Landlord who
 * is using the program.
 */
public class Landlord extends User {

    public Landlord(int userID, String name, String username, String password, String email, String type) {
        super(userID, name, username, password, email, type);
    }
    public Landlord(){}
}

