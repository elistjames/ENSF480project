/*
 * Author(s): Eli, Luke, Manjot
 * Documented by: Ryan Sommerville
 * Date created: Dec 1, 2021
 * Last Editted: Dec 6, 2021
 */

package Controller.UserController;

import Model.Listing.Listing;
import Model.Listing.ListingFee;
import Model.User.Manager;
import Model.User.SummaryReport;
import Viewer.View.ChangeFeeView;
import Viewer.View.ListingStatusView;
import Viewer.View.ManagerView;
import Viewer.View.ReportView;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;

import static javax.swing.JOptionPane.showConfirmDialog;

/**
 * A class that extends the UserController class where the currentUser
 * object contains a Landlord instance. Contains various
 * functions that implement the actions a Landlord can take.
 */
public class ManagerController extends UserController {
	//-------------------------------------------------------------------
	// Member Variables
	//-------------------------------------------------------------------
	/**
	 * The current manager who is logged in to the program.
	 */
    Manager current;
    
    /**
     * The main GUI interface for a Manager.
     */
    ManagerView mv;
    
    /**
     * The GUI interface for changing the status of a listing.
     */
    ListingStatusView lsv;
    
    /**
     * The GUI interface for changing a fee amount.
     */
    ChangeFeeView cfv;
    
    /**
     * The GUI interface for viewing a report.
     */
    ReportView rv;
    
    JList jlist = new javax.swing.JList<>();

    
    //-------------------------------------------------------------
    // Constructor
    //-------------------------------------------------------------
    /**
     * A constructor that takes a Manager and ManagerView as inputs.
     * @param {Manager} currentUser Current logged in Manager.
     * @param {ManagerView} managerV GUI interface for the Manager.
     */
    public ManagerController(Manager currentUser, ManagerView managerV) {
        super(currentUser);
        current = currentUser;
        this.mv = managerV;
        this.mv.setMc(this, jlist);
        this.mv.initComponents();
        this.mv.setLocationRelativeTo(null);
        this.mv.setVisible(true);
    }

    /**
     * Gets and returns Summary Report based on parameters.
     * @param {String} startDate Beginning date of what to include in the report.
     * @param {String} endDate End date of what to include in the report.
     * @return The requested SummaryReport
     */
    public SummaryReport getReport(String startDate, String endDate){

        return db.getSummaryReport(startDate, endDate);
    }
  
    //----------------------------------------------
    // Methods to change what is shows on main GUI
    //----------------------------------------------
    /**
     * Sets the data that the Manager can view as an array of all the renters.
     * @param {JList} jlist
     */
    public void viewRenters(JList jlist){
        DefaultListModel<String> model = new DefaultListModel<>();
        for(var renter: db.getRenters()) {
            model.addElement(renter.getName());
        }
        
        jlist.setModel(model);
    }

    /**
     * Sets the data that the Manager can view as an array of all the landlords.
     * @param jlist
     */
    public void viewLandlords(JList jlist){
        DefaultListModel<String> model = new DefaultListModel<>();
        for(var landlord: db.getLandlords()) {
            model.addElement(landlord.getName());
        }
        
        jlist.setModel(model);
    }
    
    /**
     * Sets the data that the Manager can view as an array of all the properties.
     * @param jlist
     */
    public void viewProperties(JList jlist){
        DefaultListModel<String> model = new DefaultListModel<>();
        for(var property: db.getProperties()) {
            model.addElement(property.getID() + " " + property.getState() + " " + property.getAddress());
        }

        jlist.setModel(model);
    }



    
    //----------------------------------
    // Change ListingState methods
    //----------------------------------
    
    /**
     * Changes the state of the selected listing.
     * @param selectedState {String} New state of listing.
     * @param selectedID Listing whose state is being changed.
     */

    /**
     * Finds the selected property and opens the ListingStatusView to change
     * that properties status.
     * @param {String} selected String detailing selected property and its current status.
     * @param {JList} jlist
     */
    public void openListingStateView(String selected, JList jlist){
        String[] info = selected.split(" ");

        String propertyID = info[0];
        String status = info[1];
        if(status.equals("rented")){
            JOptionPane.showMessageDialog(null, "This property is rented,\nTherefore the status cannot\n" +
                    "be changed until the property's \nlease ends.");
        }
        else if(status.equals("unlisted")){
            JOptionPane.showMessageDialog(null, "The property is currently un-posted by its owner.\n" +
                    "You may not set it to active without the owners consent");
        }
        else{
            lsv = new ListingStatusView(status, propertyID);
            lsv.setMc(this);
            lsv.initComponents();
            lsv.updateComboBox(status);
            lsv.setLocationRelativeTo(null);
            lsv.setVisible(true);
        }
    }

