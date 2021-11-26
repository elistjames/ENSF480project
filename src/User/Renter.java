package User;
import Properties.Listing;
import SingletonDatabase.Database;

import java.util.ArrayList;

public class Renter extends User{
    SearchCriteria sc;

    public Renter(){}

    public Renter(int userID, String name, String username, String password, String email) {
        super(userID, name, username, password, email);
    }

    public void makeAccount(String name, String username, String password, String email){
        setName(name);
        setUsername(username);
        setPassword(password);
        setEmail(email);
    }

    public SearchCriteria getSearchCriteria() {
        return this.sc;
    }

    public void createSearchCriteria(){
        sc = new SearchCriteria(this.getUserID());
    }
}
