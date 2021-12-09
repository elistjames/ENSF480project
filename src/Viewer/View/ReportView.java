/**
 * Author(s):
 * Editted by:
 * Documented by: Ryan Sommerville
 * Date created:
 * Last Editted:
 */

package Viewer.View;

import Controller.UserController.ManagerController;
import Controller.UserController.RenterController;
import Model.Lising.Listing;
import Model.User.SummaryReport;
import Model.Lising.Property;

import javax.swing.*;

import java.util.Date;  
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * A GUI interface class that shows the periodic report
 * that Managers can view.
 */
public class ReportView extends javax.swing.JFrame {

    private ManagerController mc;
    /**
     * Creates new form ReportView
     */
    public ReportView() {
        // initComponents();
    }

    public void setMc(ManagerController mc) {
        this.mc = mc;
    }

                         
    public void initComponents() {
        startDateField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        SearchButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        rentedList = new javax.swing.JList<>();
        totalListed = new javax.swing.JLabel();
        totalRented = new javax.swing.JLabel();
        totalActive = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        endDateField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Desired Start Date (yyyy-mm-dd)");

        SearchButton.setText("Search");
        SearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("Total houses listed");

        jLabel3.setText("Total houses rented");

        jLabel4.setText("Total houses active");

        jLabel5.setText("List of houses rented");

        rentedList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(rentedList);

        // totalListed.setText("jLabel6");

        // totalRented.setText("jLabel7");

        // totalActive.setText("jLabel8");

        jLabel6.setText("Desired End Date (yyyy-mm-dd)");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6))
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(totalActive)
                            .addComponent(totalRented)
                            .addComponent(totalListed)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(endDateField, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(startDateField, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(36, 36, 36)
                                .addComponent(SearchButton)))))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(startDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(endDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(SearchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(totalListed))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(totalRented))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(totalActive))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                        

    private void SearchButtonActionPerformed(java.awt.event.ActionEvent evt) {               
        // Check to make sure start and end date are valid
        // End date can't be bigger than start date, and end date can't be in the future
        try {
            Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startDateField.getText());
            Date end = new SimpleDateFormat("yyyy-MM-dd").parse(endDateField.getText());
            Date today = new Date();

            if(end.after(today) || start.after(end)) {
                throw new Exception("Invalid date");
            }
            
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Please input a valid date range");
            return;
        }

        
        SummaryReport report = mc.getReport(startDateField.getText(), endDateField.getText());

        totalListed.setText("" + report.getHousesListed());
        totalActive.setText("" + report.getActiveListings());
        totalRented.setText("" + report.getHousesRented());
        
        ArrayList<Property> rentedProperties = report.getRentedProperties();
        if(rentedProperties != null) {
            DefaultListModel<String> model = new DefaultListModel<>();
            for(var rented: rentedProperties) {
                model.addElement(mc.db.getLandlord(rented.getLandlordID()).getName() + " " + rented.getID() + " " + rented.getAddress());
            }
            rentedList.setModel(model);
        } 

    }                                        
                                       



    // Variables declaration - do not modify                     
    private javax.swing.JButton SearchButton;
    private javax.swing.JTextField endDateField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<String> rentedList;
    private javax.swing.JTextField startDateField;
    private javax.swing.JLabel totalActive;
    private javax.swing.JLabel totalListed;
    private javax.swing.JLabel totalRented;
    // End of variables declaration                
}
