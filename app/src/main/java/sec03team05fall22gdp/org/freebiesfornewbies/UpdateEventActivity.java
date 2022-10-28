package sec03team05fall22gdp.org.freebiesfornewbies;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateEventActivity extends AppCompatActivity {

    private Button updateBtn, cancelBtn;
    private ProgressDialog progressDialog;
    private String fetchID;
    private EditText eNameET, eDescET, eNotesET, eStDtET, eEndDtET, eAddLine1ET,eAddLine2ET, eCityET, eStateET,eCountryET,eZipET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);
        Intent intent = getIntent();
        fetchID =  intent.getStringExtra("eventID");

        progressDialog = new ProgressDialog(UpdateEventActivity.this);

        eNameET= findViewById(R.id.etUEventName);
        eDescET =findViewById(R.id.etUEventDescription);
        eNotesET =findViewById(R.id.etUEventNotes);
        eStDtET=findViewById(R.id.etUEventStartDate);
        eEndDtET=findViewById(R.id.etUEventEndDate);
        eAddLine1ET=findViewById(R.id.etUEventAddressLine1);
        eAddLine2ET=findViewById(R.id.etUEventAddressLine2);
        eCityET=findViewById(R.id.etUEventCity);
        eStateET=findViewById(R.id.etUEventState);
        eCountryET=findViewById(R.id.etUEventCountry);
        eZipET=findViewById(R.id.etUEventZipcode);

        updateBtn=findViewById(R.id.btnUpdateEvent);
        cancelBtn=findViewById(R.id.btnCancelUpdateEvent);

        fetchEvent(fetchID);

        cancelBtn.setOnClickListener( v -> {
            Intent intent1 = new Intent(UpdateEventActivity.this, DetailedEventActivity.class);
            intent1.putExtra("eventID",fetchID);
            startActivity(intent1);
        });

        updateBtn.setOnClickListener( v -> {

            try{
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");

                // Retrieve the object by id
                query.getInBackground(fetchID, new GetCallback<ParseObject>() {
                    public void done(ParseObject eventObject, ParseException e) {
                        if (e == null) {
                            // Now let's update it with some new data. In this case, only cheatMode and score
                            // will get sent to the Parse Cloud. playerName hasn't changed.
                            eventObject.put("eventName",eNameET.getText().toString());
                            eventObject.put("eventDescription",eDescET.getText().toString());
                            eventObject.put("eventNotes",eNotesET.getText().toString());
                            eventObject.put("eventAddressLine1",eAddLine1ET.getText().toString());
                            eventObject.put("eventAddressLine2",eAddLine2ET.getText().toString());
                            eventObject.put("eventCity",eCityET.getText().toString());
                            eventObject.put("eventState",eStateET.getText().toString());
                            eventObject.put("eventCountry",eCountryET.getText().toString());
                            eventObject.put("eventZipcode",eZipET.getText().toString());

                            String stDateTime=eStDtET.getText().toString();
                            String endDateTime=eEndDtET.getText().toString();

                            DateFormat formatter=new SimpleDateFormat("MM/dd/yyyy HH:mm");

                            Date stdate= null;
                            try {
                                stdate = formatter.parse(stDateTime);
                            } catch (java.text.ParseException ex) {
                                Log.v("StartDate Ex:", ex.getMessage());
                            }
                            Date etdate= null;
                            try {
                                etdate = formatter.parse(endDateTime);
                            } catch (java.text.ParseException ex) {
                                Log.v("endDate Ex:", ex.getMessage());
                            }


                            eventObject.put("eventStartDt",stdate);
                            eventObject.put("eventEndDt", etdate);
                            // Saving object
                            eventObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    progressDialog.dismiss();
                                    if (e == null) {
                                        // Success
                                        showAlert("Successfully saved..!\n", "Your object has been saved in Events class.");
                                    } else {
                                        // Error
                                        Toast.makeText(UpdateEventActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                                    }
                                }
                            });

                        } else {
                            // Failed
                        }
                    }
                });

            }catch (Exception e){
                Toast.makeText(UpdateEventActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
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
                    eventID= event.getString("objectId");
                    eventName = event.getString("eventName");
                    eventDescription =  event.getString("eventDescription");
                    eventAddressLine1 =  event.getString("eventAddressLine1") ;
                    eventAddressLine2 =  event.getString("eventAddressLine2");
                    eventCity= event.getString("eventCity") ;
                    eventState= event.getString("eventState") ;
                    eventCountry= event.getString("eventCountry");
                    eventZipcode= event.getString("eventZipcode") ;
                    eventNotes =  event.getString("eventNotes");

                    Date date = event.getDate("eventStartDt");
                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                    eventStDT = dateFormat.format(date);
                    date = event.getDate("eventEndDt");
                    eventEndDt = dateFormat.format(date);

                    eNameET.setText(eventName);
                    eStDtET.setText(eventStDT);
                    eEndDtET.setText(eventEndDt);
                    eAddLine1ET.setText(eventAddressLine1);
                    eAddLine2ET.setText(eventAddressLine2);
                    eCityET.setText(eventCity);
                    eStateET.setText(eventState);
                    eCountryET.setText(eventCountry);
                    eZipET.setText(eventZipcode);
                    eDescET.setText(eventDescription);
                    eNotesET.setText(eventNotes);

                    Log.v("Fetched EventID: ",eventID);
                    Log.v("Fetched EventName: ",eventName);
                } else {
                    Log.v("ParseException: ",e.getMessage());
                }
            }
        });
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEventActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // don't forget to change the line below with the names of your Activities
                        Intent intent = new Intent(UpdateEventActivity.this, DetailedEventActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("eventID",fetchID);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}