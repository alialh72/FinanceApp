package com.example.financeapp.Objects;

import android.os.Parcel;
import android.os.Parcelable;

public class Definition implements Parcelable {

    private String word, description;

    public Definition(String word, String description){
        this.word = word;
        this.description = description;
    }

    protected Definition(Parcel in){
        word = in.readString();
        description = in.readString();
    }

    public static final Creator<Definition> CREATOR = new Creator<Definition>() {
        @Override
        public Definition createFromParcel(Parcel in) {
            return new Definition(in);
        }

        @Override
        public Definition[] newArray(int size) {
            return new Definition[size];
        }
    };

    public String getWord(){
        return word;
    }
    public String getDescription() {return description;}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(word);
        dest.writeString(description);
    }
}
