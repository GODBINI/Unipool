package com.unipool.unipool;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MatchRequest extends StringRequest {
    final static private String URL = "http://14.49.39.152/UNIPOOL/Match.php";
    private Map<String,String> parameters;

    public MatchRequest(String userID, String departure, String arrival, Response.Listener<String> listener) {
        super(Method.POST, URL, listener,null);
        parameters = new HashMap<>();
        parameters.put("userID",userID);
        parameters.put("departure",departure);
        parameters.put("arrival",arrival);
    }

    public Map<String,String> getParams() {
        return parameters;
    }
}
