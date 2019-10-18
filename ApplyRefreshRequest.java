package com.unipool.unipool;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ApplyRefreshRequest extends StringRequest {
    final static private String URL = "http://14.49.39.152/UNIPOOL/ApplyRefresh.php";
    private Map<String, String> parameters;

    public ApplyRefreshRequest(String leader_user, Response.Listener<String> listener) {
        super(Method.POST, URL, listener,null);
        parameters = new HashMap<>();
        parameters.put("leader_user",leader_user);
    }

    public Map<String,String> getParams() {
        return parameters;
    }
}
