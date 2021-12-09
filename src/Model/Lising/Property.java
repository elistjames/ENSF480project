/*
 * Author(s): Eli, Manjot
 * Documented by: Ryan Sommerville
 * Date created: Nov 28, 2021
 * Last Edited: Dec 6, 2021
 */

package Model.Lising;

/**
 * A class that represents a property and holds information
 * about that property.
 */
public class Property {
    private int landlordID;
    private int ID;
    private String type;
    private int bedrooms;
    private int bathrooms;
    private int furnished;
    private String address;
    private String cityQuadrant;
    private String state;

    public Property(int landlordID, int ID, String type, int bedrooms, int bathrooms, int furnished,
                    String address, String cityQuadrant, String state) {
        this.landlordID = landlordID;
        this.ID = ID;
        this.type = type;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.furnished = furnished;
        this.address = address;
        this.cityQuadrant = cityQuadrant;
        this.state = state;
    }

    public Property(int landlordID, String type, int bedrooms, int bathrooms, int furnished, String address, String cityQuadrant) {
        this.landlordID = landlordID;
        this.type = type;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.furnished = furnished;
        this.address = address;
        this.cityQuadrant = cityQuadrant;
    }



    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getLandlordID() {
        return landlordID;
    }

    public String getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public int isFurnished() {
        return furnished;
    }

    public String getCityQuadrant() {
        return cityQuadrant;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
