package Model.User;

import Model.Lising.ListingFee;
import Database.Database;

public class Manager extends User{
    private Database db;

    public Manager(int userID, String name, String username, String password, String email, String type) {
        super(userID, name, username, password, email, type);

    }
}
