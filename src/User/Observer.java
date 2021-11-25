package User;

import Properties.Listing;

import java.util.ArrayList;

public interface Observer {
    public void update(ArrayList<Listing> listings);
}
