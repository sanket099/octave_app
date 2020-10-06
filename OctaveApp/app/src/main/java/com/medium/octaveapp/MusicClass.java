package com.medium.octaveapp;

import android.os.Parcel;
import android.os.Parcelable;

public class MusicClass implements Parcelable {
    private String title;
    private String artist;
    private String path;

    public MusicClass(String title, String artist,String path) {
        this.title = title;
        this.artist = artist;
        this.path = path;
    }

    protected MusicClass(Parcel in) {
        title = in.readString();
        artist = in.readString();
        path = in.readString();
    }

    public static final Creator<MusicClass> CREATOR = new Creator<MusicClass>() {
        @Override
        public MusicClass createFromParcel(Parcel in) {
            return new MusicClass(in);
        }

        @Override
        public MusicClass[] newArray(int size) {
            return new MusicClass[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getPath() {
        return path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeString(path);
    }
}
