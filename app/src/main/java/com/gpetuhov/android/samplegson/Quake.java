package com.gpetuhov.android.samplegson;


import com.google.gson.annotations.SerializedName;

// Stores information about one earthquake.
public class Quake {

    @SerializedName("properties")
    private QuakeProperties mQuakeProperties;

    private double getMagnitude() {
        return mQuakeProperties.mMagnitude;
    }

    private String getLocation() {
        return mQuakeProperties.mLocation;
    }

    private long getTime() {
        return mQuakeProperties.mTimeInMilliseconds;
    }

    @Override
    public String toString() {
        String s = getMagnitude() + ", " + getLocation() + ", " + getTime();
        return s;
    }

    // In JSON response quake properties are stored in the nested object
    private class QuakeProperties {
        // Magnitude of the earthquake
        @SerializedName("mag")
        private double mMagnitude;

        // Location name of the earthquake
        @SerializedName("place")
        private String mLocation;

        // Time of the earthquake (in milliseconds)
        @SerializedName("time")
        private long mTimeInMilliseconds;
    }
}
