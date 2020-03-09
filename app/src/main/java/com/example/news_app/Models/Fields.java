package com.example.news_app.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Fields implements Parcelable {

    private String thumbnail;
    private String body;
    private String headline;
    private String publication;

    public Fields(String thumbnail, String body, String headline, String publication) {
        this.thumbnail = thumbnail;
        this.body = body;
        this.headline = headline;
        this.publication = publication;
    }

    public Fields(){
        this.thumbnail = "";
        this.body = "";
        this.headline = "";
        this.publication = "";
    }

    protected Fields(Parcel in) {
        thumbnail = in.readString();
        body = in.readString();
        headline = in.readString();
        publication = in.readString();
    }

    public static final Creator<Fields> CREATOR = new Creator<Fields>() {
        @Override
        public Fields createFromParcel(Parcel in) {
            return new Fields(in);
        }

        @Override
        public Fields[] newArray(int size) {
            return new Fields[size];
        }
    };

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(thumbnail);
        dest.writeString(body);
        dest.writeString(headline);
        dest.writeString(publication);
    }
}
