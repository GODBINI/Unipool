package com.unipool.unipool;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UniversityRequest extends StringRequest {
    final static private String URL = "http://121.137.115.20/InitRefresh.php";
    private Map<String, String> parameters;

    public UniversityRequest(Response.Listener<String> listener) {
        super(Method.POST, URL, listener,null);
        parameters = new HashMap<>();
    }

    public Map<String,String> getParams() {
        return parameters;
    }
}
