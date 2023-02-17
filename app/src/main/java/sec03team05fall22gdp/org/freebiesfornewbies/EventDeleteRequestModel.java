package sec03team05fall22gdp.org.freebiesfornewbies;

import java.util.ArrayList;

public class EventDeleteRequestModel {

    public static class EventDeleteRequests{
        public String deleteID, eventID, eventName, deleteReason;

        public EventDeleteRequests(String deleteID, String eventID, String eventName, String deleteReason) {
            this.deleteID = deleteID;
            this.eventID = eventID;
            this.eventName = eventName;
            this.deleteReason = deleteReason;
        }

    }
    private static EventDeleteRequestModel theModel = null;
    public static EventDeleteRequestModel getSingleton(){
        theModel = new EventDeleteRequestModel();
        return theModel;
    }

    public ArrayList<EventDeleteRequests> EventDeleteRequestsList;
    private EventDeleteRequestModel(){
        EventDeleteRequestsList = new ArrayList<EventDeleteRequests>();
    }

}
