package com.unipool.unipool;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SuggestRequest extends StringRequest {
    final static private String URL = "http://121.137.115.20/Suggest.php";
    private Map<String, String> parameters;

    public SuggestRequest(String userID, String university,String departure,String arrival, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("university", university);
        parameters.put("departure", departure);
        parameters.put("arrival", arrival);
    }

    public Map<String, String> getParams () {
        return parameters;
    }
}
