package Controller.UserController;

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
                address, cityQuadrant));
        current.getMyProperties().add(db.getProperties().get(db.getProperties().size()-1));
    }
}
