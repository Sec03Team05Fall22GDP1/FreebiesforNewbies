package sec03team05fall22gdp.org.freebiesfornewbies;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Timer;
import java.util.TimerTask;

public class Event_Delete_Request extends AppCompatActivity {
    private Button cancelBtn, deleteReqBtn;
    private String fetchID;
    private TextView eNameET, eIdET;
    private EditText eDeleteReason;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_delete_page);
        Intent intent = getIntent();
        getSupportActionBar().hide();

        fetchID =  intent.getStringExtra("eventID");
        fetchEvent(fetchID);
        deleteReqBtn = findViewById(R.id.btnDeleteRequest);
        cancelBtn=findViewById(R.id.btncancel_event);
        eNameET=findViewById(R.id.tvDEventName);
        eIdET=findViewById(R.id.tvDEventID);
        eDeleteReason=findViewById(R.id.textDReason);
        progressDialog = new ProgressDialog(Event_Delete_Request.this);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canclemethod();
            }
        });

        deleteReqBtn.setOnClickListener( v -> {
            try{
                ParseObject eventObject = new ParseObject("EventDeleteRequest");
                eventObject.put("DeleteEventID",fetchID);
                eventObject.put("eventName",eNameET.getText().toString());
                eventObject.put("DeleteReason",eDeleteReason.getText().toString());
                eventObject.put("byUser",ParseUser.getCurrentUser().getEmail());
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
                            Toast.makeText(Event_Delete_Request.this, e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }catch (Exception e){
                Toast.makeText(Event_Delete_Request.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchEvent(String fetchID) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        query.whereEqualTo("objectId", fetchID);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject event, ParseException e) {
                if (e == null) {
                    String eventID, eventName;
                    eventID= event.getObjectId();
                    eventName = event.getString("eventName");

                    eNameET.setText(eventName);
                    eIdET.setText(eventID);

                    Log.v("Fetched EventID: ",eventID);
                    Log.v("Fetched EventName: ",eventName);
                } else {
                    Log.v("ParseException: ",e.getMessage());
                }
            }
        });
    }

    public void canclemethod(){
        Intent intent1 = new Intent(Event_Delete_Request.this, DetailedEventActivity.class);
        intent1.putExtra("eventID",fetchID);
        startActivity(intent1);    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Event_Delete_Request.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // don't forget to change the line below with the names of your Activities
                        Intent intent = new Intent(Event_Delete_Request.this, DetailedEventActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("eventID",fetchID);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    private static final long SESSION_TIMEOUT_DURATION = 2 * 60 * 1000; // 2 minutes
    private Timer sessionTimer;

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        startSessionTimer();
    }
    @Override
    protected void onResume() {
        super.onResume();
        startSessionTimer();
    }
    @Override
    protected void onPause() {
        super.onPause();
        stopSessionTimer();
    }
    private void startSessionTimer() {
        stopSessionTimer(); // stop any existing timer

        sessionTimer = new Timer();
        sessionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                ParseUser.logOutInBackground(e -> {
                    if (e == null){
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
            }
        }, SESSION_TIMEOUT_DURATION);
    }

    private void stopSessionTimer() {
        if (sessionTimer != null) {
            sessionTimer.cancel();
            sessionTimer = null;
        }
    }
}

