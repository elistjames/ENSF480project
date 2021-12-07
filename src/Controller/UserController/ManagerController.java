/** 
 * Author(s):
 * Editted by:
 * Documented by: Ryan Sommerville
 * Date created:
 * Last Editted:
 */

package Controller.UserController;

import java.time.temporal.ValueRange;
import java.util.ArrayList;
import Model.Lising.Listing;
import Model.Lising.ListingFee;
import Model.User.Manager;
import Model.User.SummaryReport;
import Model.User.User;
import Viewer.View.ManagerView;
import Viewer.View.ListingStatusView;
import Viewer.View.ChangeFeeView;
import Viewer.View.ReportView;
import javax.swing.*;

import static javax.swing.JOptionPane.showConfirmDialog;

public class ManagerController extends UserController {
	//-------------------------------------------------------------------
	// Member Variables
	//-------------------------------------------------------------------
    Manager current;
    ManagerView mv;
    ListingStatusView lsv;
    ChangeFeeView cfv;
    ReportView rv;
    JList jlist = new javax.swing.JList<>();

    public ManagerController(Manager currentUser, ManagerView managerV) {
        super(currentUser);
        current = currentUser;
        this.mv = managerV;
        this.mv.setMc(this, jlist);
        this.mv.initComponents();
        this.mv.setLocationRelativeTo(null);
        this.mv.setVisible(true);
    }


    // public void cancelListing(Listing l){
    //     for(Listing cl : db.getListings()){
    //         if(cl.getProperty().getID() == l.getProperty().getID()){
    //             db.getListings().remove(cl);
    //         }
    //     }
    // }

    public SummaryReport getReport(String startDate, String endDate){

        SummaryReport report = db.getSummaryReport(startDate, endDate);
        return report;
    }

    // public void changeFee(ListingFee lf, int new_price){
    //     for (ListingFee f : db.getFees()) {
    //         if (f.getDays() ==  lf.getDays()){
    //             f.setPrice(new_price);
    //         }
    //     }
    // }

    public void viewRenters(JList jlist){
        DefaultListModel<String> model = new DefaultListModel<>();
        for(var renter: db.getRenters()) {
            model.addElement(renter.getName());
        }
        
        jlist.setModel(model);
    }

    public void viewLandlords(JList jlist){
        DefaultListModel<String> model = new DefaultListModel<>();
        for(var landlord: db.getLandlords()) {
            model.addElement(landlord.getName());
        }
        
        jlist.setModel(model);
    }
    public void viewProperties(JList jlist){
        DefaultListModel<String> model = new DefaultListModel<>();
        for(var property: db.getProperties()) {
            model.addElement(property.getID() + " " + property.getState() + " " + property.getAddress());
        }

        jlist.setModel(model);
    }


    public void openListingStateView(String selected, JList jlist){
        String[] info = selected.split(" ");

        String propertyID = info[0];
        String status = info[1];

        lsv = new ListingStatusView(status, propertyID);
        lsv.setMc(this);
        lsv.initComponents();
        lsv.setLocationRelativeTo(null);
        lsv.setVisible(true);

    }

    public void openReportView(){
        rv = new ReportView();
        rv.setMc(this);
        rv.initComponents();
        rv.setLocationRelativeTo(null);
        rv.setVisible(true);

    }

    public void updateListingState(String selectedState, String selectedID) {
        db.updateListing(selectedState, selectedID);
        viewProperties(jlist);
        
        // Reload page
    }

    public void openFeeView() {
        cfv = new ChangeFeeView();
        cfv.setMc(this);
        cfv.initComponents();
        cfv.setLocationRelativeTo(null);
        cfv.setVisible(true);
    }

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
     * Changes a listing whose state is suspended to listed.
     * Adds it to the database's regular list of listings, so that it is
     * shown when renters search for listings.
     * @param {Listing} listing Listing to be unsuspended.
     */
    public void unsuspendListing(Listing listing){
        for(Listing l : db.getSuspendedListings()){
            if(l.getProperty().getID() == listing.getProperty().getID()){
                l.getProperty().setState("listed");
                db.getListings().add(l);
                db.getSuspendedListings().remove(l);
            }
        }
    }
    
    /**
     * Removes Listing from database.
     * @param {Listing} l Listing to be cancelled.
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
     * @param {Listing} listing Listing to be suspended.
     */
    public void suspendListing(Listing listing){
        for(Listing l : db.getListings()){
            if(l.getProperty().getID() == listing.getProperty().getID()){
                l.getProperty().setState("suspended");
                db.getSuspendedListings().add(l);
                db.getListings().remove(l);
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

    //----------------------------------------------------------------------
    // Listing Fee functions
    //---------------------------------------------------------------------
    /**
     * Changes the price of a ListingFee.
     * @param {ListingFee} lf Which ListingFee to change
     * @param {int} new_price Price to change ListingFee to.
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
     * @param {int} duration Duration of fee.
     * @param {int} price Price of fee.
     */
    public void addFee(int duration, int price){
        db.getFees().add(new ListingFee(price, duration));
    }

}
