package Database;
import java.sql.*;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

import Model.User.*;

import Model.Lising.*;

import static java.time.temporal.ChronoUnit.DAYS;


public class Database {
    private static Database onlyInstance;

    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Renter> renters = new ArrayList<>();
    private ArrayList<Landlord> landlords = new ArrayList<>();
    private ArrayList<Property> properties = new ArrayList<>();
    private ArrayList<Manager> managers = new ArrayList<>();
    private ArrayList<ListingFee> fees = new ArrayList<>();
    private ArrayList<Listing> listings = new ArrayList<>();
    private ArrayList<LocalDate> listingDates = new ArrayList<>();
    private ArrayList<Property> rentedProperties = new ArrayList<>();
    private ArrayList<Date> rentedDates = new ArrayList<>();
    private ArrayList<SearchCriteria> searches = new ArrayList<>();
    private ArrayList<Integer> rentersToNotify = new ArrayList<>();
    private ArrayList<Email> emails = new ArrayList<>();
    private Listing[] l;


    private ArrayList<Listing> suspendedListings = new ArrayList<>();

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
            String DBURL = "jdbc:sqlite:ensf480.db";
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
            if(getCurrentSearch(r).getType().equals(p.getType())||getCurrentSearch(r).getType().equals("N/A")){
                if(getCurrentSearch(r).getN_bedrooms() == p.getBedrooms() || getCurrentSearch(r).getN_bedrooms() == -1){
                    if(getCurrentSearch(r).getN_bathrooms() == p.getBathrooms() || getCurrentSearch(r).getN_bathrooms() == -1){
                        if(getCurrentSearch(r).isFurnished() == p.isFurnished() || getCurrentSearch(r).isFurnished() == -1){
                            if(getCurrentSearch(r).getCityQuadrant().equals(p.getCityQuadrant())||getCurrentSearch(r).getCityQuadrant().equals("N/A")){
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

    public SearchCriteria getCurrentSearch(Renter current){
        for(SearchCriteria sc : searches){
            if(sc.getRenterID() == current.getUserID()){
                return sc;
            }
        }
        return null;
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
            result = myStmt.executeQuery("SELECT * FROM renters"); //execute statement select all from Chair table
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
            result = myStmt.executeQuery("SELECT * FROM landlords"); //execute statement select all from Chair table
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
            result = myStmt.executeQuery("SELECT * FROM properties"); //execute statement select all from Chair table
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
            result = myStmt.executeQuery("SELECT * FROM managers"); //execute statement select all from Chair table
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
            result = myStmt.executeQuery("SELECT * FROM fees"); //execute statement select all from Chair table
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
            result = myStmt.executeQuery("SELECT * FROM listings"); //execute statement select all from Chair table
            while(result.next()) { //run while next row exists
                this.listings.add(new Listing(this.getProperty(result.getInt("PropertyID")),
                        result.getInt("Duration"), result.getString("State"),
                        LocalDate.parse(result.getString("StartDate")), result.getInt("CurrentDay")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

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
            result = myStmt.executeQuery("SELECT * FROM suspended"); //execute statement select all from Chair table
            while(result.next()) { //run while next row exists
                this.suspendedListings.add(new Listing(this.getProperty(result.getInt("PropertyID")),
                        result.getInt("Duration"), result.getString("State"),
                        LocalDate.parse(result.getString("StartDate")), result.getInt("CurrentDay")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void pullRTN(){
        ResultSet result; //create new ResultSet object
        try {
            Statement myStmt = dbConnect.createStatement(); //create new statement
            result = myStmt.executeQuery("SELECT * FROM rtn"); //execute statement select all from Chair table
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
            result = myStmt.executeQuery("SELECT * FROM searches"); //execute statement select all from Chair table
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
            result = myStmt.executeQuery("SELECT * FROM listingdates");
            while(result.next()) { //run while next row exists
                this.listingDates.add(LocalDate.parse(result.getString("DateOfListing")));
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
            result = myStmt.executeQuery("SELECT * FROM emails");
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
    private void pushFees(){
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
                System.out.println(l[i].getCurrentDay());
                myStmt.setInt(5, l[i].getCurrentDay());

                myStmt.execute();
                myStmt.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

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

    private void pushSearchCriterias(){
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

    private void pushEmails(){
        try{
            Statement statement = dbConnect.createStatement();
            statement.executeUpdate("DELETE FROM emails");
            String query;

            for(Email e : emails){
                query = "INSERT INTO emails (From,To,Date,Subject,Message) VALUES (?,?,?,?,?)";
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
        this.pushEmails();
        this.pushSuspendedListings();

        for(SearchCriteria sc : searches){
            System.out.println(sc.getType());
            System.out.println(sc.getN_bedrooms());
            System.out.println(sc.getN_bathrooms());
            System.out.println(sc.isFurnished());
            System.out.println(sc.getCityQuadrant());
        }

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
        this.pullEmails();
        this.pullSuspendedListings();

        for(SearchCriteria sc : searches){
            System.out.println(sc.getType());
            System.out.println(sc.getN_bedrooms());
            System.out.println(sc.getN_bathrooms());
            System.out.println(sc.isFurnished());
            System.out.println(sc.getCityQuadrant());
        }
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

    public Landlord lookupLandlord(int id){
        for(Landlord l : landlords){
            if(l.getUserID() == id){
                return l;
            }
        }
        return new Landlord(1, "d", "d", "d", "d", "landlord");
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

    public ArrayList<Date> getRentedDates() {
        return rentedDates;
    }
}
