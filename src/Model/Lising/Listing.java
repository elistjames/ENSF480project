/*
 * Author(s): Eli, Manjot
 * Documented by: Ryan Sommerville
 * Date created: Nov 28, 2021
 * Last Edited: Dec 6, 2021
 */

package Model.Lising;

import java.time.LocalDate;

/**
 * A class that represents a Property posting.
 * Includes a reference to that property, as well
 * as dates and durations for the Listing.
 */
public class Listing {

   
	Property property;
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

    public int getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }
}
