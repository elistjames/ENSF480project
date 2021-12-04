package Controller.CoreController;

import Controller.UserController.UserController;
import Database.Database;
import Viewer.Startup.StartPage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.time.LocalDate;

public class SystemController {
    public static Database db;
    UserController currentController;
    static Date currentDate;

    public static void main(String[] args){
        //db.initializeConnection();
        //db.pullAll();
        //db.updateListingDates(currentDate);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StartPage().setVisible(true);
            }
        });
    }

    public SystemController(){
        db = Database.getOnlyInstance();
        currentDate = Date.valueOf(LocalDate.now());
    }
}
