package com.unipool.unipool;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class TrustRequest extends StringRequest {
    final static private String URL = "http://218.155.17.58/Trust.php";
    private Map<String, String> parameters;

    public TrustRequest(String user_1, boolean user1_like,String user_2, boolean user2_like,String user_3, boolean user3_like,int quantity,Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("user_1", user_1);
        parameters.put("user_2", user_2);
        parameters.put("user_3", user_3);
        parameters.put("user1_like",user1_like+"");
        parameters.put("user2_like",user2_like+"");
        parameters.put("user3_like",user3_like+"");
        parameters.put("quantity",quantity+"");
    }

    public Map<String, String> getParams () {
        return parameters;
    }
}
