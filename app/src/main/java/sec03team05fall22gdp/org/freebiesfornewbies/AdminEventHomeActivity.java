package sec03team05fall22gdp.org.freebiesfornewbies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdminEventHomeActivity extends AppCompatActivity {



    private ImageView logoutBtn, createEventBtn, ivNameSearch, ivDateSearch, ivMenu;
    private EditText searchNameET;
    private ProgressDialog progressDialog;
    private Button dateButton;
    private DatePickerDialog datePickerDialog;

    private EventModel myEModel ;
    private RecyclerView recyclerView=null;
    private EventRAdapter adapter = null;
    private GestureDetectorCompat detector = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_event_home);


        myEModel = EventModel.getSingleton();

        initDatePicker();

        boolean setUpIsSuccess = setUpEventModels();

        progressDialog = new ProgressDialog(HomeActivity.this);

        logoutBtn = findViewById(R.id.ivlogout);
        createEventBtn = findViewById(R.id.ivCreateEvent);
        //date button set up
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());

        createEventBtn.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, CreateEventActivity.class));
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
                    case R.id.nav_event_home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(HomeActivity.this, "Event Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                        break;
                    case R.id.nav_add_event:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(HomeActivity.this, "Add Event is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(HomeActivity.this, CreateEventActivity.class));
                        break;
                    case R.id.nav_items_home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(HomeActivity.this, "Items Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                        break;
                    case R.id.nav_add_items:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(HomeActivity.this, "Event Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(HomeActivity.this, CreateEventActivity.class));
                        break;
                    case R.id.nav_logout:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(HomeActivity.this, "Logout is Clicked", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(HomeActivity.this, "Share Link is Clicked", Toast.LENGTH_SHORT).show();break;
                    case R.id.nav_contact:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(HomeActivity.this, "Contact us is Clicked", Toast.LENGTH_SHORT).show();break;
                    default:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }return true;
            }
        });

        ivNameSearch = findViewById(R.id.ivSearch);
        ivDateSearch = findViewById(R.id.ivDateSearch);

        ivNameSearch.setOnClickListener(v ->{
            searchNameET = findViewById(R.id.etSearchtext);
            String searchText = searchNameET.getText().toString();
            if(!searchText.matches("")){
                ParseQuery<ParseObject> queryName = ParseQuery.getQuery("Events");
                myEModel.eventsList.clear();
                queryName.whereContains("eventName",searchText).whereEqualTo("isApproved", Boolean.TRUE);
                queryName.findInBackground(new FindCallback<ParseObject>() {
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
                                adapter = new EventRAdapter(HomeActivity.this, myEModel);
                                Log.v("adapter", String.valueOf(adapter.getItemCount()));

                                recyclerView = findViewById(R.id.eventRecyclerView);
                                recyclerView.setAdapter(adapter);

                                recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));

                                detector = new GestureDetectorCompat(HomeActivity.this, new HomeActivity.RecyclerViewOnGestureListener());
                                recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener(){
                                    @Override
                                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                                        return detector.onTouchEvent(e);
                                    }
                                });
                            } else {
                                Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                });
            }else{
                Toast.makeText(HomeActivity.this, "Search text is empty.\nProvide input and try again", Toast.LENGTH_LONG).show();
            }
        });

        ivDateSearch.setOnClickListener(v ->{
            String searchDate = dateButton.getText().toString();
            if(!searchDate.matches("")){
                Calendar calendar = Calendar.getInstance();
                Date dateINst = calendar.getTime();
                try {
                    dateINst = new SimpleDateFormat("MMM dd yyyy").parse(searchDate);
                } catch (java.text.ParseException e) {
                }
}
                    Log.v("ParseException Date: ", e.getMessage());
                }
                Log.v("Input Date start: ", dateINst.toString());
        calendar.setTime(dateINst);
        calendar.add(Calendar.DATE, 1);
        Date dateINend = calendar.getTime();
        Log.v("Input Date end: ", dateINend.toString());

        ParseQuery<ParseObject> queryDate = ParseQuery.getQuery("Events");
        myEModel.eventsList.clear();
        queryDate.whereGreaterThanOrEqualTo("eventStartDt", dateINst).whereLessThan("eventStartDt", dateINend).whereEqualTo("isApproved", Boolean.TRUE);
        queryDate.findInBackground(new FindCallback<ParseObject>() {
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
                        adapter = new EventRAdapter(HomeActivity.this, myEModel);
                        Log.v("adapter", String.valueOf(adapter.getItemCount()));

                        recyclerView = findViewById(R.id.eventRecyclerView);
                        recyclerView.setAdapter(adapter);

                        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));

                        detector = new GestureDetectorCompat(HomeActivity.this, new HomeActivity.RecyclerViewOnGestureListener());
                        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener(){
                            @Override
                            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                                return detector.onTouchEvent(e);
                            }
                        });
                    } else {
                        Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }else{
        Toast.makeText(HomeActivity.this, "Search text is empty.\nProvide input and try again", Toast.LENGTH_LONG).show();
    }
});

        }
