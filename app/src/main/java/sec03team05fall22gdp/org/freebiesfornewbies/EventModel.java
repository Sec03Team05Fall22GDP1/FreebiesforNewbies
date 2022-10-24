package sec03team05fall22gdp.org.freebiesfornewbies;

public class EventModel {
    private String eventID, eventName, eventStDT, eventEndDt,eventDescription ,eventAddressLine1, eventAddressLine2, eventCity,eventState, eventCountry, eventZipcode, eventNotes;

    public EventModel(String eventID, String eventName, String eventStDT, String eventEndDt, String eventDescription, String eventAddressLine1, String eventAddressLine2, String eventCity, String eventState, String eventCountry, String eventZipcode, String eventNotes) {
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

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventStDT() {
        return eventStDT;
    }

    public void setEventStDT(String eventStDT) {
        this.eventStDT = eventStDT;
    }

    public String getEventEndDt() {
        return eventEndDt;
    }

    public void setEventEndDt(String eventEndDt) {
        this.eventEndDt = eventEndDt;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventAddressLine1() {
        return eventAddressLine1;
    }

    public void setEventAddressLine1(String eventAddressLine1) {
        this.eventAddressLine1 = eventAddressLine1;
    }

    public String getEventAddressLine2() {
        return eventAddressLine2;
    }

    public void setEventAddressLine2(String eventAddressLine2) {
        this.eventAddressLine2 = eventAddressLine2;
    }

    public String getEventCity() {
        return eventCity;
    }

    public void setEventCity(String eventCity) {
        this.eventCity = eventCity;
    }

    public String getEventState() {
        return eventState;
    }

    public void setEventState(String eventState) {
        this.eventState = eventState;
    }

    public String getEventCountry() {
        return eventCountry;
    }

    public void setEventCountry(String eventCountry) {
        this.eventCountry = eventCountry;
    }

    public String getEventZipcode() {
        return eventZipcode;
    }

    public void setEventZipcode(String eventZipcode) {
        this.eventZipcode = eventZipcode;
    }

    public String getEventNotes() {
        return eventNotes;
    }

    public void setEventNotes(String eventNotes) {
        this.eventNotes = eventNotes;
    }
}
