/**
 * Author(s):
 * Editted by:
 * Documented by: Ryan Sommerville
 * Date created:
 * Last Editted:
 */

package Controller.UserController;

import Controller.CoreController.SystemController;
import Model.Lising.*;
import Model.User.Email;
import Model.User.Landlord;
import Model.User.User;
import Viewer.View.*;

import javax.swing.*;
import java.sql.Date;
import java.time.LocalDate;

/**
 * A class that extends the UserClass where the currentUser
 * object contains a Landlord instance. Contains various
 * functions that implement the actions a Landlord can take.
 */
public class LandlordController extends UserController {

	//----------------------------------------------------------------------------------
	// Member variables
	//---------------------------------------------------------------------------------
    public Landlord current;
    public LandlordPage lp;
    FeePaymentView fp;
    RegisterPropertyPage rp;
    public EmailPage ep;
    public Email email;
    public Email recieved;
    EmailDialog ed;
    
    //-------------------------------------------------------
    // Constructor
    //-------------------------------------------------------
    /**
     * A constructor that takes a Landlord object as input.
     */
    public LandlordController(Landlord currentUser, LandlordPage landlordPage) {
        super(currentUser);
        current = currentUser;
        this.lp = landlordPage;
        this.lp.setLc(this);
        lp.initComponents();
        lp.setLocationRelativeTo(null);
        lp.setVisible(true);
        if(db.emailNotSeen(current.getEmail())){
            checkEmails();
        }
    }
    

    //--------------------------------------------
    // Property Creation and Modifying methods
    //--------------------------------------------
    /**
     * Registers a new property and adds it to the database.
     * @param {String} type Type of Property
     * @param {int} bedrooms Number of bedrooms in property
     * @param {int} bathrooms Number of bathrooms in property
     * @param {int} furnished Indicates whether property is furnished
     * @param {String} address Address of Property
     * @param {String} cityQuadrant Quadrant of Property
     */
    public void registerProperty(String type, int bedrooms, int bathrooms, int furnished, String address,
                                 String cityQuadrant){
        int next = db.getNextPropertyID();
        db.getProperties().add(new Property(current.getUserID(), next, type, bedrooms, bathrooms, furnished,
                address, cityQuadrant, "unlisted"));
    }

    /**
     * Creates Listing for Property and posts it.
     * Notifies all relevant Renters of a new Property.
     * @param {Property} p Property to be posted
     * @param {int} days Number of days to be listed
     */
    public void postProperty(Property p, int days){
        p.setState("listed");
        db.getListings().add(new Listing(p, days, "listed", LocalDate.now(), 0));
        db.updateRentersToNotify(p);
    }



    //-----------------------------------------------
    // Email methods
    //-----------------------------------------------
    /**
     * Searches through database of emails to find ones directed to current user and views them.
     */
    public void checkEmails(){
        for(Email e : db.getEmails()){
            if(e.getToEmail().equals(current.getEmail())){
                JOptionPane.showMessageDialog(null, "You have received a message from a renter.\n" +
                        "Click Ok to view it.");
                recieved = e;
                db.getEmails().remove(e);
                ed = new EmailDialog(lp, true);
                ed.setLc(this);
                ed.initComponents(e);
                ed.setLocationRelativeTo(null);
                ed.setVisible(true);
                break;
            }
        }
    }


