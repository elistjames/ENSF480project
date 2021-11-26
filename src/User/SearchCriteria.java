package User;

public class SearchCriteria {
    private int renterID;
    private String type;
    private int n_bedrooms;
    private int n_bathrooms;
    private int furnished;
    private String cityQuadrant;

    public SearchCriteria(int renterID) {
        this.renterID = renterID;
    }

    public int getRenterID() {
        return renterID;
    }

    public void setRenterID(int renterID) {
        this.renterID = renterID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getN_bedrooms() {
        return n_bedrooms;
    }

    public void setN_bedrooms(int n_bedrooms) {
        this.n_bedrooms = n_bedrooms;
    }

    public int getN_bathrooms() {
        return n_bathrooms;
    }

    public void setN_bathrooms(int n_bathrooms) {
        this.n_bathrooms = n_bathrooms;
    }

    public int isFurnished() {
        return furnished;
    }

    public void setFurnished(int furnished) {
        this.furnished = furnished;
    }

    public String getCityQuadrant() {
        return cityQuadrant;
    }

    public void setCityQuadrant(String cityQuadrant) {
        this.cityQuadrant = cityQuadrant;
    }
}
