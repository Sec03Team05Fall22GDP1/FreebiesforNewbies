package sec03team05fall22gdp.org.freebiesfornewbies;

public class EventModel {
    private String eventName, eventStDT, eventEndDt, eventLocation, eventDescription, eventNotes;

    public EventModel(String eventName, String eventStDT, String eventEndDt, String eventLocation, String eventDescription, String eventNotes) {
        this.eventName = eventName;
        this.eventStDT = eventStDT;
        this.eventEndDt = eventEndDt;
        this.eventLocation = eventLocation;
        this.eventDescription = eventDescription;
        this.eventNotes = eventNotes;
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

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventNotes() {
        return eventNotes;
    }

    public void setEventNotes(String eventNotes) {
        this.eventNotes = eventNotes;
    }
}
