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
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ItemHomeActivity extends AppCompatActivity {

    private ImageView logoutBtn, createItemBtn, ivNameSearch, ivDateSearch, ivMenu;
    private EditText searchNameET;
    private ProgressDialog progressDialog;
    private Button dateButton;
    private DatePickerDialog datePickerDialog;

    private ItemModel freeItemModel ;
    private RecyclerView recyclerView=null;
    private ItemAdapter adapter = null;
    private GestureDetectorCompat detector = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_home);

        progressDialog = new ProgressDialog(ItemHomeActivity.this);

        freeItemModel=ItemModel.getSingleton();
        setUpEventModels();

        logoutBtn = findViewById(R.id.ivlogout);
        createItemBtn = findViewById(R.id.ivCreateEvent);
        //date button set up

        createItemBtn.setOnClickListener(v -> {
            startActivity(new Intent(ItemHomeActivity.this, CreateFreeitemActivity.class));
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

        ivMenu = findViewById(R.id.ivMenuIcon);
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
                        Toast.makeText(ItemHomeActivity.this, "Switching to Admin...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ItemHomeActivity.this, AdminHomeActivity.class));
                        break;
                    case R.id.nav_event_home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(ItemHomeActivity.this, "Event Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ItemHomeActivity.this, HomeActivity.class));
                        break;
                    case R.id.nav_add_event:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(ItemHomeActivity.this, "Add Event is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ItemHomeActivity.this, CreateEventActivity.class));
                        break;
                    case R.id.nav_items_home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(ItemHomeActivity.this, "Items Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ItemHomeActivity.this, ItemHomeActivity.class));
                        break;
                    case R.id.nav_add_items:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(ItemHomeActivity.this, "Item add is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ItemHomeActivity.this, CreateFreeitemActivity.class));
                        break;
                    case R.id.nav_logout:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(ItemHomeActivity.this, "Logout is Clicked", Toast.LENGTH_SHORT).show();
                        progressDialog.show();
                        // logging out of Parse
                        ParseUser.logOutInBackground(e -> {
                            progressDialog.dismiss();
                            if (e == null)
                                showAlert("So, you're going...", "Ok...Bye-bye then");
                        });
                        break;
                    case R.id.nav_share:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(ItemHomeActivity.this, "Share Link is Clicked", Toast.LENGTH_SHORT).show();break;
                    case R.id.nav_contact:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(ItemHomeActivity.this, "Contact us is Clicked", Toast.LENGTH_SHORT).show();break;
                    default:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }return true;
            }
        });

        ivNameSearch = findViewById(R.id.ivSearch);
        ivNameSearch.setOnClickListener(v ->{
            searchNameET = findViewById(R.id.etSearchtext);
            String searchText = searchNameET.getText().toString();
            if(!searchText.matches("")){
                freeItemModel.itemsList.clear();
                // Read Parse Objects
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Items");
                query.whereEqualTo("isAvailable",Boolean.TRUE).whereEqualTo("isApproved", Boolean.FALSE).whereContains("eventName".toLowerCase(Locale.ROOT),searchText.toLowerCase(Locale.ROOT));
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
                                adapter = new ItemAdapter(ItemHomeActivity.this, freeItemModel);
                                Log.v("adapter", String.valueOf(adapter.getItemCount()));

                                recyclerView = findViewById(R.id.itemRecyclerView);
                                recyclerView.setAdapter(adapter);

                                recyclerView.setLayoutManager(new LinearLayoutManager(ItemHomeActivity.this));

                                detector = new GestureDetectorCompat(ItemHomeActivity.this, new ItemHomeActivity.RecyclerViewOnGestureListener());
                                recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener(){
                                    @Override
                                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                                        return detector.onTouchEvent(e);
                                    }
                                });
                            } else {
                                Toast.makeText(ItemHomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }else{
                Toast.makeText(ItemHomeActivity.this, "Search text is empty.\nProvide input and try again", Toast.LENGTH_LONG).show();
                searchNameET.setError("Search text is empty");
            }
        });

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
                        adapter = new ItemAdapter(ItemHomeActivity.this, freeItemModel);
                        Log.v("adapter", String.valueOf(adapter.getItemCount()));

                        recyclerView = findViewById(R.id.itemRecyclerView);
                        recyclerView.setAdapter(adapter);

                        recyclerView.setLayoutManager(new LinearLayoutManager(ItemHomeActivity.this));

                        detector = new GestureDetectorCompat(ItemHomeActivity.this, new ItemHomeActivity.RecyclerViewOnGestureListener());
                        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener(){
                            @Override
                            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                                return detector.onTouchEvent(e);
                            }
                        });
                    } else {
                        Toast.makeText(ItemHomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ItemHomeActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // don't forget to change the line below with the names of your Activities
                        Intent intent = new Intent(ItemHomeActivity.this, LoginActivity.class);
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
                if (holder instanceof ItemAdapter.MyViewHolder) {
                    int position = holder.getAdapterPosition();
                    // handle single tap
                    String sItemId= freeItemModel.itemsList.get(position).itemID;
                    Log.v("Selected ItemID: ",sItemId);

                    Intent intent = new Intent(ItemHomeActivity.this, FreeItemDetailedActivity.class);
                    intent.putExtra("itemID",sItemId);
                    startActivity(intent);
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