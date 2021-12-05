package Controller.UserController;

import Model.Lising.Listing;
import Model.User.Email;
import Model.User.Renter;
import Model.User.SearchCriteria;
import Model.User.User;
import Viewer.View.RenterView;

import javax.swing.*;
import java.util.ArrayList;

public class RenterController extends UserController {
    public Renter current;
    RenterView rv;

    public RenterController(Renter currentUser, RenterView renterV){
        super(currentUser);
        current = currentUser;
        this.rv = renterV;
        this.rv.setRc(this);
        rv.initComponents();
        rv.setLocationRelativeTo(null);
        rv.updateCriteriaBoxes(db.getCurrentSearch(current).getType(), db.getCurrentSearch(current).getN_bedrooms(),
                db.getCurrentSearch(current).getN_bathrooms(), db.getCurrentSearch(current).isFurnished(),
                db.getCurrentSearch(current).getCityQuadrant());
        rv.setVisible(true);
        for(Listing l : db.getListings()){
            System.out.println(l.getProperty().getAddress());
        }
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

    public void searchListings(){
        rv.setVisible(false);
        rv = new RenterView();
        this.rv.setRc(this);
        rv.initComponents();
        rv.setLocationRelativeTo(null);
        rv.updateCriteriaBoxes(db.getCurrentSearch(current).getType(), db.getCurrentSearch(current).getN_bedrooms(),
                db.getCurrentSearch(current).getN_bathrooms(), db.getCurrentSearch(current).isFurnished(),
                db.getCurrentSearch(current).getCityQuadrant());
        rv.setVisible(true);
        /*
        ArrayList<String> strList = new ArrayList<String>();
        String tmpType = String.valueOf(rv.getTypeOption().getSelectedItem());
        String tmpNbed = String.valueOf(rv.getnBedOption().getSelectedItem());
        String tmpNbath = String.valueOf(rv.getnBathOption().getSelectedItem());
        String tmpFurnished = String.valueOf(rv.getFurnishedOption().getSelectedItem());
        String tmpCQ = String.valueOf(rv.getQuadrantOption().getSelectedItem());
        updateSearchCriteria(tmpType, tmpNbed, tmpNbath, tmpFurnished, tmpCQ);
        int i = 0;
        for(Listing l : db.getListings()){
            if(l.getProperty().getType().equals(db.getCurrentSearch(current).getType())){
                if(l.getProperty().getBedrooms() == db.getCurrentSearch(current).getN_bedrooms()){
                    if(l.getProperty().getBathrooms() == db.getCurrentSearch(current).getN_bathrooms()){
                        if(l.getProperty().isFurnished() == db.getCurrentSearch(current).isFurnished()){
                            if(l.getProperty().getCityQuadrant().equals(db.getCurrentSearch(current).getCityQuadrant())){
                                String tmp = String.format("Address: %1$-40s Posted by: %2$-20s Posting expires in %3$3d days",
                                        l.getProperty().getAddress(), db.lookupLandlord(l.getProperty().getLandlordID()),
                                        l.getDuration()-l.getCurrentDay());
                                rv.getjList1().getModel().
                            }
                            else rv.getStrings()[i] = "";
                        }
                        else rv.getStrings()[i] = "";
                    }
                    else rv.getStrings()[i] = "";
                }
                else rv.getStrings()[i] = "";
            }
            else rv.getStrings()[i] = "";
            i++;
        }

        */
    }

    public void updateSearchCriteria(String type, String nbed, String nbath, String furnished, String cq){
        db.getCurrentSearch(current).setType(type);

        if(nbed.equals("N/A")){
            db.getCurrentSearch(current).setN_bedrooms(-1);
        }
        else{
            db.getCurrentSearch(current).setN_bedrooms(Integer.parseInt(nbed));
        }
        if(nbath.equals("N/A")){
            db.getCurrentSearch(current).setN_bathrooms(-1);
        }
        else{
            db.getCurrentSearch(current).setN_bathrooms(Integer.parseInt(nbath));
        }
        if(furnished.equals("N/A")){
            db.getCurrentSearch(current).setFurnished(-1);
        }
        else if(furnished.equals("Yes")){
            db.getCurrentSearch(current).setFurnished(1);
        }
        else{
            db.getCurrentSearch(current).setFurnished(0);
        }

        db.getCurrentSearch(current).setCityQuadrant(cq);
    }
}
