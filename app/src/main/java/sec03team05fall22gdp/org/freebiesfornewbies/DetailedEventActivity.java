package sec03team05fall22gdp.org.freebiesfornewbies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class DetailedEventActivity extends AppCompatActivity {

    private TextView eventNameTV, eventSTDTTV, eventENDDTTV, eventLocationTV, eventDescTV, eventNotesTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_event);
        Intent intent = getIntent();
        String fetchID =  intent.getStringExtra("eventID");

        eventNameTV=findViewById(R.id.tvDetailPage);
        eventSTDTTV=findViewById(R.id.tvDEventStDt);
        eventENDDTTV=findViewById(R.id.tvDEventEndDt);
        eventLocationTV=findViewById(R.id.tvDEventLoc);
        eventDescTV=findViewById(R.id.tvDEventDesc);
        eventNotesTV=findViewById(R.id.tvDEventNotes);

        fetchEvent(fetchID);
    }

    private void fetchEvent(String eventID){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        query.whereEqualTo("objectId", eventID);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject event, ParseException e) {
                if (e == null) {
                    String eventID, eventName, eventStDT, eventEndDt, eventDescription,eventAddressLine1,
                            eventAddressLine2, eventCity,eventState, eventCountry, eventZipcode, eventNotes;
                    eventID= event.getString("objectId");
                    eventName = event.getString("eventName");
                    eventStDT = String.valueOf(event.getDate("eventStartDt"));
                    eventEndDt =  String.valueOf(event.getDate("eventEndDt"));
                    eventDescription =  event.getString("eventDescription");
                    eventAddressLine1 =  event.getString("eventAddressLine1") ;
                    eventAddressLine2 =  event.getString("eventAddressLine2");
                    eventCity= event.getString("eventCity") ;
                    eventState= event.getString("eventState") ;
                    eventCountry= event.getString("eventCountry");
                    eventZipcode= event.getString("eventZipcode") ;
                    eventNotes =  event.getString("eventNotes");
                    String Loc;
                    if(eventAddressLine2.matches("")){
                        Loc = eventAddressLine1+"\n"+
                                eventCity+", "+ eventState+", "+eventCountry+" - "+eventZipcode;
                    }else{
                        Loc = eventAddressLine1+"\n"+eventAddressLine2+"\n"+
                                eventCity+", "+ eventState+", "+eventCountry+" - "+eventZipcode;
                    }


                    eventNameTV.setText(eventName);
                    eventSTDTTV.setText(eventStDT);
                    eventENDDTTV.setText(eventEndDt);
                    eventLocationTV.setText(Loc);
                    eventDescTV.setText(eventDescription);
                    eventNotesTV.setText(eventNotes);

                    Log.v("Fetched EventID: ",eventID);
                    Log.v("Fetched EventName: ",eventName);
                } else {
                    Log.v("ParseException: ",e.getMessage());
                }
            }
        });
    }
}