/*
 * Author(s): Eli St. James
 * Documented by: Ryan Sommerville
 * Date created: Dec 4, 2021
 * Last Edited: Dec 6, 2021
 */

package Viewer.View;

import Controller.UserController.RenterController;
import Model.Listing.Listing;

import javax.swing.*;

/**
 * A GUI interface that shows the main view for renters.
 */
public class RenterView extends javax.swing.JFrame {
    private RenterController rc;


    public void setRc(RenterController rc) {
        this.rc = rc;
    }

    /**
     * Creates new form RenterView
     */
    public RenterView() {

    }

    /**
     * This method is called to initialize the Components.
     */
    public void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        renterPanel = new javax.swing.JPanel();
        accountButton = new javax.swing.JButton();
        searchButton = new javax.swing.JButton();
        furnishedOption = new javax.swing.JComboBox<>();
        nBathOption = new javax.swing.JComboBox<>();
        nBedOption = new javax.swing.JComboBox<>();
        typeOption = new javax.swing.JComboBox<>();
        listingView = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        exitButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        quadrantOption = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        contactButton = new javax.swing.JButton();
        applyButton = new javax.swing.JButton();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        accountButton.setText("Register / Unregister");
        accountButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rc.accountButtonActionPerformed();
            }
        });

        searchButton.setText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed();
            }
        });

        furnishedOption.setMaximumRowCount(3);
        furnishedOption.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "N/A", "Yes", "No" }));


        nBathOption.setMaximumRowCount(7);
        nBathOption.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "N/A", "1", "2", "3", "4", "5", "6" }));


        nBedOption.setMaximumRowCount(6);
        nBedOption.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "N/A", "1", "2", "3", "4", "5" }));


        typeOption.setMaximumRowCount(6);
        typeOption.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "N/A", "Attached House", "Detached House", "Apartment", "Town Home" }));

        DefaultListModel<String> model = new DefaultListModel<>();

        for(Listing l : rc.db.getListings()){
            if(l.getProperty().getType().equals(rc.db.getCurrentSearch(rc.current.getUserID()).getType())||rc.db.getCurrentSearch(rc.current.getUserID()).getType().equals("N/A")){
                if(l.getProperty().getBedrooms() == rc.db.getCurrentSearch(rc.current.getUserID()).getN_bedrooms()||rc.db.getCurrentSearch(rc.current.getUserID()).getN_bedrooms() == -1){
                    if(l.getProperty().getBathrooms() == rc.db.getCurrentSearch(rc.current.getUserID()).getN_bathrooms()||rc.db.getCurrentSearch(rc.current.getUserID()).getN_bathrooms() == -1){
                        if(l.getProperty().isFurnished() == rc.db.getCurrentSearch(rc.current.getUserID()).isFurnished()||rc.db.getCurrentSearch(rc.current.getUserID()).isFurnished() == -1){
                            if(l.getProperty().getCityQuadrant().equals(rc.db.getCurrentSearch(rc.current.getUserID()).getCityQuadrant())||rc.db.getCurrentSearch(rc.current.getUserID()).getCityQuadrant().equals("N/A")){
                                String tmp = String.format("Address: %1$-25s Posted by: %2$-20s Posting expires in %3$3d days",
                                        l.getProperty().getAddress(), rc.db.lookupLandlord(l.getProperty().getLandlordID()).getName(),
                                        l.getDuration()-l.getCurrentDay());
                                System.out.println(tmp);
                                model.addElement(String.format("Address: %1$-25s Posted by: %2$-20s Posting expires in %3$3d days",
                                        l.getProperty().getAddress(), rc.db.lookupLandlord(l.getProperty().getLandlordID()).getName(),
                                        l.getDuration()-l.getCurrentDay()));
                            }
                        }
                    }
                }
            }
        }
        jList1.setModel(model);
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setVisibleRowCount(10);
        listingView.setViewportView(jList1);

        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed();
            }
        });

        jLabel1.setText("Type");

        jLabel2.setText("Number of Bedrooms");

        jLabel3.setText("Number of Batrooms");

        jLabel4.setText("Furnished");

        quadrantOption.setMaximumRowCount(5);
        quadrantOption.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "N/A", "NW", "NE", "SW", "SE" }));


        jLabel5.setText("City Quadrant");

        contactButton.setText("Contact Landlord");
        contactButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rc.contactButtonActionPerformed();
            }
        });

        applyButton.setText("Apply");
        applyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyButtonActionPerformed();
            }
        });

        javax.swing.GroupLayout renterPanelLayout = new javax.swing.GroupLayout(renterPanel);
        renterPanel.setLayout(renterPanelLayout);
        renterPanelLayout.setHorizontalGroup(
                renterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(renterPanelLayout.createSequentialGroup()
                                .addGroup(renterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(renterPanelLayout.createSequentialGroup()
                                                .addGap(70, 70, 70)
                                                .addComponent(listingView, javax.swing.GroupLayout.PREFERRED_SIZE, 679, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(renterPanelLayout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addGroup(renterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(renterPanelLayout.createSequentialGroup()
                                                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addGap(36, 36, 36))
                                                        .addGroup(renterPanelLayout.createSequentialGroup()
                                                                .addComponent(typeOption, 0, 139, Short.MAX_VALUE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                                                .addGap(10, 10, 10)
                                                .addGroup(renterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(nBedOption, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel2))
                                                .addGap(34, 34, 34)
                                                .addGroup(renterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(nBathOption, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel3))
                                                .addGap(18, 18, 18)
                                                .addGroup(renterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(furnishedOption, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(28, 28, 28)
                                                .addGroup(renterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(quadrantOption, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE))
                                                .addGap(79, 79, 79)))
                                .addGroup(renterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(accountButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(exitButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(contactButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(searchButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(applyButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        renterPanelLayout.setVerticalGroup(
                renterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, renterPanelLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(renterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(renterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(typeOption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(nBedOption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(nBathOption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(furnishedOption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(quadrantOption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(applyButton))
                                .addGroup(renterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(renterPanelLayout.createSequentialGroup()
                                                .addGap(12, 12, 12)
                                                .addComponent(listingView, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(26, 26, 26))
                                        .addGroup(renterPanelLayout.createSequentialGroup()
                                                .addGap(40, 40, 40)
                                                .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(contactButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(159, 159, 159)
                                                .addComponent(accountButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap())))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(renterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(renterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    private void exitButtonActionPerformed() {
        int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?",
                "Confirmation:", JOptionPane.YES_NO_OPTION);
        if(choice == JOptionPane.YES_OPTION) {
            rc.db.pushAll();
            System.exit(0);
        }
    }

    private void searchButtonActionPerformed() {
        rc.searchListings();
    }

    private void applyButtonActionPerformed() {
        String tmpType = String.valueOf(typeOption.getSelectedItem());
        String tmpNbed = String.valueOf(nBedOption.getSelectedItem());
        String tmpNbath = String.valueOf(nBathOption.getSelectedItem());
        String tmpFurnished = String.valueOf(furnishedOption.getSelectedItem());
        String tmpCQ = String.valueOf(quadrantOption.getSelectedItem());
        rc.updateSearchCriteria(tmpType, tmpNbed, tmpNbath, tmpFurnished, tmpCQ);
    }

    public void updateCriteriaBoxes(String t, int nb1, int nb2, int f, String cq){
        System.out.println(t);
        System.out.println(nb1);
        System.out.println(nb2);
        System.out.println(f);
        System.out.println(cq);

        switch (t) {
            case "N/A" -> typeOption.setSelectedIndex(0);
            case "Attached House" -> typeOption.setSelectedIndex(1);
            case "Detached House" -> typeOption.setSelectedIndex(2);
            case "Apartment" -> typeOption.setSelectedIndex(3);
            default -> typeOption.setSelectedIndex(4);
        }

        if (nb1 == -1) {
            nBedOption.setSelectedIndex(0);
        } else {
            nBedOption.setSelectedIndex(nb1);
        }

        if (nb2 == -1) {
            nBathOption.setSelectedIndex(0);
        } else {
            nBathOption.setSelectedIndex(nb2);
        }

        switch (f) {
            case -1 -> furnishedOption.setSelectedIndex(0);
            case 1 -> furnishedOption.setSelectedIndex(1);
            default -> furnishedOption.setSelectedIndex(2);
        }

        switch (cq) {
            case "N/A" -> quadrantOption.setSelectedIndex(0);
            case "NW" -> quadrantOption.setSelectedIndex(1);
            case "NE" -> quadrantOption.setSelectedIndex(2);
            case "SW" -> quadrantOption.setSelectedIndex(3);
            default -> quadrantOption.setSelectedIndex(4);
        }
    }

    // Variables declaration - do not modify
    private javax.swing.JButton accountButton;
    private javax.swing.JButton applyButton;
    private javax.swing.JButton contactButton;
    private javax.swing.JButton exitButton;
    private javax.swing.JComboBox<String> furnishedOption;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JList<String> jList1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane listingView;
    private javax.swing.JComboBox<String> nBathOption;
    private javax.swing.JComboBox<String> nBedOption;
    private javax.swing.JComboBox<String> quadrantOption;
    private javax.swing.JPanel renterPanel;
    private javax.swing.JButton searchButton;
    private javax.swing.JComboBox<String> typeOption;
    // End of variables declaration


    public JLabel getjLabel1() {
        return jLabel1;
    }

    public JList<String> getjList1() {
        return jList1;
    }
}
