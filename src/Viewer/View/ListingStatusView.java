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

import javax.swing.*;
import java.util.ArrayList;

/**
 * A GUI interface class that appears when a Manager
 * tries to change a listings status.
 */
public class ListingStatusView extends javax.swing.JFrame {
    private ManagerController mc;
    private String currStatus;
    private String currID;

    public ListingStatusView(String status, String ID) {
        currStatus = status;
        currID = ID;
    }

    public void setMc(ManagerController mc) {
        this.mc = mc;
    }

    public void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        currentStatusLabel = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        changeStatusButton = new javax.swing.JButton();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        jLabel1.setText("This property's status is currently: ");

        currentStatusLabel.setText(currStatus);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "rented", "listed", "unlisted", "suspended" }));

        changeStatusButton.setText("Change Status");
        changeStatusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeStatusButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(currentStatusLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(152, Short.MAX_VALUE)
                .addComponent(changeStatusButton)
                .addGap(145, 145, 145))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(currentStatusLabel))
                .addGap(97, 97, 97)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addComponent(changeStatusButton)
                .addGap(44, 44, 44))
        );

        pack();
    }// </editor-fold>                        

    private void changeStatusButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                   
        // TODO add your handling code here:
        mc.updateListingState(jComboBox1.getSelectedItem().toString(), currID);
        currentStatusLabel.setText(jComboBox1.getSelectedItem().toString());
        JOptionPane.showMessageDialog(null, "Success!");
    }                                                  


    // Variables declaration - do not modify                     
    private javax.swing.JButton changeStatusButton;
    private javax.swing.JLabel currentStatusLabel;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration                   
}
