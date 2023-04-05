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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class UpdateItemsApproveActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ImageView logoutBtn, ivMenu;

    private ItemUpdateRequestModel myIModel ;
    private RecyclerView recyclerView=null;
    private ItemUpdateRequestAdapter adapter = null;
    private GestureDetectorCompat detector = null;

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
        setContentView(R.layout.activity_update_items_approve);

        handler.postDelayed(myRunnable, 2 * 60 * 1000);

        myIModel = ItemUpdateRequestModel.getSingleton();

        setUpEventModels();

        progressDialog = new ProgressDialog(UpdateItemsApproveActivity.this);

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
                        Toast.makeText(UpdateItemsApproveActivity.this, "Admin Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UpdateItemsApproveActivity.this, AdminHomeActivity.class));
                        break;
                    case R.id.admin_nav_switch_user:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(UpdateItemsApproveActivity.this, "Switching to user...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UpdateItemsApproveActivity.this, HomeActivity.class));
                        break;
                    case R.id.admin_nav_logout:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(UpdateItemsApproveActivity.this, "Logout is Clicked", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(UpdateItemsApproveActivity.this, "Share Link is Clicked", Toast.LENGTH_SHORT).show();break;
                    case R.id.admin_nav_contact:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(UpdateItemsApproveActivity.this, "Contact us is Clicked", Toast.LENGTH_SHORT).show();break;
                    default:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }return true;
            }
        });
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateItemsApproveActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // don't forget to change the line below with the names of your Activities
                        Intent intent = new Intent(UpdateItemsApproveActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    private void setUpEventModels() {
        // Read Parse Objects
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ItemsUpdateRequest");
        query.whereEqualTo("isAvailable",Boolean.TRUE).whereEqualTo("isApproved", Boolean.FALSE);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, ParseException e) {
                for (ParseObject itemObj : results) {
                    if (e == null) {
                        String itemID,updateID,updateReason, itemName, itemURL, itemAddressLine1, itemAddressLine2, itemCity, itemState, itemCountry, itemZipcode,itemDescription;
                        itemID=itemObj.getObjectId();
                        updateID= itemObj.getString("updateItemId");
                        itemName=itemObj.getString("itemName");
                        itemURL=itemObj.getString("itemURL");
                        itemAddressLine1=itemObj.getString("itemAddressLine1");
                        itemCity=itemObj.getString("itemCity");
                        itemState=itemObj.getString("itemState");
                        itemCountry=itemObj.getString("itemCountry");
                        itemZipcode=itemObj.getString("itemZipcode");
                        itemAddressLine2=itemObj.getString("itemAddressLine2");
                        itemDescription= itemObj.getString("itemDescription");
                        updateReason= itemObj.getString("updateReason");
                        Log.v("ObjectID",String.valueOf(itemID));

                        myIModel.itemsList.add(new ItemUpdateRequestModel.ItemUpdateRequests(itemID, updateID, itemName, itemURL, itemAddressLine1, itemAddressLine2, itemCity, itemState, itemCountry, itemZipcode,itemDescription,updateReason));

                        Log.v("Setup EventList Size:", String.valueOf(myIModel.itemsList.size()));
                        adapter = new ItemUpdateRequestAdapter(UpdateItemsApproveActivity.this, myIModel);
                        Log.v("adapter", String.valueOf(adapter.getItemCount()));

                        recyclerView = findViewById(R.id.itemRecyclerView);
                        recyclerView.setAdapter(adapter);

                        recyclerView.setLayoutManager(new LinearLayoutManager(UpdateItemsApproveActivity.this));

                        detector = new GestureDetectorCompat(UpdateItemsApproveActivity.this, new UpdateItemsApproveActivity.RecyclerViewOnGestureListener());
                        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener(){
                            @Override
                            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                                return detector.onTouchEvent(e);
                            }
                        });
                    } else {
                        Toast.makeText(UpdateItemsApproveActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                if (holder instanceof ItemUpdateRequestAdapter.MyViewHolder) {
                    int position = holder.getAdapterPosition();
                    // handle single tap
                    String sItemId= myIModel.itemsList.get(position).itemID;
                    String sIUpdateId= myIModel.itemsList.get(position).updateID;
                    String sItemName= myIModel.itemsList.get(position).itemName;
                    String sUpdateReason= myIModel.itemsList.get(position).updateReason;
                    Log.v("Selected ItemID: ",sItemId);

                    Toast.makeText(UpdateItemsApproveActivity.this, "Selected ItemID: "+sItemId, Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateItemsApproveActivity.this)
                            .setTitle("Approve Update Request")
                            .setMessage("Do you want to approve below event? \nEvent ID: "+sIUpdateId+"\nEvent Name: "+sItemName+"\nUpdate Reason: "+sUpdateReason)
                            .setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Log.v("Button Selected: ","Approve");

                                    Toast.makeText(UpdateItemsApproveActivity.this, "Item is approved!", Toast.LENGTH_SHORT).show();

                                    try{
                                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Items");

                                        // Retrieve the object by id
                                        query.getInBackground(sIUpdateId, new GetCallback<ParseObject>() {
                                            public void done(ParseObject itemObject, ParseException e) {
                                                if (e == null) {
                                                    itemObject.put("itemName", myIModel.itemsList.get(position).itemName );
                                                    itemObject.put("itemURL", myIModel.itemsList.get(position).itemURL);
                                                    itemObject.put("isAvailable", true);
                                                    itemObject.put("itemAddressLine1", myIModel.itemsList.get(position).itemAddressLine1);
                                                    itemObject.put("itemCity", myIModel.itemsList.get(position).itemCity);
                                                    itemObject.put("itemState", myIModel.itemsList.get(position).itemState);
                                                    itemObject.put("itemCountry", myIModel.itemsList.get(position).itemCountry);
                                                    itemObject.put("itemZipcode", myIModel.itemsList.get(position).itemZipcode);
                                                    itemObject.put("itemAddressLine2", myIModel.itemsList.get(position).itemAddressLine2);
                                                    itemObject.put("itemDescription", myIModel.itemsList.get(position).itemDescription);

                                                    itemObject.put("isApproved",Boolean.TRUE );
                                                    // Saving object
                                                    progressDialog.show();
                                                    itemObject.saveInBackground(new SaveCallback() {
                                                        @Override
                                                        public void done(ParseException e) {
                                                            progressDialog.dismiss();
                                                            if (e == null) {
                                                                // Success
                                                                Toast.makeText(UpdateItemsApproveActivity.this, "Item updated in database...!", Toast.LENGTH_LONG).show();
                                                                // don't forget to change the line below with the names of your Activities

                                                                ParseQuery<ParseObject> query = ParseQuery.getQuery("ItemsUpdateRequest");
                                                                query.getInBackground(sItemId, new GetCallback<ParseObject>() {
                                                                    public void done(ParseObject object, ParseException e) {
                                                                        if (e == null) {
                                                                            object.put("isApproved", true);
                                                                            object.saveInBackground(new SaveCallback() {
                                                                                @Override
                                                                                public void done(ParseException e) {
                                                                                    if (e == null) {
                                                                                        //code here
                                                                                        Intent intent = new Intent(UpdateItemsApproveActivity.this, UpdateItemsApproveActivity.class);
                                                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                        startActivity(intent);
                                                                                    }
                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                });


                                                            } else {
                                                                // Error
                                                                Toast.makeText(UpdateItemsApproveActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }catch (Exception e){
                                        Toast.makeText(UpdateItemsApproveActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }).setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    // don't forget to change the line below with the names of your Activities
                                    Log.v("Button Selected: ","Approve");
                                    Toast.makeText(UpdateItemsApproveActivity.this, "Item is rejected.", Toast.LENGTH_SHORT).show();
                                    ParseQuery<ParseObject> queryEvents = ParseQuery.getQuery("ItemsUpdateRequest");
                                    // Query parameters based on the item name
                                    queryEvents.whereEqualTo("objectId", sItemId);
                                    queryEvents.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(final List<ParseObject> event, ParseException e) {
                                            if (e == null) {
                                                event.get(0).deleteInBackground(new DeleteCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e == null) {
                                                            // Success
                                                            Toast.makeText(UpdateItemsApproveActivity.this, "Item Removed in database...!", Toast.LENGTH_LONG).show();

                                                            // don't forget to change the line below with the names of your Activities
                                                            Intent intent = new Intent(UpdateItemsApproveActivity.this, UpdateItemsApproveActivity.class);
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
                                    Toast.makeText(UpdateItemsApproveActivity.this, "Item is Ignored.", Toast.LENGTH_SHORT).show();
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