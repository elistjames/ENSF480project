package Controller.UserController;

import Database.Database;
import Model.User.User;

public abstract class UserController {
    public Database db;

    public User currentUser;

    public UserController(User currentUser){
        this.currentUser = currentUser;
        db = Database.getOnlyInstance();
    }



}
