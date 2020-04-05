package com.MobileProject.appchat.Model;

public class User {
    private String id;
    private String username;
    private String imageURL;
    private Object Calling = null;
    private Object Ringing = null;

    public User(String id, String username, String imageURL, Object calling, Object ringing) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        Calling = calling;
        Ringing = ringing;
    }

    public User(String id, String username, String imageURL) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
    }

    public User() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Object getCalling() {
        return Calling;
    }

    public void setCalling(Object calling) {
        Calling = calling;
    }

    public Object getRinging() {
        return Ringing;
    }

    public void setRinging(Object ringing) {
        Ringing = ringing;
    }
}
