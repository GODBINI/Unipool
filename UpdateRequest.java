package com.unipool.unipool;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdateRequest extends StringRequest {
    final static private String URL = "http://14.49.39.152/UNIPOOL/Update.php";
    private Map<String, String> parameters;

    public UpdateRequest(String userID, String school, String title,String date, String comment, Response.Listener<String> listener) {
        super(Method.POST, URL, listener,null);
        parameters = new HashMap<>();
        parameters.put("userID",userID);
        parameters.put("school",school);
        parameters.put("title",title);
        parameters.put("date",date);
        parameters.put("comment",comment);
    }

    public Map<String,String> getParams() {
        return parameters;
    }
}
