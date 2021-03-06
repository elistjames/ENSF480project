/*
 * Author(s): Luke
 * Documented by: Ryan Sommerville
 * Date created: Dec 5, 2021
 * Last Edited: Dec 6, 2021
 */

package Viewer.View;

import Controller.UserController.ManagerController;
import Model.Listing.Listing;

import javax.swing.*;

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

    /**
     * This method is called to initialize the Components.
     */
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
                changeStatusButtonActionPerformed();
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
    }

    private void changeStatusButtonActionPerformed() {
        String selected = String.valueOf(jComboBox1.getSelectedItem());
        String st = "suspended";
        for(Listing l: mc.db.getListings()){
            if(Integer.parseInt(currID) == l.getProperty().getID()){
                st = "listed";
                mc.changeListingState(l, selected);
                break;
            }
        }
        if(st.equals("suspended")){
            for(Listing l: mc.db.getSuspendedListings()){
                if(Integer.parseInt(currID) == l.getProperty().getID()){
                    if(selected.equals("rented")){
                        mc.changeListingState(l, "listed");
                    }
                    mc.changeListingState(l, selected);
                    break;
                }
            }
        }
        //mc.updateListingState(selected, currID);
        currentStatusLabel.setText(selected);
        JOptionPane.showMessageDialog(null, "Success!");
    }

    public void updateComboBox(String currentState){
        switch (currentState) {
            case "rented" -> jComboBox1.setSelectedIndex(0);
            case "unlisted" -> jComboBox1.setSelectedIndex(2);
            case "suspended" -> jComboBox1.setSelectedIndex(3);
            default -> jComboBox1.setSelectedIndex(1);
        }
    }


    // Variables declaration - do not modify                     
    private javax.swing.JButton changeStatusButton;
    private javax.swing.JLabel currentStatusLabel;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration                   
}
