package User;

import Properties.Property;

import java.util.ArrayList;

public class Landlord extends User {
    private ArrayList<Property> properties = new ArrayList<Property>();

    public Landlord(int userID, String name, String username, String password, String email) {
        super(userID, name, username, password, email);
    }

    public void registerProperty(int ID, String type, int bedrooms, int bathrooms, boolean furnished, String address,
                                 String cityQuadrant) {

        properties.add(new Property(this, ID, type, bedrooms, bathrooms, furnished, address, cityQuadrant));
    }

    public void postProperty(int duration){

    }
}

