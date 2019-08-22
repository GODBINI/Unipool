package com.unipool.unipool;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ApplyAcceptRequest extends StringRequest {
    final static private String URL = "http://59.12.172.7/ApplyAccept.php";
    private Map<String, String> parameters;

    public ApplyAcceptRequest(String userID,String leader_user, Response.Listener<String> listener) {
        super(Method.POST, URL, listener,null);
        parameters = new HashMap<>();
        parameters.put("userID",userID);
        parameters.put("leader_user",leader_user);
    }

    public Map<String,String> getParams() {
        return parameters;
    }
}
