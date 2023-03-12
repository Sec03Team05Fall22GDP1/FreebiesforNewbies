package sec03team05fall22gdp.org.freebiesfornewbies;

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
            }
        });
    }

    public void updateObject() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ItemsDeleteRequest");

        // Retrieve the object by id
        query.getInBackground("<PARSE_OBJECT_ID>", (object, e) -> {
            if (e == null) {
                //Object was successfully retrieved
                // Update the fields we want to
                object.put("DeleteItemID", "A string");
                object.put("UpdateItemId", "A string");
                object.put("DeleteReason", "A string");
                object.put("isApproved", true);
                object.put("itemName", "A string");

                //All other fields will remain the same
                object.saveInBackground();

            } else {
                // something went wrong
            }
        });

    }

    public void deleteObject() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ItemsDeleteRequest");

        // Retrieve the object by id
        query.getInBackground("<PARSE_OBJECT_ID>", (object, e) -> {
            if (e == null) {
                //Object was fetched
                //Deletes the fetched ParseObject from the database
                object.deleteInBackground(e2 -> {
                    if(e2==null){
                    }else{
                        //Something went wrong while deleting the Object
                    }
                });
            }else{
                //Something went wrong
            }
        });

    }

}
