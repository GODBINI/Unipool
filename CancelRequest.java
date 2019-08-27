package com.unipool.unipool;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CancelRequest extends StringRequest {
    final static private String URL = "http://112.186.52.105/Cancel_match.php";
    private Map<String,String> parameters;

    public CancelRequest(String userID, String departure, String arrival, Response.Listener<String> listener) {
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("userID",userID);
        parameters.put("departure",departure);
        parameters.put("arrival",arrival);
    }

    public Map<String,String> getParams() {
        return parameters;
    }
}
