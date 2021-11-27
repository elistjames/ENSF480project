package User;
import Properties.Listing;
import SingletonDatabase.Database;

import java.util.ArrayList;



public class Renter extends User implements Observer{
    SearchCriteria sc;
    ArrayList<Listing> newListings;

    public Renter(){}


    public Renter(int userID, String name, String username, String password, String email) {
        super(userID, name, username, password, email);

    }

    public void makeAccount(String name, String username, String password, String email){
        setName(name);
        setUsername(username);
        setPassword(password);
        setEmail(email);
    }

    public SearchCriteria getSearchCriteria() {
        return this.sc;
    }

    public void createSearchCriteria(){
        sc = new SearchCriteria(this.getUserID());
    }

    @Override
    public void update(Listing new_listing) {
        if(new_listing.getProperty().getType().equals(sc.getType())||sc.getType().equals("any")){
            if(new_listing.getProperty().getBedrooms()==sc.getN_bedrooms()||sc.getN_bedrooms()==-1){
                if(new_listing.getProperty().getBathrooms()==sc.getN_bathrooms()||sc.getN_bathrooms()==-1){
                    if(new_listing.getProperty().isFurnished()==sc.isFurnished()||sc.isFurnished()==-1){
                        if(new_listing.getProperty().getCityQuadrant().equals(sc.getCityQuadrant())||sc.getCityQuadrant().equals("any")){
                            newListings.add(new_listing);
                        }
                    }
                }
            }
        }
    }
}
