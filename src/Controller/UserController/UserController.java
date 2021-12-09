/*
 * Author(s): Eli, Luke, Manjot
 * Documented by: Ryan Sommerville
 * Date created: Dec 1, 2021
 * Last Edited: Dec 6, 2021
 */


package Controller.UserController;

import Database.Database;
import Model.User.User;

/**
 * An abstract class that acts as an interface
 * for various classes that extend UserController.
 * Each class contains a User object of the same type
 * as the class.
 * 			eg. A LandlordController contains a Landlord class.
 * 
 * These classes act as a go between between it's contained user
 * and the Database.
 */
public abstract class UserController {
    public Database db; //Database object that contains User data.

    public User currentUser; //Current User logged in to Program
    
    /**
     * A constructor that takes a User object and initializes the currentUser
     * member variable with it. The db object takes the instance of the Database
     * class.
     * @param currentUser An object referencing the User that is logged in to the program.
     */
    public UserController(User currentUser){
        this.currentUser = currentUser;
        db = Database.getOnlyInstance();
    }
}
