package Model.Lising;

import java.time.LocalDate;

public class Listing {

    private Property property;
    private int duration;
    private String state;
    private LocalDate startDate;
    private int currentDay;

    public Listing(Property property, int duration, String state, LocalDate startDate, int currentDay) {
        this.property = property;
        this.duration = duration;
        this.state = state;
        this.startDate = startDate;
        this.currentDay = currentDay;
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

    public void setProperty(Property property) {
        this.property = property;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }
}
