package Controller.UserController;

import Model.Lising.*;
import Model.User.Email;
import Model.User.Landlord;
import Model.User.User;

import java.sql.Date;
import java.time.LocalDate;

public class LandlordController extends UserController {
    Landlord current;

    public LandlordController(Landlord currentUser) {
        super(currentUser);
        current = currentUser;

    }

    public void registerProperty(String type, int bedrooms, int bathrooms, int furnished, String address,
                                 String cityQuadrant){
        int next = db.getNextPropertyID();
        db.getProperties().add(new Property(current.getUserID(), next, type, bedrooms, bathrooms, furnished,
                address, cityQuadrant, "unlisted"));
    }

    public void postProperty(Property p, int days){
        db.getListings().add(new Listing(p, days, "listed", LocalDate.now(), 0));
        p.setState("listed");
        db.updateRentersToNotify(p);
    }

    public void cancelListing(Listing l){
        for(Listing cl : db.getListings()){
            if(cl.getProperty().getID() == l.getProperty().getID()){
                db.getListings().remove(cl);
            }
        }
    }


    public void replyEmail(Email recived, String msg){
        db.getEmails().add(new Email(current.getEmail(), recived.getFromEmail(), recived.getSubject(), msg));
    }

    public void changeListingState(Listing listing, String state){
        if(listing.getState().equals("suspended")){
            if(state.equals("cancelled")){
                unsuspendListing(listing);
                cancelListing(listing);
            }
            else if(state.equals("listed")){
                unsuspendListing(listing);
            }
        }
        else if(listing.getState().equals("listed")){
            if(state.equals("suspended")){
                suspendListing(listing);
            }
            else if(state.equals("rented")){
                rentOutListing(listing);
            }
        }
    }

    public void unsuspendListing(Listing listing){
        for(Listing l : db.getSuspendedListings()){
            if(l.getProperty().getID() == listing.getProperty().getID()){
                l.getProperty().setState("listed");
                db.getListings().add(l);
                db.getSuspendedListings().remove(l);
            }
        }
    }

    public void suspendListing(Listing listing){
        for(Listing l : db.getListings()){
            if(l.getProperty().getID() == listing.getProperty().getID()){
                l.getProperty().setState("suspended");
                db.getSuspendedListings().add(l);
                db.getListings().remove(l);
            }
        }
    }

    public void rentOutListing(Listing listing){
        for(Listing l : db.getListings()){
            if(l.getProperty().getID() == listing.getProperty().getID()){
                l.getProperty().setState("rented");
                db.getRentedProperties().add(l.getProperty());
                db.getRentedDates().add(Date.valueOf(LocalDate.now()));
                db.getListings().remove(l);
            }
        }
    }
}
