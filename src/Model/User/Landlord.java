package Model.User;

import Model.Lising.Listing;
import Model.Lising.Property;

import java.util.ArrayList;

public class Landlord extends User {
    private ArrayList<Property> myProperties;
    private ArrayList<Listing> myListings;

    public Landlord(int userID, String name, String username, String password, String email, String type) {
        super(userID, name, username, password, email, type);
        myProperties = new ArrayList<Property>();
        myListings = new ArrayList<Listing>();
    }

    public void registerProperty(String type, int bedrooms, int bathrooms, int furnished, String address,
                                 String cityQuadrant) {

        myProperties.add(new Property(getUserID(),type,bedrooms,bathrooms,furnished,address,cityQuadrant));
    }

    public Property getLatestProperty(){
        return myProperties.get(myProperties.size()-1);
    }

    public void postProperty(int duration){

    }

    public ArrayList<Property> getMyProperties() {
        return myProperties;
    }

    public ArrayList<Listing> getMyListings() {
        return myListings;
    }
}

