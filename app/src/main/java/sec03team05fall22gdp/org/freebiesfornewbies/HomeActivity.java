package sec03team05fall22gdp.org.freebiesfornewbies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.DatePicker;
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
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity {

    private ImageView logoutBtn, createEventBtn, ivNameSearch, ivDateSearch, ivMenu;
    private EditText searchNameET;
    private ProgressDialog progressDialog;
    private Button dateButton;
    private DatePickerDialog datePickerDialog;

    private EventModel myEModel ;
    private RecyclerView recyclerView=null;
    private EventRAdapter adapter = null;
    private GestureDetectorCompat detector = null;

    private Timer inactivityTimer;
    private boolean isUserActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
                        Toast.makeText(HomeActivity.this, "Switching to Admin...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(HomeActivity.this, AdminHomeActivity.class));
                        break;
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
                        startActivity(new Intent(HomeActivity.this, ItemHomeActivity.class));
                        break;
                    case R.id.nav_add_items:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(HomeActivity.this, "Event Home is Clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(HomeActivity.this, CreateFreeitemActivity.class));
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

                                detector = new GestureDetectorCompat(HomeActivity.this, new RecyclerViewOnGestureListener());
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

                                detector = new GestureDetectorCompat(HomeActivity.this, new RecyclerViewOnGestureListener());
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

    private boolean setUpEventModels() {
        // Read Parse Objects
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        String formattedDate = df.format(date);

        Date dateToday = null;
        try {
            dateToday = new SimpleDateFormat("MM-dd-yyyy").parse(formattedDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        Log.v("today's Date: ", dateToday.toString());
        calendar.setTime(dateToday);
        calendar.add(Calendar.DATE, 1);
        Date dateTomorrow = calendar.getTime();
        Log.v("tomorrow's Date: ", dateTomorrow.toString());
        query.whereGreaterThanOrEqualTo("eventStartDt", dateToday).whereLessThan("eventStartDt", dateTomorrow).whereEqualTo("isApproved", Boolean.TRUE);
        query.findInBackground(new FindCallback<ParseObject>() {
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

                        detector = new GestureDetectorCompat(HomeActivity.this, new RecyclerViewOnGestureListener());
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


        return true;
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // don't forget to change the line below with the names of your Activities
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    public void openDatePicker(View view){
        datePickerDialog.show();
    }

    private void initDatePicker()    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, android.R.style.Holo_Light_ButtonBar_AlertDialog, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year){
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getTodaysDate()    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private String getMonthFormat(int month){
        if(month == 1)
            return "Jan";
        if(month == 2)
            return "Feb";
        if(month == 3)
            return "Mar";
        if(month == 4)
            return "Apr";
        if(month == 5)
            return "May";
        if(month == 6)
            return "Jun";
        if(month == 7)
            return "Jul";
        if(month == 8)
            return "Aug";
        if(month == 9)
            return "Sep";
        if(month == 10)
            return "Oct";
        if(month == 11)
            return "Nov";
        if(month == 12)
            return "Dec";

        //default should never happen
        return "JAN";
    }

    private class RecyclerViewOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (view != null) {
                RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(view);
                if (holder instanceof EventRAdapter.MyViewHolder) {
                    int position = holder.getAdapterPosition();
                    // handle single tap
                    String sEventId= myEModel.eventsList.get(position).eventID;
                    Log.v("Selected EventID: ",sEventId);

                    Intent intent = new Intent(HomeActivity.this, DetailedEventActivity.class);
                    intent.putExtra("eventID",sEventId);
                    startActivity(intent);
                    return true; // Use up the tap gesture
                }
            }            // we didn't handle the gesture so pass it on
            return false;
        }
    }

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
        }, 2 * 60 * 1000); // 2 minutes in milliseconds
    }

    private void logoutUserAndReturnToLogin() {
        // logging out of Parse
        ParseUser.logOutInBackground(e -> {
            if (e == null){
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
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