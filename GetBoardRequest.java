package com.unipool.unipool;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetBoardRequest extends StringRequest {
    final static private String URL = "http://121.137.115.20/GetBoard.php";
    private Map<String,String> parameters;

    public GetBoardRequest(String userID, Response.Listener<String> listener) {
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("userID",userID);
    }

    public Map<String,String> getParams() {
        return parameters;
    }
}
