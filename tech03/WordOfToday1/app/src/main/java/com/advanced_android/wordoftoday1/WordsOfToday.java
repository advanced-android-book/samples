package com.advanced_android.wordoftoday1;

import android.os.Parcel;
import android.os.Parcelable;

public class WordsOfToday implements Parcelable {
    long _id;
    String name;
    String words;
    int date;

    public WordsOfToday() {
    }

    public WordsOfToday(long id, String name, String words, int date) {
        this._id = id;
        this.name = name;
        this.words = words;
        this.date = date;
    }

    protected WordsOfToday(Parcel in) {
        _id = in.readLong();
        name = in.readString();
        words = in.readString();
        date = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeString(name);
        dest.writeString(words);
        dest.writeInt(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WordsOfToday> CREATOR = new Creator<WordsOfToday>() {
        @Override
        public WordsOfToday createFromParcel(Parcel in) {
            return new WordsOfToday(in);
        }

        @Override
        public WordsOfToday[] newArray(int size) {
            return new WordsOfToday[size];
        }
    };

    @Override
    public String toString() {
        return "WordsOfToday _id=" + _id + ", name=" + name +
                ", words=" + words + ", date=" + date;
    }
}
