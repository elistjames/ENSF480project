package edu.ucalgary.ensf480;

public class Manager extends User{
    Database db;

    public Manager(int userID, String name, String username, String password, String email) {
        super(userID, name, username, password, email);

        db = Database.getOnlyInstance();
    }


}
