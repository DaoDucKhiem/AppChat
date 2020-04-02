package com.MobileProject.appchat.Model;
//lớp Chat thể hiện thông tin cho tin nhắn vừa gửi
public class Chat {

    private String sender;
    private String receiver;
    private String message;

    public Chat (String sender, String receiver, String message) {
       this.message = message;
       this.receiver = receiver;
       this.sender = sender;
    }

    public Chat() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
