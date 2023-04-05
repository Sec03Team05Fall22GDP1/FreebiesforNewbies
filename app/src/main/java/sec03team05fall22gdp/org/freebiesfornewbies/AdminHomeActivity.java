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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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
import java.util.Timer;
import java.util.TimerTask;

public class AdminHomeActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private ImageView logoutBtn, ivMenu;
    private AdminRequestModel adminReqModel ;

    private RecyclerView recyclerView=null;
    private AdminRequestAdapter adapter = null;
    private GestureDetectorCompat detector = null;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release any resources or clean up any state that needs to be cleaned up when the activity is destroyed
        handler.removeCallbacks(myRunnable);
        Log.d("OnDestroy", "inside");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        handler.postDelayed(myRunnable, 2 * 60 * 1000);

        App.sendEmail( "mjaichandms@gmail.com",  "This is Subject", "This is mail body" );

        adminReqModel = AdminRequestModel.getSingleton();
        progressDialog = new ProgressDialog(AdminHomeActivity.this);
        
        setUpRequestModel();

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
                        Toast.makeText(AdminHomeActivity.this, "Admin Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminHomeActivity.this, AdminHomeActivity.class));
                        break;
//                    case R.id.admin_nav_event_home:
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        Toast.makeText(AdminHomeActivity.this, "Event Home is Clicked", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(AdminHomeActivity.this, AdminHomeActivity.class));
//                        break;
//                    case R.id.admin_nav_add_event:
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        Toast.makeText(AdminHomeActivity.this, "Add Event is Clicked", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(AdminHomeActivity.this, AdminHomeActivity.class));
//                        break;
//                    case R.id.admin_nav_items_home:
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        Toast.makeText(AdminHomeActivity.this, "Items Home is Clicked", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(AdminHomeActivity.this, AdminHomeActivity.class));
//                        break;
//                    case R.id.admin_nav_add_items:
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        Toast.makeText(AdminHomeActivity.this, "Event Home is Clicked", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(AdminHomeActivity.this, AdminHomeActivity.class));
//                        break;
                    case R.id.admin_nav_switch_user:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(AdminHomeActivity.this, "Switching to user...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminHomeActivity.this, HomeActivity.class));
                        break;
                    case R.id.admin_nav_logout:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(AdminHomeActivity.this, "Logout is Clicked", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AdminHomeActivity.this, "Share Link is Clicked", Toast.LENGTH_SHORT).show();break;
                    case R.id.admin_nav_contact:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(AdminHomeActivity.this, "Contact us is Clicked", Toast.LENGTH_SHORT).show();break;
                    default:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }return true;
            }
        });
    }

    private void setUpRequestModel() {

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Events");
        query1.whereEqualTo("isApproved", Boolean.FALSE);
        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, ParseException e) {
                int cnt = results.size();
                adminReqModel.reqList.add(new AdminRequestModel.Requests("New Event Requests",cnt));
                Log.v("Setup EventList Size:", String.valueOf(adminReqModel.reqList.size()));
                adapter = new AdminRequestAdapter(AdminHomeActivity.this, adminReqModel);
                Log.v("adapter", String.valueOf(adapter.getItemCount()));
                recyclerView = findViewById(R.id.adminRecyclerView);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(AdminHomeActivity.this));
                detector = new GestureDetectorCompat(AdminHomeActivity.this, new AdminHomeActivity.RecyclerViewOnGestureListener());
                recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener(){
                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        return detector.onTouchEvent(e);
                    }
                });
            }
        });

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("EventUpdateRequest");
        query2.whereEqualTo("isApproved", Boolean.FALSE);
        query2.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, ParseException e) {
                int cnt = results.size();
                adminReqModel.reqList.add(new AdminRequestModel.Requests("Event Update Requests",cnt));
                Log.v("Setup EventList Size:", String.valueOf(adminReqModel.reqList.size()));
                adapter = new AdminRequestAdapter(AdminHomeActivity.this, adminReqModel);
                Log.v("adapter", String.valueOf(adapter.getItemCount()));
                recyclerView = findViewById(R.id.adminRecyclerView);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(AdminHomeActivity.this));
                detector = new GestureDetectorCompat(AdminHomeActivity.this, new AdminHomeActivity.RecyclerViewOnGestureListener());
                recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener(){
                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        return detector.onTouchEvent(e);
                    }
                });
            }
        });

        ParseQuery<ParseObject> query3 = ParseQuery.getQuery("EventDeleteRequest");
        query3.whereEqualTo("isApproved", Boolean.FALSE);
        query3.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, ParseException e) {
                int cnt = results.size();
                adminReqModel.reqList.add(new AdminRequestModel.Requests("Event Delete Requests",cnt));
                Log.v("Setup EventList Size:", String.valueOf(adminReqModel.reqList.size()));
                adapter = new AdminRequestAdapter(AdminHomeActivity.this, adminReqModel);
                Log.v("adapter", String.valueOf(adapter.getItemCount()));
                recyclerView = findViewById(R.id.adminRecyclerView);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(AdminHomeActivity.this));
                detector = new GestureDetectorCompat(AdminHomeActivity.this, new AdminHomeActivity.RecyclerViewOnGestureListener());
                recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener(){
                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        return detector.onTouchEvent(e);
                    }
                });
            }
        });

        ParseQuery<ParseObject> query4 = ParseQuery.getQuery("Items");
        query4.whereEqualTo("isAvailable",Boolean.TRUE).whereEqualTo("isApproved", Boolean.FALSE);
        query4.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, ParseException e) {
                int cnt = results.size();
                adminReqModel.reqList.add(new AdminRequestModel.Requests("New Item Requests",cnt));
                Log.v("Setup EventList Size:", String.valueOf(adminReqModel.reqList.size()));
                adapter = new AdminRequestAdapter(AdminHomeActivity.this, adminReqModel);
                Log.v("adapter", String.valueOf(adapter.getItemCount()));
                recyclerView = findViewById(R.id.adminRecyclerView);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(AdminHomeActivity.this));
                detector = new GestureDetectorCompat(AdminHomeActivity.this, new AdminHomeActivity.RecyclerViewOnGestureListener());
                recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener(){
                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        return detector.onTouchEvent(e);
                    }
                });
            }
        });

        ParseQuery<ParseObject> query5 = ParseQuery.getQuery("ItemsUpdateRequest");
        query5.whereEqualTo("isAvailable",Boolean.TRUE).whereEqualTo("isApproved", Boolean.FALSE);
        query5.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, ParseException e) {
                int cnt = results.size();
                adminReqModel.reqList.add(new AdminRequestModel.Requests("Item Update Requests",cnt));
                Log.v("Setup EventList Size:", String.valueOf(adminReqModel.reqList.size()));
                adapter = new AdminRequestAdapter(AdminHomeActivity.this, adminReqModel);
                Log.v("adapter", String.valueOf(adapter.getItemCount()));
                recyclerView = findViewById(R.id.adminRecyclerView);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(AdminHomeActivity.this));
                detector = new GestureDetectorCompat(AdminHomeActivity.this, new AdminHomeActivity.RecyclerViewOnGestureListener());
                recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener(){
                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        return detector.onTouchEvent(e);
                    }
                });
            }
        });

        ParseQuery<ParseObject> query6 = ParseQuery.getQuery("ItemsDeleteRequest");
        query6.whereEqualTo("isApproved", Boolean.FALSE);
        query6.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, ParseException e) {
                int cnt = results.size();
                adminReqModel.reqList.add(new AdminRequestModel.Requests("Item Delete Requests",cnt));
                Log.v("Setup EventList Size:", String.valueOf(adminReqModel.reqList.size()));
                adapter = new AdminRequestAdapter(AdminHomeActivity.this, adminReqModel);
                Log.v("adapter", String.valueOf(adapter.getItemCount()));
                recyclerView = findViewById(R.id.adminRecyclerView);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(AdminHomeActivity.this));
                detector = new GestureDetectorCompat(AdminHomeActivity.this, new AdminHomeActivity.RecyclerViewOnGestureListener());
                recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener(){
                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        return detector.onTouchEvent(e);
                    }
                });
            }
        });

    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminHomeActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // don't forget to change the line below with the names of your Activities
                        Intent intent = new Intent(AdminHomeActivity.this, LoginActivity.class);
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
                if (holder instanceof AdminRequestAdapter.RequestHolder) {
                    int position = holder.getAdapterPosition();
                    // handle single tap
                    String reqType= adminReqModel.reqList.get(position).requestType;
                    Log.v("Selected Request Type: ",reqType);

                    if(reqType.matches("New Event Requests")){
                        Intent intent = new Intent(AdminHomeActivity.this, NewEventsApproveActivity.class);
                        startActivity(intent);
                    }else if(reqType.matches("Event Update Requests")){
                        Intent intent = new Intent(AdminHomeActivity.this, UpdateEventsApproveActivity.class);
                        startActivity(intent);
                    }else if(reqType.matches("Event Delete Requests")) {
                        Intent intent = new Intent(AdminHomeActivity.this, DeleteEventsApproveActivity.class);
                        startActivity(intent);
                    }else if(reqType.matches("New Item Requests")) {
                        Intent intent = new Intent(AdminHomeActivity.this, NewItemsApproveActivity.class);
                        startActivity(intent);
                    }else if(reqType.matches("Item Update Requests")) {
                        Intent intent = new Intent(AdminHomeActivity.this, UpdateItemsApproveActivity.class);
                        startActivity(intent);
                    }else if(reqType.matches("Item Delete Requests")) {
                        Intent intent = new Intent(AdminHomeActivity.this, DeleteItemsApproveActivity.class);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(AdminHomeActivity.this, AdminHomeActivity.class);
                        startActivity(intent);

                    }

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
        Log.d("OnResume", "inside");
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

}