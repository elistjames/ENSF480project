package Properties;

public class Listing {

    private int propertyID;
    private int duration;
    private String state;

    public Listing(int propertyID, int duration, String state) {
        this.propertyID = propertyID;
        this.duration = duration;
        this.state = state;
    }

    public int getPropertyID() {
        return propertyID;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
