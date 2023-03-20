package sec03team05fall22gdp.org.freebiesfornewbies;

import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;


import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class ItemQueries {

    public void createItemObject(){
        try{
            ParseObject itemObject = new ParseObject("Items");
            // Store an object

            itemObject.put("itemName","Name");
            itemObject.put("itemURL","URL");
            itemObject.put("itemAddressLine1","AddressLine1");
            itemObject.put("itemAddressLine2","AddressLine2");
            itemObject.put("itemCity","City");
            itemObject.put("itemState","State");
            itemObject.put("itemCountry","Country");
            itemObject.put("itemZipcode","Zipcode");

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
        }catch (Exception e){
            Log.v("Create Item",e.getMessage());
        }
    }

    public void readMultiItems(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Items");
        query.whereEqualTo("isAvailable",Boolean.TRUE).whereEqualTo("isApproved", Boolean.FALSE);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, ParseException e) {
                for (ParseObject itemObj : results) {
                    if (e == null) {
                        String itemID, itemName, itemURL, itemAddressLine1, itemAddressLine2, itemCity, itemState, itemCountry, itemZipcode,itemDescription;
                        itemID=itemObj.getObjectId();
                        itemName=itemObj.getString("itemName");
                        itemURL=itemObj.getString("itemURL");
                        itemAddressLine1=itemObj.getString("itemAddressLine1");
                        itemCity=itemObj.getString("itemCity");
                        itemState=itemObj.getString("itemState");
                        itemCountry=itemObj.getString("itemCountry");
                        itemZipcode=itemObj.getString("itemZipcode");
                        itemAddressLine2=itemObj.getString("itemAddressLine2");
                        itemDescription= itemObj.getString("itemDescription");

                        Log.v("ObjectID",String.valueOf(itemID));

                    } else {
                        //Toast.makeText(ItemHomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}