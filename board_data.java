package com.unipool.unipool;

public class board_data {
    private String board_schoolText;
    private String board_titleText;
    private String board_commentText;
    private String board_userIDText;
    private String board_trustText;
    private String board_quantityText;
    private String board_DateText;
    private String user;

    public board_data(String board_schoolText, String board_titleText,String board_DateText, String board_commentText,String board_userIDText,String board_trustText,String board_quantityText,String user) {
        this.board_schoolText = board_schoolText;
        this.board_titleText = board_titleText;
        this.board_DateText = board_DateText;
        this.board_commentText = board_commentText;
        this.board_userIDText = board_userIDText;
        this.board_trustText = board_trustText;
        this.board_quantityText = board_quantityText;
        this.user = user;
    }

    public String getBoard_commentText() {
        return board_commentText;
    }

    public String getBoard_schoolText() {
        return board_schoolText;
    }

    public String getBoard_titleText() {
        return board_titleText;
    }

    public String getBoard_trustText() {
        return board_trustText;
    }

    public String getBoard_userIDText() {
        return board_userIDText;
    }

    public String getBoard_quantityText() {
        return board_quantityText;
    }

    public String getUser() {
        return user;
    }

    public String getBoard_DateText() {
        return board_DateText;
    }

    public void setBoard_commentText(String board_commentText) {
        this.board_commentText = board_commentText;
    }

    public void setBoard_schoolText(String board_schoolText) {
        this.board_schoolText = board_schoolText;
    }

    public void setBoard_titleText(String board_titleText) {
        this.board_titleText = board_titleText;
    }

    public void setBoard_trustText(String board_trustText) {
        this.board_trustText = board_trustText;
    }

    public void setBoard_userIDText(String board_userIDText) {
        this.board_userIDText = board_userIDText;
    }

    public void setBoard_quantityText(String board_quantityText) {
        this.board_quantityText = board_quantityText;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setBoard_DateText(String board_DateText) {
        this.board_DateText = board_DateText;
    }
}
