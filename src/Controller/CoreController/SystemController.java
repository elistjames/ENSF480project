package Controller.CoreController;

import Controller.UserController.ManagerController;
import Controller.UserController.RenterController;
import Controller.UserController.UserController;
import Database.Database;
import Model.User.Manager;
import Model.User.Renter;
import Model.User.User;
import Viewer.Startup.StartPage;
import Viewer.View.RenterView;
import Viewer.View.ManagerView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.time.LocalDate;

public class SystemController {
    public Database db;
    UserController currentController;
    Date currentDate;
    StartPage startPage;
    RenterView renterPage;
    ManagerView managerView;


    public static void main(String[] args){
        SystemController sc = new SystemController();
        sc.db = Database.getOnlyInstance();
        sc.currentDate = Date.valueOf(LocalDate.now());
        sc.db.initializeConnection();
        sc.db.pullAll();
        sc.db.updateListingDates(sc.currentDate);
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
