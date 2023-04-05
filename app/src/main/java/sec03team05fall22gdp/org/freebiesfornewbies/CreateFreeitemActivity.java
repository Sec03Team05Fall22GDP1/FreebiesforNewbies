package sec03team05fall22gdp.org.freebiesfornewbies;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class CreateFreeitemActivity extends AppCompatActivity {


    private Button createBtn, cancelBtn;
    private ProgressDialog progressDialog;
    private EditText iNameET, iDescET, iURLET, iAddLine1ET,iAddLine2ET, iCityET, iStateET,iCountryET,iZipET;

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
        setContentView(R.layout.activity_create_freeitem);
        handler.postDelayed(myRunnable, 2 * 60 * 1000);

        progressDialog = new ProgressDialog(CreateFreeitemActivity.this);

        iNameET= findViewById(R.id.etItemName);
        iDescET =findViewById(R.id.etItemDescription);
        iURLET =findViewById(R.id.etItemURL);
        iAddLine1ET=findViewById(R.id.etItemAddressLine1);
        iAddLine2ET=findViewById(R.id.etItemAddressLine1);
        iCityET=findViewById(R.id.etItemCity);
        iStateET=findViewById(R.id.etItemState);
        iCountryET=findViewById(R.id.etItemCountry);
        iZipET=findViewById(R.id.etItemZipcode);

        createBtn=findViewById(R.id.btnCreateItem);
        cancelBtn=findViewById(R.id.btnCancelCreateItem);

        cancelBtn.setOnClickListener( v -> {
            startActivity(new Intent(this,ItemHomeActivity.class));
        });

        createBtn.setOnClickListener( v -> {

            try{
                // Store an object

                ParseObject itemObject = new ParseObject("Items");

                itemObject.put("itemName", iNameET.getText().toString());
                itemObject.put("itemURL", iURLET.getText().toString());
                itemObject.put("isAvailable", true);
                itemObject.put("isApproved", false);
                itemObject.put("itemAddressLine1", iAddLine1ET.getText().toString());
                itemObject.put("itemCity", iCityET.getText().toString());
                itemObject.put("itemState", iStateET.getText().toString());
                itemObject.put("itemCountry", iCountryET.getText().toString());
                itemObject.put("itemZipcode", iZipET.getText().toString());
                itemObject.put("itemAddressLine2", iAddLine2ET.getText().toString());
                itemObject.put("itemDescription", iDescET.getText().toString());
                // Saving object
                itemObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        progressDialog.dismiss();
                        if (e == null) {
                            // Success
                            showAlert("Successfully saved..!\n", "Your object has been saved in Items class.");
                        } else {
                            // Error
                            Toast.makeText(CreateFreeitemActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });

            }catch (Exception e){
                Toast.makeText(CreateFreeitemActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });



    }

    // For the Alert messages we use the showAlert.
    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateFreeitemActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // don't forget to change the line below with the names of your Activities
                        Intent intent = new Intent(CreateFreeitemActivity.this, ItemHomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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