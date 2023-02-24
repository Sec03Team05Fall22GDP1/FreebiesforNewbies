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
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DeleteEventsApproveActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ImageView logoutBtn, ivMenu;

    private EventDeleteRequestModel myEModel ;
    private RecyclerView recyclerView=null;
    private EventDeleteRequestAdapter adapter = null;
    private GestureDetectorCompat detector = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_events_approve);

        myEModel = EventDeleteRequestModel.getSingleton();

        setUpEventModels();

        /*
        deleteBtn.setOnClickListener(v -> {
            ParseQuery<ParseObject> queryEvents = ParseQuery.getQuery("Events");
            // Query parameters based on the item name
            queryEvents.whereEqualTo("objectId", fetchID);
            queryEvents.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List<ParseObject> event, ParseException e) {
                    if (e == null) {
                        event.get(0).deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    // Success
                                    showAlert("Delete Status:", "Object has been deleted Successfully");
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
        });
         */
        progressDialog = new ProgressDialog(DeleteEventsApproveActivity.this);

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
                        Toast.makeText(DeleteEventsApproveActivity.this, "Admin Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DeleteEventsApproveActivity.this, AdminHomeActivity.class));
                        break;
                    case R.id.admin_nav_event_home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(DeleteEventsApproveActivity.this, "Event Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DeleteEventsApproveActivity.this, DeleteEventsApproveActivity.class));
                        break;
                    case R.id.admin_nav_add_event:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(DeleteEventsApproveActivity.this, "Add Event is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DeleteEventsApproveActivity.this, DeleteEventsApproveActivity.class));
                        break;
                    case R.id.admin_nav_items_home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(DeleteEventsApproveActivity.this, "Items Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DeleteEventsApproveActivity.this, DeleteEventsApproveActivity.class));
                        break;
                    case R.id.admin_nav_add_items:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(DeleteEventsApproveActivity.this, "Event Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DeleteEventsApproveActivity.this, DeleteEventsApproveActivity.class));
                        break;
                    case R.id.admin_nav_logout:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(DeleteEventsApproveActivity.this, "Logout is Clicked", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DeleteEventsApproveActivity.this, "Share Link is Clicked", Toast.LENGTH_SHORT).show();break;
                    case R.id.admin_nav_contact:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(DeleteEventsApproveActivity.this, "Contact us is Clicked", Toast.LENGTH_SHORT).show();break;
                    default:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }return true;
            }
        });
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DeleteEventsApproveActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // don't forget to change the line below with the names of your Activities
                        Intent intent = new Intent(DeleteEventsApproveActivity.this, LoginActivity.class);
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
                        String deleteID, eventID, eventName, delReason, eventEndDt, eventDescription, eventAddressLine1,
                                eventAddressLine2, eventCity, eventState, eventCountry, eventZipcode, eventNotes, updateReason;
                        eventID = eventObj.getObjectId();
                        deleteID = eventObj.getString("DeleteEventID");
                        eventName = eventObj.getString("eventName");
                        delReason = eventObj.getString("DeleteReason");


                        Log.v("ObjectID", String.valueOf(eventID));

                        myEModel.EventDeleteRequestsList.add(new EventDeleteRequestModel.EventDeleteRequests(deleteID, eventID, eventName, delReason));

                        Log.v("Setup EventList Size:", String.valueOf(myEModel.EventDeleteRequestsList.size()));
                        adapter = new EventDeleteRequestAdapter(DeleteEventsApproveActivity.this, myEModel);
                        Log.v("adapter", String.valueOf(adapter.getItemCount()));

                        recyclerView = findViewById(R.id.adminRecyclerView);
                        recyclerView.setAdapter(adapter);

                        recyclerView.setLayoutManager(new LinearLayoutManager(DeleteEventsApproveActivity.this));

                        detector = new GestureDetectorCompat(DeleteEventsApproveActivity.this, new DeleteEventsApproveActivity.RecyclerViewOnGestureListener());
                        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
                            @Override
                            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                                return detector.onTouchEvent(e);
                            }
                        });
                    } else {
                        Toast.makeText(DeleteEventsApproveActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                if (holder instanceof EventDeleteRequestAdapter.RequestHolder) {
                    int position = holder.getAdapterPosition();
                    // handle single tap
                    String objId = myEModel.EventDeleteRequestsList.get(position).eventID;
                    String delId = myEModel.EventDeleteRequestsList.get(position).deleteID;
                    String objName = myEModel.EventDeleteRequestsList.get(position).eventName;
                    Log.v("Selected EventID: ", objId);

                    Toast.makeText(DeleteEventsApproveActivity.this, "Selected EventID: " + objId, Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(DeleteEventsApproveActivity.this)
                            .setTitle("Approve Delete Request")
                            .setMessage("Do you want to approve below event? \nEvent ID: " + delId + "\nEvent Name: " + objName)
                            .setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Log.v("Button Selected: ", "Approve");

                                    ParseQuery<ParseObject> queryEvents = ParseQuery.getQuery("Events");
                                    // Query parameters based on the item name
                                    queryEvents.whereEqualTo("objectId", delId);
                                    queryEvents.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(final List<ParseObject> event, ParseException e) {
                                            if (e == null) {
                                                event.get(0).deleteInBackground(new DeleteCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e == null) {
                                                            // Success
                                                            Toast.makeText(DeleteEventsApproveActivity.this, "Event delete request is rejected...!", Toast.LENGTH_LONG).show();

                                                            // don't forget to change the line below with the names of your Activities
                                                            Intent intent = new Intent(DeleteEventsApproveActivity.this, DeleteEventsApproveActivity.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                        } else {
                                                            Log.v("Delete Inner Ex:", e.getMessage());
                                                        }
                                                    }
                                                });
                                                Toast.makeText(DeleteEventsApproveActivity.this, "Event is Deleted!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Log.v("Delete Parse Outer Ex: ", e.getMessage());
                                            }
                                        }
                                    });

                                }
                            })
                            .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    ParseQuery<ParseObject> queryEvents = ParseQuery.getQuery("EventDeleteRequest");
                                    // Query parameters based on the item name
                                    queryEvents.whereEqualTo("objectId", objId);
                                    queryEvents.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(final List<ParseObject> event, ParseException e) {
                                            if (e == null) {
                                                event.get(0).deleteInBackground(new DeleteCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e == null) {
                                                            // Success
                                                            Toast.makeText(DeleteEventsApproveActivity.this, "Event delete request is rejected...!", Toast.LENGTH_LONG).show();

                                                            // don't forget to change the line below with the names of your Activities
                                                            Intent intent = new Intent(DeleteEventsApproveActivity.this, DeleteEventsApproveActivity.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                        } else {
                                                            Log.v("Delete Inner Ex:", e.getMessage());
                                                        }
                                                    }
                                                });
                                            } else {
                                                Log.v("Delete Parse Outer Ex: ", e.getMessage());
                                            }
                                        }
                                    });
                                }
                            })
                            .setNeutralButton("Ignore", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    // don't forget to change the line below with the names of your Activities
                                    Log.v("Button Selected: ", "Ignore");
                                    Toast.makeText(DeleteEventsApproveActivity.this, "Event is Ignored.", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
                return true;
            }
            return false;
        }
    }
}
