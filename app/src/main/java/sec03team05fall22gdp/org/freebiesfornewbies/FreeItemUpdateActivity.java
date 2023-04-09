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

public class FreeItemUpdateActivity extends AppCompatActivity {

    private Button updateBtn, cancelBtn;
    private ProgressDialog progressDialog;
    private String fetchID;
    private EditText iNameET, iDescET, iURLET, iAddLine1ET,iAddLine2ET, iCityET, iStateET,iCountryET,iZipET, eUpdateReason;

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
        setContentView(R.layout.activity_free_item_update);
        Intent intent = getIntent();
        fetchID =  intent.getStringExtra("itemID");

        handler.postDelayed(myRunnable, 2 * 60 * 1000);

        progressDialog = new ProgressDialog(FreeItemUpdateActivity.this);

        iNameET= findViewById(R.id.etUItemName);
        iDescET =findViewById(R.id.etUItemDescription);
        iURLET =findViewById(R.id.etUItemURL);
        iAddLine1ET=findViewById(R.id.etUItemAddressLine1);
        iAddLine2ET=findViewById(R.id.etUItemAddressLine2);
        iCityET=findViewById(R.id.etUItemCity);
        iStateET=findViewById(R.id.etUItemState);
        iCountryET=findViewById(R.id.etUItemCountry);
        iZipET=findViewById(R.id.etUItemZipcode);

        eUpdateReason=findViewById(R.id.etUpdateReason);

        updateBtn=findViewById(R.id.btnUpdateItem);
        cancelBtn=findViewById(R.id.btnCancelUpdateItem);

        fetchEvent(fetchID);

        cancelBtn.setOnClickListener( v -> {
            handler.removeCallbacks(myRunnable);
            Intent intent1 = new Intent(FreeItemUpdateActivity.this, FreeItemDetailedActivity.class);
            intent1.putExtra("eventID",fetchID);
            startActivity(intent1);
        });

        updateBtn.setOnClickListener( v -> {
            try{

                ParseObject itemObj = new ParseObject("ItemsUpdateRequest");

                itemObj.put("itemDescription", iDescET.getText().toString());
                itemObj.put("itemAddressLine2", iAddLine2ET.getText().toString());
                itemObj.put("itemAddressLine1", iAddLine1ET.getText().toString());
                itemObj.put("itemName", iNameET.getText().toString());
                itemObj.put("itemCity", iCityET.getText().toString());
                itemObj.put("itemZipcode", iZipET.getText().toString());
                itemObj.put("itemState", iStateET.getText().toString());
                itemObj.put("isApproved", false);
                itemObj.put("itemCountry", iCountryET.getText().toString());
                itemObj.put("updateItemId", fetchID);
                itemObj.put("updateReason", eUpdateReason.getText().toString());
                itemObj.put("itemURL", iURLET.getText().toString());
                itemObj.put("isAvailable", true);
                itemObj.put("byUser",ParseUser.getCurrentUser().getEmail());

                // Saving object
                itemObj.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        progressDialog.dismiss();
                        if (e == null) {
                            // Success
                            showAlert("Request Successful!\n", "Your request is now under review");
                        } else {
                            // Error
                            Toast.makeText(FreeItemUpdateActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });

            }catch (Exception e){
                Toast.makeText(FreeItemUpdateActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchEvent(String itemID){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Items");
        query.whereEqualTo("objectId", itemID);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject itemObj, ParseException e) {
                if (e == null) {
                    String itemID, itemName, itemURL, itemAddressLine1, itemAddressLine2, itemCity, itemState, itemCountry, itemZipcode,itemDescription;
                    itemID=itemObj.getObjectId();
                    itemName=itemObj.getString("itemName");
                    itemURL=itemObj.getString("itemURL");
                    itemAddressLine1=itemObj.getString("itemAddressLine1");
                    itemCity=itemObj.getString("itemCity");
                    itemState=itemObj.getString("itemState");
                    itemCountry=itemObj.getString("itemCountry");
                    itemZipcode=itemObj.getString("itemZipcode");
                    itemAddressLine2=itemObj.getString("itemAddressLine2");
                    itemDescription= itemObj.getString("itemDescription");

                    iNameET.setText(itemName);
                    iDescET.setText(itemDescription);
                    iURLET.setText(itemURL);
                    iAddLine1ET.setText(itemAddressLine1);
                    iAddLine2ET.setText(itemAddressLine2);
                    iCityET.setText(itemCity);
                    iStateET.setText(itemState);
                    iCountryET.setText(itemCountry);
                    iZipET.setText(itemZipcode);

                    Log.v("Fetched EventID: ",itemID);
                    Log.v("Fetched EventName: ",itemName);
                } else {
                    Log.v("ParseException: ",e.getMessage());
                }
            }
        });
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FreeItemUpdateActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        handler.removeCallbacks(myRunnable);
                        // don't forget to change the line below with the names of your Activities
                        Intent intent = new Intent(FreeItemUpdateActivity.this, FreeItemDetailedActivity.class);
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
                handler.removeCallbacks(myRunnable);
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
        handler.postDelayed(myRunnable, 2 * 60 * 1000);
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
        handler.postDelayed(myRunnable, 2 * 60 * 1000);
        Log.d("onPause", "inside");

    }

}
