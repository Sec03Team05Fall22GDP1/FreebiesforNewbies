package sec03team05fall22gdp.org.freebiesfornewbies;
import android.widget.Toast;

import java.util.Date;
i

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
}
