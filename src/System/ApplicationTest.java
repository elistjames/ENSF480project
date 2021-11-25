package System;
import Properties.ListingFee;
import SingletonDatabase.*;
import Strategy.*;
import User.*;

public class ApplicationTest {

    public static void main(String[] args) {
        Database db = Database.getOnlyInstance();
        Renter r1 = new Renter(10001, "eli","bigBoy8745","H@ckey00", "ehstjames@gmail.com");
        Manager manager = new Manager(10032, "dave","guyincharge007","yeyeye", "yeahbaby@sexymail.com");
        manager.setValidateData(new ValidateListingFee());
        if(manager.performValidateData(new ListingFee(120,100).getDays())){
            System.out.println("Listing Fee is ok to add");
        }
        else{
            System.out.println("There is already a price set for that amount of days");
        }
        r1.setValidateData(new ValidateUsername());
        if(r1.performValidateData(r1.getUsername())){
            System.out.println("Username is ok to use");
            r1.setValidateData(new ValidatePassword());
            if(r1.performValidateData(r1.getPassword())){
                System.out.println("Password is ok to use");
            }
            else {
                System.out.println("Password is already in use");
            }
        }
        else{
            System.out.println("Username is already in use");
        }
    }
}
