package com.example.covid_19tracker.ModelClass;

public class Users {
    private String username,profilepic,lastMessage,about,location;
    private String email;
    private String status;



    public Users() {

    }

    public Users(String username, String profilepic, String about, String location, String email, String status) {
        this.username = username;
        this.profilepic = profilepic;
        this.about = about;
        this.location = location;
        this.email = email;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getAbout() {
        return about;
    }

    public String getLocation() {
        return location;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }
}
