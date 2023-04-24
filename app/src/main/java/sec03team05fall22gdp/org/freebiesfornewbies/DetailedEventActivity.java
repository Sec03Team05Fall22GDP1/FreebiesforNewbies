package sec03team05fall22gdp.org.freebiesfornewbies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DetailedEventActivity extends AppCompatActivity {
    private ImageView logoutBtn, ivMenu;
    private ProgressDialog progressDialog;
    private TextView eventNameTV, eventSTDTTV, eventENDDTTV, eventLocationTV, eventDescTV, eventNotesTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_event);
        Intent intent = getIntent();
        getSupportActionBar().hide();

        String fetchID =  intent.getStringExtra("eventID");
        progressDialog = new ProgressDialog(DetailedEventActivity.this);

        eventNameTV=findViewById(R.id.tvDEvent);
        eventSTDTTV=findViewById(R.id.tvDEventStDt);
        eventENDDTTV=findViewById(R.id.tvDEventEndDt);
        eventLocationTV=findViewById(R.id.tvDEventLoc);
        eventDescTV=findViewById(R.id.tvDEventDesc);
        eventNotesTV=findViewById(R.id.tvDEventNotes);
        logoutBtn = findViewById(R.id.ivEDlogout);
        fetchEvent(fetchID);

        Button updateBtn, deleteBtn;
        updateBtn = findViewById(R.id.btnDetailUpdate);
        deleteBtn = findViewById(R.id.btnDetailDelete);

        updateBtn.setOnClickListener(v -> {
            Intent intent1 = new Intent(DetailedEventActivity.this, UpdateEventActivity.class);
            intent1.putExtra("eventID",fetchID);
            startActivity(intent1);
        });
        deleteBtn.setOnClickListener(v -> {
            Intent intent1 = new Intent(DetailedEventActivity.this, Event_Delete_Request.class);
            intent1.putExtra("eventID",fetchID);
            startActivity(intent1);
        });

        ivMenu = findViewById(R.id.ivEDMenuIcon);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            TextView userTV= navigationView.getHeaderView(0).findViewById(R.id.profile_user);
            TextView emailTV= navigationView.getHeaderView(0).findViewById(R.id.profile_email);
            Log.v("currentUser",currentUser.getUsername());
            Log.v("email",currentUser.getEmail());
            userTV.setText(currentUser.getUsername());
            emailTV.setText(currentUser.getEmail());
            if(currentUser.getBoolean("isAdmin")==Boolean.TRUE){
                Menu menu = navigationView.getMenu();
                MenuItem sItem= menu.findItem(R.id.nav_switch_admin);
                sItem.setVisible(true);
                this.invalidateOptionsMenu();
            } else{
                Menu menu = navigationView.getMenu();
                MenuItem sItem= menu.findItem(R.id.nav_switch_admin);
                sItem.setVisible(false);
                this.invalidateOptionsMenu();
            }
        }

        ivMenu.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });
        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {
                int id = item.getItemId();

                Log.v("Inside:","onNavigationItemSelected");
                switch (id){
                    case R.id.nav_switch_admin:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(DetailedEventActivity.this, "Switching to Admin...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DetailedEventActivity.this, AdminHomeActivity.class));
                        break;
                    case R.id.nav_event_home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(DetailedEventActivity.this, "Event Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DetailedEventActivity.this, HomeActivity.class));
                        break;
                    case R.id.nav_add_event:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(DetailedEventActivity.this, "Add Event is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DetailedEventActivity.this, CreateEventActivity.class));
                        break;
                    case R.id.nav_items_home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(DetailedEventActivity.this, "Items Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DetailedEventActivity.this, ItemHomeActivity.class));
                        break;
                    case R.id.nav_add_items:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(DetailedEventActivity.this, "Event Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DetailedEventActivity.this, CreateFreeitemActivity.class));
                        break;
                    case R.id.nav_logout:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(DetailedEventActivity.this, "Logout is Clicked", Toast.LENGTH_SHORT).show();
                        progressDialog.show();
                        // logging out of Parse
                        ParseUser.logOutInBackground(e -> {
                            progressDialog.dismiss();
                            if (e == null) {
                                Intent intent = new Intent(DetailedEventActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                        break;
                    case R.id.nav_share:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(DetailedEventActivity.this, "Share Link is Clicked", Toast.LENGTH_SHORT).show();break;
                    case R.id.nav_contact:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(DetailedEventActivity.this, "Contact us is Clicked", Toast.LENGTH_SHORT).show();break;
                    default:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }return true;
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