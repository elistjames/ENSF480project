package Model.User;
import Model.Lising.*;

import java.util.ArrayList;



public class Renter extends User{
    SearchCriteria sc;

    public Renter(){}

    public Renter(int userID, String name, String username, String password, String email, String type) {
        super(userID, name, username, password, email, type);
    }

    public SearchCriteria getSc() {
        return sc;
    }

    public void setSc(SearchCriteria sc) {
        this.sc = sc;
    }

}
