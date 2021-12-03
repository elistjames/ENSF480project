package Model.User;

import Model.Lising.Property;

import java.util.ArrayList;

public class SummaryReport {
    private int housesListed;
    private int housesRented;
    private int activitiesListings;
    private ArrayList<Property> rentedProperties;

    public SummaryReport(int housesListed, int housesRented, int activitiesListings, ArrayList<Property> rentedProperties) {
        this.housesListed = housesListed;
        this.housesRented = housesRented;
        this.activitiesListings = activitiesListings;
        this.rentedProperties = rentedProperties;
    }

    public int getHousesListed() {
        return housesListed;
    }

    public int getHousesRented() {
        return housesRented;
    }

    public int getActivitiesListings() {
        return activitiesListings;
    }

    public ArrayList<Property> getRentedProperties() {
        return rentedProperties;
    }
}
