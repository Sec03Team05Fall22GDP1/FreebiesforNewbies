package sec03team05fall22gdp.org.freebiesfornewbies;

import android.app.Application;
import android.util.Log;

import com.parse.FunctionCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.HashMap;
import java.util.Map;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }

    public static void sendEmail(String emailAddress, String emailSubject, String emailBody){

        Map<String, String> params = new HashMap<>();

// Create the fields "emailAddress", "emailSubject" and "emailBody"
// As Strings and use this piece of code to add it to the request
        params.put("toEmail", emailAddress);
        params.put("subject", emailSubject);
        params.put("body", emailBody);

        ParseCloud.callFunctionInBackground("sendgridEmail", params, new FunctionCallback<Object>() {
            @Override
            public void done(Object response, ParseException exc) {
                if(exc == null) {
                    Log.v("Email Status","Success");
                }
                else {
                    Log.v("Email Status","Failed "+exc.getMessage());
                }
            }
        });
    }


}
