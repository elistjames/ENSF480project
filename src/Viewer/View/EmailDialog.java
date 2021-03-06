/*
 * Author(s): Eli St. James
 * Documented by: Ryan Sommerville
 * Date created: Dec 5, 2021
 * Last Edited: Dec 6, 2021
 */

package Viewer.View;

import Controller.UserController.LandlordController;
import Controller.UserController.RenterController;
import Model.User.Email;

import java.time.LocalDate;

/**
 * A Dialog box that shows an email that a Renter or Landlord has received.
 */
public class EmailDialog extends javax.swing.JDialog {
    RenterController rc;
    LandlordController lc;

    /**
     * Creates new form EmailDialog
     */
    public EmailDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
    }

    public void setRc(RenterController rc) {
        this.rc = rc;
    }
    public void setLc(LandlordController lc) {this.lc = lc; }

    /**
     * This method is called to initialize the Components.
     */
    public void initComponents(Email e) {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        fromTextPane = new javax.swing.JTextPane();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        messageTextPane = new javax.swing.JTextPane();
        replyButton = new javax.swing.JButton();
        ignoreButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        fromTextPane.setEditable(false);
        fromTextPane.setText(e.getFromEmail());
        fromTextPane.setBorder(null);
        jScrollPane1.setViewportView(fromTextPane);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("From:");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Subject:");

        jTextPane1.setEditable(false);

        jTextPane1.setText(e.getSubject());


        jScrollPane2.setViewportView(jTextPane1);

        messageTextPane.setText(e.getMessage());

        messageTextPane.setEditable(false);

        jScrollPane3.setViewportView(messageTextPane);

        replyButton.setText("Reply");
        replyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                replyButtonActionPerformed();
            }
        });

        ignoreButton.setText("Ignore");
        ignoreButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ignoreButtonActionPerformed();
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE))
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                        .addComponent(jScrollPane2)))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addGap(0, 295, Short.MAX_VALUE)
                                                .addComponent(replyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(15, 15, 15)
                                                .addComponent(ignoreButton, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(replyButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(ignoreButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    private void replyButtonActionPerformed() {
        if(rc == null){
            lc.email = new Email(lc.recieved.getToEmail(), lc.recieved.getFromEmail());
            lc.email.setDate(LocalDate.now());
            lc.ep = new EmailPage();
            lc.ep.setLc(lc);
            lc.ep.initComponents("reply");
            lc.ep.setLocationRelativeTo(null);
            this.setVisible(false);
            lc.ep.setVisible(true);

        }
        else{
            rc.email = new Email(rc.recieved.getToEmail(), rc.recieved.getFromEmail());
            rc.email.setDate(LocalDate.now());
            rc.ep = new EmailPage();
            rc.ep.setRc(rc);
            rc.ep.initComponents("reply");
            rc.ep.setLocationRelativeTo(null);
            this.setVisible(false);
            rc.ep.setVisible(true);

        }
    }

    private void ignoreButtonActionPerformed() {
        if(rc == null){
            this.setVisible(false);
            lc.lp.setVisible(true);
            if(lc.db.emailNotSeen(lc.current.getEmail())){
                lc.checkEmails();
            }
        }
        else{
            this.setVisible(false);
            rc.rv.setVisible(true);
            if(rc.db.emailNotSeen(rc.current.getEmail())){
                rc.checkEmails();
            }
        }
    }

    // Variables declaration
    private javax.swing.JTextPane fromTextPane;
    private javax.swing.JButton ignoreButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane messageTextPane;
    private javax.swing.JButton replyButton;
    // End of variables declaration
}