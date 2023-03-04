package sec03team05fall22gdp.org.freebiesfornewbies;

import android.util.Log;


import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

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
}