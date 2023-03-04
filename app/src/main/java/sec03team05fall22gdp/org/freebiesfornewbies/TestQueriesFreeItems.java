package sec03team05fall22gdp.org.freebiesfornewbies;

import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class TestQueriesFreeItems {

    public void createItemObject() {
        try {
            ParseObject itemObject = new ParseObject("ItemsUpdateRequest");
            // Store an object

            itemObject.put("itemName", "Name");
            itemObject.put("itemURL", "URL");
            itemObject.put("itemAddressLine1", "AddressLine1");
            itemObject.put("itemAddressLine2", "AddressLine2");
            itemObject.put("itemCity", "City");
            itemObject.put("itemState", "State");
            itemObject.put("itemCountry", "Country");
            itemObject.put("itemZipcode", "Zipcode");
            itemObject.put("updateReason", "UpdateReason");

            // Saving object
            itemObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        // Success
                    } else {
                        // Error
                    }
                }
            });
        } catch (Exception e) {
            Log.v("Create Item", e.getMessage());
        }
    }


    // Deleting the particular item.
    public void deleteItemObject(String fetchID){
        ParseQuery<ParseObject> soccerPlayers = ParseQuery.getQuery("Items");
        // Query parameters based on the item name
        soccerPlayers.whereEqualTo("objectId", fetchID);
        soccerPlayers.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> event, ParseException e) {
                if (e == null) {
                    event.get(0).deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                // Success
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
    }
}
