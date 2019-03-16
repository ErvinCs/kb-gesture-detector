package com.kb_p_d.csoka.kb_patter_detector.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Pattern implements Parcelable {
    private String name;
    private Uri uri;

    public Pattern() { }

    public Pattern(String name, Uri uri) {
        this.name = name;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public Uri getUri() {
        return uri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //if(this.id != null)
        dest.writeString(this.name);
        dest.writeString(this.uri.toString());
    }

    private Pattern(Parcel in) {
        this.name = in.readString();
        this.uri = Uri.parse(in.readString());
    }

    // After implementing the `Parcelable` interface, we need to create the
    // `Parcelable.Creator<MyParcelable> CREATOR` constant for our class;
    public static final Parcelable.Creator<Pattern> CREATOR
            = new Parcelable.Creator<Pattern>() {
        // This simply calls the new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Pattern createFromParcel(Parcel in) {
            return new Pattern(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Pattern[] newArray(int size) {
            return new Pattern[size];
        }
    };

    @Override
    public String toString() {
        return "Pattern{name=" + name +
                "; uri=" + uri.toString() +
                "}";
    }
}
