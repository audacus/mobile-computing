package ch.hftm.mobilecomputing.api;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BackendApiServiceFactory {

    public static BackendApiService create() {
        // create http client
        var client = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();

        var gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        return new Retrofit.Builder()
                .baseUrl("https://whattodo-backend.azurewebsites.net/api/v1/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
                .create(BackendApiService.class);
    }
}
