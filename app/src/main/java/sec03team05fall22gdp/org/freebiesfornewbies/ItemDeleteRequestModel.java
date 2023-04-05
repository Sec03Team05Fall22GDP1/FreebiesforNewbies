package sec03team05fall22gdp.org.freebiesfornewbies;

import java.util.ArrayList;

public class ItemDeleteRequestModel {

    public static class ItemDeleteRequests{
        public String deleteID, ItemID, ItemName, deleteReason;

        public ItemDeleteRequests(String deleteID, String ItemID, String ItemName, String deleteReason) {
            this.deleteID = deleteID;
            this.ItemID = ItemID;
            this.ItemName = ItemName;
            this.deleteReason = deleteReason;
        }

    }
    private static ItemDeleteRequestModel theModel = null;
    public static ItemDeleteRequestModel getSingleton(){
        theModel = new ItemDeleteRequestModel();
        return theModel;
    }

    public ArrayList<ItemDeleteRequests> ItemDeleteRequestsList;
    private ItemDeleteRequestModel(){
        ItemDeleteRequestsList = new ArrayList<ItemDeleteRequests>();
    }

}
