package Strategy;

import Properties.ListingFee;

public class ValidateListingFee implements ValidateData{
    @Override
    public boolean validate(Object d) {
        int days = Integer.parseInt(d.toString());
        for (ListingFee lf : db.getFees()) {
            if (lf.getDays() == days)
                return false;
        }
        return true;
    }
}
