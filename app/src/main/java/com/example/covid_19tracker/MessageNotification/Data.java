package com.example.covid_19tracker.MessageNotification;

public class Data {
    String userId;
    String title;
    String body;

    public Data(String userId, String title, String body, String sent) {
        this.userId = userId;
        this.title = title;
        this.body = body;
        this.sent = sent;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }

    String sent;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


}
