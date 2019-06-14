package com.jesuraj.java.businesscard;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Card implements Parcelable {
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

    private String productPhotos;

    protected Card(Parcel in) {
        usrid = in.readInt();
        cmpyname = in.readString();
        description = in.readString();
        comments = in.readString();
        fimgpath = in.readString();
        bimgpath = in.readString();
        datetime = in.readString();
        productPhotos = in.readString();
    }

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    public String getProductPhotos() {
        return productPhotos;
    }

    public void setProductPhotos(String productPhotos) {
        this.productPhotos = productPhotos;
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
    public Card(String cmpyname, String description, String comments, String fimgpath, String bimgpath, String datetime,String productPhotos) {
        this.cmpyname = cmpyname;
        this.description = description;
        this.comments = comments;
        this.fimgpath = fimgpath;
        this.bimgpath = bimgpath;
        this.datetime = datetime;
        this.productPhotos=productPhotos;
    }

    public Card(int usrid, String cmpyname, String description, String comments, String fimgpath, String bimgpath, String datetime, String productPhotos) {
        this.usrid = usrid;
        this.cmpyname = cmpyname;
        this.description = description;
        this.comments = comments;
        this.fimgpath = fimgpath;
        this.bimgpath = bimgpath;
        this.datetime = datetime;
        this.productPhotos = productPhotos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(usrid);
        dest.writeString(cmpyname);
        dest.writeString(description);
        dest.writeString(comments);
        dest.writeString(fimgpath);
        dest.writeString(bimgpath);
        dest.writeString(datetime);
        dest.writeString(productPhotos);
    }
}

