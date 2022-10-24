package sec03team05fall22gdp.org.freebiesfornewbies;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.Arrays;

public class TestQueries {

    public void createParseObjects(){
        // Configure Query
        ParseObject soccerPlayers = new ParseObject("SoccerPlayers");
        // Store an object
        soccerPlayers.put("playerName", "A. Wed");
        soccerPlayers.put("yearOfBirth", 1997);
        soccerPlayers.put("emailContact", "a.wed@email.io");
        soccerPlayers.addAllUnique("attributes", Arrays.asList("fast", "good conditioning"));
        // Saving object
        soccerPlayers.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // Success
                    Log.v("Create Object:","Successful");
                } else {
                    Log.v("Create Object:","failed due to ParseException "+e.getMessage());
                }
            }
        });
    }

    public void readParseObjects(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("SoccerPlayers");
        query.whereEqualTo("objectId", "HMcTr9rD3s");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject player, ParseException e) {
                if (e == null) {
                    String playerName = player.getString("playerName");
                    int yearOfBirth = player.getInt("yearOfBirth");
                    String emailPlayer =  player.getString("emailContact");
                    Log.v("playerName",playerName);
                    Log.v("yearOfBirth",String.valueOf(yearOfBirth));
                    Log.v("emailContact",emailPlayer);
                } else {
                    Log.v("ParseException",e.getMessage());
                }
            }
        });
    }
}