    /**
     * Adds an email to the database.
     * @param {Email} email Email to be added to the database.
     */
    public void sendEmail(Email email){
        db.getEmails().add(new Email(email.getFromEmail(), email.getToEmail(), email.getDate(), email.getSubject(), email.getMessage()));
    }

    
    //---------------------------------------------
    // Methods that modify a Listing
    //---------------------------------------------
    /**
     * Changes a Listings state.
     * If the current state is suspended, the listing can become cancelled or listed.
     * If the current state is listed, the listing can become suspended, rented, or cancelled.
     * Renting or cancelling a listing deletes the listing.
     * @param {Listing} listing Listing to be changed
     * @param {String} state State to change Listing state too
     */
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
            else if(state.equals("cancelled")){
                cancelListing(listing);
            }
        }
    }
    
    /**
     * Changes the state of a suspended listing to listed.
     * @param {Listing} listing Listing to unsuspend.
     */
    private void unsuspendListing(Listing listing){

        for(Listing l : db.getSuspendedListings()){
            if(l.getProperty().getID() == listing.getProperty().getID()){

                l.getProperty().setState("listed");
                l.setState("listed");
                db.getListings().add(l);
                db.getSuspendedListings().remove(l);
                break;
            }
        }
    }
    
    /**
     * Cancels Listing and deletes it from the database.
     * @param {Listing} l Listing to be cancelled
     */
    private void cancelListing(Listing l){

        for(Listing cl : db.getListings()){
            if(cl.getProperty().getID() == l.getProperty().getID()){

                cl.getProperty().setState("unlisted");
                db.getListings().remove(cl);
                break;
            }
        }

    }

    /**
     * Changes the state of a listed Listing to suspended.
     * @param {Listing} listing Listing to be suspended.
     */
    private void suspendListing(Listing listing){
        for(Listing l : db.getListings()){
            if(l.getProperty().getID() == listing.getProperty().getID()){
                l.getProperty().setState("suspended");
                l.setState("suspended");
                db.getSuspendedListings().add(l);
                db.getListings().remove(l);
                break;
            }
        }
    }
    
    /**
     * Changes the Listing's Property's state to rented and deletes the Listing.
     * @param {Listing} listing Listing that is being rented.
     */
    private void rentOutListing(Listing listing){
        for(Listing l : db.getListings()){
            if(l.getProperty().getID() == listing.getProperty().getID()){
                l.getProperty().setState("rented");
                db.getRentedProperties().add(l.getProperty());
                db.getRentedDates().add(LocalDate.now());
                db.getListings().remove(l);
                break;
            }
        }
    }
    
    
    //--------------------------------------------------
    // Action Performed methods
    //----------------------------------------------------
    /**
     * Runs when register Button has been pressed. Brings up a RegisterProperty page.
     * Makes the LandlordView invisible.
     * @param {ActionEvent} evt Event that occured
     */
    public void registerButtonActionPerformed(java.awt.event.ActionEvent evt) {
        rp = new RegisterPropertyPage();
        rp.setLc(this);
        rp.initComponents();
        rp.setLocationRelativeTo(null);
        lp.setVisible(false);
        rp.setVisible(true);
    }

    /**
     * Runs when Post Property button has been pressed. Starts process to post selected property
     * and displays a FeePaymentView to the screen.
     * @param {ActionEvent} evt Event that occurred
     */
    public void postPropertButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if(lp.getPropertyList().getSelectedValue() == null){
            JOptionPane.showMessageDialog(null, "Must select one of the properties to post.");
        }
        else{
            String selected = lp.getPropertyList().getSelectedValue();
            selected = selected.substring(4, 11);
            if(db.getProperty(Integer.parseInt(selected.trim())).getState().equals("listed")){
                JOptionPane.showMessageDialog(null, "The property with id: "+selected+", is already listed.");
            }
            else{
                fp = new FeePaymentView();
                fp.setLc(this);
                fp.initComponents();
                fp.setLocationRelativeTo(null);
                lp.setVisible(false);
                fp.setVisible(true);
            }

        }
    }


    /**
     * Runs when cancel Posting button has been pressed. Cancels selected posting and
     * removes from the database.
     * @param {ActionEvent} evt Event that occurred
     */
    public void cancelPostingButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if(lp.getPostedList().getSelectedValue() == null){
            JOptionPane.showMessageDialog(null, "You must select a Posting to cancel.");
        }
        else{
            int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel this posting?\n" +
                    "You will not get any money back for paying a listing fee", "Confirmation", JOptionPane.YES_NO_OPTION);
            if(choice == JOptionPane.YES_OPTION){

                String selectedProperty = lp.getPostedList().getSelectedValue();

                selectedProperty = selectedProperty.substring(4, 11);

                if(db.getProperty(Integer.parseInt(selectedProperty)).getState().equals("listed")){
                    for(Listing l : db.getListings()){
                        if(l.getProperty().getID() == Integer.parseInt(selectedProperty.trim())){
                            changeListingState(l, "cancelled");
                            break;
                        }
                    }
                }
                else{
                    for(Listing l : db.getSuspendedListings()) {
                        if (l.getProperty().getID() == Integer.parseInt(selectedProperty.trim())) {
                            changeListingState(l, "cancelled");
                            break;
                        }
                    }
                }
                lp.setVisible(false);
                lp = new LandlordPage();
                lp.setLc(this);
                lp.initComponents();
                lp.setLocationRelativeTo(null);
                lp.setVisible(true);
                JOptionPane.showMessageDialog(null, "Propery with id: "+selectedProperty+" was successfully un-posted.\n" +
                        "You may re-post the property if you wish but will have to re-pay the listing fee.");
            }
        }
    }

    /**
     * Runs when Suspend button has been pressed. Changes selected Postings state to
     * suspended if it was listed and moves it from the regular Listing array in the database to the 
     * SuspendedListings one. If it was already suspended, gives the option to unsuspend it.
     * @param {ActionEvent} evt Event that occurred
     */
    public void suspendButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if(lp.getPostedList().getSelectedValue() == null){
            JOptionPane.showMessageDialog(null, "You must select a Posting to suspend / un-suspend.");
        }
        else{
            String cState = "";
            String selectedProperty = lp.getPostedList().getSelectedValue();
            selectedProperty = selectedProperty.substring(4, 11);
            for(Listing l : db.getListings()){
                if(l.getProperty().getID() == Integer.parseInt(selectedProperty.trim())){
                    cState = "listed";
                    changeListingState(l, "suspended");
                    lp.setVisible(false);
                    lp = new LandlordPage();
                    lp.setLc(this);
                    lp.initComponents();
                    lp.setLocationRelativeTo(null);
                    lp.setVisible(true);
                    JOptionPane.showMessageDialog(null, "Propery with id: "+selectedProperty+" was successfully suspended.");
                    break;
                }
                else{
                    cState = "suspended";
                }
            }
            if(cState.equals("suspended")){
                for(Listing l : db.getSuspendedListings()){
                    if(l.getProperty().getID() == Integer.parseInt(selectedProperty.trim())){
                        int choice = JOptionPane.showConfirmDialog(null, "The selected posting is already suspended.\n" +
                                        "If you click Yes, the selected posting will be un-suspended", "Are you sure?",
                                JOptionPane.YES_NO_OPTION);
                        if(choice == JOptionPane.YES_OPTION){
                            changeListingState(l, "listed");
                            lp.setVisible(false);
                            lp = new LandlordPage();
                            lp.setLc(this);
                            lp.initComponents();
                            lp.setLocationRelativeTo(null);
                            lp.setVisible(true);
                            JOptionPane.showMessageDialog(null, "Propery with id: "+selectedProperty+" was successfully un-suspended.");
                        }
                        break;
                    }
                }
            }
        }
    }


    /**
     * Runs when Rent Out button has been pressed. Rents out selected property and refreshes
     * LandlordView.
     * @param {ActionEvent} evt Event that occurred
     */
    public void rentOutButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if(lp.getPostedList().getSelectedValue() == null){
            JOptionPane.showMessageDialog(null, "You must select a Posting to rent to someone.");
        }
        else{
            String selectedProperty = lp.getPostedList().getSelectedValue();
            int propertyID = Integer.parseInt(selectedProperty.substring(4, 11));
            if(db.getProperty(propertyID).getState().equals("suspended")){
                JOptionPane.showMessageDialog(null, "Cannot rent out a posting that is suspended.\n" +
                        "You must un-suspend it first");
            }
            else{
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you'd like to rent" +
                        "this property to a customer?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if(choice == JOptionPane.YES_OPTION){
                    for(Listing l : db.getListings()){
                        if(l.getProperty().getID() == propertyID){
                            changeListingState(l, "rented");
                            break;
                        }
                    }
                    lp.setVisible(false);
                    lp = new LandlordPage();
                    lp.setLc(this);
                    lp.initComponents();
                    lp.setLocationRelativeTo(null);
                    lp.setVisible(true);
                    JOptionPane.showMessageDialog(null, "Property with id: "+propertyID+"\n" +
                            "has been rented.");
                }
            }
        }
    }

    /**
     * Runs when Exit button has been pressed. Stores all data to the database and exits the
     * program.
     * @param {ActionEvent} evt Event that occurred
     */
    public void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {
        int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?",
                "Confirmation:", JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.YES_OPTION){
            db.pushAll();
            System.exit(0);
        }
    }
    
    /**
     * Runs when Pay button has been pressed. Posts Property for specified duration and fee.
     * @param {ActionEvent} evt Event that occurred
     */
    public void payButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if(fp.getFeeList().getSelectedValue() == null){
            JOptionPane.showMessageDialog(null, "Must select the amount of days for the property to be posted");
        }
        else{
            String selectedFee = fp.getFeeList().getSelectedValue();
            selectedFee = selectedFee.substring(0, selectedFee.length()-5);
            String selectedProperty = lp.getPropertyList().getSelectedValue();
            selectedProperty = selectedProperty.substring(4, 11);
            for(ListingFee lf : db.getFees()){
                if(lf.getDays() == Integer.parseInt(selectedFee.trim())){
                    postProperty(db.getProperty(Integer.parseInt(selectedProperty.trim())), lf.getDays());
                    break;
                }
            }
            lp = new LandlordPage();
            lp.setLc(this);
            lp.initComponents();
            lp.setLocationRelativeTo(null);
            fp.setVisible(false);
            lp.setVisible(true);
            JOptionPane.showMessageDialog(null, "Propery with id: "+selectedProperty+" was successfully posted.");
        }
    }

    /**
     * Runs when Cancel Pay button has been pressed. Makes FeePaymentView invisible and displays
     * the LandlordPage.
     * @param {ActionEvent} evt Event that occurred
     */
    public void cancelPayButtonActionPerformed(java.awt.event.ActionEvent evt) {
        fp.setVisible(false);
        lp.setVisible(true);
    }
    
    /**
     * Runs when Post Property button has been pressed. Starts process to post selected property
     * and displays a FeePaymentView to the screen.
     * @param {ActionEvent} evt Event that occurred
     */
    public void feeListMouseClicked(java.awt.event.MouseEvent evt) {
        String selected = fp.getFeeList().getSelectedValue();
        selected = selected.substring(0, selected.length()-5);
        for(ListingFee lf : db.getFees()){
            if(lf.getDays() == Integer.parseInt(selected.trim())){
                fp.getPriceTextPane().setText(lf.getPrice() +".00");
                break;
            }
        }
    }

    /**
     * Runs when Back button has been pressed from the RegisterPropertyPage. 
     * Makes the RegisterPropertyPage invisible and displays the LandlordPage.
     * @param {ActionEvent} evt Event that occurred
     */
    public void registerBackButtonActionPerformed(java.awt.event.ActionEvent evt) {
        rp.setVisible(false);
        lp.setVisible(true);
    }

    /**
     * Runs when confirm Register button has been pressed. Validates all entered info
     * and if valid creates the new Property and adds it to the database. Closes
     * the RegisterPropertyPage afterwards.
     * @param {ActionEvent} evt Event that occurred
     */
    public void cnfirmRegisterButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if(rp.getAddressText().getText() == null){
            JOptionPane.showMessageDialog(null, "All fields must be filled");
        }
        else if(String.valueOf(rp.getTypeOption().getSelectedItem()).equals("N/A")){
            JOptionPane.showMessageDialog(null, "All fields must be filled");
        }
        else if(String.valueOf(rp.getnBedOption().getSelectedItem()).equals("N/A")){
            JOptionPane.showMessageDialog(null, "All fields must be filled");
        }
        else if(String.valueOf(rp.getnBathOption().getSelectedItem()).equals("N/A")){
            JOptionPane.showMessageDialog(null, "All fields must be filled");
        }
        else if(String.valueOf(rp.getFurnishedOption().getSelectedItem()).equals("N/A")){
            JOptionPane.showMessageDialog(null, "All fields must be filled");
        }
        else if (String.valueOf(rp.getQuadrantOption().getSelectedItem()).equals("N/A")){
            JOptionPane.showMessageDialog(null, "All fields must be filled");
        }
        else{
            String address = rp.getAddressText().getText().replaceAll("( +)", " ").trim();
            if(db.validateAddress(address)){
                String type = String.valueOf(rp.getTypeOption().getSelectedItem());
                String nBed = String.valueOf(rp.getnBedOption().getSelectedItem());
                String nBath = String.valueOf(rp.getnBathOption().getSelectedItem());
                String furnished = String.valueOf(rp.getFurnishedOption().getSelectedItem());
                String cq = String.valueOf(rp.getQuadrantOption().getSelectedItem());
                int isFurnished;
                if(furnished.equals("Yes")){
                    isFurnished = 1;
                }
                else{
                    isFurnished = 0;
                }
                registerProperty(type, Integer.parseInt(nBed), Integer.parseInt(nBath), isFurnished, address, cq);
                lp = new LandlordPage();
                lp.setLc(this);
                lp.initComponents();
                lp.setLocationRelativeTo(null);
                rp.setVisible(false);
                lp.setVisible(true);
                JOptionPane.showMessageDialog(null, "Property registered.");
            }
            else{
                JOptionPane.showMessageDialog(null, "This property Address is already in the system.");
            }
        }
    }
 
    /**
     * Runs when Send Email button has been pressed. Validates message and if valid,
     * creates email and adds it to the database.
     * @param {ActionEvent} evt Event that occurred
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
            lp.setVisible(true);
            JOptionPane.showMessageDialog(null, "Email sent");
            if(db.emailNotSeen(current.getEmail())){
                recieved = null;
                email = null;
                checkEmails();
            }
        }
    }

    /**
     * Makes the EmailPage invisible and displays the LandlordPage.
     * @param {ActionEvent} evt Event that occurred
     */
    public void cancelEmailButtonActionPerformed(java.awt.event.ActionEvent evt) {
        ep.setVisible(false);
        lp.setVisible(true);
        if(db.emailNotSeen(current.getEmail())){
            recieved = null;
            email = null;
            checkEmails();
        }
    }
}
