/**
 * Author(s):
 * Editted by:
 * Documented by: Ryan Sommerville
 * Date created:
 * Last Editted:
 */

package Model.Lising;

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

    public void setDays(int days) {
        this.days = days;
    }
}
