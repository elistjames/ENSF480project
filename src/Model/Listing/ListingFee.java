/*
 * Author(s): Eli, Manjot
 * Documented by: Ryan Sommerville
 * Date created: Nov 28, 2021
 * Last Edited: Dec 6, 2021
 */

package Model.Listing;

/**
 * A class that represents a particular listing fee,
 * including the duration and amount of the fee.
 */
public class ListingFee {
    private int price;
    private int days;

    public ListingFee(int price, int days) {
        this.price = price;
        this.days = days;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDays() {
        return days;
    }

}
