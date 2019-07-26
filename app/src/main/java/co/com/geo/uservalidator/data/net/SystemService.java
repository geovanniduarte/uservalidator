package co.com.geo.uservalidator.data.net;

import org.json.JSONObject;

public class SystemService extends Service {

    public SystemService() {

    }

    public JSONObject getDateFromLocation(String url) throws Exception {
        String response = GET(url);
        JSONObject jsonObject = new JSONObject(response);
        return  jsonObject;
    }

}
