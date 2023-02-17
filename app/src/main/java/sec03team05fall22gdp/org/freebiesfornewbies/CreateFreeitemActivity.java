package sec03team05fall22gdp.org.freebiesfornewbies;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateFreeitemActivity extends AppCompatActivity {


    private Button createBtn, cancelBtn;
    private ProgressDialog progressDialog;
    //private EditText eNameET, eDescET, eNotesET, eStDtET, eEndDtET, eAddLine1ET,eAddLine2ET, eCityET, eStateET,eCountryET,eZipET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_freeitem);

        progressDialog = new ProgressDialog(CreateFreeitemActivity.this);

        createBtn=findViewById(R.id.btnCreateEvent);
        cancelBtn=findViewById(R.id.btnCancelCreateEvent);

        cancelBtn.setOnClickListener( v -> {
            startActivity(new Intent(this,ItemHomeActivity.class));
        });

        createBtn.setOnClickListener( v -> {

            try{
                ParseObject eventObject = new ParseObject("Items");
                // Store an object

                // Saving object
                eventObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        progressDialog.dismiss();
                        if (e == null) {
                            // Success
                            showAlert("Successfully saved..!\n", "Your object has been saved in Items class.");
                        } else {
                            // Error
                            Toast.makeText(CreateFreeitemActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });

            }catch (Exception e){
                Toast.makeText(CreateFreeitemActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    // For the Alert messages we use the showAlert.
    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateFreeitemActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // don't forget to change the line below with the names of your Activities
                        Intent intent = new Intent(CreateFreeitemActivity.this, ItemHomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

}