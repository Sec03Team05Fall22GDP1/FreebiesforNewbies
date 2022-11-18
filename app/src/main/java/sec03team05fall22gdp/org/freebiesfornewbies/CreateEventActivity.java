package sec03team05fall22gdp.org.freebiesfornewbies;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateEventActivity extends AppCompatActivity {
    private EditText date_time_st;
    private EditText date_time_end;
    private DatePickerDialog datePickerDialog;

    private Button createBtn, cancelBtn;
    private ProgressDialog progressDialog;
    private EditText eNameET, eDescET, eNotesET, eStDtET, eEndDtET, eAddLine1ET,eAddLine2ET, eCityET, eStateET,eCountryET,eZipET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        progressDialog = new ProgressDialog(CreateEventActivity.this);

        eNameET= findViewById(R.id.etEventName);
        eDescET =findViewById(R.id.etEventDescription);
        eNotesET =findViewById(R.id.etEventNotes);
        eStDtET=findViewById(R.id.etEventStartDate);
        eEndDtET=findViewById(R.id.etEventEndDate);
        eAddLine1ET=findViewById(R.id.etEventAddressLine1);
        eAddLine2ET=findViewById(R.id.etEventAddressLine2);
        eCityET=findViewById(R.id.etEventCity);
        eStateET=findViewById(R.id.etEventState);
        eCountryET=findViewById(R.id.etEventCountry);
        eZipET=findViewById(R.id.etEventZipcode);

        //datatime picker
        initDatePicker();

        date_time_st =findViewById(R.id.etEventStartDate);
        date_time_end =findViewById(R.id.etEventEndDate);

        date_time_st.setInputType(InputType.TYPE_NULL);
        date_time_end.setInputType(InputType.TYPE_NULL);

        date_time_st.setOnClickListener(v ->{
            showDateTimeDialog(date_time_st);
        });
        date_time_end.setOnClickListener(v ->{
            showDateTimeDialog(date_time_end);
        });

        createBtn=findViewById(R.id.btnCreateEvent);
        cancelBtn=findViewById(R.id.btnCancelCreateEvent);

        cancelBtn.setOnClickListener( v -> {
            startActivity(new Intent(this,HomeActivity.class));
        });

        createBtn.setOnClickListener( v -> {

            try{
                ParseObject eventObject = new ParseObject("Events");
                // Store an object

                eventObject.put("eventName",eNameET.getText().toString());
                eventObject.put("eventDescription",eDescET.getText().toString());
                eventObject.put("eventNotes",eNotesET.getText().toString());
                eventObject.put("eventAddressLine1",eAddLine1ET.getText().toString());
                eventObject.put("eventAddressLine2",eAddLine2ET.getText().toString());
                eventObject.put("eventCity",eCityET.getText().toString());
                eventObject.put("eventState",eStateET.getText().toString());
                eventObject.put("eventCountry",eCountryET.getText().toString());
                eventObject.put("eventZipcode",eZipET.getText().toString());

                String stDateTime=eStDtET.getText().toString();
                String endDateTime=eEndDtET.getText().toString();

                DateFormat formatter=new SimpleDateFormat("MM/dd/yyyy HH:mm");

                Date stdate=formatter.parse(stDateTime);
                Date etdate=formatter.parse(endDateTime);


                eventObject.put("eventStartDt",stdate);
                eventObject.put("eventEndDt", etdate);
                // Saving object
                eventObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        progressDialog.dismiss();
                        if (e == null) {
                            // Success
                            showAlert("Successfully saved..!\n", "Your object has been saved in Events class.");
                        } else {
                            // Error
                            Toast.makeText(CreateEventActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });

            }catch (Exception e){
                Toast.makeText(CreateEventActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateEventActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // don't forget to change the line below with the names of your Activities
                        Intent intent = new Intent(CreateEventActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
    private void showDateTimeDialog(EditText date_time_in){
        Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                TimePickerDialog.OnTimeSetListener timeSetListener= new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");
                        date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };
                new TimePickerDialog(CreateEventActivity.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };
        new DatePickerDialog(CreateEventActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
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
}