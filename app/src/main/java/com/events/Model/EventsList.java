package com.events.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arjun.
 */
public class EventsList implements Serializable{
    ArrayList<Event> events = new ArrayList<>();
    String quoteMax="";
    String quoteAvailable="";

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public String getQuoteMax() {
        return quoteMax;
    }

    public void setQuoteMax(String quoteMax) {
        this.quoteMax = quoteMax;
    }

    public String getQuoteAvailable() {
        return quoteAvailable;
    }

    public void setQuoteAvailable(String quoteAvailable) {
        this.quoteAvailable = quoteAvailable;
    }
}
