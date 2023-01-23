package sec03team05fall22gdp.org.freebiesfornewbies;

import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.util.Log;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
public class EventUpdateRequestQueries {



        public void createEventObject(){
            try{
                ParseObject eventObject = new ParseObject("Events");
                // Store an object
///
                eventObject.put("eventName","eNameET.getText().toString()");
                eventObject.put("eventDescription","eDescET.getText().toString()");
                eventObject.put("eventNotes","eNotesET.getText().toString()");
                eventObject.put("eventAddressLine1","eAddLine1ET.getText().toString()");
                eventObject.put("eventAddressLine2","eAddLine2ET.getText().toString()");
                eventObject.put("eventCity","eCityET.getText().toString()");
                eventObject.put("eventState","eStateET.getText().toString()");
                eventObject.put("eventCountry","eCountryET.getText().toString()");
                eventObject.put("eventZipcode","eZipET.getText().toString()");

                String stDateTime="eStDtET.getText().toString()";
                String endDateTime="eEndDtET.getText().toString()";

                DateFormat formatter=new SimpleDateFormat("MM/dd/yyyy HH:mm");

                Date stdate=formatter.parse(stDateTime);
                Date etdate=formatter.parse(endDateTime);


                eventObject.put("eventStartDt",stdate);
                eventObject.put("eventEndDt", etdate);
                // Saving object
                eventObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            // Success
                        } else {
                            // Error
                        }
                    }
                });

            }catch (Exception e){
            }
        }

        public void readEventObject(String eventID) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
            query.whereEqualTo("objectId", eventID);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject event, ParseException e) {
                    if (e == null) {
                        String eventID, eventName, eventStDT, eventEndDt, eventDescription, eventAddressLine1,
                                eventAddressLine2, eventCity, eventState, eventCountry, eventZipcode, eventNotes;
                        eventID = event.getObjectId();
                        eventName = event.getString("eventName");
                        eventStDT = String.valueOf(event.getDate("eventStartDt"));
                        eventEndDt = String.valueOf(event.getDate("eventEndDt"));
                        eventDescription = event.getString("eventDescription");
                        eventAddressLine1 = event.getString("eventAddressLine1");
                        eventAddressLine2 = event.getString("eventAddressLine2");
                        eventCity = event.getString("eventCity");
                        eventState = event.getString("eventState");
                        eventCountry = event.getString("eventCountry");
                        eventZipcode = event.getString("eventZipcode");
                        eventNotes = event.getString("eventNotes");
                        String Loc;
                        if (eventAddressLine2.matches("")) {
                            Loc = eventAddressLine1 + "\n" +
                                    eventCity + ", " + eventState + ", " + eventCountry + " - " + eventZipcode;
                        } else {
                            Loc = eventAddressLine1 + "\n" + eventAddressLine2 + "\n" +
                                    eventCity + ", " + eventState + ", " + eventCountry + " - " + eventZipcode;
                        }

                    } else {
                        Log.v("ParseException: ", e.getMessage());
                    }
                }
            });
        }
        public void deleteEventObject(String fetchID){
            ParseQuery<ParseObject> soccerPlayers = ParseQuery.getQuery("Events");
            // Query parameters based on the item name
            soccerPlayers.whereEqualTo("objectId", fetchID);
            soccerPlayers.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List<ParseObject> event, ParseException e) {
                    if (e == null) {
                        event.get(0).deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    // Success
                                } else {
                                    Log.v("Delete Inner Ex:",e.getMessage());
                                }
                            }
                        });
                    }else {
                        Log.v("Delete Parse Outer Ex: ",e.getMessage());
                    }
                }
            });
        }

        public void updateEventObject(String fetchID) {

            try {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");

                // Retrieve the object by id
                query.getInBackground(fetchID, new GetCallback<ParseObject>() {
                    public void done(ParseObject eventObject, ParseException e) {
                        if (e == null) {
                            // Now let's update it with some new data. In this case, only cheatMode and score
                            // will get sent to the Parse Cloud. playerName hasn't changed.
                            eventObject.put("eventName", "eNameET.getText().toString()");
                            eventObject.put("eventDescription", "eDescET.getText().toString()");
                            eventObject.put("eventNotes", "eNotesET.getText().toString()");
                            eventObject.put("eventAddressLine1", "eAddLine1ET.getText().toString()");
                            eventObject.put("eventAddressLine2", "eAddLine2ET.getText().toString()");
                            eventObject.put("eventCity", "eCityET.getText().toString()");
                            eventObject.put("eventState", "eStateET.getText().toString()");
                            eventObject.put("eventCountry", "eCountryET.getText().toString()");
                            eventObject.put("eventZipcode", "eZipET.getText().toString()");

                            String stDateTime = "eStDtET.getText().toString()";
                            String endDateTime = "eEndDtET.getText().toString()";

                            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");

                            Date stdate = null;
                            try {
                                stdate = formatter.parse(stDateTime);
                            } catch (java.text.ParseException ex) {
                                Log.v("StartDate Ex:", ex.getMessage());
                            }
                            Date etdate = null;
                            try {
                                etdate = formatter.parse(endDateTime);
                            } catch (java.text.ParseException ex) {
                                Log.v("endDate Ex:", ex.getMessage());
                            }


                            eventObject.put("eventStartDt", stdate);
                            eventObject.put("eventEndDt", etdate);
                            // Saving object
                            eventObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        // Success
                                    } else {
                                        // Error
                                    }
                                }
                            });
                        } else {
                            // Failed
                        }
                    }
                });

            } catch (Exception e) {
            }
        }
}
