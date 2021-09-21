package com.example.covid_19tracker.ModelClass;

public class Chats {
    String message,sender,receiver,timestamp;
   boolean isSeen;
    public Chats() {

    }

    public Chats(String message, String sender, String receiver, String timestamp, boolean isSeen) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
        this.isSeen = isSeen;
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

    public void setMessage(String msg) {
        this.message = msg;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String senderId) {
        this.sender = senderId;
    }
}
