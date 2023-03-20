package sec03team05fall22gdp.org.freebiesfornewbies;

import java.util.ArrayList;

public class ItemModel {

    public static class Items{
        public String itemID, itemName, itemURL, itemAddressLine1, itemAddressLine2, itemCity,itemState, itemCountry, itemZipcode, itemDescription;

        public Items(String itemID, String itemName, String itemURL, String itemAddressLine1, String itemAddressLine2, String itemCity, String itemState, String itemCountry, String itemZipcode, String itemDescription) {
            this.itemID = itemID;
            this.itemName = itemName;
            this.itemURL = itemURL;
            this.itemAddressLine1 = itemAddressLine1;
            this.itemAddressLine2 = itemAddressLine2;
            this.itemCity = itemCity;
            this.itemState = itemState;
            this.itemCountry = itemCountry;
            this.itemZipcode = itemZipcode;
            this.itemDescription= itemDescription;
        }

    }
    private static ItemModel iModel = null;
    public static ItemModel getSingleton(){
        iModel = new ItemModel();
        return iModel;
    }

    public ArrayList<Items> itemsList;
    private ItemModel(){
        itemsList = new ArrayList<Items>();
    }

}