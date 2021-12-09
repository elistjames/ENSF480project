/*
 * Author(s): Manjot, Luke
 * Documented by: Ryan Sommerville
 * Date created: Dec 1, 2021
 * Last Edited: Dec 6, 2021
 */

package Model.User;

import Model.Lising.Property;
import java.util.ArrayList;

/**
 * A class that holds the information that forms a periodic report.
 */
public class SummaryReport {
    private int housesListed;
    private int housesRented;
    private int activeListings;
    private ArrayList<Property> rentedProperties;

    public SummaryReport() {}

    public SummaryReport(int housesListed, int housesRented, int activeListings, ArrayList<Property> rentedProperties) {
        this.housesListed = housesListed;
        this.housesRented = housesRented;
        this.activeListings = activeListings;
        this.rentedProperties = rentedProperties;
    }

    public int getHousesListed() {
        return housesListed;
    }

    public int getHousesRented() {
        return housesRented;
    }

    public int getActiveListings() {
        return activeListings;
    }

    public ArrayList<Property> getRentedProperties() {
        return rentedProperties;
    }
}
