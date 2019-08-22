package com.unipool.unipool;

public class chat_data {
    private String chat_userID;
    private String chat_Text;
    private boolean myText;

    public chat_data(String chat_userID, String chat_Text, boolean myText) {
        this.chat_userID = chat_userID;
        this.chat_Text = chat_Text;
        this.myText = myText;
    }
    public String getChat_userID() {
        return chat_userID;
    }

    public void setChat_userID(String chat_userID) {
        this.chat_userID = chat_userID;
    }

    public String getChat_Text() {
        return chat_Text;
    }

    public void setChat_Text(String chat_text) {
        this.chat_Text = chat_text;
    }

    public void setMyText(boolean myText) {
        this.myText = myText;
    }
    public boolean getMyText(){
        return myText;
    }
}
