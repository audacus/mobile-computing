package ch.hftm.mobilecomputing.api;

import java.util.List;

import ch.hftm.mobilecomputing.entity.Event;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BackendApiService {

    @GET("events")
    Call<List<Event>> getEvents();

    @GET("events/{id}")
    Call<Event> getEvent(@Path("id") String id);

    @POST("events")
    Call<Event> createEvent(@Body Event event);

    @DELETE("events/{id}")
    Call<Event> deleteEvent(@Path("id") String id);
}
