package Model.Lising;

public class Listing {

    private Property property;
    private int duration;
    private String state;

    public Listing(Property property, int duration, String state) {
        this.property = property;
        this.duration = duration;
        this.state = state;
    }

    public Property getProperty() {
        return property;
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
