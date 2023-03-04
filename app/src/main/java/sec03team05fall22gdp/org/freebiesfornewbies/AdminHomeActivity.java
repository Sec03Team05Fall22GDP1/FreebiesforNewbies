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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class AdminHomeActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private ImageView logoutBtn, ivMenu;
    private AdminRequestModel adminReqModel ;

    private RecyclerView recyclerView=null;
    private AdminRequestAdapter adapter = null;
    private GestureDetectorCompat detector = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

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
                    case R.id.admin_nav_event_home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(AdminHomeActivity.this, "Event Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminHomeActivity.this, AdminHomeActivity.class));
                        break;
                    case R.id.admin_nav_add_event:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(AdminHomeActivity.this, "Add Event is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminHomeActivity.this, AdminHomeActivity.class));
                        break;
                    case R.id.admin_nav_items_home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(AdminHomeActivity.this, "Items Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminHomeActivity.this, AdminHomeActivity.class));
                        break;
                    case R.id.admin_nav_add_items:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(AdminHomeActivity.this, "Event Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminHomeActivity.this, AdminHomeActivity.class));
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
        query1.whereGreaterThanOrEqualTo("isApproved", Boolean.FALSE);
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
        query2.whereGreaterThanOrEqualTo("isApproved", Boolean.FALSE);
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
        query3.whereGreaterThanOrEqualTo("isApproved", Boolean.FALSE);
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
}