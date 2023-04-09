package sec03team05fall22gdp.org.freebiesfornewbies;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class UpdateEventActivity extends AppCompatActivity {

    private EditText date_time_st;
    private EditText date_time_end;
    private DatePickerDialog datePickerDialog;
    private Button updateBtn, cancelBtn;
    private ProgressDialog progressDialog;
    private String fetchID;
    private EditText eNameET, eDescET, eNotesET, eStDtET, eEndDtET, eAddLine1ET,eAddLine2ET, eCityET, eStateET,eCountryET,eZipET, eUpdateReason;

    private Handler handler = new Handler();
    private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            // logging out of Parse
            ParseUser.logOutInBackground(e -> {
                if (e == null){
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    moveTaskToBack(true);
                }
            });
            Log.d("MyApp", "Performing operation after 2 minutes in background");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);
        Intent intent = getIntent();
        fetchID =  intent.getStringExtra("eventID");

        handler.postDelayed(myRunnable, 2 * 60 * 1000);

        initDatePicker();

        date_time_st =findViewById(R.id.etUEventStartDate);
        date_time_end =findViewById(R.id.etUEventEndDate);

        date_time_st.setInputType(InputType.TYPE_NULL);
        date_time_end.setInputType(InputType.TYPE_NULL);

        date_time_st.setOnClickListener(v ->{
                showDateTimeDialog(date_time_st);
        });
        date_time_end.setOnClickListener(v ->{
                showDateTimeDialog(date_time_end);
        });

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
        eUpdateReason=findViewById(R.id.etUpdateReason);

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
                /*
                ParseQuery<ParseObject> query = ParseQuery.getQuery("EventUpdateRequest");

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
                */
                ParseObject eventObject = new ParseObject("EventUpdateRequest");
                eventObject.put("updateEventId",fetchID);
                eventObject.put("eventName",eNameET.getText().toString());
                eventObject.put("eventDescription",eDescET.getText().toString());
                eventObject.put("eventNotes",eNotesET.getText().toString());
                eventObject.put("eventAddressLine1",eAddLine1ET.getText().toString());
                eventObject.put("eventAddressLine2",eAddLine2ET.getText().toString());
                eventObject.put("eventCity",eCityET.getText().toString());
                eventObject.put("eventState",eStateET.getText().toString());
                eventObject.put("eventCountry",eCountryET.getText().toString());
                eventObject.put("eventZipcode",eZipET.getText().toString());
                eventObject.put("eventZipcode",eZipET.getText().toString());
                eventObject.put("updateReason",eUpdateReason.getText().toString());
                eventObject.put("isApproved",Boolean.FALSE);
                eventObject.put("byUser",ParseUser.getCurrentUser().getEmail());

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
                            showAlert("Request Successful!\n", "Your request is now under review");
                        } else {
                            // Error
                            Toast.makeText(UpdateEventActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

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
                    eventID= event.getObjectId();
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

    private void showDateTimeDialog(EditText date_time_in){
        Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                TimePickerDialog.OnTimeSetListener timeSetListener= new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");
                        date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };
                new TimePickerDialog(UpdateEventActivity.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };
        new DatePickerDialog(UpdateEventActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }
    private void initDatePicker()    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(this, android.R.style.Holo_Light_ButtonBar_AlertDialog, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }
    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }
    private String getTodaysDate()    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }
    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "Jan";
        if(month == 2)
            return "Feb";
        if(month == 3)
            return "Mar";
        if(month == 4)
            return "Apr";
        if(month == 5)
            return "May";
        if(month == 6)
            return "Jun";
        if(month == 7)
            return "Jul";
        if(month == 8)
            return "Aug";
        if(month == 9)
            return "Sep";
        if(month == 10)
            return "Oct";
        if(month == 11)
            return "Nov";
        if(month == 12)
            return "Dec";
        //default should never happen
        return "JAN";
    }
    private Timer inactivityTimer;
    private boolean isUserActive = true;

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        resetInactivityTimer();
        isUserActive = true;
        Log.v("onUserInteraction()","inside");
        handler.removeCallbacks(myRunnable);
        handler.postDelayed(myRunnable, 2 * 60 * 1000);
    }

    private void resetInactivityTimer() {
        if (inactivityTimer != null) {
            inactivityTimer.cancel();
        }
        inactivityTimer = new Timer();
        inactivityTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                logoutUserAndReturnToLogin();
            }
        }, 2 * 60 * 1000); // 2 minutes in milliseconds
    }

    private void logoutUserAndReturnToLogin() {
        // logging out of Parse
        ParseUser.logOutInBackground(e -> {
            if (e == null){
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isUserActive) {
            resetInactivityTimer();
        }
        handler.removeCallbacks(myRunnable);
        Log.d("onResume", "inside");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (inactivityTimer != null) {
            inactivityTimer.cancel();
        }
        isUserActive = false;
        handler.removeCallbacks(myRunnable);
        Log.d("onPause", "inside");

    }
}