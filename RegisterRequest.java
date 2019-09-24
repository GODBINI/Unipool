package com.unipool.unipool;

import android.util.Log;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest{
    final static private String URL = "http://121.137.115.20/Register.php";
    private Map<String, String> parameters;

    public RegisterRequest(String email,String userID, String userPW,String Uni,String Account, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("email",email);
        parameters.put("userID", userID);
        parameters.put("userPW", userPW);
        parameters.put("Uni", Uni);
        parameters.put("Account", Account);
    }

    public Map<String, String> getParams () {
        return parameters;
    }
}
