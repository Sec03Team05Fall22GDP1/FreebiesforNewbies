package sec03team05fall22gdp.org.freebiesfornewbies;

import java.util.ArrayList;

public class ItemUpdateRequestModel {

    public static class ItemUpdateRequests{
        public String updateReason,updateID, itemID, itemName, itemURL, itemAddressLine1, itemAddressLine2, itemCity,itemState, itemCountry, itemZipcode, itemDescription;

        public ItemUpdateRequests(String itemID,String updateID, String itemName, String itemURL, String itemAddressLine1, String itemAddressLine2, String itemCity, String itemState, String itemCountry, String itemZipcode, String itemDescription, String updateReason) {
            this.itemID = itemID;
            this.updateID=updateID;
            this.itemName = itemName;
            this.itemURL = itemURL;
            this.itemAddressLine1 = itemAddressLine1;
            this.itemAddressLine2 = itemAddressLine2;
            this.itemCity = itemCity;
            this.itemState = itemState;
            this.itemCountry = itemCountry;
            this.itemZipcode = itemZipcode;
            this.itemDescription= itemDescription;
            this.updateReason=updateReason;
        }

    }
    private static ItemUpdateRequestModel iModel = null;
    public static ItemUpdateRequestModel getSingleton(){
        iModel = new ItemUpdateRequestModel();
        return iModel;
    }

    public ArrayList<ItemUpdateRequests> itemsList;
    private ItemUpdateRequestModel(){
        itemsList = new ArrayList<ItemUpdateRequests>();
    }

}