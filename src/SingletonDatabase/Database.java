package SingletonDatabase;

import Properties.Listing;
import Properties.ListingFee;
import Properties.Property;
import User.*;


import java.sql.*;
import java.util.ArrayList;


public class Database {
    private static Database onlyInstance;
    private String DBURL;
    private String USERNAME;
    private String PASSWORD;
    private ArrayList<User> users;
    private ArrayList<Renter> renters;
    private ArrayList<Landlord> landlords;
    private ArrayList<Property> properties;
    private ArrayList<Manager> managers;
    private ArrayList<ListingFee> fees;
    private ArrayList<Listing> listings;

    private Connection dbConnect;
    private ResultSet results;

    private Database() {
        DBURL = "whatever";
        USERNAME = "whatever";
        PASSWORD = "whatever";
        users = new ArrayList<User>();
        users.add(new Renter(10001,"eli","ehstjames","H@ckey00","ehstjames@gmail.com"));
        fees = new ArrayList<ListingFee>();
        fees.add(new ListingFee(100, 100));
        //initializeConnection();
        //this.pullAll();
    }

    public static Database getOnlyInstance() {
        if (onlyInstance == null)
            onlyInstance = new Database();
        return onlyInstance;
    }

    private void initializeConnection() {
        try {
                dbConnect = DriverManager.getConnection(DBURL, USERNAME, PASSWORD); //initialize connections
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void registerRenter(Renter r){
        int nextID = getNextUserID();
        r.setUserID(nextID);
        renters.add(r);
    }

    public void registerProperty(Property p){
        int nextID = getNextPropertyID();
        p.setID(nextID);
        properties.add(p);
    }
/*
    public User getCurrentUser(String username, String password) {
        for(Renter r : renters){
            if(r.getUsername().equals(username)&&r.getPassword().equals(password)){

            }
        }
    }
*/
    private void pullRenters(){
        ResultSet result; //create new ResultSet object
        try {
            Statement myStmt = dbConnect.createStatement(); //create new statement
            result = myStmt.executeQuery("SELECT * FROM RENTERS"); //execute statement select all from Chair table
            while(result.next()) { //run while next row exists
                this.renters.add(new Renter(result.getInt("ID"), result.getString("Name"),
                        result.getString("Username"), result.getString("Password"),
                        result.getString("Email")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void pullLandlords(){
        ResultSet result; //create new ResultSet object
        try {
            Statement myStmt = dbConnect.createStatement(); //create new statement
            result = myStmt.executeQuery("SELECT * FROM LANDLORDS"); //execute statement select all from Chair table
            while(result.next()) { //run while next row exists
                this.landlords.add(new Landlord(result.getInt("ID"), result.getString("Name"),
                        result.getString("Username"), result.getString("Password"),
                        result.getString("Email")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void pullProperties(){
        ResultSet result; //create new ResultSet object
        try {
            Statement myStmt = dbConnect.createStatement(); //create new statement
            result = myStmt.executeQuery("SELECT * FROM PROPERTIES"); //execute statement select all from Chair table
            while(result.next()) { //run while next row exists
                this.properties.add(new Property(result.getInt("LandlordID"),
                        result.getInt("ID"), result.getString("Type"),
                        result.getInt("Bedrooms"), result.getInt("Bathrooms"),
                        result.getInt("Furnished"), result.getString("Address"),
                        result.getString("CityQuadrant")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void pullManagers(){
        ResultSet result; //create new ResultSet object
        try {
            Statement myStmt = dbConnect.createStatement(); //create new statement
            result = myStmt.executeQuery("SELECT * FROM MANAGERS"); //execute statement select all from Chair table
            while(result.next()) { //run while next row exists
                this.managers.add(new Manager(result.getInt("ID"), result.getString("Name"),
                        result.getString("Username"), result.getString("Password"),
                        result.getString("Email")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void pullFees(){
        ResultSet result; //create new ResultSet object
        try {
            Statement myStmt = dbConnect.createStatement(); //create new statement
            result = myStmt.executeQuery("SELECT * FROM FEES"); //execute statement select all from Chair table
            while(result.next()) { //run while next row exists
                this.fees.add(new ListingFee(result.getInt("Price"), result.getInt("Days")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void pullListings(){
        ResultSet result; //create new ResultSet object
        try {
            Statement myStmt = dbConnect.createStatement(); //create new statement
            result = myStmt.executeQuery("SELECT * FROM LISTINGS"); //execute statement select all from Chair table
            while(result.next()) { //run while next row exists
                this.listings.add(new Listing(this.getProperty(result.getInt("PropertyID")),
                        result.getInt("Duration"), result.getString("State")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void pullUsers(){
        users.addAll(renters);
        users.addAll(landlords);
        users.addAll(managers);
    }

    private void pushRenters(){
        try{
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate("TRUNCATE RENTERS");
            String query;

            for(Renter renter : renters){
                query = "INSERT INTO RENTERS (ID,Name,Username,Password,Email) VALUES (?,?,?,?,?)";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setInt(1, renter.getUserID());
                myStmt.setString(2, renter.getName());
                myStmt.setString(3, renter.getUsername());
                myStmt.setString(4, renter.getPassword());
                myStmt.setString(5, renter.getEmail());

                myStmt.execute();
                myStmt.close();
            }
        } catch (SQLException e){

        }
    }
    private void pushLandlords(){
        try{
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate("TRUNCATE LANDLORDS");
            String query;

            for(Landlord landlord : landlords){
                query = "INSERT INTO LANDLORDS (ID,Name,Username,Password,Email) VALUES (?,?,?,?,?)";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setInt(1, landlord.getUserID());
                myStmt.setString(2, landlord.getName());
                myStmt.setString(3, landlord.getUsername());
                myStmt.setString(4, landlord.getPassword());
                myStmt.setString(5, landlord.getEmail());

                myStmt.execute();
                myStmt.close();
            }
        } catch (SQLException e){

        }
    }
    private void pushProperties(){
        try{
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate("TRUNCATE PROPERTIES");
            String query;

            for(Property property : properties){
                query = "INSERT INTO PROPERTIES (LandlordID,ID,Type,Bedrooms,Bathrooms,Furnished,Address,CityQuadrant) VALUES (?,?,?,?,?,?,?,?)";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setInt(1, property.getLandlordID());
                myStmt.setInt(2, property.getID());
                myStmt.setString(3, property.getType());
                myStmt.setInt(4, property.getBedrooms());
                myStmt.setInt(5, property.getBathrooms());
                myStmt.setInt(6, property.isFurnished());
                myStmt.setString(7, property.getAddress());
                myStmt.setString(8, property.getCityQuadrant());

                myStmt.execute();
                myStmt.close();
            }
        } catch (SQLException e){

        }
    }
    private void pushManagers(){
        try{
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate("TRUNCATE MANAGERS");
            String query;

            for(Manager manager : managers){
                query = "INSERT INTO MANAGERS (ID,Name,Username,Password,Email) VALUES (?,?,?,?,?)";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setInt(1, manager.getUserID());
                myStmt.setString(2, manager.getName());
                myStmt.setString(3, manager.getUsername());
                myStmt.setString(4, manager.getPassword());
                myStmt.setString(5, manager.getEmail());

                myStmt.execute();
                myStmt.close();
            }
        } catch (SQLException e){

        }
    }
    private void pushFees(){
        try{
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate("TRUNCATE FEES");
            String query;

            for(ListingFee fee : fees){
                query = "INSERT INTO FEES (Price,Duration) VALUES (?,?)";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setInt(1, fee.getPrice());
                myStmt.setInt(2, fee.getDays());

                myStmt.execute();
                myStmt.close();
            }
        } catch (SQLException e){

        }
    }
    private void pushListings(){
        try{
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate("TRUNCATE LISTINGS");
            String query;

            for(Listing listing : listings){
                query = "INSERT INTO LISTINGS (PropertyID,Duration,State) VALUES (?,?,?)";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setInt(1, listing.getProperty().getID());
                myStmt.setInt(2, listing.getDuration());
                myStmt.setString(3, listing.getState());

                myStmt.execute();
                myStmt.close();
            }
        } catch (SQLException e){

        }
    }
    private void pushUsers(){
        try{
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate("TRUNCATE USERS");
            String query;

            for(Renter renter : renters){
                query = "INSERT INTO USERS (ID,Name,Username,Password,Email) VALUES (?,?,?,?,?)";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setInt(1, renter.getUserID());
                myStmt.setString(2, renter.getName());
                myStmt.setString(3, renter.getUsername());
                myStmt.setString(4, renter.getPassword());
                myStmt.setString(5, renter.getEmail());

                myStmt.execute();
                myStmt.close();
            }
            for(Landlord landlord : landlords){
                query = "INSERT INTO USERS (ID,Name,Username,Password,Email) VALUES (?,?,?,?,?)";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setInt(1, landlord.getUserID());
                myStmt.setString(2, landlord.getName());
                myStmt.setString(3, landlord.getUsername());
                myStmt.setString(4, landlord.getPassword());
                myStmt.setString(5, landlord.getEmail());

                myStmt.execute();
                myStmt.close();
            }
            for(Manager manager : managers){
                query = "INSERT INTO USERS (ID,Name,Username,Password,Email) VALUES (?,?,?,?,?)";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setInt(1, manager.getUserID());
                myStmt.setString(2, manager.getName());
                myStmt.setString(3, manager.getUsername());
                myStmt.setString(4, manager.getPassword());
                myStmt.setString(5, manager.getEmail());

                myStmt.execute();
                myStmt.close();
            }
        } catch (SQLException e){

        }
    }

    public void pushAll(){
        this.pushRenters();
        this.pushLandlords();
        this.pushProperties();
        this.pushManagers();
        this.pushFees();
        this.pushListings();
        this.pushUsers();
    }

    public void pullAll(){
        this.pullRenters();
        this.pullLandlords();
        this.pullProperties();
        this.pullManagers();
        this.pullFees();
        this.pullListings();
        this.pullUsers();
    }



    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Renter> getRenters() {
        return renters;
    }

    public ArrayList<Landlord> getLandlords() {
        return landlords;
    }

    public ArrayList<Manager> getManagers() {
        return managers;
    }

    public ArrayList<ListingFee> getFees() {
        return fees;
    }

    public ArrayList<Listing> getListings() {
        return listings;
    }

    public boolean validateUsername(String username){
        for(User user : users){
           if(user.getUsername().compareTo(username) == 0){
               return false;
           }
        }
        return true;
    }

    public boolean validatePassword(String password){
        for(User user : users){
            if(user.getPassword().compareTo(password) == 0){
                return false;
            }
        }
        return true;
    }



    private int getNextPropertyID(){
        if(properties.isEmpty()){
            return 1000000;
        }
        int nextID = properties.get(0).getID();
        for(Property p : properties){
            if(p.getID() > nextID){
                nextID = p.getID();
            }
        }
        return (nextID+1);
    }

    private int getNextUserID(){
        if(users.isEmpty()){
            return 1000000;
        }
        int nextID = users.get(0).getUserID();
        for(User user : users){
            if(user.getUserID() > nextID){
                nextID = user.getUserID();
            }
        }
        return (nextID+1);
    }

    private Property getProperty(int propertyID){
        for(Property p : properties){
            if(p.getID() == propertyID){
                return p;
            }
        }
        return null;
    }
}
