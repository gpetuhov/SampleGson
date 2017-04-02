package com.gpetuhov.android.samplegson;


import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class QuakeFetcher {

    public static final String LOG_TAG = QuakeFetcher.class.getName();

    // USGS host address
    private final String HOST_ADDRESS = "http://earthquake.usgs.gov/fdsnws/event/1/";

    private QuakeList mQuakeList;

    // USGS API interface to be used in Retrofit
    private interface QuakeFetchService {
        // For USGS query parameters see http://earthquake.usgs.gov/fdsnws/event/1/
        // If magnitude and number of days are not specified,
        // the server returns all magnitudes for the last 30 days.

        @GET("query") // USGS URL for queries is http://earthquake.usgs.gov/fdsnws/event/1/query
        Observable<Response<ResponseBody>> getQuakes(
                @Query("format") String format,             // Response format
                @Query("minmagnitude") String minMagnitude);  // Minimum magnitude
    }

    // USGS API interface to be used in Retrofit with Gson as converter
    private interface QuakeFetchServiceForRetrofitWithGson {
        // Here getQuakes will return not JSON string, but QuakeList object itself
        // (Retrofit will use Gson converter for this)
        @GET("query")
        Observable<QuakeList> getQuakes(
                @Query("format") String format,             // Response format
                @Query("minmagnitude") String minMagnitude);  // Minimum magnitude
    }

    // Fetches earthquake list from USGS server with Retrofit.
    // Gson is used separately from Retrofit (Retrofit returns just JSON string).
    public void fetchQuakes() {

        // Create instance of Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HOST_ADDRESS)
                .client(new OkHttpClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())   // This is needed to use Observables with Retrofit
                .build();

        // Create instance of the USGS API interface implementation
        QuakeFetchService service = retrofit.create(QuakeFetchService.class);

        // getQuakes returns OBSERVABLE, so we can subscribe to it to receive response
        service.getQuakes("geojson", "5")
                .subscribeOn(Schedulers.io())   // Network access runs in background thread
                .observeOn(AndroidSchedulers.mainThread())  // Subscriber runs on the main thread
                .subscribe(
                        // Called on Retrofit success
                        response -> {
                            try {
                                // Get JSON string from response body
                                String jsonString = response.body().string();

                                // Deserialize JSON
                                Gson gson = new Gson();
                                mQuakeList = gson.fromJson(jsonString, QuakeList.class);

                                printQuakes(mQuakeList);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        },
                        // Called on Retrofit error
                        error -> {
                            error.printStackTrace();
                        }
                );
    }

    private void printQuakes(QuakeList quakeList) {
        List<Quake> quakes = quakeList.getQuakeList();

        for (Quake quake : quakes) {
            Log.d(LOG_TAG, quake.toString());
        }
    }

    // Fetches earthquake list from USGS server with Retrofit.
    // Gson is attached to Retrofit (Retrofit returns not JSON string, but quake list).
    public void fetchQuakesWithGsonAttachedToRetrofit() {

        // Create instance of Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HOST_ADDRESS)
                .client(new OkHttpClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())   // This is needed to use Observables with Retrofit
                .addConverterFactory(GsonConverterFactory.create())     // This is needed to use Gson converter in Retrofit
                .build();

        // Create instance of the USGS API interface implementation
        QuakeFetchServiceForRetrofitWithGson service = retrofit.create(QuakeFetchServiceForRetrofitWithGson.class);

        // getQuakes returns OBSERVABLE, so we can subscribe to it to receive response
        service.getQuakes("geojson", "5")
                .subscribeOn(Schedulers.io())   // Network access runs in background thread
                .observeOn(AndroidSchedulers.mainThread())  // Subscriber runs on the main thread
                .subscribe(
                        // Called on Retrofit success
                        quakeList -> {
                            // In this case Retrofit returns QuakeList object already deserialized from JSON
                            mQuakeList = quakeList;
                            printQuakes(mQuakeList);
                        },
                        // Called on Retrofit error
                        error -> {
                            error.printStackTrace();
                        }
                );
    }
}
