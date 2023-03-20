package sec03team05fall22gdp.org.freebiesfornewbies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NewEventsApproveActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private ImageView logoutBtn, ivMenu;

    private EventModel myEModel ;
    private RecyclerView recyclerView=null;
    private EventRAdapter adapter = null;
    private GestureDetectorCompat detector = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_events_approve);

        progressDialog = new ProgressDialog(NewEventsApproveActivity.this);

        myEModel = EventModel.getSingleton();

        setUpEventModels();

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        logoutBtn= findViewById(R.id.ivlogout);

        logoutBtn.setOnClickListener(v ->{
            progressDialog.show();
            // logging out of Parse
            ParseUser.logOutInBackground(e -> {
                progressDialog.dismiss();
                if (e == null)
                    showAlert("So, you're going...", "Ok...Bye-bye then");
            });
        });
        ivMenu = findViewById(R.id.ivMenuIcon);
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
                    case R.id.nav_admin_home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(NewEventsApproveActivity.this, "Admin Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NewEventsApproveActivity.this, AdminHomeActivity.class));
                        break;
//                    case R.id.admin_nav_event_home:
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        Toast.makeText(NewEventsApproveActivity.this, "Event Home is Clicked", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(NewEventsApproveActivity.this, NewEventsApproveActivity.class));
//                        break;
//                    case R.id.admin_nav_add_event:
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        Toast.makeText(NewEventsApproveActivity.this, "Add Event is Clicked", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(NewEventsApproveActivity.this, NewEventsApproveActivity.class));
//                        break;
//                    case R.id.admin_nav_items_home:
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        Toast.makeText(NewEventsApproveActivity.this, "Items Home is Clicked", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(NewEventsApproveActivity.this, NewEventsApproveActivity.class));
//                        break;
//                    case R.id.admin_nav_add_items:
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        Toast.makeText(NewEventsApproveActivity.this, "Event Home is Clicked", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(NewEventsApproveActivity.this, NewEventsApproveActivity.class));
//                        break;
                    case R.id.admin_nav_switch_user:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(NewEventsApproveActivity.this, "Switching to user...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NewEventsApproveActivity.this, HomeActivity.class));
                        break;
                    case R.id.admin_nav_logout:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(NewEventsApproveActivity.this, "Logout is Clicked", Toast.LENGTH_SHORT).show();
                        progressDialog.show();
                        // logging out of Parse
                        ParseUser.logOutInBackground(e -> {
                            progressDialog.dismiss();
                            if (e == null)
                                showAlert("So, you're going...", "Ok...Bye-bye then");
                        });
                        break;
                    case R.id.admin_nav_share:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(NewEventsApproveActivity.this, "Share Link is Clicked", Toast.LENGTH_SHORT).show();break;
                    case R.id.admin_nav_contact:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(NewEventsApproveActivity.this, "Contact us is Clicked", Toast.LENGTH_SHORT).show();break;
                    default:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }return true;
            }
        });
    }

    private void setUpEventModels() {
        // Read Parse Objects
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        query.whereEqualTo("isApproved", Boolean.FALSE);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, ParseException e) {
                for (ParseObject eventObj : results) {
                    if (e == null) {
                        String eventID, eventName, eventStDT, eventEndDt, eventDescription,eventAddressLine1,
                                eventAddressLine2, eventCity,eventState, eventCountry, eventZipcode, eventNotes;
                        eventID= eventObj.getObjectId();
                        eventName = eventObj.getString("eventName");
                        eventStDT = String.valueOf(eventObj.getDate("eventStartDt"));
                        eventEndDt =  String.valueOf(eventObj.getDate("eventEndDt"));
                        eventDescription =  eventObj.getString("eventDescription");
                        eventAddressLine1 =  eventObj.getString("eventAddressLine1") ;
                        eventAddressLine2 =  eventObj.getString("eventAddressLine2");
                        eventCity= eventObj.getString("eventCity") ;
                        eventState= eventObj.getString("eventState") ;
                        eventCountry= eventObj.getString("eventCountry");
                        eventZipcode= eventObj.getString("eventZipcode") ;
                        eventNotes =  eventObj.getString("eventNotes");
                        Log.v("ObjectID",String.valueOf(eventID));

                        myEModel.eventsList.add(new EventModel.Events(eventID,eventName,eventStDT,eventEndDt,eventDescription,eventAddressLine1,eventAddressLine2,eventCity,eventState,eventCountry,eventZipcode,eventNotes));

                        Log.v("Setup EventList Size:", String.valueOf(myEModel.eventsList.size()));
                        adapter = new EventRAdapter(NewEventsApproveActivity.this, myEModel);
                        Log.v("adapter", String.valueOf(adapter.getItemCount()));

                        recyclerView = findViewById(R.id.eventRecyclerView);
                        recyclerView.setAdapter(adapter);

                        recyclerView.setLayoutManager(new LinearLayoutManager(NewEventsApproveActivity.this));

                        detector = new GestureDetectorCompat(NewEventsApproveActivity.this, new RecyclerViewOnGestureListener());
                        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener(){
                            @Override
                            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                                return detector.onTouchEvent(e);
                            }
                        });
                    } else {
                        Toast.makeText(NewEventsApproveActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewEventsApproveActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // don't forget to change the line below with the names of your Activities
                        Intent intent = new Intent(NewEventsApproveActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    private class RecyclerViewOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (view != null) {
                RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(view);
                if (holder instanceof EventRAdapter.MyViewHolder) {
                    int position = holder.getAdapterPosition();
                    // handle single tap
                    String sEventId= myEModel.eventsList.get(position).eventID;
                    String sEventName= myEModel.eventsList.get(position).eventName;
                    Log.v("Selected EventID: ",sEventId);

                    Toast.makeText(NewEventsApproveActivity.this, "Selected EventID: "+sEventId, Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(NewEventsApproveActivity.this)
                            .setTitle("Approve New Request")
                            .setMessage("Do you want to approve below event? \nEvent ID: "+sEventId+"\nEvent Name: "+sEventName)
                            .setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Log.v("Button Selected: ","Approve");

                                    Toast.makeText(NewEventsApproveActivity.this, "Event is approved!", Toast.LENGTH_SHORT).show();

                                    try{
                                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");

                                        // Retrieve the object by id
                                        query.getInBackground(sEventId, new GetCallback<ParseObject>() {
                                            public void done(ParseObject eventObject, ParseException e) {
                                                if (e == null) {
                                                    eventObject.put("isApproved",Boolean.TRUE );
                                                    // Saving object
                                                    progressDialog.show();
                                                    eventObject.saveInBackground(new SaveCallback() {
                                                        @Override
                                                        public void done(ParseException e) {
                                                            progressDialog.dismiss();
                                                            if (e == null) {
                                                                // Success
                                                                Toast.makeText(NewEventsApproveActivity.this, "Event updated in database...!", Toast.LENGTH_LONG).show();

                                                                // don't forget to change the line below with the names of your Activities
                                                                Intent intent = new Intent(NewEventsApproveActivity.this, NewEventsApproveActivity.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                startActivity(intent);
                                                            } else {
                                                                // Error
                                                                Toast.makeText(NewEventsApproveActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }catch (Exception e){
                                        Toast.makeText(NewEventsApproveActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }).setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    // don't forget to change the line below with the names of your Activities
                                    Log.v("Button Selected: ","Approve");
                                    Toast.makeText(NewEventsApproveActivity.this, "Event is rejected.", Toast.LENGTH_SHORT).show();
                                    ParseQuery<ParseObject> queryEvents = ParseQuery.getQuery("Events");
                                    // Query parameters based on the item name
                                    queryEvents.whereEqualTo("objectId", sEventId);
                                    queryEvents.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(final List<ParseObject> event, ParseException e) {
                                            if (e == null) {
                                                event.get(0).deleteInBackground(new DeleteCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e == null) {
                                                            // Success
                                                            Toast.makeText(NewEventsApproveActivity.this, "Event Removed in database...!", Toast.LENGTH_LONG).show();

                                                            // don't forget to change the line below with the names of your Activities
                                                            Intent intent = new Intent(NewEventsApproveActivity.this, NewEventsApproveActivity.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
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
                                }
                            }).setNeutralButton("Ignore", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    // don't forget to change the line below with the names of your Activities
                                    Log.v("Button Selected: ","Ignore");
                                    Toast.makeText(NewEventsApproveActivity.this, "Event is Ignored.", Toast.LENGTH_SHORT).show();
                                }
                            });
                    AlertDialog ok = builder.create();
                    ok.show();


                    return true; // Use up the tap gesture
                }
            }            // we didn't handle the gesture so pass it on
            return false;
        }
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
        }, 5 * 60 * 1000); // 5 minutes in milliseconds
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