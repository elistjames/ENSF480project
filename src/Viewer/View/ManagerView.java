/**
 * Author(s): Luke
 * Editted by:
 * Documented by: Ryan Sommerville
 * Date created:
 * Last Editted:
 */

package Viewer.View;

import Controller.UserController.ManagerController;
import Controller.UserController.RenterController;
import Model.Lising.Listing;

import javax.swing.*;
import java.util.ArrayList;

/**
 * A GUI interface class for Managers that gives options to show
 * available information and change certain info.
 */
public class ManagerView extends javax.swing.JFrame {

    private ManagerController mc;
    // private boolean modifyProperty;

    public void setMc(ManagerController mc, JList jlist) {
        this.mc = mc;
        jList1 = jlist;
    }

    public ManagerView() {
        // initComponents();
    }

    public void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        // jList1 = new javax.swing.JList<>();
        renterButton = new javax.swing.JButton();
        landlordButton = new javax.swing.JButton();
        propertyButton = new javax.swing.JButton();
        updatePropertyButton = new javax.swing.JButton();
        changeFeeButton = new javax.swing.JButton();
        summaryButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();
        updatePropertyButton.setVisible(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Admin Dashboard");

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        renterButton.setText("View All Renters\n");
        renterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renterButtonActionPerformed(evt);
            }
        });

        landlordButton.setText("View All Landlords");
        landlordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                landlordButtonActionPerformed(evt);
            }
        });

        propertyButton.setText("View All Properties");
        propertyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                propertyButtonActionPerformed(evt);
            }
        });

        updatePropertyButton.setText("Update Property Status");
        updatePropertyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatePropertyButtonActionPerformed(evt);
            }
        });

        changeFeeButton.setText("Change Fee");
        changeFeeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeFeeButtonActionPerformed(evt);
            }
        });

        summaryButton.setText("Get Periodical Summary");
        summaryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                summaryButtonActionPerformed(evt);
            }
        });

        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 629, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(224, 224, 224)
                                .addComponent(summaryButton)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(propertyButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(landlordButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(renterButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(updatePropertyButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(changeFeeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(exitButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(changeFeeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(renterButton)
                        .addGap(44, 44, 44)
                        .addComponent(landlordButton)
                        .addGap(42, 42, 42)
                        .addComponent(propertyButton)
                        .addGap(48, 48, 48)
                        .addComponent(updatePropertyButton)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(summaryButton))
                    .addComponent(exitButton, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                .addContainerGap())
        );
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 29, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                        

    private void renterButtonActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // Pass in jList element to update its model
        updatePropertyButton.setVisible(false);
        mc.viewRenters(jList1);
        
        System.out.println("Renter button pressed");
    }                                            

    private void landlordButtonActionPerformed(java.awt.event.ActionEvent evt) {                                               
        // TODO add your handling code here:
        updatePropertyButton.setVisible(false);
        mc.viewLandlords(jList1);
        System.out.println("Landlord Button pressed");
    }                                              

    private void propertyButtonActionPerformed(java.awt.event.ActionEvent evt) {                                               
        // TODO add your handling code here:
        updatePropertyButton.setVisible(true);
        mc.viewProperties(jList1);
        System.out.println("Property Button pressed");
        
    }

    private void updatePropertyButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                     
        // Popup small window that allows you to view the current status of a property and change it 
        // (Dropdown menu)
        String selected = jList1.getSelectedValue();
        System.out.println(selected);
        if(selected == null) {
            JOptionPane.showMessageDialog(null, "Please select a property from the list.");
        } else {
            mc.openListingStateView(selected, jList1);
        }
    }    
    
    private void changeFeeButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                
        // TODO add your handling code here:
        mc.openFeeView();
    }          

    private void summaryButtonActionPerformed(java.awt.event.ActionEvent evt) {                                              
        // TODO add your handling code here:
        mc.openReportView();
    }                 

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
        int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?",
        "Confirmation:", JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.YES_OPTION){
            mc.db.pushAll();
            System.exit(0);
        }

    }                                          

    
    
    // Variables declaration - do not modify                     
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton landlordButton;
    private javax.swing.JButton propertyButton;
    private javax.swing.JButton renterButton;
    private javax.swing.JButton updatePropertyButton;
    private javax.swing.JButton changeFeeButton;
    private javax.swing.JButton summaryButton;
    private javax.swing.JButton exitButton;
}
