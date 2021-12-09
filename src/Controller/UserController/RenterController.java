/** 
 * Author(s):
 * Editted by:
 * Documented by: Ryan Sommerville
 * Date created:
 * Last Editted:
 */

package Controller.UserController;

import Model.Lising.Listing;
import Model.Lising.Property;
import Model.User.Email;
import Model.User.Renter;
import Model.User.SearchCriteria;
import Model.User.User;
import Viewer.View.EmailDialog;
import Viewer.View.EmailPage;
import Viewer.View.RegisterPage;
import Viewer.View.RenterView;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * A class that extends the UserController class where the currentUser
 * object contains a Renter instance. Contains various
 * functions that implement the actions a Renter can take.
 */
public class RenterController extends UserController {
	//--------------------------------------------------------------------------
	// Member variables
	//--------------------------------------------------------------------------
    /**
     * Current Renter that is logged in.
     */
	public Renter current;
	
	/**
	 * Main GUI interface for the logged in Renter.
	 */
    public RenterView rv;
    
    /**
     * Page that an unregistered Renter can use to make an account.
     */
    public RegisterPage rp;
    
    /**
     * The GUI interface for when the renter is sending an email.
     */
    public EmailPage ep;
    
    /**
     * Email object that the renter can send.
     */
    public Email email;

    /**
     * Email that the renter has received.
     */
    public Email recieved;
    
    /**
     * GUI interface that shows an email the renter has received.
     */
    EmailDialog ed;


    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------
    /**
     * A constructor that takes a Renter and RenterView as inputs.
     * @param {Renter} currentUser The User currently signed in.
     * @param {RenterView} renterV The GUI interface for the renter
     */
    public RenterController(Renter currentUser, RenterView renterV){
        super(currentUser);
        current = currentUser;
        this.rv = renterV;
        this.rv.setRc(this);
        //Update RenterView object with currentUser data
        rv.initComponents();
        rv.setLocationRelativeTo(null);
        rv.updateCriteriaBoxes(db.getCurrentSearch(current.getUserID()).getType(), db.getCurrentSearch(current.getUserID()).getN_bedrooms(),
                db.getCurrentSearch(current.getUserID()).getN_bathrooms(), db.getCurrentSearch(current.getUserID()).isFurnished(),
                db.getCurrentSearch(current.getUserID()).getCityQuadrant());
        rv.setVisible(true);
        if(db.notifyRenter(current.getUserID())){
            JOptionPane.showMessageDialog(null, "There are new listings posted that match your criteria!");
        }
        if(db.emailNotSeen(current.getEmail())){
            checkEmails();
        }
    }

    //-----------------------------------------------------------------
    // Email functions
    //-----------------------------------------------------------------
    /**
     * Goes through the Emails in the database and finds the first email
     * directed to the current User.
     */
    public void checkEmails(){
        for(Email e : db.getEmails()){
            if(e.getToEmail().equals(current.getEmail())){
                JOptionPane.showMessageDialog(null, "You have received a message from a landlord.\n" +
                        "Click Ok to view it.");
                recieved = e;
                db.getEmails().remove(e);
                ed = new EmailDialog(rv, true);
                ed.setRc(this);
                ed.initComponents(e);
                ed.setLocationRelativeTo(null);
                ed.setVisible(true);

                break;
            }
        }
    }
    
    /**
     * Adds an email sent by the renter to the database.
     * @param {Email} email The Email object to be sent.
     */
    public void sendEmail(Email email){
        db.getEmails().add(new Email(email.getFromEmail(), email.getToEmail(), email.getDate(), email.getSubject(), email.getMessage()));
    }

    
    //-----------------------------------------------------------------------------
    // Search Listings methods
    //-----------------------------------------------------------------------------
    /**
     * Modifies the Renter's Search Criteria. Does not modify the database.
     * @param {String} type Type of property to be searched.
     * @param {int} bedrooms Number of bedrooms for properties to be searched.
     * @param {int} bathrooms Number of bathrooms in properties to be searched.
     * @param {int} furnished Signifies whether property has been furnished.
     * @param {String} cityQuadrant City Quadrant of properties to be searched.
     */
    public void setSearchCriteria(String type, int bedrooms, int bathrooms, int furnished, String cityQuadrant){
        current.getSc().setType(type);
        current.getSc().setN_bedrooms(bedrooms);
        current.getSc().setN_bathrooms(bathrooms);
        current.getSc().setFurnished(furnished);
        current.getSc().setCityQuadrant(cityQuadrant);
    }
    
