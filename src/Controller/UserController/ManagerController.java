package Controller.UserController;


import Model.Lising.Listing;
import Model.Lising.ListingFee;
import Model.User.Manager;
import Model.User.SummaryReport;
import Model.User.User;


public class ManagerController extends UserController {
    Manager current;

    public ManagerController(Manager currentUser) {
        super(currentUser);
        current = currentUser;
    }

    public void cancelListing(Listing l){

    }

    public void viewListings(){

    }


    public void changeListingState(String state, Listing l){

    }

    public void getReport(){
        int active_list = db.getListings().size();
        //  SummaryReport monthlyReport = new SummaryReport();
    }

    public void changeFee(ListingFee lf, int new_price){
            for (ListingFee f : db.getFees()) {
                if (f.getDays() ==  lf.getDays()){
                    f.setPrice(new_price);
                }
            }
    }

    public void viewRenters(){

    }

    public void viewLandlords(){

    }
    public void viewProperties(){

    }

    public void addFee(int duration, int price){
        db.getFees().add(new ListingFee(price, duration));
    }

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

    public void unsuspendListing(Listing listing){
        for(Listing l : db.getSuspendedListings()){
            if(l.getProperty().getID() == listing.getProperty().getID()){
                l.getProperty().setState("listed");
                db.getListings().add(l);
                db.getSuspendedListings().remove(l);
            }
        }
    }

    public void suspendListing(Listing listing){
        for(Listing l : db.getListings()){
            if(l.getProperty().getID() == listing.getProperty().getID()){
                l.getProperty().setState("suspended");
                db.getSuspendedListings().add(l);
                db.getListings().remove(l);
            }
        }
    }
}
