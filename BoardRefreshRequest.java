package com.unipool.unipool;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class BoardRefreshRequest extends StringRequest {
    final static private String URL = "http://112.186.52.105/BoardRefresh.php";
    private Map<String, String> parameters;

    public BoardRefreshRequest(Response.Listener<String> listener) {
        super(Method.POST, URL, listener,null);
        parameters = new HashMap<>();
    }

    public Map<String,String> getParams() {
        return parameters;
    }
}
