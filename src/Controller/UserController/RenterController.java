package Controller.UserController;

import Model.Lising.Listing;
import Model.User.Email;
import Model.User.Renter;
import Model.User.SearchCriteria;
import Model.User.User;
import Viewer.View.RenterView;

import java.util.ArrayList;

public class RenterController extends UserController {
    Renter current;
    RenterView rv;

    public RenterController(Renter currentUser, RenterView rv){
        super(currentUser);
        current = currentUser;
        this.rv = rv;
        rv.setLocationRelativeTo(null);
        rv.setVisible(true);

    }

    public void sendEmail(Listing l, String msg){
        db.getEmails().add(new Email(current.getEmail(), db.lookupLandlordEmail(l.getProperty().getLandlordID()),
                l.getProperty().getAddress(), msg));
    }

    public void setSearchCriteria(String type, int bedrooms, int bathrooms, int furnished, String cityQuadrant){
        current.getSc().setType(type);
        current.getSc().setN_bedrooms(bedrooms);
        current.getSc().setN_bathrooms(bathrooms);
        current.getSc().setFurnished(furnished);
        current.getSc().setCityQuadrant(cityQuadrant);
    }

    public void registerAccount(String name, String username, String password, String email){
        int next = db.getNextUserID();
        current.setUserID(next);
        current.setName(name);
        current.setUsername(username);
        current.setPassword(password);
        current.setEmail(email);
        db.getRenters().add(current);
        db.getUsers().add(current);
        db.getSearches().add(current.getSc());
    }

    public void unregisterAccount(){
        for(Renter r : db.getRenters()){
            if(r.getUserID() == currentUser.getUserID()){
                db.getRenters().remove(r);
                break;
            }
        }
        for(User u : db.getUsers()){
            if(u.getUserID() == current.getUserID()){
                db.getUsers().remove(u);
                break;
            }
        }
        for(SearchCriteria sc : db.getSearches()){
            if(sc.getRenterID() == current.getUserID()){
                db.getSearches().remove(sc);
                break;
            }
        }
    }

    public void SearchListings(){
        ArrayList<String> strList = new ArrayList<String>();
        rv.getTypeOption().getSelectedItem().toString();
        for(Listing l : db.getListings()){

        }
    }

    public void updateSearchCriteria(String type, String nbed, String nbath, String furnished, String cq){
        if(type.equals("N/A")){

        }
    }
}