    /**
     * Modifies the Search Criteria saved in the database.
     */
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

    /**
     * Searches Listings for ones that match the renters search Criteria.
     */
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

    //--------------------------------------------------------------------
    // Registration/Deregistration methods
    //--------------------------------------------------------------------
    /**
     * Registers User as a Registered Renter.
     * @param {String} name Name of registering renter
     * @param {String} username Username of registering renter
     * @param {String} password Password of registering renter
     * @param {String} email Email of registering renter
     */
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

    /**
     * Unregisters renter, deleting their data from the database
     * and resetting the current user to a default unregistered renter.
     */
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


    //-----------------------------------------------------------------------
    // ActionPerformed methods
    //-----------------------------------------------------------------------
    /**
     * Runs when Account Button has been pressed. Displays Register page and 
     * gives option to delete account.
     * @param {ActionEvent} evt Event that occurred.
     */
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
    
    /**
     * Runs when Back Button has been pressed. Causes the RegisterPage to disappear
     * and the RenterView to appear.
     * @param {ActionEvent} evt Event that occurred.
     */
    public void backButtonActionPerformed(java.awt.event.ActionEvent evt) {
        rp.setVisible(false);
        rv.setVisible(true);
    }

    /**
     * Runs when RegisterButton has been pressed. Checks user inputs for name, username,
     * password, and email and checks to make sure that they are valid. If they are, registers
     * the account. If not, displays an error message to the screen.
     * @param {ActionEvent} evt Event that occurred.
     */
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

    //-------------------------------
    // Email Related
    //-------------------------------
    /**
     * Runs when Contact Button has been pressed. Displays Email Page and finds 
     * the selected properties' Landlord's email.
     * @param {ActionEvent} evt Event that occurred.
     */
    public void contactButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if(current.getUserID() == 0){
            JOptionPane.showMessageDialog(null, "make an account to be able to use the messaging system");
        }
        else{
            if(rv.getjList1().getSelectedValue() == null){
                JOptionPane.showMessageDialog(null, "You must select a property first\n" +
                        "to specify which landlord you\nyou would like to contact.");
            }
            else{
                ep = new EmailPage();
                ep.setRc(this);
                ep.initComponents("send");
                ep.setLocationRelativeTo(null);
                String selected = rv.getjList1().getSelectedValue();
                selected = selected.substring(9, selected.length()-1);
                selected = selected.substring(0, 25);
                selected = selected.trim();
                System.out.println(selected);
                for(Property p : db.getProperties()){
                    if(p.getAddress().equals(selected)){
                        email = new Email(current.getEmail(), db.lookupLandlordEmail(p.getLandlordID()));
                        email.setSubject(p.getAddress());
                    }
                }
                rv.setVisible(false);
                ep.setVisible(true);
            }
        }

    }
    
    /**
     * Runs when Send Email Button has been pressed. Checks that message is valid. If it is,
     * it constructs and sends the email. Then closes down the EmailPage and opens the
     * RenterView.
     * @param {ActionEvent} evt Event that occurred.
     */
    public void sendEmailButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String msg = ep.getEmailTextArea().getText();
        String sub = ep.getSubjectText().getText();
        if(msg.length() >= 1000){
            JOptionPane.showMessageDialog(null, "Message is too long.\n Must be less than 1000 characters");
        }
        else{
            String fullSubject = "";
            if(recieved==null){
                fullSubject = email.getSubject() + "   ||   "+sub;
            }
            else{
                fullSubject = sub;
            }

            email.setSubject(fullSubject);
            email.setMessage(msg);
            email.setDate(LocalDate.now());
            sendEmail(email);
            ep.setVisible(false);
            rv.setVisible(true);
            JOptionPane.showMessageDialog(null, "Email sent");
            if(db.emailNotSeen(current.getEmail())){
                checkEmails();
            }
        }
    }

    /**
     * Runs when Cancel Email Button has been pressed. Closes the EmailPage 
     * and opens the RenterView.
     * @param {ActionEvent} evt Event that occurred.
     */
    public void cancelEmailButtonActionPerformed(java.awt.event.ActionEvent evt) {
        ep.setVisible(false);
        rv.setVisible(true);
        if(db.emailNotSeen(current.getEmail())){
            checkEmails();
        }
    }
}
