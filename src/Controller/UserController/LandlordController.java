/*
 * Author(s): Eli, Luke, Manjot
 * Documented by: Ryan Sommerville
 * Date created: Dec 3, 2021
 * Last Editted: Dec 6, 2021
 */

package Controller.UserController;

import Model.Lising.Listing;
import Model.Lising.ListingFee;
import Model.Lising.Property;
import Model.User.Email;
import Model.User.Landlord;
import Viewer.View.*;

import javax.swing.*;
import java.time.LocalDate;

/**
 * A class that extends the UserController class where the currentUser
 * object contains a Landlord instance. Contains various
 * functions that implement the actions a Landlord can take.
 */
public class LandlordController extends UserController {

	//----------------------------------------------------------------------------------
	// Member variables
	//---------------------------------------------------------------------------------
	/**
	 * The current User that is logged in.
	 */
    public Landlord current;
    
    /**
     * The main GUI interface for the logged in User.
     */
    public LandlordPage lp;
    
    /**
     * The GUI interface for when a Landlord is paying to post a property.
     */
    FeePaymentView fp;
    
    /**
     * The GUI interface for when the landlord is registering a property.
     */
    RegisterPropertyPage rp;
    
    /**
     * The GUI interface for when the landlord is sending an email.
     */
    public EmailPage ep;
    
    /**
     * Email object that the landlord can send.
     */
    public Email email;
    
    /**
     * Email that the landlord has received.
     */
    public Email recieved;
    
    /**
     * GUI interface that shows an email the landlord has received.
     */
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
     * @param type {String} Type of Property
     * @param bedrooms {int} Number of bedrooms in property
     * @param bathrooms {int} Number of bathrooms in property
     * @param furnished{int} Indicates whether property is furnished
     * @param address {String} Address of Property
     * @param cityQuadrant {String} Quadrant of Property
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
     * @param p {Property} Property to be posted
     * @param days {int} Number of days to be listed
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
     * @param email {Email} Email to be added to the database.
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
     * @param listing {Listing} Listing to be changed
     * @param state {String} State to change Listing state too
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
            switch (state) {
                case "suspended" -> suspendListing(listing);
                case "rented" -> rentOutListing(listing);
                case "cancelled" -> cancelListing(listing);
            }
        }
    }
    
    /**
     * Changes the state of a suspended listing to listed.
     * @param listing {Listing} Listing to unsuspend.
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
     * @param l {Listing} Listing to be cancelled
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
     * @param listing {Listing} Listing to be suspended.
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
     * @param listing {Listing} Listing that is being rented.
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
    
    
    //-----------------------------------------------------------------------------------
    // Action Performed methods
    //------------------------------------------------------------------------------------
    
    //------------------------------
    // Registering Property Buttons
    //------------------------------
    /**
     * Runs when register Button has been pressed. Brings up a RegisterProperty page.
     * Makes the LandlordView invisible.
     */
    public void registerButtonActionPerformed() {
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
     */
    public void postPropertButtonActionPerformed() {
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
     */
    public void cancelPostingButtonActionPerformed() {
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
     */
    public void suspendButtonActionPerformed() {
        if(lp.getPostedList().getSelectedValue() == null){
            JOptionPane.showMessageDialog(null, "You must select a Posting to suspend / un-suspend.");
        }
        else{
            System.out.println("got to the button");
            String cState = "";
            String selectedProperty = lp.getPostedList().getSelectedValue();
            selectedProperty = selectedProperty.substring(4, 11);
            if(db.getListings().isEmpty()){
                cState = "suspended";
            }
            for(Listing l : db.getListings()){
                if(l.getProperty().getID() == Integer.parseInt(selectedProperty.trim())){
                    System.out.println("no no no");
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
                    System.out.println("Propery hello");
                    cState = "suspended";
                }
            }
            System.out.println(cState);
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
     */
    public void rentOutButtonActionPerformed() {
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
     */
    public void exitButtonActionPerformed() {
        int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?",
                "Confirmation:", JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.YES_OPTION){
            db.pushAll();
            System.exit(0);
        }
    }
    
    /**
     * Runs when Pay button has been pressed. Posts Property for specified duration and fee.
     */
    public void payButtonActionPerformed() {
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
     */
    public void cancelPayButtonActionPerformed() {
        fp.setVisible(false);
        lp.setVisible(true);
    }
    
    /**
     * Runs when Post Property button has been pressed. Starts process to post selected property
     * and displays a FeePaymentView to the screen.
     */
    public void feeListMouseClicked() {
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
     */
    public void registerBackButtonActionPerformed() {
        rp.setVisible(false);
        lp.setVisible(true);
    }

    /**
     * Runs when confirm Register button has been pressed. Validates all entered info
     * and if valid creates the new Property and adds it to the database. Closes
     * the RegisterPropertyPage afterwards.
     */
    public void cnfirmRegisterButtonActionPerformed() {
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
     */
    public void sendEmailButtonActionPerformed() {
        String msg = ep.getEmailTextArea().getText();
        String sub = ep.getSubjectText().getText();
        if(msg.length() >= 1000){
            JOptionPane.showMessageDialog(null, "Message is too long.\n Must be less than 1000 characters");
        }
        else{
            String fullSubject;
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
     */
    public void cancelEmailButtonActionPerformed() {
        ep.setVisible(false);
        lp.setVisible(true);
        if(db.emailNotSeen(current.getEmail())){
            recieved = null;
            email = null;
            checkEmails();
        }
    }
}
