package sec03team05fall22gdp.org.freebiesfornewbies;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class FreeItemDeleteActivity extends AppCompatActivity {

    private Button cancelBtn, deleteBtn;
    private String fetchID;
    private TextView iNameTV, iIdTV;
    private EditText iDeleteReason;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_item_delete);
        Intent intent = getIntent();
        fetchID =  intent.getStringExtra("itemID");
        fetchEvent(fetchID);
        deleteBtn = findViewById(R.id.btnDeleteItem);
        cancelBtn=findViewById(R.id.btncancelItem);
        iNameTV=findViewById(R.id.tvDItemName);
        iIdTV=findViewById(R.id.tvDItemID);
        iDeleteReason=findViewById(R.id.etDReason);

        progressDialog = new ProgressDialog(FreeItemDeleteActivity.this);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canclemethod();
            }
        });

        deleteBtn.setOnClickListener( v -> {
            try{
                ParseObject eventObject = new ParseObject("ItemsDeleteRequest");
                eventObject.put("DeleteItemID",fetchID);
                eventObject.put("itemName",iNameTV.getText().toString());
                eventObject.put("DeleteReason",iDeleteReason.getText().toString());
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
                            Toast.makeText(FreeItemDeleteActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }catch (Exception e){
                Toast.makeText(FreeItemDeleteActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    private void canclemethod() {
        Intent intent1 = new Intent(FreeItemDeleteActivity.this, FreeItemDetailedActivity.class);
        intent1.putExtra("itemID",fetchID);
        startActivity(intent1);
    }

    private void fetchEvent(String fetchID) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Items");
        query.whereEqualTo("objectId", fetchID);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject event, ParseException e) {
                if (e == null) {
                    String itemID, itemName;
                    itemID= event.getObjectId();
                    itemName = event.getString("itemName");

                    iNameTV.setText(itemName);
                    iIdTV.setText(itemID);

                    Log.v("Fetched EventID: ",itemID);
                    Log.v("Fetched EventName: ",itemName);
                } else {
                    Log.v("ParseException: ",e.getMessage());
                }
            }
        });
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FreeItemDeleteActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // don't forget to change the line below with the names of your Activities
                        Intent intent = new Intent(FreeItemDeleteActivity.this, FreeItemDetailedActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("itemID",fetchID);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
    private Timer inactivityTimer;
    private boolean isUserActive = true;

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        resetInactivityTimer();
        isUserActive = true;
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (inactivityTimer != null) {
            inactivityTimer.cancel();
        }
        isUserActive = false;
    }
}