package sec03team05fall22gdp.org.freebiesfornewbies;

import java.util.ArrayList;

public class EventUpdateRequestModel {

    public static class EventUpdateRequests{
        public String updateID, updateReason, eventID, eventName, eventStDT, eventEndDt,eventDescription ,eventAddressLine1, eventAddressLine2, eventCity,eventState, eventCountry, eventZipcode, eventNotes;

        public EventUpdateRequests(String updateID, String eventID, String eventName, String eventStDT, String eventEndDt, String eventDescription, String eventAddressLine1, String eventAddressLine2, String eventCity, String eventState, String eventCountry, String eventZipcode, String eventNotes, String updateReason) {
            this.updateID = updateID;
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
            this.updateReason = updateReason;
        }

    }
    private static EventUpdateRequestModel theModel = null;
    public static EventUpdateRequestModel getSingleton(){
        theModel = new EventUpdateRequestModel();
        return theModel;
    }

    public ArrayList<EventUpdateRequests> EventUpdateRequestsList;
    private EventUpdateRequestModel(){
        EventUpdateRequestsList = new ArrayList<EventUpdateRequests>();
    }



}
