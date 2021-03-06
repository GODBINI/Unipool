package com.unipool.unipool;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ChangeUniRequest extends StringRequest {
    final static private String URL = "http://14.49.39.152/UNIPOOL/ChangeUni.php";
    private Map<String,String> parameters;

    public ChangeUniRequest(String userID, String newUni, Response.Listener<String> listener) {
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("userID",userID);
        parameters.put("newUni",newUni);
    }

    public Map<String,String> getParams() {
        return parameters;
    }
}
