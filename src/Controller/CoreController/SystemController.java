package Controller.CoreController;

import Controller.UserController.RenterController;
import Controller.UserController.UserController;
import Database.Database;
import Model.User.Renter;
import Model.User.User;
import Viewer.Startup.StartPage;
import Viewer.View.RenterView;

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
                                sc.currentController = new RenterController((Renter)current, new RenterView());

                            }
                            else if(current.getType().equals("landlord")){

                            }
                            else if(current.getType().equals("manager")){

                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "invalid login");
                        }
                    }
                });
                sc.startPage.getjButton2().addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {

                    }
                });
                sc.startPage.getjButton3().addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        sc.db.pushAll();
                        System.exit(0);
                    }
                });
            }
        });



    }

    public SystemController(){

    }

}
