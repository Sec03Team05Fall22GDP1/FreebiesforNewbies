package sec03team05fall22gdp.org.freebiesfornewbies;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ImageView logoutBtn, createEventBtn;
    private ProgressDialog progressDialog;
    ArrayList<EventModel> eModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        progressDialog = new ProgressDialog(HomeActivity.this);
        logoutBtn = findViewById(R.id.ivlogout);
        createEventBtn = findViewById(R.id.ivCreateEvent);
        setUpEventModels();

        createEventBtn.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, CreateEventActivity.class));
        });

        logoutBtn.setOnClickListener(v ->{
            progressDialog.show();
            // logging out of Parse
            ParseUser.logOutInBackground(e -> {
                progressDialog.dismiss();
                if (e == null)
                    showAlert("So, you're going...", "Ok...Bye-bye then");
            });
        });
    }

    private void setUpEventModels(){
        // Read Parse Objects
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, ParseException e) {
                for (ParseObject event : results) {
                    if (e == null) {
                        String eName = event.getString("eventName");
                        Log.v("eStartDT", String.valueOf(event.getDate("eventStartDt")));
                        String eStartDate = String.valueOf(event.getDate("eventStartDt"));
                        String eEndDate =  String.valueOf(event.getDate("eventEndDt"));
                        String eDescription =  event.getString("eventDescription");
                        String eLocation =  event.getString("eventAddressLine1") +", "+
                                event.getString("eventCity") +", "+
                                event.getString("eventState") +", "+
                                event.getString("eventCountry") +", "+
                                event.getString("eventZipcode") ;
                        String eNotes =  event.getString("eventNotes");
                        eModels.add(new EventModel(eName, eStartDate, eEndDate, eLocation, eDescription, eNotes));
                    } else {
                        Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                Log.v("eModels", String.valueOf(eModels.size()));
                RecyclerView recyclerView = findViewById(R.id.eventRecyclerView);

                EventRAdapter adapter = new EventRAdapter(HomeActivity.this, eModels);
                Log.v("adapter", String.valueOf(adapter.getItemCount()));
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
            }
        });

    }


    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // don't forget to change the line below with the names of your Activities
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}