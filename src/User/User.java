package User;

import SingletonDatabase.Database;
import Strategy.ValidateData;

public abstract class User {
    private String name;
    private String username;
    private String password;
    private int userID;
    private String email;
    private Database db;
    ValidateData vd;

    public User(int userID, String name, String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.userID = userID;
        this.email = email;
        this.name = name;
    }

    public void setValidateData(ValidateData vd) {
        this.vd = vd;
    }

    public boolean performValidateData(Object d){
        return vd.validate(d);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
