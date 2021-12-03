package Controller.UserController;

import Model.User.Manager;
import Model.User.User;

public class ManagerController extends UserController {
    Manager current;

    public ManagerController(Manager currentUser) {
        super(currentUser);
        current = currentUser;

    }
}
