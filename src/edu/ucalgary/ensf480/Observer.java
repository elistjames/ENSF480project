package edu.ucalgary.ensf480;

import java.util.ArrayList;

public interface Observer {
    public void update(ArrayList<Listing> listings);
}
