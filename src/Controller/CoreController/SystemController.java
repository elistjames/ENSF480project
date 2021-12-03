package Controller.CoreController;

import Controller.UserController.UserController;
import Database.Database;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.time.LocalDate;

public class SystemController {
    static Database db;
    UserController currentController;
    static Date currentDate;

    public static void main(String[] args){
        //db.initializeConnection();
        //db.pullAll();
        //db.updateListingDates(currentDate);
        JFrame frame = new JFrame("ENSF480 Application");
    }

    public SystemController(){
        db = Database.getOnlyInstance();
        currentDate = Date.valueOf(LocalDate.now());
    }
}
