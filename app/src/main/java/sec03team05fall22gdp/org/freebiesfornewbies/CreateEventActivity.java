package sec03team05fall22gdp.org.freebiesfornewbies;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateEventActivity extends AppCompatActivity {

    private Button createBtn, cancelBtn;
    private ProgressDialog progressDialog;
    private EditText eNameET, eDescET, eNotesET, eStDtET, eEndDtET, eAddLine1ET,eAddLine2ET, eCityET, eStateET,eCountryET,eZipET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        progressDialog = new ProgressDialog(CreateEventActivity.this);

        eNameET= findViewById(R.id.etEventName);
        eDescET =findViewById(R.id.etEventDescription);
        eNotesET =findViewById(R.id.etEventNotes);
        eStDtET=findViewById(R.id.etEventStartDate);
        eEndDtET=findViewById(R.id.etEventEndDate);
        eAddLine1ET=findViewById(R.id.etEventAddressLine1);
        eAddLine2ET=findViewById(R.id.etEventAddressLine2);
        eCityET=findViewById(R.id.etEventCity);
        eStateET=findViewById(R.id.etEventState);
        eCountryET=findViewById(R.id.etEventCountry);
        eZipET=findViewById(R.id.etEventZipcode);

        createBtn=findViewById(R.id.btnCreateEvent);
        cancelBtn=findViewById(R.id.btnCancelCreateEvent);

        cancelBtn.setOnClickListener( v -> {
            startActivity(new Intent(this,HomeActivity.class));
        });

        createBtn.setOnClickListener( v -> {

            try{
                ParseObject eventObject = new ParseObject("Events");
                // Store an object

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

                DateFormat formatter=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

                Date stdate=formatter.parse(stDateTime);
                Date etdate=formatter.parse(endDateTime);


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
                            Toast.makeText(CreateEventActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });

            }catch (Exception e){
                Toast.makeText(CreateEventActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateEventActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // don't forget to change the line below with the names of your Activities
                        Intent intent = new Intent(CreateEventActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}