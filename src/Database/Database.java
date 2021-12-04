package Database;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

import Model.User.*;

import Model.Lising.*;



public class Database {
    private static Database onlyInstance;

    private ArrayList<User> users;
    private ArrayList<Renter> renters;
    private ArrayList<Landlord> landlords;
    private ArrayList<Property> properties;
    private ArrayList<Manager> managers;
    private ArrayList<ListingFee> fees;
    private ArrayList<Listing> listings;
    private ArrayList<Date> listingDates;
    private ArrayList<Date> listing;
    private ArrayList<Property> rentedProperties;
    private ArrayList<Date> rentedDates;
    private ArrayList<SearchCriteria> searches;
    private ArrayList<Integer> rentersToNotify;
    private ArrayList<Email> emails;

    private ArrayList<Listing> suspendedListings;

    private Connection dbConnect;
    private ResultSet results;

    private Database() {

    }

    public static Database getOnlyInstance() {
        if (onlyInstance == null)
            onlyInstance = new Database();
        return onlyInstance;
    }

    public void initializeConnection() {
        try {
            String DBURL = "path to .db file";
                dbConnect = DriverManager.getConnection(DBURL); //initialize connections
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
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

    public boolean validateListingFee(int days){
        for(ListingFee lf : fees){
            if(lf.getDays() == days){
                return false;
            }
        }
        return true;
    }

    public boolean validateLogin(String username, String password){
        boolean validLogin = false;
        for(User u : users){
            if(u.getUsername().equals(username)&&u.getPassword().equals(password)){
                validLogin = true;
                break;
            }
        }
        return validLogin;
    }

    public void updateListingDates(Date currentDate){
        for(Listing l : listings){
            int days = (int)Duration.between(l.getStartDate().toLocalDate(), currentDate.toLocalDate()).toDays();
            l.setCurrentDay(days);
            if(l.getCurrentDay() >= l.getDuration()){
                l.setState("expired");
                listings.remove(l);
            }
        }
    }

    public void registerRenter(Renter r){
        int nextID = getNextUserID();
        r.setUserID(nextID);
        r.setType("renter");
        renters.add(r);
    }

    public void registerProperty(Property p){
        int nextID = getNextPropertyID();
        p.setID(nextID);
        properties.add(p);
    }

    public void updateRentersToNotify(Property p){
        for(Renter r : renters){
            if(r.getSc().getType().equals(p.getType())||r.getSc().getType().equals("any")){
                if(r.getSc().getN_bedrooms() == p.getBedrooms() || r.getSc().getN_bedrooms() == -1){
                    if(r.getSc().getN_bathrooms() == p.getBathrooms() || r.getSc().getN_bathrooms() == -1){
                        if(r.getSc().isFurnished() == p.isFurnished() || r.getSc().isFurnished() == -1){
                            if(r.getSc().getCityQuadrant().equals(p.getCityQuadrant())||r.getSc().getCityQuadrant().equals("any")){
                                if(!rentersToNotify.contains(r.getUserID())){
                                    rentersToNotify.add(r.getUserID());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean notifyRenter(int id){
        for(int i : rentersToNotify){
            if(i == id){
                rentersToNotify.remove(i);
                return true;
            }
        }
        return false;
    }

    private void pullCurrentSearchCriteria(Renter current){
        for(SearchCriteria sc : searches){
            if(sc.getRenterID() == current.getUserID()){
                current.setSc(sc);
            }
        }
    }

    private void updateCurrentSearchCriteria(SearchCriteria current){
        for(int i = 0; i < searches.size(); i++){
            if(searches.get(i).getRenterID() == current.getRenterID()){
                searches.set(i, current);
            }
        }
    }



    public User getCurrentUser(String username, String password) {
        User current = new Renter(0,"**","**","**","**","**");
        for(Renter r : renters){
            if(r.getUsername().equals(username)&&r.getPassword().equals(password)){
                pullCurrentSearchCriteria(r);
                return r;
            }
        }
        for(Landlord l : landlords){
            if(l.getUsername().equals(username)&&l.getPassword().equals(password)){
                return l;
            }
        }
        for(Manager m : managers){
            if(m.getUsername().equals(username)&&m.getPassword().equals(password)){
                return m;
            }
        }
        return current;
    }

    private void pullRenters(){
        ResultSet result; //create new ResultSet object
        try {
            Statement myStmt = dbConnect.createStatement(); //create new statement
            result = myStmt.executeQuery("SELECT * FROM RENTERS"); //execute statement select all from Chair table
            while(result.next()) { //run while next row exists
                this.renters.add(new Renter(result.getInt("ID"), result.getString("Name"),
                        result.getString("Username"), result.getString("Password"),
                        result.getString("Email"), "renter"));
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
                        result.getString("Email"), "landlord"));
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
                        result.getString("CityQuadrant"), result.getString("State")));
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
                        result.getString("Email"), "manager"));
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
                        result.getInt("Duration"), result.getString("State"),
                        result.getDate("StartDate"), result.getInt("CurrentDay")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void pullRentedProperties(){
        ResultSet result; //create new ResultSet object
        try {
            Statement myStmt = dbConnect.createStatement(); //create new statement
            result = myStmt.executeQuery("SELECT * FROM RENTED"); //execute statement select all from Chair table
            while(result.next()) { //run while next row exists
                this.rentedProperties.add(new Property(result.getInt("LandlordID"),
                        result.getInt("ID"), result.getString("Type"),
                        result.getInt("Bedrooms"), result.getInt("Bathrooms"),
                        result.getInt("Furnished"), result.getString("Address"),
                        result.getString("CityQuadrant"), "rented"));
                this.rentedDates.add(result.getDate("DateRented"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void pullSuspendedListings(){
        ResultSet result; //create new ResultSet object
        try {
            Statement myStmt = dbConnect.createStatement(); //create new statement
            result = myStmt.executeQuery("SELECT * FROM SUSPENDED"); //execute statement select all from Chair table
            while(result.next()) { //run while next row exists
                this.suspendedListings.add(new Listing(this.getProperty(result.getInt("PropertyID")),
                        result.getInt("Duration"), result.getString("State"),
                        result.getDate("StartDate"), result.getInt("CurrentDay")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void pullRTN(){
        ResultSet result; //create new ResultSet object
        try {
            Statement myStmt = dbConnect.createStatement(); //create new statement
            result = myStmt.executeQuery("SELECT * FROM RTN"); //execute statement select all from Chair table
            while(result.next()) { //run while next row exists
                this.rentersToNotify.add(result.getInt("renterID"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void pullSearchCriterias(){
        ResultSet result; //create new ResultSet object
        try {
            Statement myStmt = dbConnect.createStatement(); //create new statement
            result = myStmt.executeQuery("SELECT * FROM SEARCHES"); //execute statement select all from Chair table
            while(result.next()) { //run while next row exists
                this.searches.add(new SearchCriteria(result.getInt("RenterID"),
                        result.getString("Type"),result.getInt("Bedrooms"),
                        result.getInt("Bathrooms"),result.getInt("Furnished"),
                        result.getString("CityQuadrant")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void pullListingDates(){
        ResultSet result; //create new ResultSet object
        try {
            Statement myStmt = dbConnect.createStatement(); //create new statement
            result = myStmt.executeQuery("SELECT * FROM LISTINGDATES");
            while(result.next()) { //run while next row exists
                this.listingDates.add(result.getDate("DateOfListing"));
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

    private void pullEmails(){
        ResultSet result; //create new ResultSet object
        try {
            Statement myStmt = dbConnect.createStatement(); //create new statement
            result = myStmt.executeQuery("SELECT * FROM EMAILS");
            while(result.next()) { //run while next row exists
                this.emails.add(new Email(result.getString("From"), result.getString("To"),
                        result.getDate("Date"), result.getString("Subject"),
                        result.getString("Message")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
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
                query = "INSERT INTO PROPERTIES (LandlordID,ID,Type,Bedrooms,Bathrooms,Furnished,Address,CityQuadrant,State) VALUES (?,?,?,?,?,?,?,?,?)";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setInt(1, property.getLandlordID());
                myStmt.setInt(2, property.getID());
                myStmt.setString(3, property.getType());
                myStmt.setInt(4, property.getBedrooms());
                myStmt.setInt(5, property.getBathrooms());
                myStmt.setInt(6, property.isFurnished());
                myStmt.setString(7, property.getAddress());
                myStmt.setString(8, property.getCityQuadrant());
                myStmt.setString(9, property.getState());

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
                query = "INSERT INTO LISTINGS (PropertyID,Duration,State,StartDate,CurrentDay) VALUES (?,?,?,?,?)";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setInt(1, listing.getProperty().getID());
                myStmt.setInt(2, listing.getDuration());
                myStmt.setString(3, listing.getState());
                myStmt.setDate(4, (Date) listing.getStartDate());
                myStmt.setInt(5, listing.getCurrentDay());

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

    private void pushSuspendedListings(){
        try{
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate("TRUNCATE SUSPENDED");
            String query;

            for(Listing listing : suspendedListings){
                query = "INSERT INTO SUSPENDED (PropertyID,Duration,State,StartDate,CurrentDay) VALUES (?,?,?,?,?)";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setInt(1, listing.getProperty().getID());
                myStmt.setInt(2, listing.getDuration());
                myStmt.setString(3, listing.getState());
                myStmt.setDate(4, (Date) listing.getStartDate());
                myStmt.setInt(5, listing.getCurrentDay());

                myStmt.execute();
                myStmt.close();
            }
        } catch (SQLException e){

        }
    }

    private void pushRTN(){
        try{
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate("TRUNCATE RTN");
            String query;

            for(int renterID : rentersToNotify){
                query = "INSERT INTO RTN (renterID) VALUES (?)";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setInt(1, renterID);

                myStmt.execute();
                myStmt.close();
            }
        } catch (SQLException e){

        }
    }

    private void pushRentedProperties(){
        try{
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate("TRUNCATE RENTED");
            String query;

            for(int i = 0; i < rentedProperties.size(); i++){
                query = "INSERT INTO RENTED (LandlordID,ID,Type,Bedrooms,Bathrooms,Furnished,Address,CityQuadrant,DateRented) VALUES (?,?,?,?,?,?,?,?,?)";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setInt(1, rentedProperties.get(i).getLandlordID());
                myStmt.setInt(2, rentedProperties.get(i).getID());
                myStmt.setString(3, rentedProperties.get(i).getType());
                myStmt.setInt(4, rentedProperties.get(i).getBedrooms());
                myStmt.setInt(5, rentedProperties.get(i).getBathrooms());
                myStmt.setInt(6, rentedProperties.get(i).isFurnished());
                myStmt.setString(7, rentedProperties.get(i).getAddress());
                myStmt.setString(8, rentedProperties.get(i).getCityQuadrant());
                myStmt.setDate(9, rentedDates.get(i));

                myStmt.execute();
                myStmt.close();
            }
        } catch (SQLException e){

        }
    }

    private void pushListingDates(){
        try{
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate("TRUNCATE LISTINGDATES");
            String query;

            for(Date d : listingDates){
                query = "INSERT INTO LISTINGDATES (DateOfListing) VALUES (?)";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setDate(1, d);

                myStmt.execute();
                myStmt.close();
            }
        } catch (SQLException e){

        }
    }

    private void pushSearchCriterias(){
        try{
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate("TRUNCATE SEARCHES");
            String query;

            for(SearchCriteria search : searches){
                query = "INSERT INTO SEARCHES (RenterID,Type,Bedrooms,Bathrooms,Furnished,CityQuadrant) VALUES (?,?,?,?,?,?)";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setInt(1, search.getRenterID());
                myStmt.setString(2, search.getType());
                myStmt.setInt(3, search.getN_bedrooms());
                myStmt.setInt(4, search.getN_bathrooms());
                myStmt.setInt(5, search.isFurnished());
                myStmt.setString(6, search.getCityQuadrant());

                myStmt.execute();
                myStmt.close();
            }
        } catch (SQLException e){

        }
    }

    private void pushEmails(){
        try{
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate("TRUNCATE EMAILS");
            String query;

            for(Email e : emails){
                query = "INSERT INTO EMAILS (From,To,Date,Subject,Message) VALUES (?,?,?,?,?)";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setString(1, e.getFromEmail());
                myStmt.setString(2, e.getToEmail());
                myStmt.setDate(3, e.getDate());
                myStmt.setString(4, e.getSubject());
                myStmt.setString(5, e.getMessage());


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
        this.pushListingDates();
        this.pushRentedProperties();
        this.pushRTN();
        this.pushSearchCriterias();
    }

    public void pullAll(){
        this.pullRenters();
        this.pullLandlords();
        this.pullProperties();
        this.pullManagers();
        this.pullFees();
        this.pullListings();
        this.pullUsers();
        this.pullListingDates();
        this.pullRentedProperties();
        this.pullRTN();
        this.pullSearchCriterias();
    }

    public int getNextPropertyID(){
        if(properties.isEmpty()&&rentedProperties.isEmpty()){
            return 1000000;
        }
        int nextID = properties.get(0).getID();
        for(Property p : properties){
            if(p.getID() > nextID){
                nextID = p.getID();
            }
        }
        for(Property p : rentedProperties){
            if(p.getID() > nextID){
                nextID = p.getID();
            }
        }
        return (nextID+1);
    }

    public int getNextUserID(){
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

    public String lookupLandlordEmail(int id){
        String email = "";
        for(Landlord l : landlords){
            if(l.getUserID() == id){
                email = l.getEmail();
                break;
            }
        }
        return email;
    }


    //getters
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

    public ArrayList<Property> getProperties() {
        return properties;
    }

    public ArrayList<Date> getListingDates() {
        return listingDates;
    }

    public ArrayList<Property> getRentedProperties() {
        return rentedProperties;
    }

    public ArrayList<SearchCriteria> getSearches() {
        return searches;
    }

    public ArrayList<Integer> getRentersToNotify() {
        return rentersToNotify;
    }

    public ArrayList<Listing> getSuspendedListings() {
        return suspendedListings;
    }

    public ArrayList<Email> getEmails() {
        return emails;
    }

    public ArrayList<Date> getRentedDates() {
        return rentedDates;
    }
}
