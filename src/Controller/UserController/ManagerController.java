package Controller.UserController;

import Model.Lising.ListingFee;
import Model.User.Manager;
import Model.User.User;

public class ManagerController extends UserController {
    Manager current;

    public ManagerController(Manager currentUser) {
        super(currentUser);
        current = currentUser;

    }

    public void addFee(int duration, int price){
        db.getFees().add(new ListingFee(price, duration));
    }
}
