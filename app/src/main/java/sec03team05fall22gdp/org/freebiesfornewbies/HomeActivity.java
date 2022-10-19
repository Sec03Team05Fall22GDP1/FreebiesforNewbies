package sec03team05fall22gdp.org.freebiesfornewbies;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ImageView logoutBtn, createEventBtn, ivNameSearch, ivDateSearch;
    private ProgressDialog progressDialog;
    private Button dateButton;
    private DatePickerDialog datePickerDialog;

    ArrayList<EventModel> eModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initDatePicker();

        progressDialog = new ProgressDialog(HomeActivity.this);
        logoutBtn = findViewById(R.id.ivlogout);
        createEventBtn = findViewById(R.id.ivCreateEvent);
        setUpEventModels();

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
    }

    private void setUpEventModels(){
        // Read Parse Objects
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        Calendar calendar = Calendar.getInstance();
        Log.v("today's Date", String.valueOf(calendar.YEAR)+String.valueOf(calendar.MONTH)+String.valueOf(calendar.DAY_OF_MONTH));
        calendar.set(calendar.YEAR,calendar.MONTH,calendar.DAY_OF_MONTH,00,00,00);
        Date date = calendar.getTime();
        calendar.set(calendar.YEAR,calendar.MONTH,calendar.DAY_OF_MONTH+1,00,00,00);
        Date date1 = calendar.getTime();

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, ParseException e) {
                for (ParseObject event : results) {
                    if (e == null) {
                        String eventID, eventName, eventStDT, eventEndDt, eventDescription,eventAddressLine1,
                                eventAddressLine2, eventCity,eventState, eventCountry, eventZipcode, eventNotes;
                        eventID= event.getString("objectId");
                        eventName = event.getString("eventName");
                        eventStDT = String.valueOf(event.getDate("eventStartDt"));
                        eventEndDt =  String.valueOf(event.getDate("eventEndDt"));
                        eventDescription =  event.getString("eventDescription");
                        eventAddressLine1 =  event.getString("eventAddressLine1") ;
                        eventAddressLine2 =  event.getString("eventAddressLine2");
                        eventCity= event.getString("eventCity") ;
                        eventState= event.getString("eventState") ;
                        eventCountry= event.getString("eventCountry");
                        eventZipcode= event.getString("eventZipcode") ;
                        eventNotes =  event.getString("eventNotes");

                        eModels.add(new EventModel(eventID,eventName,eventStDT,eventEndDt,eventDescription,eventAddressLine1,eventAddressLine2,eventCity,eventState,eventCountry,eventZipcode,eventNotes));
                    } else {
                        Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                Log.v("eModels", String.valueOf(eModels.size()));
                RecyclerView recyclerView = findViewById(R.id.eventRecyclerView);

                EventRAdapter adapter = new EventRAdapter(HomeActivity.this, eModels);
                Log.v("adapter", String.valueOf(adapter.getItemCount()));
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
            }
        });
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

    public void openDatePicker(View view)
    {
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
    private String makeDateString(int day, int month, int year)
    {
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
    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

}