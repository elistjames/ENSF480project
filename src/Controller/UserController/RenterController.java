package Controller.UserController;

import Model.Lising.Listing;
import Model.User.Renter;
import Model.User.User;

public class RenterController extends UserController {
    Renter current;

    public RenterController(Renter currentUser){
        super(currentUser);
        current = currentUser;

    }

    public void requestMeeting(Listing l){

    }

    public void sendEmail(int toId, String msg){
        //db.getEmails().add(new Email(current.getUserID, toID, msg);
    }

    public void registerAccount(){

    }
    public void unregisterAccount(){
        for(Renter r : db.getRenters()){
            if(r.getUserID() == currentUser.getUserID()){
                db.getRenters().remove(r);
                db.getUsers().remove(r);
            }
        }
    }

    public void viewListings(){

    }
}
