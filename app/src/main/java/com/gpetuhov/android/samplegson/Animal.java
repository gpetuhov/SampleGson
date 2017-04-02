package com.gpetuhov.android.samplegson;

import com.google.gson.annotations.SerializedName;

// Class to be serialized / deserialized by Gson in simple example
public class Animal {

    // Annotation tells how this field will be named in JSON
    @SerializedName("type")
    private String mType;

    @SerializedName("name")
    private String mName;

    @SerializedName("age")
    private int mAge;

    public Animal(String type, String name, int age) {
        mType = type;
        mName = name;
        mAge = age;
    }

    @Override
    public String toString() {
        String s = "Type = "
                + mType + ", "
                + "Name = "
                + mName + ", "
                + "Age = "
                + mAge;
        return s;
    }
}
