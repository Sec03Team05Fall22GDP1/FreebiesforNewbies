package sec03team05fall22gdp.org.freebiesfornewbies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class DeleteEventsApproveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_events_approve);

        /*
        deleteBtn.setOnClickListener(v -> {
            ParseQuery<ParseObject> queryEvents = ParseQuery.getQuery("Events");
            // Query parameters based on the item name
            queryEvents.whereEqualTo("objectId", fetchID);
            queryEvents.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List<ParseObject> event, ParseException e) {
                    if (e == null) {
                        event.get(0).deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    // Success
                                    showAlert("Delete Status:", "Object has been deleted Successfully");
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
        });
         */
    }
}