package Model.User;

import Model.Lising.ListingFee;
import Database.Database;

public class Manager extends User{
    private Database db;

    public Manager(int userID, String name, String username, String password, String email) {
        super(userID, name, username, password, email);

    }

    public void accessDatabase(){
        db = Database.getOnlyInstance();
    }

    public void setListingFeePrice(int price, int duration){
        for(ListingFee fee : db.getFees()){
            if(fee.getDays() == duration){
                fee.setPrice(price);
            }
        }
    }

    public void addListingFee(int price, int duration){

    }


}
