package sec03team05fall22gdp.org.freebiesfornewbies;

import java.util.ArrayList;

public class AdminRequestModel {

    public static class Requests{
        public String requestType;
        public int requestCount;

        public Requests(String requestType, int requestCount) {
            this.requestType = requestType;
            this.requestCount = requestCount;
        }

    }

    private static AdminRequestModel reqModel = null;
    public static AdminRequestModel getSingleton(){
        reqModel = new AdminRequestModel();
        return reqModel;
    }

    public ArrayList<AdminRequestModel.Requests> reqList;
    private AdminRequestModel(){
        reqList = new ArrayList<Requests>();
    }

}