    public void changeListingState(Listing listing, String state){
        System.out.println(listing.getProperty().getState());
        System.out.println(state);
        if(listing.getState().equals("suspended")){

            if(state.equals("unlisted")){
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
                case "unlisted" -> cancelListing(listing);
            }
        }
        viewProperties(jlist);
    }

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

    /**
     * Changes a listing whose state is suspended to listed.
     * Adds it to the database's regular list of listings, so that it is
     * shown when renters search for listings.
     * @param listing {Listing} Listing to be unsuspended.
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


    public void updateListingState(String selectedState, String selectedID) {
        db.updateListing(selectedState, selectedID);
        viewProperties(jlist);
        
        // Reload page
    }
    
    /**
     * Removes Listing from database.
     * @param l {Listing} Listing to be cancelled.
     */
    public void cancelListing(Listing l){
        for(Listing cl : db.getListings()){
            if(cl.getProperty().getID() == l.getProperty().getID()){
                db.getListings().remove(cl);
            }
        }
    }

    /**
     * Changes the listings state to suspended.
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

    
    //----------------------------------------------------------------------
    // Periodic Report functions
    //-----------------------------------------------------------------------
    
    /**
     * Constructs a periodic report and displays it on the screen.
     */
    public void getReport(){
        int monthlyListings = 0;
        for(Listing l : db.getListings()) {
            if(l.getCurrentDay() <= 30){
                monthlyListings++;
            }
        }
        int active_list = db.getListings().size();
        SummaryReport monthlyReport = new SummaryReport(monthlyListings,4,active_list,db.getRentedProperties());
        //gui implementation
    }
    
    /**
     * Opens the ReportView GUI interface
     */
    public void openReportView(){
        rv = new ReportView();
        rv.setMc(this);
        rv.initComponents();
        rv.setLocationRelativeTo(null);
        rv.setVisible(true);

    }


    //----------------------------------------------------------------------
    // Listing Fee functions
    //---------------------------------------------------------------------
    /**
     * Changes the price of a ListingFee.
     * @param lf {ListingFee} Which ListingFee to change
     * @param new_price {int} Price to change ListingFee to.
     */
    public void changeFee(ListingFee lf, int new_price){
            for (ListingFee f : db.getFees()) {
                if (f.getDays() ==  lf.getDays()){
                    f.setPrice(new_price);
                }
            }
    }

    /**
     * Creates new ListingFee.
     * @param duration {int} Duration of fee.
     * @param price {int} Price of fee.
     */
    public void addFee(int duration, int price){
        db.getFees().add(new ListingFee(price, duration));
    }
    
    /**
     * Changes the fee for an already existing ListingFee.
     * @param period {int} Duration of ListingFee
     * @param fee {int} New fee
     */
    public void updateFee(int period, int fee) {
        ArrayList<ListingFee> curr = db.getFees();
        ListingFee newFee = new ListingFee(fee, period);
        boolean found = false;

        for(var existingFee: curr) {
            if(existingFee.getDays() == period) {
                found = true;
                break;
            }
        }

        if(!found) {
            db.getFees().add(newFee);
            db.pushFees();
            JOptionPane.showMessageDialog(null, "Success!");
        } else {
            int choice = showConfirmDialog(null, "Already have a fee for this listing period.\n Would you like to replace it?",
                    "Replace?", JOptionPane.YES_NO_OPTION);
            if(choice == JOptionPane.YES_OPTION){
                for(ListingFee f : db.getFees()){
                    if(f.getDays() == newFee.getDays()){
                        db.getFees().remove(f);
                        db.pushFees();
                        JOptionPane.showMessageDialog(null, "Success!");
                        break;
                    }
                }
                db.getFees().add((newFee));
                db.pushFees();
            }
        }
    }
    
    /**
     * Opens the changeFeeView interface.
     */
    public void openFeeView() {
        cfv = new ChangeFeeView();
        cfv.setMc(this);
        cfv.initComponents();
        cfv.setLocationRelativeTo(null);
        cfv.setVisible(true);
    }
}
