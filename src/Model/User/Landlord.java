package Model.User;

import Model.Lising.Listing;
import Model.Lising.Property;

import java.util.ArrayList;

public class Landlord extends User {

    public Landlord(int userID, String name, String username, String password, String email, String type) {
        super(userID, name, username, password, email, type);
    }
    public Landlord(){}
}

