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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UpdateEventsApproveActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ImageView logoutBtn, ivMenu;

    private EventUpdateRequestModel myEModel ;
    private RecyclerView recyclerView=null;
    private EventUpdateRequestAdapter adapter = null;
    private GestureDetectorCompat detector = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_events_approve);

        myEModel = EventUpdateRequestModel.getSingleton();

        setUpEventModels();

        progressDialog = new ProgressDialog(UpdateEventsApproveActivity.this);

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
                        Toast.makeText(UpdateEventsApproveActivity.this, "Admin Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UpdateEventsApproveActivity.this, AdminHomeActivity.class));
                        break;
//                    case R.id.admin_nav_event_home:
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        Toast.makeText(UpdateEventsApproveActivity.this, "Event Home is Clicked", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(UpdateEventsApproveActivity.this, UpdateEventsApproveActivity.class));
//                        break;
//                    case R.id.admin_nav_add_event:
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        Toast.makeText(UpdateEventsApproveActivity.this, "Add Event is Clicked", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(UpdateEventsApproveActivity.this, UpdateEventsApproveActivity.class));
//                        break;
//                    case R.id.admin_nav_items_home:
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        Toast.makeText(UpdateEventsApproveActivity.this, "Items Home is Clicked", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(UpdateEventsApproveActivity.this, UpdateEventsApproveActivity.class));
//                        break;
//                    case R.id.admin_nav_add_items:
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        Toast.makeText(UpdateEventsApproveActivity.this, "Event Home is Clicked", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(UpdateEventsApproveActivity.this, UpdateEventsApproveActivity.class));
//                        break;
                    case R.id.admin_nav_switch_user:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(UpdateEventsApproveActivity.this, "Switching to user...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UpdateEventsApproveActivity.this, HomeActivity.class));
                        break;
                    case R.id.admin_nav_logout:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(UpdateEventsApproveActivity.this, "Logout is Clicked", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(UpdateEventsApproveActivity.this, "Share Link is Clicked", Toast.LENGTH_SHORT).show();break;
                    case R.id.admin_nav_contact:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(UpdateEventsApproveActivity.this, "Contact us is Clicked", Toast.LENGTH_SHORT).show();break;
                    default:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }return true;
            }
        });
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEventsApproveActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // don't forget to change the line below with the names of your Activities
                        Intent intent = new Intent(UpdateEventsApproveActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    private void setUpEventModels() {
        // Read Parse Objects
        ParseQuery<ParseObject> query = ParseQuery.getQuery("EventUpdateRequest");
        query.whereEqualTo("isApproved", Boolean.FALSE);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, ParseException e) {
                for (ParseObject eventObj : results) {
                    if (e == null) {
                        Date startDate1= eventObj.getDate("eventStartDt");
                        Date endDate1= eventObj.getDate("eventEndDt");
                        DateFormat formatter1=new SimpleDateFormat("MM/dd/yyyy HH:mm");


                        String updateID, eventID, eventName, eventStDT, eventEndDt, eventDescription,eventAddressLine1,
                                eventAddressLine2, eventCity,eventState, eventCountry, eventZipcode, eventNotes, updateReason;
                        eventID= eventObj.getObjectId();
                        updateID= eventObj.getString("updateEventId");
                        eventName = eventObj.getString("eventName");
                        eventStDT = formatter1.format(startDate1);
                        eventEndDt =  formatter1.format(endDate1);
                        eventDescription =  eventObj.getString("eventDescription");
                        eventAddressLine1 =  eventObj.getString("eventAddressLine1") ;
                        eventAddressLine2 =  eventObj.getString("eventAddressLine2");
                        eventCity= eventObj.getString("eventCity") ;
                        eventState= eventObj.getString("eventState") ;
                        eventCountry= eventObj.getString("eventCountry");
                        eventZipcode= eventObj.getString("eventZipcode") ;
                        eventNotes =  eventObj.getString("eventNotes");
                        updateReason = eventObj.getString("updateReason");
                        Log.v("ObjectID",String.valueOf(eventID));

                        myEModel.EventUpdateRequestsList.add(new EventUpdateRequestModel.EventUpdateRequests(updateID, eventID, eventName, eventStDT, eventEndDt, eventDescription, eventAddressLine1, eventAddressLine2, eventCity, eventState, eventCountry, eventZipcode, eventNotes, updateReason));

                        Log.v("Setup EventList Size:", String.valueOf(myEModel.EventUpdateRequestsList.size()));
                        adapter = new EventUpdateRequestAdapter(UpdateEventsApproveActivity.this, myEModel);
                        Log.v("adapter", String.valueOf(adapter.getItemCount()));

                        recyclerView = findViewById(R.id.adminRecyclerView);
                        recyclerView.setAdapter(adapter);

                        recyclerView.setLayoutManager(new LinearLayoutManager(UpdateEventsApproveActivity.this));

                        detector = new GestureDetectorCompat(UpdateEventsApproveActivity.this, new UpdateEventsApproveActivity.RecyclerViewOnGestureListener());
                        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener(){
                            @Override
                            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                                return detector.onTouchEvent(e);
                            }
                        });
                    } else {
                        Toast.makeText(UpdateEventsApproveActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
    private class RecyclerViewOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (view != null) {
                RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(view);
                if (holder instanceof EventUpdateRequestAdapter.MyViewHolder) {
                    int position = holder.getAdapterPosition();
                    // handle single tap
                    String sEventId= myEModel.EventUpdateRequestsList.get(position).eventID;
                    String sEUpdateId= myEModel.EventUpdateRequestsList.get(position).updateID;
                    String sEventName= myEModel.EventUpdateRequestsList.get(position).eventName;
                    String sUpdateReason= myEModel.EventUpdateRequestsList.get(position).updateReason;
                    Log.v("Selected EventID: ",sEventId);

                    Toast.makeText(UpdateEventsApproveActivity.this, "Selected EventID: "+sEventId, Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEventsApproveActivity.this)
                            .setTitle("Approve Update Request")
                            .setMessage("Do you want to approve below event? \nEvent ID: "+sEUpdateId+"\nEvent Name: "+sEventName+"\nUpdate Reason: "+sUpdateReason)
                            .setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Log.v("Button Selected: ","Approve");

                                    Toast.makeText(UpdateEventsApproveActivity.this, "Event is approved!", Toast.LENGTH_SHORT).show();

                                    try{
                                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");

                                        // Retrieve the object by id
                                        query.getInBackground(sEUpdateId, new GetCallback<ParseObject>() {
                                            public void done(ParseObject eventObject, ParseException e) {
                                                if (e == null) {
                                                    eventObject.put("eventName",myEModel.EventUpdateRequestsList.get(position).eventName);
                                                    eventObject.put("eventDescription",myEModel.EventUpdateRequestsList.get(position).eventDescription);
                                                    eventObject.put("eventNotes",myEModel.EventUpdateRequestsList.get(position).eventNotes);
                                                    eventObject.put("eventAddressLine1",myEModel.EventUpdateRequestsList.get(position).eventAddressLine1);
                                                    eventObject.put("eventAddressLine2",myEModel.EventUpdateRequestsList.get(position).eventAddressLine2);
                                                    eventObject.put("eventCity",myEModel.EventUpdateRequestsList.get(position).eventCity);
                                                    eventObject.put("eventState",myEModel.EventUpdateRequestsList.get(position).eventState);
                                                    eventObject.put("eventCountry",myEModel.EventUpdateRequestsList.get(position).eventCountry);
                                                    eventObject.put("eventZipcode",myEModel.EventUpdateRequestsList.get(position).eventZipcode);
                                                    eventObject.put("isApproved",Boolean.TRUE);

                                                    String stDateTime=myEModel.EventUpdateRequestsList.get(position).eventStDT;
                                                    String endDateTime=myEModel.EventUpdateRequestsList.get(position).eventEndDt;

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

                                                    eventObject.put("isApproved",Boolean.TRUE );
                                                    // Saving object
                                                    progressDialog.show();
                                                    eventObject.saveInBackground(new SaveCallback() {
                                                        @Override
                                                        public void done(ParseException e) {
                                                            progressDialog.dismiss();
                                                            if (e == null) {
                                                                // Success
                                                                Toast.makeText(UpdateEventsApproveActivity.this, "Event updated in database...!", Toast.LENGTH_LONG).show();
                                                                /*

                                                                                                             ParseQuery<ParseObject> queryEventsU = ParseQuery.getQuery("EventUpdateRequest");
                                                                // Query parameters based on the item name
                                                                queryEventsU.whereEqualTo("updateEventId", sEUpdateId).whereEqualTo("isApproved", Boolean.FALSE);
                                                                queryEventsU.findInBackground(new FindCallback<ParseObject>() {
                                                                    @Override
                                                                    public void done(final List<ParseObject> event, ParseException e) {
                                                                        if (e == null) {
                                                                            event.get(0).deleteInBackground(new DeleteCallback() {
                                                                                @Override
                                                                                public void done(ParseException e) {
                                                                                    if (e == null) {
                                                                                        // Success
                                                                                        Toast.makeText(UpdateEventsApproveActivity.this, "Event Removed in database...!", Toast.LENGTH_LONG).show();

                                                                                        // don't forget to change the line below with the names of your Activities
                                                                                        Intent intent = new Intent(UpdateEventsApproveActivity.this, UpdateEventsApproveActivity.class);
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

                                                                 */
                                                                // don't forget to change the line below with the names of your Activities
                                                                Intent intent = new Intent(UpdateEventsApproveActivity.this, UpdateEventsApproveActivity.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                startActivity(intent);
                                                            } else {
                                                                // Error
                                                                Toast.makeText(UpdateEventsApproveActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }catch (Exception e){
                                        Toast.makeText(UpdateEventsApproveActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }).setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    // don't forget to change the line below with the names of your Activities
                                    Log.v("Button Selected: ","Approve");
                                    Toast.makeText(UpdateEventsApproveActivity.this, "Event is rejected.", Toast.LENGTH_SHORT).show();
                                    ParseQuery<ParseObject> queryEvents = ParseQuery.getQuery("EventUpdateRequest");
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
                                                            Toast.makeText(UpdateEventsApproveActivity.this, "Event Removed in database...!", Toast.LENGTH_LONG).show();

                                                            // don't forget to change the line below with the names of your Activities
                                                            Intent intent = new Intent(UpdateEventsApproveActivity.this, UpdateEventsApproveActivity.class);
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
                                    Toast.makeText(UpdateEventsApproveActivity.this, "Event is Ignored.", Toast.LENGTH_SHORT).show();
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



}