package com.jesuraj.java.businesscard;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Card {
    @PrimaryKey(autoGenerate = true)
    private int usrid;
    @ColumnInfo(name = "companyname")
    private String cmpyname;
    @ColumnInfo()
    private String description;
    private String comments;
    private String fimgpath;
    private String bimgpath;
    private String datetime;

    public Card() {
    }

    public int getUsrid() {
        return usrid;
    }

    public void setUsrid(int usrid) {
        this.usrid = usrid;
    }

    public String getCmpyname() {
        return cmpyname;
    }

    public void setCmpyname(String cmpyname) {
        this.cmpyname = cmpyname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getFimgpath() {
        return fimgpath;
    }

    public void setFimgpath(String fimgpath) {
        this.fimgpath = fimgpath;
    }

    public String getBimgpath() {
        return bimgpath;
    }

    public void setBimgpath(String bimgpath) {
        this.bimgpath = bimgpath;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Ignore
    public Card(String cmpyname, String description, String comments, String fimgpath, String bimgpath, String datetime) {
        this.cmpyname = cmpyname;
        this.description = description;
        this.comments = comments;
        this.fimgpath = fimgpath;
        this.bimgpath = bimgpath;
        this.datetime = datetime;
    }

    public Card(int usrid, String cmpyname, String description, String comments, String fimgpath, String bimgpath, String datetime) {
        this.usrid = usrid;
        this.cmpyname = cmpyname;
        this.description = description;
        this.comments = comments;
        this.fimgpath = fimgpath;
        this.bimgpath = bimgpath;
        this.datetime = datetime;
    }
}

