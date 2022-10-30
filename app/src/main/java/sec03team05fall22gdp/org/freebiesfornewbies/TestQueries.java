package sec03team05fall22gdp.org.freebiesfornewbies;

import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.Arrays;
import java.util.List;

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

    public void writeParseObjects(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("SoccerPlayers");
        // Retrieve the object by id
        query.getInBackground("HMcTr9rD3s", new GetCallback<ParseObject>() {
            public void done(ParseObject player, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    player.put("yearOfBirth", 1998);
                    player.put("emailContact", "a.wed@domain.io");
                    player.saveInBackground();
                }else {
                    // Failed
                }
            }
        });
    }

    public void deleteParseObjects(){
        ParseQuery<ParseObject> soccerPlayers = ParseQuery.getQuery("SoccerPlayers");
        // Query parameters based on the item name
        soccerPlayers.whereEqualTo("objectId", "HMcTr9rD3s");
        soccerPlayers.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> player, ParseException e) {
                if (e == null) {
                    player.get(0).deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                // Success
                            } else {
                                // Failed
                            }
                        }
                    });
                } else {
                    // Something is wrong
                }
            }
        });
    }
}
