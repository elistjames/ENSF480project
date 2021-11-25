package edu.ucalgary.ensf480;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


//Imports are listed in full to show what's being used
// could just

public class AppWindow {
    //Note: Typically the main method will be in a
    // separate class. As this is a simple one class
    // example it's all in the one class.
    public static void main(String[] args) { new AppWindow(); }
    public AppWindow() {
        JFrame guiFrame = new JFrame();

        //make sure the program exits when the frame closes
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setTitle("Example GUI");
        guiFrame.setSize(1200,800);
        //This will center the JFrame in the middle of the screen
        guiFrame.setLocationRelativeTo(null);
        //Options for the JComboBox
        String[] fruitOptions = {"Apple", "Apricot", "Banana" ,"Cherry", "Date", "Kiwi", "Orange", "Pear", "Strawberry"};
        //Options for the JList
        String[] vegOptions = {"Asparagus", "Beans", "Broccoli", "Cabbage" , "Carrot", "Celery", "Cucumber", "Leek",
                "Mushroom" , "Pepper", "Radish", "Shallot", "Spinach", "Swede" , "Turnip"};
        //The first JPanel contains a JLabel and JCombobox
        final JPanel comboPanel = new JPanel();
        JLabel comboLbl = new JLabel("Fruits:");
        JComboBox fruits = new JComboBox(fruitOptions);
        comboPanel.add(comboLbl);
        comboPanel.add(fruits);
        //Create the second JPanel. Add a JLabel and JList and
        // make use the JPanel is not visible.
        final JPanel listPanel = new JPanel();
        listPanel.setVisible(false);
        JLabel listLbl = new JLabel("Vegetables:");

        JButton login = new JButton( "Login");
        //The ActionListener class is used to handle the
        // event that happens when the user clicks the button.
        // As there is not a lot that needs to happen we can
        // define an anonymous inner class to make the code simpler.
        login.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent event) {
                //When the fruit of veg button is pressed
                // the setVisible value of the listPanel and
                // comboPanel is switched from true to
                // value or vice versa.
                listPanel.setVisible(!listPanel.isVisible());
                comboPanel.setVisible(!comboPanel.isVisible());
            }
        });
        //The JFrame uses the BorderLayout layout manager.
        // Put the two JPanels and JButton in different areas.
        guiFrame.add(comboPanel, BorderLayout.NORTH);
        guiFrame.add(listPanel, BorderLayout.CENTER);
        guiFrame.add(login,BorderLayout.SOUTH);
        //make sure the JFrame is visible
        guiFrame.setVisible(true);
    }
}

