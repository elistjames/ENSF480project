package edu.ucalgary.ensf480;

import java.util.ArrayList;

public class Renter extends User implements Observer{

    public Renter(int userID, String name, String username, String password, String email) {
        super(userID, name, username, password, email);
    }



    @Override
    public void update(ArrayList<Listing> listings) {

    }
}
