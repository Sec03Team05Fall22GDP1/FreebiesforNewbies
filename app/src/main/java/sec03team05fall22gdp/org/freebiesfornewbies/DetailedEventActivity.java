package sec03team05fall22gdp.org.freebiesfornewbies;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class DetailedEventActivity extends AppCompatActivity {

    private TextView eventNameTV, eventSTDTTV, eventENDDTTV, eventLocationTV, eventDescTV, eventNotesTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_event);
        Intent intent = getIntent();
        String fetchID =  intent.getStringExtra("eventID");

        eventNameTV=findViewById(R.id.tvDEvent);
        eventSTDTTV=findViewById(R.id.tvDEventStDt);
        eventENDDTTV=findViewById(R.id.tvDEventEndDt);
        eventLocationTV=findViewById(R.id.tvDEventLoc);
        eventDescTV=findViewById(R.id.tvDEventDesc);
        eventNotesTV=findViewById(R.id.tvDEventNotes);

        fetchEvent(fetchID);

        Button updateBtn, deleteBtn, homeBtn;
        updateBtn = findViewById(R.id.btnDetailUpdate);
        deleteBtn = findViewById(R.id.btnDetailDelete);
        homeBtn = findViewById(R.id.btnDetailHome);

        updateBtn.setOnClickListener(v -> {
            Intent intent1 = new Intent(DetailedEventActivity.this, UpdateEventActivity.class);
            intent1.putExtra("eventID",fetchID);
            startActivity(intent1);
        });
        homeBtn.setOnClickListener(v -> {
            startActivity(new Intent(DetailedEventActivity.this, HomeActivity.class));
        });
        deleteBtn.setOnClickListener(v -> {
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
                                    showAlert("Delete Status:", "Object has been deleted Successfully");
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
        });
    }


    private void fetchEvent(String eventID){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        query.whereEqualTo("objectId", eventID);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject event, ParseException e) {
                if (e == null) {
                    String eventID, eventName, eventStDT, eventEndDt, eventDescription,eventAddressLine1,
                            eventAddressLine2, eventCity,eventState, eventCountry, eventZipcode, eventNotes;
                    eventID= event.getObjectId();
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

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailedEventActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // don't forget to change the line below with the names of your Activities
                        Intent intent = new Intent(DetailedEventActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}