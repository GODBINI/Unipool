package com.unipool.unipool;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ChatInputRequest extends StringRequest{
    final static private String URL = "http://112.186.52.105/Chat.php";
    private Map<String,String> parameters;

    public ChatInputRequest(String userID,String title, String chat_Text, Response.Listener<String> listener) {
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("userID",userID);
        parameters.put("title",title);
        parameters.put("chat_Text",chat_Text);
    }

    public Map<String,String> getParams() {
        return parameters;
    }
}
