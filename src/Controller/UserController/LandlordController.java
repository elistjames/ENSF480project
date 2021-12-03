package Controller.UserController;

import Model.Lising.Listing;
import Model.Lising.Property;
import Model.User.Landlord;
import Model.User.User;

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
        current.getMyProperties().add(db.getProperties().get(db.getProperties().size()-1));
    }

    public void cancelListing(Listing l){
        for(Listing cl : db.getListings()){
            if(cl.getProperty().getID() == l.getProperty().getID()){
                db.getListings().remove(cl);
            }
        }
    }

    public void sendEmail(int toId, String msg){
        //db.getEmails().add(new Email(current.getUserID, toID, msg);
    }

}
