package edu.ucalgary.ensf480;

public class Property {
    private Landlord landlord;
    private int ID;
    private String type;
    private int bedrooms;
    private int bathrooms;
    private boolean furnished;
    private String address;
    private String cityQuadrant;

    public Property(Landlord landlord, int ID, String type, int bedrooms, int bathrooms, boolean furnished,
                    String address, String cityQuadrant) {
        this.landlord = landlord;
        this.ID = ID;
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

    public Landlord getLandlord() {
        return landlord;
    }

    public void setLandlord(Landlord landlord) {
        this.landlord = landlord;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }

    public boolean isFurnished() {
        return furnished;
    }

    public void setFurnished(boolean furnished) {
        this.furnished = furnished;
    }

    public String getCityQuadrant() {
        return cityQuadrant;
    }

    public void setCityQuadrant(String cityQuadrant) {
        this.cityQuadrant = cityQuadrant;
    }
}
