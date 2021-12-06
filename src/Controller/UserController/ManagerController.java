/** 
 * Author(s):
 * Editted by:
 * Documented by: Ryan Sommerville
 * Date created:
 * Last Editted:
 */

package Controller.UserController;


import Model.Lising.Listing;
import Model.Lising.ListingFee;
import Model.User.Manager;
import Model.User.SummaryReport;
import Model.User.User;


public class ManagerController extends UserController {
	//-------------------------------------------------------------------
	// Member Variables
	//-------------------------------------------------------------------
    Manager current;

    //--------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------
    public ManagerController(Manager currentUser) {
        super(currentUser);
        current = currentUser;
    }

    //--------------------------------------------------------------------
    // Viewing Functions
    //--------------------------------------------------------------------
    public void viewListings(){

    }

    public void viewRenters(){

    }
    
    public void viewLandlords(){

    }

    public void viewProperties(){

    }
    
    //--------------------------------------------------------------------
    // Listing State Change functions
    //--------------------------------------------------------------------

    /**
     * Changes the state of a Listing.
     * @param {Listing} listing Listing to be altered.
     * @param {String} state State to change to.
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
