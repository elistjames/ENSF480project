/*
 * Author(s): Eli, Luke, Manjot
 * Documented by: Ryan Sommerville
 * Date Created: Dec 3, 2021
 * Last Edited: Dec 6, 2021
 */



package Controller.CoreController;

import Controller.UserController.LandlordController;
import Controller.UserController.ManagerController;
import Controller.UserController.RenterController;
import Controller.UserController.UserController;
import Database.Database;
import Model.User.Landlord;
import Model.User.Manager;
import Model.User.Renter;
import Model.User.User;
import Viewer.Startup.StartPage;
import Viewer.View.LandlordPage;
import Viewer.View.ManagerView;
import Viewer.View.RenterView;

import javax.swing.*;
import java.sql.Date;
import java.time.LocalDate;

/**
 * This class is the main controller for the ENSF480project for group 23 in Fall 2021.
 * It contains the main function to start the program and the essential objects to link
 * the View package and the Model package and handles the communication between the two.
 */
public class SystemController {

	//------------------------------------------------------
	// Member Variables - Fields
	//------------------------------------------------------
	
	/**
	 * An object that retrieves data from a MySQL Database
     * and stores it.
	 */
    public Database db; 
    
    /**
     * An object that stores the current User object and acts
     * as a go-between between the User and this page.
     */
    UserController currentController;
    
    /**
     * The current date, from when the program was booted up.
     */
    Date currentDate;
    
    /**
     * An object that holds a GUI interface that is shown when the project is
     * first booted up.
     */
    StartPage startPage;
    
    /**
     * An object that holds the GUI interface for the current Renter
     */
    RenterView renterPage;
    
    /**
     * Object that holds the GUI interface for the current Landlord
     */
    LandlordPage landlordPage;
    
    /**
     * Object that holds the GUI interface for the current Manager
     */
    ManagerView managerView;
    
    //-------------------------------------------------------
    // Main function
    //-------------------------------------------------------
    /**
     * The main function for the ENSF480project for group 23 in Fall 2021.
     * Initializes the SystemController, Database, currentDate, and runs the initial routine
     * to ask for a username and password from the user. Then keeps the program running by
     * waiting for and responding to the users input.
     */

    public static void main(String[] args){
        SystemController sc = new SystemController();
        sc.db = Database.getOnlyInstance(); //Get instance of Database, generate one 
        									//if it doesn't already exist
        sc.currentDate = Date.valueOf(LocalDate.now()); //Get current data
        
        //Downloading data from database and updating data
        sc.db.initializeConnection();
        sc.db.pullAll();
        sc.db.updateListingDates(sc.currentDate);
        
        //Runs the following function after all previous events have been completed.
        java.awt.EventQueue.invokeLater(() -> {
            //Open StartPage
            sc.startPage = new StartPage();
            sc.startPage.setLocationRelativeTo(null);
            sc.startPage.setVisible(true);

            //Response to Pressing Login on StartPage
            sc.startPage.getjButton1().addActionListener(evt -> {
                //Get Username and Password
                String usernameIn = sc.startPage.getjTextField1().getText();
                String passwordIn = new String(sc.startPage.getjPasswordField1().getPassword());

                //Validates Username and Password and finds the User they correspond to
                if(sc.db.validateLogin(usernameIn, passwordIn)){
                    User current = sc.db.getCurrentUser(usernameIn, passwordIn);
                    switch (current.getType()) {
                        case "renter" -> {
                            sc.startPage.setVisible(false);
                            sc.renterPage = new RenterView();
                            sc.currentController = new RenterController((Renter) current, sc.renterPage);
                        }
                        case "landlord" -> {
                            sc.startPage.setVisible(false);
                            sc.landlordPage = new LandlordPage();
                            sc.currentController = new LandlordController((Landlord) current, sc.landlordPage);
                        }
                        case "manager" -> {
                            sc.startPage.setVisible(false);
                            sc.managerView = new ManagerView();
                            sc.currentController = new ManagerController((Manager) current, sc.managerView);
                            System.out.println("you made it!");
                        }
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null, "invalid login");
                }
            });

            //Responds to Pressing Skip on the StartPage
            sc.startPage.getjButton2().addActionListener(evt -> {
                sc.startPage.setVisible(false);
                sc.renterPage = new RenterView();
                //Makes the User a default renter
                sc.currentController = new RenterController(new Renter(0, "none", "none",
                        "none", "none", "renter"), sc.renterPage);
            });

            //Responds to pressing Exit on the StartPage
            sc.startPage.getjButton3().addActionListener(evt -> {
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?",
                        "Confirmation:", JOptionPane.YES_NO_OPTION);
                if(choice == JOptionPane.YES_OPTION){
                    sc.db.pushAll();
                    System.exit(0);
                }

            });

        });



    }

    /**
     * A default constructor.
     */
    public SystemController(){

    }

}
