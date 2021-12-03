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
}
