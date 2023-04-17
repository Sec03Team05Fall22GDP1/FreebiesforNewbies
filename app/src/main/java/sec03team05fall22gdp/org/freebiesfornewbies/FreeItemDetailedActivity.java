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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FreeItemDetailedActivity extends AppCompatActivity {

    private ImageView logoutBtn, ivMenu;
    private ProgressDialog progressDialog;
    private TextView itemNameTV, itemURLTV, itemLocationTV, itemDescTV;

    private Handler handler = new Handler();
    private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            // logging out of Parse
            ParseUser.logOutInBackground(e -> {
                if (e == null){
                    if (!isUserActive) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        moveTaskToBack(true);
                    }
                }
            });
            Log.d("MyApp", "Performing operation after 2 minutes in background");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_item_detailed);

        handler.postDelayed(myRunnable, 2 * 60 * 1000);

        Intent intent = getIntent();
        String fetchID =  intent.getStringExtra("itemID");
        progressDialog = new ProgressDialog(FreeItemDetailedActivity.this);

        itemNameTV=findViewById(R.id.tvDItem);
        itemURLTV=findViewById(R.id.tvDItemURL);
        itemLocationTV=findViewById(R.id.tvDItemLoc);
        itemDescTV=findViewById(R.id.tvDItemDesc);
        logoutBtn = findViewById(R.id.ivEDlogout);
        fetchItem(fetchID);

        Button updateBtn, deleteBtn;
        updateBtn = findViewById(R.id.btnDetailUpdate);
        deleteBtn = findViewById(R.id.btnDetailDelete);

        updateBtn.setOnClickListener(v -> {
            handler.removeCallbacks(myRunnable);
            Intent intent1 = new Intent(FreeItemDetailedActivity.this, FreeItemUpdateActivity.class);
            intent1.putExtra("itemID",fetchID);
            startActivity(intent1);
        });
        deleteBtn.setOnClickListener(v -> {
            handler.removeCallbacks(myRunnable);
            Intent intent1 = new Intent(FreeItemDetailedActivity.this, FreeItemDeleteActivity.class);
            intent1.putExtra("itemID",fetchID);
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
                        handler.removeCallbacks(myRunnable);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(FreeItemDetailedActivity.this, "Switching to Admin...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(FreeItemDetailedActivity.this, AdminHomeActivity.class));
                        break;
                    case R.id.nav_event_home:
                        handler.removeCallbacks(myRunnable);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(FreeItemDetailedActivity.this, "Event Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(FreeItemDetailedActivity.this, HomeActivity.class));
                        break;
                    case R.id.nav_add_event:
                        handler.removeCallbacks(myRunnable);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(FreeItemDetailedActivity.this, "Add Event is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(FreeItemDetailedActivity.this, CreateEventActivity.class));
                        break;
                    case R.id.nav_items_home:
                        handler.removeCallbacks(myRunnable);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(FreeItemDetailedActivity.this, "Items Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(FreeItemDetailedActivity.this, ItemHomeActivity.class));
                        break;
                    case R.id.nav_add_items:
                        handler.removeCallbacks(myRunnable);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(FreeItemDetailedActivity.this, "Event Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(FreeItemDetailedActivity.this, CreateFreeitemActivity.class));
                        break;
                    case R.id.nav_logout:
                        handler.removeCallbacks(myRunnable);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(FreeItemDetailedActivity.this, "Logout is Clicked", Toast.LENGTH_SHORT).show();
                        progressDialog.show();
                        // logging out of Parse
                        ParseUser.logOutInBackground(e -> {
                            progressDialog.dismiss();
                            if (e == null){
                                Intent intent = new Intent(FreeItemDetailedActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                        break;
                    case R.id.nav_share:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(FreeItemDetailedActivity.this, "Share Link is Clicked", Toast.LENGTH_SHORT).show();break;
                    case R.id.nav_contact:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(FreeItemDetailedActivity.this, "Contact us is Clicked", Toast.LENGTH_SHORT).show();break;
                    default:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }return true;
            }
        });
    }

    private void fetchItem(String itemID){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Items");
        query.whereEqualTo("objectId", itemID);
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

                        String Loc;
                        if(itemAddressLine2.matches("")){
                            Loc = itemAddressLine1+"\n"+
                                    itemCity+", "+ itemState+", "+itemCountry+" - "+itemZipcode;
                        }else{
                            Loc = itemAddressLine1+"\n"+itemAddressLine2+"\n"+
                                    itemCity+", "+ itemState+", "+itemCountry+" - "+itemZipcode;
                        }

                        itemNameTV.setText(itemName);
                        itemURLTV.setText(itemURL);
                        itemLocationTV.setText(Loc);
                        itemDescTV.setText(itemDescription);

                        Log.v("Fetched ItemID: ",itemID);
                        Log.v("Fetched ItemName: ",itemName);

                    } else {
                        Toast.makeText(FreeItemDetailedActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FreeItemDetailedActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        handler.removeCallbacks(myRunnable);
                        // don't forget to change the line below with the names of your Activities
                        Intent intent = new Intent(FreeItemDetailedActivity.this, ItemHomeActivity.class);
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
                handler.removeCallbacks(myRunnable);
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
        handler.postDelayed(myRunnable, 2 * 60 * 1000);
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
        handler.postDelayed(myRunnable, 2 * 60 * 1000);
        Log.d("onPause", "inside");

    }
}