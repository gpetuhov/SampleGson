package com.gpetuhov.android.samplegson;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        simpleGsonExample();
    }

    private void simpleGsonExample() {
        // Create new animal
        Animal animal = new Animal("Dog", "Butch", 5);

        // Get Gson object
        Gson gson = new Gson();

        // Serialize animal to JSON string
        String animalJson = gson.toJson(animal);

        Log.d(LOG_TAG, "Serialized to " + animalJson);

        // Create animal 2 from JSON string
        Animal animal2 = gson.fromJson(animalJson, Animal.class);

        Log.d(LOG_TAG, "Deserialized to " + animal2.toString());
    }
}
