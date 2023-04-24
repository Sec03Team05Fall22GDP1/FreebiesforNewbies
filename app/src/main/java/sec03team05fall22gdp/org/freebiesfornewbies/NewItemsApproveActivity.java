package sec03team05fall22gdp.org.freebiesfornewbies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.EditText;
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

public class NewItemsApproveActivity extends AppCompatActivity {

    private ImageView logoutBtn, ivMenu;
    private ProgressDialog progressDialog;

    private ItemModel freeItemModel ;
    private RecyclerView recyclerView=null;
    private ItemAdapter adapter = null;
    private GestureDetectorCompat detector = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_items_approve);
        progressDialog = new ProgressDialog(NewItemsApproveActivity.this);
        getSupportActionBar().hide();


        freeItemModel=ItemModel.getSingleton();
        setUpEventModels();

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
                        Toast.makeText(NewItemsApproveActivity.this, "Admin Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NewItemsApproveActivity.this, AdminHomeActivity.class));
                        break;
                    case R.id.admin_nav_switch_user:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(NewItemsApproveActivity.this, "Switching to user...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NewItemsApproveActivity.this, HomeActivity.class));
                        break;
                    case R.id.admin_nav_logout:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(NewItemsApproveActivity.this, "Logout is Clicked", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(NewItemsApproveActivity.this, "Share Link is Clicked", Toast.LENGTH_SHORT).show();break;
                    case R.id.admin_nav_contact:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(NewItemsApproveActivity.this, "Contact us is Clicked", Toast.LENGTH_SHORT).show();break;
                    default:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }return true;
            }
        });

    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewItemsApproveActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // don't forget to change the line below with the names of your Activities
                        Intent intent = new Intent(NewItemsApproveActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    private void setUpEventModels() {
        // Read Parse Objects
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Items");
        query.whereEqualTo("isAvailable",Boolean.TRUE).whereEqualTo("isApproved", Boolean.FALSE);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, ParseException e) {
                for (ParseObject itemObj : results) {
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

                        Log.v("ObjectID",String.valueOf(itemID));

                        freeItemModel.itemsList.add(new ItemModel.Items(itemID, itemName, itemURL, itemAddressLine1, itemAddressLine2, itemCity, itemState, itemCountry, itemZipcode,itemDescription));

                        Log.v("Setup EventList Size:", String.valueOf(freeItemModel.itemsList.size()));
                        adapter = new ItemAdapter(NewItemsApproveActivity.this, freeItemModel);
                        Log.v("adapter", String.valueOf(adapter.getItemCount()));

                        recyclerView = findViewById(R.id.itemRecyclerView);
                        recyclerView.setAdapter(adapter);

                        recyclerView.setLayoutManager(new LinearLayoutManager(NewItemsApproveActivity.this));

                        detector = new GestureDetectorCompat(NewItemsApproveActivity.this, new NewItemsApproveActivity.RecyclerViewOnGestureListener());
                        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener(){
                            @Override
                            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                                return detector.onTouchEvent(e);
                            }
                        });
                    } else {
                        Toast.makeText(NewItemsApproveActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                if (holder instanceof ItemAdapter.MyViewHolder) {
                    int position = holder.getAdapterPosition();
                    // handle single tap
                    String sItemId= freeItemModel.itemsList.get(position).itemID;
                    String sItemName= freeItemModel.itemsList.get(position).itemName;
                    Log.v("Selected ItemID: ",sItemId);
                    final ParseObject[] objEmail = new ParseObject[1];
                    final String[] userEmail = new String[1];
                    ParseQuery<ParseObject> queryEmail = ParseQuery.getQuery("Items");
                    queryEmail.whereEqualTo("objectId", sItemId);
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(NewItemsApproveActivity.this)
                            .setTitle("Approve New Request")
                            .setMessage("Do you want to approve below Item? \nItem ID: "+sItemId+"\nItem Name: "+sItemName)
                            .setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Log.v("Button Selected: ","Approve");

                                    App.sendEmail( userEmail[0],  "Your Request Has Been Approved", "Dear User,\n" +
                                            "\n" +
                                            "I am pleased to inform you that your request has been approved. We have carefully reviewed your request and have found it to be appropriate and relevant.\n" +
                                            "\n" +
                                            "If you have any questions or concerns, please do not hesitate to contact us. We are always here to assist you.\n" +
                                            "\n " +objEmail[0].toString()+
                                            "\nSupport Team,\n" +
                                            "Freebies for Newbies");

                                    Toast.makeText(NewItemsApproveActivity.this, "Item is approved!", Toast.LENGTH_SHORT).show();

                                    try{
                                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Items");

                                        // Retrieve the object by id
                                        query.getInBackground(sItemId, new GetCallback<ParseObject>() {
                                            public void done(ParseObject itemObject, ParseException e) {
                                                if (e == null) {
                                                    itemObject.put("isApproved",Boolean.TRUE );
                                                    // Saving object
                                                    progressDialog.show();
                                                    itemObject.saveInBackground(new SaveCallback() {
                                                        @Override
                                                        public void done(ParseException e) {
                                                            progressDialog.dismiss();
                                                            if (e == null) {
                                                                // Success
                                                                Toast.makeText(NewItemsApproveActivity.this, "Event updated in database...!", Toast.LENGTH_LONG).show();
                                                                // don't forget to change the line below with the names of your Activities
                                                                Intent intent = new Intent(NewItemsApproveActivity.this, NewItemsApproveActivity.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                startActivity(intent);
                                                            } else {
                                                                // Error
                                                                Toast.makeText(NewItemsApproveActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }catch (Exception e){
                                        Toast.makeText(NewItemsApproveActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }).setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    // don't forget to change the line below with the names of your Activities
                                    Log.v("Button Selected: ","Deny");
                                    App.sendEmail( userEmail[0],"Your Request Has Been Rejected","Dear User,\n" +
                                            "\n" +
                                            "Thank you for submitting your request. After careful consideration, we regret to inform you that we are unable to approve your request at this time.\n" +
                                            "\n" +
                                            "We understand that this may be disappointing news, but we encourage you to update latest data. Please feel free to contact us if you have any questions or would like to discuss this further.\n" +
                                            "\n" + objEmail[0].toString()+
                                            "\nSupport Team,\n" +
                                            "Freebies for Newbies");
                                    Toast.makeText(NewItemsApproveActivity.this, "Item is rejected.", Toast.LENGTH_SHORT).show();
                                    ParseQuery<ParseObject> queryItems = ParseQuery.getQuery("Items");
                                    // Query parameters based on the item name
                                    queryItems.whereEqualTo("objectId", sItemId);
                                    queryItems.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(final List<ParseObject> event, ParseException e) {
                                            if (e == null) {
                                                event.get(0).deleteInBackground(new DeleteCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e == null) {
                                                            // Success
                                                            Toast.makeText(NewItemsApproveActivity.this, "Event Removed in database...!", Toast.LENGTH_LONG).show();
                                                            // don't forget to change the line below with the names of your Activities
                                                            Intent intent = new Intent(NewItemsApproveActivity.this, NewItemsApproveActivity.class);
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
                                    Toast.makeText(NewItemsApproveActivity.this, "Item is Ignored.", Toast.LENGTH_SHORT).show();
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