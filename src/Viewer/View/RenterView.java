package Viewer.View;

public class RenterView extends javax.swing.JFrame {

    /**
     * Creates new form RenterView
     */
    public RenterView() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

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

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        accountButton.setText("Register / Unregister");

        searchButton.setText("Search");

        furnishedOption.setMaximumRowCount(3);
        furnishedOption.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "N/A", "Yes", "No" }));

        nBathOption.setMaximumRowCount(7);
        nBathOption.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "N/A", "1", "2", "3", "4", "5", "6" }));

        nBedOption.setMaximumRowCount(6);
        nBedOption.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "N/A", "1", "2", "3", "4", "5" }));

        typeOption.setMaximumRowCount(6);
        typeOption.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "N/A", "Attatched House", "dettatched House", "Apartment", "Town Home" }));

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1 uhsgdhfgs", "Item 2", "Item 3", "Item 4", "Item 5", "asdasd", "asdasda", "asf", "d", "gs", "fgfg", "dfg", "dfg", "d", "ghd", "fh", "dh", "dh", "d", "hd", "h", "dfh", "dh", "d", "h", "dfh", "dfh", "dh", " ", "fgh", "h", "h", "gh", "h" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setVisibleRowCount(10);
        listingView.setViewportView(jList1);

        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
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

        javax.swing.GroupLayout renterPanelLayout = new javax.swing.GroupLayout(renterPanel);
        renterPanel.setLayout(renterPanelLayout);
        renterPanelLayout.setHorizontalGroup(
                renterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, renterPanelLayout.createSequentialGroup()
                                .addGroup(renterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                                                .addGap(79, 79, 79))
                                        .addGroup(renterPanelLayout.createSequentialGroup()
                                                .addGap(70, 70, 70)
                                                .addComponent(listingView, javax.swing.GroupLayout.PREFERRED_SIZE, 679, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(renterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(searchButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(accountButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(exitButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(contactButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        renterPanelLayout.setVerticalGroup(
                renterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, renterPanelLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(renterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(renterPanelLayout.createSequentialGroup()
                                                .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18))
                                        .addGroup(renterPanelLayout.createSequentialGroup()
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
                                                        .addComponent(quadrantOption, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(14, 14, 14)))
                                .addGroup(renterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(renterPanelLayout.createSequentialGroup()
                                                .addComponent(listingView, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(26, 26, 26))
                                        .addGroup(renterPanelLayout.createSequentialGroup()
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
    }// </editor-fold>

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    // Variables declaration - do not modify
    private javax.swing.JButton accountButton;
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
}
