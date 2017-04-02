package com.gpetuhov.android.samplegson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

// Stores list of earhquakes.
// Deserialized by Gson from JSON received from USGS server.
public class QuakeList {

    @SerializedName("features")
    private List<Quake> mQuakeList;

    public List<Quake> getQuakeList() {
        return mQuakeList;
    }
}
