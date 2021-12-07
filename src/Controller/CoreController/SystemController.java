/**
 * Author(s):
 * Editted by:
 * Documented by: Ryan Sommerville
 * Date Created:
 * Last Editted:
 */



package Controller.CoreController;

import Controller.UserController.ManagerController;
import Controller.UserController.RenterController;
import Controller.UserController.UserController;
import Database.Database;
import Model.User.Manager;
import Controller.UserController.LandlordController;
import Controller.UserController.RenterController;
import Controller.UserController.UserController;
import Database.Database;
import Model.User.Landlord;

import Model.User.Renter;
import Model.User.User;
import Viewer.Startup.StartPage;
import Viewer.View.LandlordPage;
import Viewer.View.RenterView;
import Viewer.View.ManagerView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.time.LocalDate;

/**
 * @author 
 * This class is the main controller for the ENSF480project for group 23 in Fall 2021.
 * It contains the main function to start the program and the essential objects to link
 * the View package and the Model package and handles the communication between the two.
 */
public class SystemController {

	//------------------------------------------------------
	// Member Variables - Fields
	//------------------------------------------------------
	
    public Database db; //An object that retrieves data from a MySQL Database
    					//and stores it.
    UserController currentController; //An object that stores the current User object and acts
    								  //as a go between between the User and this page.
    Date currentDate; // The current date, from when the program was booted up.
    StartPage startPage; // An object that holds a GUI interface that is shown when the project is
    					 // first booted up.
    RenterView renterPage; //An object that holds the GUI interface for the Renter
    LandlordPage landlordPage;
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
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                sc.startPage = new StartPage();
                sc.startPage.setLocationRelativeTo(null);
                sc.startPage.setVisible(true);
                sc.startPage.getjButton1().addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        String usernameIn = sc.startPage.getjTextField1().getText();
                        String passwordIn = new String(sc.startPage.getjPasswordField1().getPassword());
                        if(sc.db.validateLogin(usernameIn, passwordIn)){
                            User current = sc.db.getCurrentUser(usernameIn, passwordIn);
                            if(current.getType().equals("renter")){
                                sc.startPage.setVisible(false);
                                sc.renterPage = new RenterView();
                                sc.currentController = new RenterController((Renter)current, sc.renterPage);
                            }
                            else if(current.getType().equals("landlord")){
                                sc.startPage.setVisible(false);
                                sc.landlordPage = new LandlordPage();
                                sc.currentController = new LandlordController((Landlord)current, sc.landlordPage);
                            }
                            else if(current.getType().equals("manager")){
                                sc.startPage.setVisible(false);
                                sc.managerView = new ManagerView();
                                sc.currentController = new ManagerController((Manager)current, sc.managerView);
                                System.out.println("you made it!");
                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "invalid login");
                        }
                    }
                });
                sc.startPage.getjButton2().addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        sc.startPage.setVisible(false);
                        sc.renterPage = new RenterView();
                        sc.currentController = new RenterController(new Renter(0, "none", "none",
                                "none", "none", "renter"), sc.renterPage);
                    }
                });
                sc.startPage.getjButton3().addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?",
                                "Confirmation:", JOptionPane.YES_NO_OPTION);
                        if(choice == JOptionPane.YES_OPTION){
                            sc.db.pushAll();
                            System.exit(0);
                        }

                    }
                });

            }
        });



    }

    public SystemController(){

    }

}
