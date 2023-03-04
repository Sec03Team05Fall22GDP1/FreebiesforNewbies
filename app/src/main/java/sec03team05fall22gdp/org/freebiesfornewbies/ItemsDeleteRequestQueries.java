package sec03team05fall22gdp.org.freebiesfornewbies;

import java.util.Date;
import javax.json.JSONArray;
import javax.json.JSONObject;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.SaveCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class ItemsDeleteRequestQueries {

    public void createObject() {
        ParseObject entity = new ParseObject("ItemsDeleteRequest");

        entity.put("DeleteItemID", "A string");
        entity.put("UpdateItemId", "A string");
        entity.put("DeleteReason", "A string");
        entity.put("isApproved", true);
        entity.put("itemName", "A string");

        // Saves the new object.
        // Notice that the SaveCallback is totally optional!
        entity.saveInBackground(e -> {
            if (e==null){
                //Save was done
            }else{
                //Something went wrong
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void readObject() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ItemsDeleteRequest");

        // The query will search for a ParseObject, given its objectId.
        // When the query finishes running, it will invoke the GetCallback
        // with either the object, or the exception thrown
        query.getInBackground("<PARSE_OBJECT_ID>", (object, e) -> {
            if (e == null) {
                //Object was successfully retrieved
            } else {
                // something went wrong
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
