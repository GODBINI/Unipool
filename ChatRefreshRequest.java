package com.unipool.unipool;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ChatRefreshRequest extends StringRequest {
    final static private String URL = "http://218.155.17.58/ChatRefresh_Test.php";
    private Map<String,String> parameters;

    public ChatRefreshRequest(String userID, String title, Response.Listener<String> listener) {
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("userID",userID);
        parameters.put("title",title);
    }

    public Map<String,String> getParams() {
        return parameters;
    }
}
