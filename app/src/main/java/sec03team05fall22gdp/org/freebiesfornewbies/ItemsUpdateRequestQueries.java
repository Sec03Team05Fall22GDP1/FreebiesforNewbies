package sec03team05fall22gdp.org.freebiesfornewbies;

import android.widget.Toast;
import java.util.Date;
import javax.json.JSONArray;
import javax.json.JSONObject;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import java.util.Date;
import javax.json.*;
import com.parse.*;
import com.parse.*;
import com.parse.ParseQuery;
import com.parse.*;
import com.parse.ParseException;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
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

import com.parse.SaveCallback;

public class ItemsUpdateRequestQueries {

    public void createObject() {
        ParseObject entity = new ParseObject("ItemsUpdateRequest");

        entity.put("itemDescription", "A string");
        entity.put("itemAddressLine2", "A string");
        entity.put("itemAddressLine1", "A string");
        entity.put("itemName", "A string");
        entity.put("itemCity", "A string");
        entity.put("uploadedDate", new Date());
        entity.put("itemEndDt", new Date());
        entity.put("itemZipcode", "A string");
        entity.put("itemState", "A string");
        entity.put("itemStartDt", new Date());
        entity.put("isApproved", true);
        entity.put("itemCountry", "A string");
        entity.put("updateItemId", "A string");
        entity.put("updateReason", "A string");
        entity.put("itemNotes", "A string");
        entity.put("itemURL", "A string");

        // Saves the new object.
        // Notice that the SaveCallback is totally optional!
        entity.saveInBackground(e -> {
            if (e==null){
                //Save was done
            }else{
                //Something went wrong
            }
        });

    }
    public void readObject() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("EventUpdateRequest");

        // The query will search for a ParseObject, given its objectId.
        // When the query finishes running, it will invoke the GetCallback
        // with either the object, or the exception thrown
        query.getInBackground("<PARSE_OBJECT_ID>", (object, e) -> {
            if (e == null) {
                //Object was successfully retrieved
            } else {
                // something went wrong
            }
        });
    }
    public void updateObject() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");

        // Retrieve the object by id
        query.getInBackground("<PARSE_OBJECT_ID>", (object, e) -> {
            if (e == null) {
                //Object was successfully retrieved
                // Update the fields we want to
                object.put("eventName", "A string");
                object.put("eventDescription", "A string");
                object.put("eventNotes", "A string");
                object.put("eventAddressLine1", "A string");
                object.put("eventAddressLine2", "A string");
                object.put("eventCity", "A string");
                object.put("eventState", "A string");
                object.put("eventCountry", "A string");
                object.put("eventZipcode", "A string");
                object.put("uploadedDate", new Date());
                object.put("isApproved", true);
                object.put("eventStartDt", new Date());
                object.put("eventEndDt", new Date());

                //All other fields will remain the same
                object.saveInBackground();

            } else {
                // something went wrong
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void deleteObject() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");

        // Retrieve the object by id
        query.getInBackground("<PARSE_OBJECT_ID>", (object, e) -> {
            if (e == null) {
                //Object was fetched
                //Deletes the fetched ParseObject from the database
                object.deleteInBackground(e2 -> {
                    if(e2==null){
                        Toast.makeText(this, "Delete Successful", Toast.LENGTH_SHORT).show();
                    }else{
                        //Something went wrong while deleting the Object
                        Toast.makeText(this, "Error: "+e2.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                //Something went wrong
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
