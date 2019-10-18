package com.unipool.unipool;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ArriveRequest extends StringRequest {
    final static private String URL = "http://14.49.39.152/UNIPOOL/Arrive.php";
    private Map<String,String> parameters;

    public ArriveRequest(String user_1,String user_2,String user_3,String user_4,int isBoard,String title, Response.Listener<String> listener) {
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("user_1",user_1);
        parameters.put("user_2",user_2);
        parameters.put("user_3",user_3);
        parameters.put("user_4",user_4);
        parameters.put("isBoard",isBoard+"");
        parameters.put("title",title);
    }

    public Map<String,String> getParams() {
        return parameters;
    }
}
