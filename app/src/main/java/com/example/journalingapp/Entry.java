package com.example.journalingapp;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;

@IgnoreExtraProperties
public class Entry implements Serializable {

    private String Title;
    private String Content;
    private @ServerTimestamp Date date;
    private String UserID;
    private String ownerName;


    public Entry() { //no argument constructor because the compiler was angry at me
    }

    public Entry(String title, String content, Date date, String userID) {
        this.Title = title;
        this.Content = content;
        this.date = date;
        this.UserID = userID;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        this.Content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String writer) {
        this.UserID = writer;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "title='" + Title + '\'' +
                ", content='" + Content + '\'' +
                ", date=" + date +
                ", writer='" + UserID + '\'' +
                '}';
    }
}
