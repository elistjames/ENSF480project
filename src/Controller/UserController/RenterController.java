package Controller.UserController;

import Model.Lising.Listing;
import Model.Lising.Property;
import Model.User.Email;
import Model.User.Renter;
import Model.User.SearchCriteria;
import Model.User.User;
import Viewer.View.EmailPage;
import Viewer.View.RegisterPage;
import Viewer.View.RenterView;

import javax.swing.*;
import java.util.ArrayList;

public class RenterController extends UserController {
    public Renter current;
    RenterView rv;
    RegisterPage rp;
    EmailPage ep;
    Email email;

    public RenterController(Renter currentUser, RenterView renterV){
        super(currentUser);
        current = currentUser;
        this.rv = renterV;
        this.rv.setRc(this);
        rv.initComponents();
        rv.setLocationRelativeTo(null);
        rv.updateCriteriaBoxes(db.getCurrentSearch(current.getUserID()).getType(), db.getCurrentSearch(current.getUserID()).getN_bedrooms(),
                db.getCurrentSearch(current.getUserID()).getN_bathrooms(), db.getCurrentSearch(current.getUserID()).isFurnished(),
                db.getCurrentSearch(current.getUserID()).getCityQuadrant());
        rv.setVisible(true);
        for(Listing l : db.getListings()){
            System.out.println(l.getProperty().getAddress());
        }
    }

    public void sendEmail(Email email){
        db.getEmails().add(email);
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
        db.getSearches().add(new SearchCriteria(current.getUserID(), db.getCurrentSearch(0).getType(),
                db.getCurrentSearch(0).getN_bedrooms(), db.getCurrentSearch(0).getN_bathrooms(),
                db.getCurrentSearch(0).isFurnished(), db.getCurrentSearch(0).getCityQuadrant()));
    }

    public void unregisterAccount(){
        for(Renter r : db.getRenters()){
            if(r.getUserID() == current.getUserID()){
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
        current = new Renter(0, "none", "none", "none", "none", "renter");
    }

    public void searchListings(){
        rv.setVisible(false);
        rv = new RenterView();
        this.rv.setRc(this);
        rv.initComponents();
        rv.setLocationRelativeTo(null);
        rv.updateCriteriaBoxes(db.getCurrentSearch(current.getUserID()).getType(), db.getCurrentSearch(current.getUserID()).getN_bedrooms(),
                db.getCurrentSearch(current.getUserID()).getN_bathrooms(), db.getCurrentSearch(current.getUserID()).isFurnished(),
                db.getCurrentSearch(current.getUserID()).getCityQuadrant());
        rv.setVisible(true);
    }

    public void updateSearchCriteria(String type, String nbed, String nbath, String furnished, String cq){
        db.getCurrentSearch(current.getUserID()).setType(type);

        if(nbed.equals("N/A")){
            db.getCurrentSearch(current.getUserID()).setN_bedrooms(-1);
        }
        else{
            db.getCurrentSearch(current.getUserID()).setN_bedrooms(Integer.parseInt(nbed));
        }
        if(nbath.equals("N/A")){
            db.getCurrentSearch(current.getUserID()).setN_bathrooms(-1);
        }
        else{
            db.getCurrentSearch(current.getUserID()).setN_bathrooms(Integer.parseInt(nbath));
        }
        if(furnished.equals("N/A")){
            db.getCurrentSearch(current.getUserID()).setFurnished(-1);
        }
        else if(furnished.equals("Yes")){
            db.getCurrentSearch(current.getUserID()).setFurnished(1);
        }
        else{
            db.getCurrentSearch(current.getUserID()).setFurnished(0);
        }

        db.getCurrentSearch(current.getUserID()).setCityQuadrant(cq);
    }

    public void accountButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if(current.getUserID() == 0){
            rp = new RegisterPage();
            rp.setRc(this);
            rp.initComponents();
            rp.setLocationRelativeTo(null);
            rv.setVisible(false);
            rp.setVisible(true);
        }
        else{
            int choice = JOptionPane.showConfirmDialog(null, "You already have an existing account. \n" +
                            "If you choose Yes, your account will be deleted.", "Are you sure?", JOptionPane.YES_NO_OPTION);
            if(choice == JOptionPane.YES_OPTION){
                unregisterAccount();
                JOptionPane.showMessageDialog(null, "Account deleted.\nYou may register again anytime :)");

            }
        }
    }
    public void backButtonActionPerformed(java.awt.event.ActionEvent evt) {
        rp.setVisible(false);
        rv.setVisible(true);
    }

    public void registerButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String userIn = rp.getUsernameText().getText();
        String passIn = rp.getPasswordText().getText();
        String nameIn = rp.getFirstNameText().getText();
        String emailIn = rp.getEmailText().getText();
        if(userIn.length() != 0&&passIn.length()!=0&&nameIn.length()!=0&&emailIn.length()!=0){
            if(db.validateUsername(userIn)){
                if(db.validatePassword(passIn)){
                    if(db.validateEmail(emailIn)){
                        registerAccount(nameIn, userIn, passIn, emailIn);
                        rp.setVisible(false);
                        rv.setVisible(true);
                        JOptionPane.showMessageDialog(null, "Your account was made Successfully.\n" +
                                "You can now save your preferences and will be notified when a new property gets posted " +
                                "that matches those preferences");
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "this email is already in use :(");
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null, "this password is already in use :(");
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "this username is already in use :(");
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "None of the text fields can be left blank");
        }
    }

    public void contactButtonActionPerformed(java.awt.event.ActionEvent evt) {
        ep = new EmailPage();
        ep.setRc(this);
        ep.initComponents();
        ep.setLocationRelativeTo(null);
        String selected = rv.getjList1().getSelectedValue();
        selected = selected.substring(9, selected.length()-1);
        selected = selected.substring(0, 25);
        selected = selected.trim();
        System.out.println(selected);
        for(Property p : db.getProperties()){
            if(p.getAddress().equals(selected)){
                email = new Email(current.getEmail(), db.lookupLandlordEmail(p.getLandlordID()));
            }
        }
        rv.setVisible(false);
        ep.setVisible(true);

    }
    public void sendEmailButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String msg = ep.getEmailTextArea().getText();
        String sub = ep.getSubjectText().getText();
        if(msg.length() >= 1000){
            JOptionPane.showMessageDialog(null, "Message is too long.\n Must be less than 1000 characters");

        }
        else{

        }
    }

    public void cancelEmailButtonActionPerformed(java.awt.event.ActionEvent evt) {
        ep.setVisible(false);
        rv.setVisible(true);
    }

}
