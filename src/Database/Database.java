/*
 * Author(s): Eli St. James
 * Documented by: Ryan Sommerville
 * Date created: Nov 27, 2021
 * Last Edited: Dec 6, 2021
 */




/*
 * A class that accesses a database and retrieves and
 * stores the data there. It is also in charge of filling
 * the database with new data when the program exits. It follows the Singleton design
 * pattern and therefore only one instance of it can exist
 * at any point in time.
 */

package Database;

import Model.Lising.Listing;
import Model.Lising.ListingFee;
import Model.Lising.Property;
import Model.User.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * A class that accesses a database and retrieves and
 * stores the data there. It is also in charge of filling
 * the database with new data when the program exits. It follows the Singleton design
 * pattern and therefore only one instance of it can exist
 * at any point in time.
 */
public class Database {
	/**
	 * The variable that stores the only instance of Database
	 * that can exist, according to the Singleton design pattern.
	 */
    private static Database onlyInstance;

    //Arrays of data
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Renter> renters = new ArrayList<>();
    private ArrayList<Landlord> landlords = new ArrayList<>();
    private ArrayList<Property> properties = new ArrayList<>();
    private ArrayList<Manager> managers = new ArrayList<>();
    private ArrayList<ListingFee> fees = new ArrayList<>();
    private ArrayList<Listing> listings = new ArrayList<>();
    private ArrayList<LocalDate> listingDates = new ArrayList<>();
    private ArrayList<Property> rentedProperties = new ArrayList<>();
    private ArrayList<LocalDate> rentedDates = new ArrayList<>();
    private ArrayList<SearchCriteria> searches = new ArrayList<>();
    private ArrayList<Integer> rentersToNotify = new ArrayList<>();
    private ArrayList<Email> emails = new ArrayList<>();
    private Listing[] l;


    private ArrayList<Listing> suspendedListings = new ArrayList<>();

    /**
     * Connection used to connect to the database.
     */
    private Connection dbConnect;
    private ResultSet results;

    //-------------------------------
    // Constructor
    //-------------------------------
    
    /**
     * Basic constructor. Made private for the
     * Singleton design pattern.
     */
    private Database() {

    }

    //----------------------------------
    // Database instance retrieval
    //----------------------------------
    /**
     * Returns the only instance of Database in the program,
     * creates one if there is none.
     * @return {Database} The only instance of Database in the program.
     */
    public static Database getOnlyInstance() {
        if (onlyInstance == null)
            onlyInstance = new Database();
        return onlyInstance;
    }

