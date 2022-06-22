package ch.hftm.mobilecomputing.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import ch.hftm.mobilecomputing.MainActivity;
import ch.hftm.mobilecomputing.api.BackendApiService;
import ch.hftm.mobilecomputing.api.BackendApiServiceFactory;
import ch.hftm.mobilecomputing.database.EventDatabase;
import ch.hftm.mobilecomputing.entity.Event;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventRepository {

    private static EventRepository instance;
    private final BackendApiService apiService;

    private final MutableLiveData<List<Event>> events;

    private EventRepository() {
        this.apiService = BackendApiServiceFactory.create();

        this.events = new MutableLiveData<>();
    }

    public static EventRepository getInstance() {
        if (instance == null) {
            instance = new EventRepository();
        }
        return instance;
    }

    public LiveData<List<Event>> getEvents() {
        return this.events;
    }

    public void loadEvents() {
        var getEventsCall = this.apiService.getEvents();

        getEventsCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Event>> call, @NonNull Response<List<Event>> response) {
                if (!response.isSuccessful() || response.body() == null) return;

                events.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Event>> call, @NonNull Throwable t) {
                Log.e(MainActivity.TAG, Objects.requireNonNull(t.getCause()).getMessage());
            }
        });
    }

    public void loadEvent(String id, Callback<Event> callback) {
        this.apiService.getEvent(id).enqueue(callback);
    }

    public void addEvent(Event event, Callback<Event> callback) {
        this.apiService.createEvent(event).enqueue(callback);
    }

    public void deleteEvent(String id, Callback<Event> callback) {
        this.apiService.deleteEvent(id).enqueue(callback);
    }

    private void updateDatabase() {
        EventDatabase.events.clear();
        EventDatabase.events.addAll(Objects.requireNonNull(events.getValue()));
    }
}
