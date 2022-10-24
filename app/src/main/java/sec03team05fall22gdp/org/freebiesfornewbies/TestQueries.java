package sec03team05fall22gdp.org.freebiesfornewbies;

import com.parse.ParseException;
import com.parse.ParseObject;
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
                } else {
                    // Error
                }
            }
        });
    }
}
