package sec03team05fall22gdp.org.freebiesfornewbies;

import java.util.ArrayList;

public class EventModel {

    public static class Events{
        public String eventID, eventName, eventStDT, eventEndDt,eventDescription ,eventAddressLine1, eventAddressLine2, eventCity,eventState, eventCountry, eventZipcode, eventNotes;

        public Events(String eventID, String eventName, String eventStDT, String eventEndDt, String eventDescription, String eventAddressLine1, String eventAddressLine2, String eventCity, String eventState, String eventCountry, String eventZipcode, String eventNotes) {
            this.eventID = eventID;
            this.eventName = eventName;
            this.eventStDT = eventStDT;
            this.eventEndDt = eventEndDt;
            this.eventDescription = eventDescription;
            this.eventAddressLine1 = eventAddressLine1;
            this.eventAddressLine2 = eventAddressLine2;
            this.eventCity = eventCity;
            this.eventState = eventState;
            this.eventCountry = eventCountry;
            this.eventZipcode = eventZipcode;
            this.eventNotes = eventNotes;
        }

    }
    private static EventModel theModel = null;
    public static EventModel getSingleton(){
        theModel = new EventModel();
        return theModel;
    }

    public ArrayList<Events> eventsList;
    private EventModel(){
        eventsList = new ArrayList<Events>();
    }



}