    //---------------------------------
    // Database connection
    //---------------------------------
    /**
     * Initializes connection to the database.
     */
    public void initializeConnection() {
        try {
            String DBURL = "jdbc:sqlite:ensf480.db";
                dbConnect = DriverManager.getConnection(DBURL); //initialize connections
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    
    
    //------------------------------------------------------------------------
    // Validation methods, finding whether usernames, etc are already in use.
    //------------------------------------------------------------------------
    
    /**
     * Checks to see whether a username is already being used.
     * @param {String} username Username to be validated.
     * @return False if the username corresponds to an existing User, true if it doesn't.
     */
    public boolean validateUsername(String username){
        for(User user : users){
            if(user.getUsername().compareTo(username) == 0){
                return false;
            }
        }
        return true;
    }

    /**
     * Checks to see whether password is already in use.
     * @param {String} password Password to be validated.
     * @return True if password is not being used by any User in the program. False if it is.
     */
    public boolean validatePassword(String password){
        for(User user : users){
            if(user.getPassword().compareTo(password) == 0){
                return false;
            }
        }
        return true;
    }

    /**
     * Checks to see whether Email is already in use.
     * @param {String} email Email to be validated.
     * @return True if email is not being used by any other User in the program. False if it is.
     */
    public boolean validateEmail(String email){
        for(User user : users){
            if(user.getEmail().compareTo(email) == 0){
                return false;
            }
        }
        return true;
    }

    /**
     * Checks to see whether ListingFee already exists with this duration.
     * @param {int} days Duration of fee.
     * @return False if there is already a fee with this duration. True otherwise.
     */
    public boolean validateListingFee(int days){
        for(ListingFee lf : fees){
            if(lf.getDays() == days){
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if there is a User with specified username and password.
     * @param {String} username Username to be verified.
     * @param {String} password Password to be verified.
     * @return True if User with username and password exists, false otherwise.
     */
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

    /**
     * Checks if there if already a Property with this address.
     * @param {String} address Address to be verified.
     * @return False if there is a Property with this address already, true otherwise.
     */
    public boolean validateAddress(String address){
        for(Property p : properties){
            if(p.getAddress().equals(address)){
                return false;
            }
        }
        return true;
    }

    
    //--------------------------------------------------------------------------
    // Methods to find, modify and add data
    //--------------------------------------------------------------------------
    
    /**
     * Updates Listings based on current date, expiring Listings who's durations
     * have expired.
     * @param {Date} currentDate Date object holding the current date.
     */
    public void updateListingDates(Date currentDate){
        int counter = 0;
        while (counter < listings.size()){
            for(Listing l : listings){
                long days = l.getStartDate().until(currentDate.toLocalDate(), DAYS);

                l.setCurrentDay((int)days);
                if(l.getCurrentDay() >= l.getDuration()){
                    l.setState("expired");
                    listings.remove(l);
                    break;
                }
            }
            counter++;
        }

    }

    /**
     * Adds a Renter to the database.
     * @param {Renter} r Renter to be registered.
     */
    public void registerRenter(Renter r){
        int nextID = getNextUserID();
        r.setUserID(nextID);
        r.setType("renter");
        renters.add(r);
    }

    /**
     * Checks to see if there exists an email with the ToEmail matching the parameter.s
     * @param {String} email Email to be verified.
     * @return True if an email exists with a toEmail that mathes the paramter. False otherwise.
     */
    public boolean emailNotSeen(String email){
        for(Email e : emails){
            if(e.getToEmail().equals(email)){
                return true;
            }
        }
        return false;
    }

    /**
     * Adds Property to the database.
     * @param {Property} p Property to be registered.
     */
    public void registerProperty(Property p){
        int nextID = getNextPropertyID();
        p.setID(nextID);
        properties.add(p);
    }

    /**
     * Finds Renters whose search criteria matches the Property parameter and adds them
     * to the array of rentersToNotify if they are not already on there.
     * @param {Property} p Property to notify renters of.
     */
    public void updateRentersToNotify(Property p){
        for(Renter r : renters){
            if(getCurrentSearch(r.getUserID()).getType().equals(p.getType())||getCurrentSearch(r.getUserID()).getType().equals("N/A")){
                if(getCurrentSearch(r.getUserID()).getN_bedrooms() == p.getBedrooms() || getCurrentSearch(r.getUserID()).getN_bedrooms() == -1){
                    if(getCurrentSearch(r.getUserID()).getN_bathrooms() == p.getBathrooms() || getCurrentSearch(r.getUserID()).getN_bathrooms() == -1){
                        if(getCurrentSearch(r.getUserID()).isFurnished() == p.isFurnished() || getCurrentSearch(r.getUserID()).isFurnished() == -1){
                            if(getCurrentSearch(r.getUserID()).getCityQuadrant().equals(p.getCityQuadrant())||getCurrentSearch(r.getUserID()).getCityQuadrant().equals("N/A")){
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

    /**
     * Checks whether Renter is to be notified.
     * @param {int} id Id of Renter
     * @return True if Renter is on rentersToNotify array, false otherwise.
     */
    public boolean notifyRenter(int id){
        for(int i = 0; i < rentersToNotify.size(); i++){
            if(rentersToNotify.get(i) == id){
                rentersToNotify.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Finds the SearchCriteria of the Renter with the parameter's ID.
     * @param {int} currentId Id of current Renter.
     * @return Search criteria of Renter.
     */
    public SearchCriteria getCurrentSearch(int currentId){
        for(SearchCriteria sc : searches){
            if(sc.getRenterID() == currentId){
                return sc;
            }
        }
        return null;
    }

    /**
     * Replaces the SearchCriteria in the database with the same RenterID
     * as current with current.
     * @param {SearchCriteria} current
     */
    private void updateCurrentSearchCriteria(SearchCriteria current){
        for(int i = 0; i < searches.size(); i++){
            if(searches.get(i).getRenterID() == current.getRenterID()){
                searches.set(i, current);
            }
        }
    }

    /**
     * Changes the state of Property and Listing with selectedID.
     * @param {String} selectedState State to change to.
     * @param {String} selectedID ID of selected Property.
     */
    public void updateListing(String selectedState, String selectedID) {

        // Modify property state
        for(var property: properties) {
            if(property.getID() == Integer.parseInt(selectedID)) {
                property.setState(selectedState);
            }
        }

        // Modify listing status
        ArrayList<Listing> newListings = new ArrayList<>();
        boolean found = false;
        for(var listing: listings) {
            Property currProperty = listing.getProperty();
            // Modifying existing
            if(currProperty.getID() == Integer.parseInt(selectedID)) {
                found = true;
                listing.setState(selectedState);
            } 

            newListings.add(listing);
        }

        if(!found) {
            Listing toAdd = new Listing(this.getProperty(Integer.parseInt(selectedID)),
            30, selectedState, LocalDate.now(), 0);
            newListings.add(toAdd);
        }

        listings = newListings;
    }

    /**
     * Finds  and returns User with username and password from the database.
     * @param {String} username Username of User to find.
     * @param {String} password Password of User to find.
     * @return User with username and password that equals the parameters.
     */
    public User getCurrentUser(String username, String password) {
        User current = new Renter(0,"**","**","**","**","**");
        for(Renter r : renters){
            if(r.getUsername().equals(username)&&r.getPassword().equals(password)){
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

    
    
    //--------------------------------------------
    // Pulling methods
    //--------------------------------------------
    /**
     * Takes Renter info from the database and stores it into renters Array.
     */
    private void pullRenters(){
        ResultSet result; //create new ResultSet object
        try {
            Statement myStmt = dbConnect.createStatement(); //create new statement
            result = myStmt.executeQuery("SELECT * FROM renters"); //execute statement select all from Chair table
            while(result.next()) { //run while next row exists
                this.renters.add(new Renter(result.getInt("ID"), result.getString("Name"),
                        result.getString("Username"), result.getString("Password"),
                        result.getString("Email"), "renter"));
            }
            result.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Takes Landlord info from the database and stores it into landlords Array.
     */
    private void pullLandlords(){
        ResultSet result; //create new ResultSet object
        try {
            Statement myStmt = dbConnect.createStatement(); //create new statement
            result = myStmt.executeQuery("SELECT * FROM landlords"); //execute statement select all from Chair table
            while(result.next()) { //run while next row exists
                this.landlords.add(new Landlord(result.getInt("ID"), result.getString("Name"),
                        result.getString("Username"), result.getString("Password"),
                        result.getString("Email"), "landlord"));
            }
            result.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Takes Property info from the database and stores it into properties Array.
     */
    private void pullProperties(){
        ResultSet result; //create new ResultSet object
        try {
            Statement myStmt = dbConnect.createStatement(); //create new statement
            result = myStmt.executeQuery("SELECT * FROM properties"); //execute statement select all from Chair table
            while(result.next()) { //run while next row exists
                this.properties.add(new Property(result.getInt("LandlordID"),
                        result.getInt("ID"), result.getString("Type"),
                        result.getInt("Bedrooms"), result.getInt("Bathrooms"),
                        result.getInt("Furnished"), result.getString("Address"),
                        result.getString("CityQuadrant"), result.getString("State")));
            }
            result.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Takes Manager info from the database and stores it into managers Array.
     */
    private void pullManagers(){
        ResultSet result; //create new ResultSet object
        try {
            Statement myStmt = dbConnect.createStatement(); //create new statement
            result = myStmt.executeQuery("SELECT * FROM managers"); //execute statement select all from Chair table
            while(result.next()) { //run while next row exists
                this.managers.add(new Manager(result.getInt("ID"), result.getString("Name"),
                        result.getString("Username"), result.getString("Password"),
                        result.getString("Email"), "manager"));
            }
            result.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Takes ListingFee info from the database and stores it into fees Array.
     */
    private void pullFees(){
        ResultSet result; //create new ResultSet object
        try {
            Statement myStmt = dbConnect.createStatement(); //create new statement
            result = myStmt.executeQuery("SELECT * FROM fees"); //execute statement select all from Chair table
            while(result.next()) { //run while next row exists
                this.fees.add(new ListingFee(result.getInt("Price"), result.getInt("Days")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Takes Listing info from the database and stores it into listings Array.
     */
    private void pullListings(){
        ResultSet result; //create new ResultSet object
        try {
            Statement myStmt = dbConnect.createStatement(); //create new statement
            result = myStmt.executeQuery("SELECT * FROM listings"); //execute statement select all from Chair table
            while(result.next()) { //run while next row exists
                this.listings.add(new Listing(this.getProperty(result.getInt("PropertyID")),
                        result.getInt("Duration"), result.getString("State"),
                        LocalDate.parse(result.getString("StartDate")), result.getInt("CurrentDay")));
            }
            result.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Takes RentedProperties info from the database and stores it into rentedProperties and rentedDates Arrays.
     */
    private void pullRentedProperties(){
        ResultSet result; //create new ResultSet object
        try {
            Statement myStmt = dbConnect.createStatement(); //create new statement
            result = myStmt.executeQuery("SELECT * FROM rented"); //execute statement select all from Chair table
            while(result.next()) { //run while next row exists
                this.rentedProperties.add(new Property(result.getInt("LandlordID"),
                        result.getInt("ID"), result.getString("Type"),
                        result.getInt("Bedrooms"), result.getInt("Bathrooms"),
                        result.getInt("Furnished"), result.getString("Address"),
                        result.getString("CityQuadrant"), "rented"));
                this.rentedDates.add(LocalDate.parse(result.getString("DateRented")));
            }
            result.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Takes info of suspended Listings from the database and stores it into suspendedListings Array.
     */
    private void pullSuspendedListings(){
        ResultSet result; //create new ResultSet object
        try {
            Statement myStmt = dbConnect.createStatement(); //create new statement
            result = myStmt.executeQuery("SELECT * FROM suspended"); //execute statement select all from Chair table
            while(result.next()) { //run while next row exists
                this.suspendedListings.add(new Listing(this.getProperty(result.getInt("PropertyID")),
                        result.getInt("Duration"), result.getString("State"),
                        LocalDate.parse(result.getString("StartDate")), result.getInt("CurrentDay")));
            }
            result.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Takes info of Renters to notify from the database and stores it into rentersToNotify Array.
     */
    private void pullRTN(){
        ResultSet result; //create new ResultSet object
        try {
            Statement myStmt = dbConnect.createStatement(); //create new statement
            result = myStmt.executeQuery("SELECT * FROM rtn"); //execute statement select all from Chair table
            while(result.next()) { //run while next row exists
                this.rentersToNotify.add(result.getInt("renterID"));
            }
            result.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Takes SearchCriteria info from the database and stores it into searches Array.
     */
    private void pullSearchCriterias(){
        ResultSet result; //create new ResultSet object
        try {
            Statement myStmt = dbConnect.createStatement(); //create new statement
            result = myStmt.executeQuery("SELECT * FROM searches"); //execute statement select all from Chair table
            while(result.next()) { //run while next row exists
                this.searches.add(new SearchCriteria(result.getInt("RenterID"),
                        result.getString("Type"),result.getInt("Bedrooms"),
                        result.getInt("Bathrooms"),result.getInt("Furnished"),
                        result.getString("CityQuadrant")));
            }
            result.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Takes info of listingDates from the database and stores it into listingDates Array.
     */
    private void pullListingDates(){
        ResultSet result; //create new ResultSet object
        try {
            Statement myStmt = dbConnect.createStatement(); //create new statement
            result = myStmt.executeQuery("SELECT * FROM listingdates");
            while(result.next()) { //run while next row exists
                this.listingDates.add(LocalDate.parse(result.getString("DateOfListing")));
            }
            result.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Populates users Array with contents of renters, landlords, and managers Arrays.
     */
    private void pullUsers(){
        users.addAll(renters);
        users.addAll(landlords);
        users.addAll(managers);
    }

    /**
     * Takes Email info from the database and stores it into emails Array.
     */
    private void pullEmails(){
        ResultSet result; //create new ResultSet object
        try {
            Statement myStmt = dbConnect.createStatement(); //create new statement
            result = myStmt.executeQuery("SELECT * FROM emails");
            while(result.next()) { //run while next row exists
                this.emails.add(new Email(result.getString("UserFrom"), result.getString("UserTo"),
                        LocalDate.parse(result.getString("Date")), result.getString("Subject"),
                        result.getString("Message")));
            }
            result.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Takes all data from the database and stores it in arrays in this class.
     */
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
        this.pullEmails();
        this.pullSuspendedListings();

    }

    
    
    
    //---------------------------------------------
    // Methods to push to database
    //---------------------------------------------
    
    /**
     * Stores info from renters Array into the database.
     */
    private void pushRenters(){
        try{
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate("DELETE FROM renters");
            String query;

            for(Renter renter : renters){
                query = "INSERT INTO renters (ID,Name,Username,Password,Email) VALUES (?,?,?,?,?)";
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
            e.printStackTrace();
        }
    }
    
    /**
     * Stores info from landlords Array into the database.
     */
    private void pushLandlords(){
        try{
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate("DELETE FROM landlords");
            String query;

            for(Landlord landlord : landlords){
                query = "INSERT INTO landlords (ID,Name,Username,Password,Email) VALUES (?,?,?,?,?)";
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
            e.printStackTrace();
        }
    }
    
    /**
     * Stores info from properties Array into the database.
     */
    private void pushProperties(){
        try{
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate("DELETE FROM properties");
            String query;

            for(Property property : properties){
                query = "INSERT INTO properties (LandlordID,ID,Type,Bedrooms,Bathrooms,Furnished,Address,CityQuadrant,State) VALUES (?,?,?,?,?,?,?,?,?)";
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
            e.printStackTrace();
        }
    }
    
    /**
     * Stores info from managers Array into the database.
     */
    private void pushManagers(){
        try{
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate("DELETE FROM managers");
            String query;

            for(Manager manager : managers){
                query = "INSERT INTO managers (ID,Name,Username,Password,Email) VALUES (?,?,?,?,?)";
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
            e.printStackTrace();
        }
    }
    
    /**
     * Stores info from fees Array into the database.
     */
    public void pushFees(){
        try{
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate("DELETE FROM fees");
            String query;

            for(ListingFee fee : fees){
                query = "INSERT INTO fees (Price,Days) VALUES (?,?)";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setInt(1, fee.getPrice());
                myStmt.setInt(2, fee.getDays());

                myStmt.execute();
                myStmt.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    /**
     * Stores info from listings Array into the database.
     */
    private void pushListings(){
        l = new Listing[listings.size()];
        for(int i = 0; i < l.length; i++){
            l[i] = listings.get(i);
        }
        try{
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate("DELETE FROM listings");
            String query;
            for(int i = 0; i < l.length; i++){
                query = "INSERT INTO listings (PropertyID,Duration,State,StartDate,CurrentDay) VALUES (?,?,?,?,?)";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setInt(1, l[i].getProperty().getID());
                myStmt.setInt(2, l[i].getDuration());
                myStmt.setString(3, l[i].getState());
                myStmt.setString(4, l[i].getStartDate().toString());
                myStmt.setInt(5, l[i].getCurrentDay());

                myStmt.execute();
                myStmt.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Stores info from renters, landlords, and managers Array into a User category in the database.
     */
    private void pushUsers(){
        try{
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate("DELETE FROM users");
            String query;

            for(Renter renter : renters){
                query = "INSERT INTO users (ID,Name,Username,Password,Email,Type) VALUES (?,?,?,?,?,?)";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setInt(1, renter.getUserID());
                myStmt.setString(2, renter.getName());
                myStmt.setString(3, renter.getUsername());
                myStmt.setString(4, renter.getPassword());
                myStmt.setString(5, renter.getEmail());
                myStmt.setString(6, renter.getType());

                myStmt.execute();
                myStmt.close();
            }
            for(Landlord landlord : landlords){
                query = "INSERT INTO users (ID,Name,Username,Password,Email,Type) VALUES (?,?,?,?,?,?)";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setInt(1, landlord.getUserID());
                myStmt.setString(2, landlord.getName());
                myStmt.setString(3, landlord.getUsername());
                myStmt.setString(4, landlord.getPassword());
                myStmt.setString(5, landlord.getEmail());
                myStmt.setString(6, landlord.getType());

                myStmt.execute();
                myStmt.close();
            }
            for(Manager manager : managers){
                query = "INSERT INTO users (ID,Name,Username,Password,Email,Type) VALUES (?,?,?,?,?,?)";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setInt(1, manager.getUserID());
                myStmt.setString(2, manager.getName());
                myStmt.setString(3, manager.getUsername());
                myStmt.setString(4, manager.getPassword());
                myStmt.setString(5, manager.getEmail());
                myStmt.setString(6, manager.getType());

                myStmt.execute();
                myStmt.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Stores info from suspendedListings Array into the database.
     */
    private void pushSuspendedListings(){
        try{
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate("DELETE FROM suspended");
            String query;

            for(Listing listing : suspendedListings){
                query = "INSERT INTO suspended (PropertyID,Duration,State,StartDate,CurrentDay) VALUES (?,?,?,?,?)";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setInt(1, listing.getProperty().getID());
                myStmt.setInt(2, listing.getDuration());
                myStmt.setString(3, listing.getState());
                myStmt.setString(4, listing.getStartDate().toString());
                myStmt.setInt(5, listing.getCurrentDay());

                myStmt.execute();
                myStmt.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Stores info from rentersToNotify Array into the database.
     */
    private void pushRTN(){
        try{
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate("DELETE FROM rtn");
            String query;

            for(int renterID : rentersToNotify){
                query = "INSERT INTO rtn (renterID) VALUES (?)";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setInt(1, renterID);

                myStmt.execute();
                myStmt.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Stores info from rentedProperties Array into the database.
     */
    private void pushRentedProperties(){
        try{
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate("DELETE FROM rented");
            String query;

            for(int i = 0; i < rentedProperties.size(); i++){
                query = "INSERT INTO rented (LandlordID,ID,Type,Bedrooms,Bathrooms,Furnished,Address,CityQuadrant,DateRented) VALUES (?,?,?,?,?,?,?,?,?)";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setInt(1, rentedProperties.get(i).getLandlordID());
                myStmt.setInt(2, rentedProperties.get(i).getID());
                myStmt.setString(3, rentedProperties.get(i).getType());
                myStmt.setInt(4, rentedProperties.get(i).getBedrooms());
                myStmt.setInt(5, rentedProperties.get(i).getBathrooms());
                myStmt.setInt(6, rentedProperties.get(i).isFurnished());
                myStmt.setString(7, rentedProperties.get(i).getAddress());
                myStmt.setString(8, rentedProperties.get(i).getCityQuadrant());
                myStmt.setString(9, rentedDates.get(i).toString());

                myStmt.execute();
                myStmt.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Stores info from listingDates Array into the database.
     */
    private void pushListingDates(){
        try{
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate("DELETE FROM listingdates");
            String query;

            for(LocalDate d : listingDates){
                query = "INSERT INTO listingdates (DateOfListing) VALUES (?)";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setString(1, d.toString());

                myStmt.execute();
                myStmt.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Stores info from searches Array into the database.
     */
    private void pushSearchCriterias(){
        for(SearchCriteria criteria : searches){
            if(criteria.getRenterID() == 0){
                criteria.setType("N/A");
                criteria.setN_bedrooms(-1);
                criteria.setN_bathrooms(-1);
                criteria.setFurnished(-1);
                criteria.setCityQuadrant("N/A");
            }
        }
        try{
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate("DELETE FROM searches");
            String query;

            for(SearchCriteria search : searches){
                query = "INSERT INTO searches (RenterID,Type,Bedrooms,Bathrooms,Furnished,CityQuadrant) VALUES (?,?,?,?,?,?)";
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
            e.printStackTrace();
        }
    }

    /**
     * Stores info from emails Array into the database.
     */
    private void pushEmails(){
        try{
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate("DELETE FROM emails");
            String query;

            for(Email e : emails){
                query = "INSERT INTO emails (UserFrom,UserTo,Date,Subject,Message) VALUES (?,?,?,?,?)";
                PreparedStatement myStmt = dbConnect.prepareStatement(query);

                myStmt.setString(1, e.getFromEmail());
                myStmt.setString(2, e.getToEmail());
                myStmt.setString(3, e.getDate().toString());
                myStmt.setString(4, e.getSubject());
                myStmt.setString(5, e.getMessage());


                myStmt.execute();
                myStmt.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Stores all data in this class in the database.
     */
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
        this.pushEmails();
        this.pushSuspendedListings();

    }


    //------------------------------------------------------
    // Methods to find IDs
    //------------------------------------------------------
   
    /**
     * Finds the next available Property ID by searching for the
     * greatest property ID that currently exists.
     * @return Next available id.
     */
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

    /**
     * Finds the next available User ID by searching for the
     * greatest User ID that currently exists.
     * @return Next available id.
     */
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

    
    //-------------------------------------------------------
    // Methods to retrieve data from database.
    //-------------------------------------------------------
    
    /**
     * Finds relevant data for the SummaryReport and returns it.
     * @param {String} startDate First date to consider for the SummaryReport.
     * @param {String} endDate Last date to consider for the SummaryReport.
     * @return SummaryReport with data regarding what occurred between startDate and endDate
     */
    public SummaryReport getSummaryReport(String startDate, String endDate) {
        try {
            // Statement stmt = dbConnect.createStatement();
            ResultSet listedCount = dbConnect.createStatement().executeQuery("SELECT COUNT(*) AS listedCount FROM listingdates WHERE DateOfListing between \""+ startDate + "\" and \"" + endDate + "\""); 
            ResultSet activeCount = dbConnect.createStatement().executeQuery("SELECT COUNT(*) AS activeCount FROM listings WHERE State = \"Active\" AND StartDate between \""+ startDate + "\" and \"" + endDate + "\""); 
            ResultSet rentedCount = dbConnect.createStatement().executeQuery("SELECT COUNT(*) AS rentedCount FROM rented WHERE DateRented between \""+ startDate + "\" and \"" + endDate + "\""); 
            ResultSet rentedList = dbConnect.createStatement().executeQuery("SELECT * FROM rented WHERE DateRented between \""+ startDate + "\" and \"" + endDate + "\""); 
            listedCount.next();
            activeCount.next();
            rentedCount.next();
            System.out.println(listedCount.getInt("listedCount") + rentedCount.getInt("rentedCount") +  
            activeCount.getInt("activeCount"));
            ArrayList<Property> rented = new ArrayList<Property>();

            while(rentedList.next()) {
                Property curr = new Property(rentedList.getInt("LandlordID"), rentedList.getString("Type"), rentedList.getInt("Bedrooms"), 
                rentedList.getInt("Bathrooms"), rentedList.getInt("Furnished"), rentedList.getString("Address"), rentedList.getString("CityQuadrant"));
                System.out.println("------------------------------------");
                rented.add(curr);
            }
            // int userID, String name, String username, String password, String email, String type
            SummaryReport report = new SummaryReport(listedCount.getInt("listedCount"), rentedCount.getInt("rentedCount"),  
                                                activeCount.getInt("activeCount"),  rented);

            return report;
        } catch(SQLException e) {System.out.println(e);}

        return new SummaryReport();
    }

    /**
     * Finds and returns landlord.
     * @param {int} id
     * @return Landlord with id equal to parameter.
     */
    public Landlord getLandlord(int id) {
        try {
            Statement stmt = dbConnect.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM landlords WHERE ID = \"" + id + "\""); 
            // int userID, String name, String username, String password, String email, String type
            Landlord returned = new Landlord(result.getInt("ID"), result.getString("Name"), 
            result.getString("Username"), result.getString("Password"), result.getString("Email"), "landlord");

            return returned;
        } catch(Exception e) {}

        return new Landlord();
    }

    /**
     * Finds and returns Property.
     * @param {int} propertyID
     * @return Property with id equal to parameter.
     */
    public Property getProperty(int propertyID){

        for(Property p : properties){
            if(p.getID() == propertyID){
                return p;
            }
        }
        return null;
    }

    /**
     * Finds the email of a landlord from their ID.
     * @param {int} id ID of landlord.
     * @return Email of the landlord.
     */
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

    /**
     * Finds a Landlord from their ID.
     * @param {int} id ID of landlord.
     * @return Landlord with that id.
     */
    public Landlord lookupLandlord(int id){
        for(Landlord l : landlords){
            if(l.getUserID() == id){
                return l;
            }
        }
        return new Landlord(1, "d", "d", "d", "d", "landlord");
    }


    //----------------------------------
    // Getters
    //----------------------------------
    
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

    public ArrayList<LocalDate> getListingDates() {
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

    public ArrayList<LocalDate> getRentedDates() {
        return rentedDates;
    }
}
