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
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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
import com.parse.SaveCallback;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DeleteItemsApproveActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ImageView logoutBtn, ivMenu;

    private ItemDeleteRequestModel myIModel ;
    private RecyclerView recyclerView=null;
    private ItemDeleteRequestAdapter adapter = null;
    private GestureDetectorCompat detector = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_items_approve);
        myIModel = ItemDeleteRequestModel.getSingleton();
        getSupportActionBar().hide();


        setUpEventModels();

        progressDialog = new ProgressDialog(DeleteItemsApproveActivity.this);

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
        }

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
                        Toast.makeText(DeleteItemsApproveActivity.this, "Admin Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DeleteItemsApproveActivity.this, AdminHomeActivity.class));
                        break;
                    case R.id.admin_nav_switch_user:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(DeleteItemsApproveActivity.this, "Switching to user...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DeleteItemsApproveActivity.this, HomeActivity.class));
                        break;
                    case R.id.admin_nav_logout:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(DeleteItemsApproveActivity.this, "Logout is Clicked", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DeleteItemsApproveActivity.this, "Share Link is Clicked", Toast.LENGTH_SHORT).show();break;
                    case R.id.admin_nav_contact:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(DeleteItemsApproveActivity.this, "Contact us is Clicked", Toast.LENGTH_SHORT).show();break;
                    default:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }return true;
            }
        });
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DeleteItemsApproveActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // don't forget to change the line below with the names of your Activities
                        Intent intent = new Intent(DeleteItemsApproveActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    private void setUpEventModels() {
        // Read Parse Objects
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ItemsDeleteRequest");
        query.whereEqualTo("isApproved", Boolean.FALSE);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, ParseException e) {
                for (ParseObject itemObj : results) {
                    if (e == null) {
                        String deleteID, itemID, itemName, delReason;
                        itemID = itemObj.getObjectId();
                        deleteID = itemObj.getString("DeleteItemID");
                        itemName = itemObj.getString("itemName");
                        delReason = itemObj.getString("DeleteReason");


                        Log.v("ObjectID", String.valueOf(itemID));

                        myIModel.ItemDeleteRequestsList.add(new ItemDeleteRequestModel.ItemDeleteRequests(deleteID, itemID, itemName, delReason));

                        Log.v("Setup EventList Size:", String.valueOf(myIModel.ItemDeleteRequestsList.size()));
                        adapter = new ItemDeleteRequestAdapter(DeleteItemsApproveActivity.this, myIModel);
                        Log.v("adapter", String.valueOf(adapter.getItemCount()));

                        recyclerView = findViewById(R.id.adminRecyclerView);
                        recyclerView.setAdapter(adapter);

                        recyclerView.setLayoutManager(new LinearLayoutManager(DeleteItemsApproveActivity.this));

                        detector = new GestureDetectorCompat(DeleteItemsApproveActivity.this, new DeleteItemsApproveActivity.RecyclerViewOnGestureListener());
                        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
                            @Override
                            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                                return detector.onTouchEvent(e);
                            }
                        });
                    } else {
                        Toast.makeText(DeleteItemsApproveActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                if (holder instanceof ItemDeleteRequestAdapter.RequestHolder) {
                    int position = holder.getAdapterPosition();
                    // handle single tap
                    String objId = myIModel.ItemDeleteRequestsList.get(position).ItemID;
                    String delId = myIModel.ItemDeleteRequestsList.get(position).deleteID;
                    String objName = myIModel.ItemDeleteRequestsList.get(position).ItemName;
                    Log.v("Selected EventID: ", objId);
                    final ParseObject[] objEmail = new ParseObject[1];
                    final String[] userEmail = new String[1];
                    ParseQuery<ParseObject> queryEmail = ParseQuery.getQuery("ItemsDeleteRequest");
                    queryEmail.whereEqualTo("objectId", objId);
                    queryEmail.getFirstInBackground(new GetCallback<ParseObject>() {
                        public void done(ParseObject event, ParseException e){
                            if (e == null) {
                                objEmail[0] =event;
                                userEmail[0] = event.getString("byUser");
                            } else {
                                Log.v("Email Back4App", e.getMessage());
                            }
                        }
                    });

                    Toast.makeText(DeleteItemsApproveActivity.this, "Selected ItemID: " + objId, Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(DeleteItemsApproveActivity.this)
                            .setTitle("Item Delete Request")
                            .setMessage("Do you want to Remove below Item? \nItem ID: " + delId + "\nItem Name: " + objName)
                            .setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Log.v("Button Selected: ", "Approve");

                                    App.sendEmail( userEmail[0],  "Your Request Has Been Approved", "Dear User,\n" +
                                            "\n" +
                                            "I am pleased to inform you that your request has been approved. We have carefully reviewed your request and have found it to be appropriate and relevant.\n" +
                                            "\n" +
                                            "If you have any questions or concerns, please do not hesitate to contact us. We are always here to assist you.\n" +
                                            "\n " +objEmail[0].toString()+
                                            "\nSupport Team,\n" +
                                            "Freebies for Newbies");

                                    ParseQuery<ParseObject> queryEvents = ParseQuery.getQuery("Items");
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
                                                            Toast.makeText(DeleteItemsApproveActivity.this, "Item delete request is approved...!", Toast.LENGTH_LONG).show();

                                                            ParseQuery<ParseObject> query = ParseQuery.getQuery("ItemsDeleteRequest");
                                                            query.getInBackground(objId, new GetCallback<ParseObject>() {
                                                                public void done(ParseObject object, ParseException e) {
                                                                    if (e == null) {
                                                                        object.put("isApproved", true);
                                                                        object.saveInBackground(new SaveCallback() {
                                                                            @Override
                                                                            public void done(ParseException e) {
                                                                                if (e == null) {
                                                                                    //code here
                                                                                    // don't forget to change the line below with the names of your Activities
                                                                                    Intent intent = new Intent(DeleteItemsApproveActivity.this, DeleteItemsApproveActivity.class);
                                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                    startActivity(intent);
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                            });

                                                        } else {
                                                            Log.v("Delete Inner Ex:", e.getMessage());
                                                        }
                                                    }
                                                });
                                                Toast.makeText(DeleteItemsApproveActivity.this, "Item is Deleted!", Toast.LENGTH_SHORT).show();
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
                                    App.sendEmail( userEmail[0],"Your Request Has Been Rejected","Dear User,\n" +
                                            "\n" +
                                            "Thank you for submitting your request. After careful consideration, we regret to inform you that we are unable to approve your request at this time.\n" +
                                            "\n" +
                                            "We understand that this may be disappointing news, but we encourage you to update latest data. Please feel free to contact us if you have any questions or would like to discuss this further.\n" +
                                            "\n" + objEmail[0].toString()+
                                            "\nSupport Team,\n" +
                                            "Freebies for Newbies");
                                    ParseQuery<ParseObject> queryEvents = ParseQuery.getQuery("ItemsDeleteRequest");
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
                                                            Toast.makeText(DeleteItemsApproveActivity.this, "Item delete request is rejected...!", Toast.LENGTH_LONG).show();
                                                            // don't forget to change the line below with the names of your Activities
                                                            Intent intent = new Intent(DeleteItemsApproveActivity.this, DeleteItemsApproveActivity.class);
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
                                    Toast.makeText(DeleteItemsApproveActivity.this, "Item is Ignored.", Toast.LENGTH_SHORT).show();
                                }
                            });
                    AlertDialog ok = builder.create();
                    ok.show();

                }
                return true;
            }
            return false;
        }
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