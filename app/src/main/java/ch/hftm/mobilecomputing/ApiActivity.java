package ch.hftm.mobilecomputing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

import java.util.Objects;

import ch.hftm.mobilecomputing.adapter.EventIdsSpinnerAdapter;
import ch.hftm.mobilecomputing.databinding.ActivityApiBinding;
import ch.hftm.mobilecomputing.entity.Event;
import ch.hftm.mobilecomputing.repository.EventRepository;
import ch.hftm.mobilecomputing.viewmodel.EventListViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiActivity extends AppCompatActivity {

    private EventListViewModel model;

    private Spinner spinnerEvents;
    private TextView textViewApiResult;

    private final Callback<Event> printResponseJsonCallback = new Callback<>() {
        @Override
        public void onResponse(@NonNull Call<Event> call, @NonNull Response<Event> response) {
            textViewApiResult.setText(new GsonBuilder()
                    .setPrettyPrinting()
                    .create()
                    .toJson(response.body()));
        }

        @Override
        public void onFailure(@NonNull Call<Event> call, @NonNull Throwable t) {
            Log.e(MainActivity.TAG, Objects.requireNonNull(t.getCause()).getMessage());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);

        this.model = new ViewModelProvider(this).get(EventListViewModel.class);
        ActivityApiBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_api);
        binding.setLifecycleOwner(this);
        binding.setEventListViewModel(this.model);

        findViewById(R.id.buttonGetEvents).setOnClickListener(this::onGetEvents);
        findViewById(R.id.buttonGetEvent).setOnClickListener(this::onGetEvent);
        findViewById(R.id.buttonCreateEvent).setOnClickListener(this::onCreateEvent);
        findViewById(R.id.buttonDeleteEvent).setOnClickListener(this::onDeleteEvent);

        this.spinnerEvents = findViewById(R.id.spinnerEvents);
        this.textViewApiResult = findViewById(R.id.textViewApiResult);

        this.model.getEvents().observe(this, e -> {
            var adapter = new EventIdsSpinnerAdapter(
                    this,
                    R.layout.item_event_spinner,
                    R.id.textViewEventItemText,
                    Objects.requireNonNull(this.model.getEvents().getValue()));

            this.spinnerEvents.setAdapter(adapter);
        });
    }

    private void onGetEvents(View view) {
        this.model.loadEvents();
    }

    private void onGetEvent(View view) {
        var event = (Event) this.spinnerEvents.getSelectedItem();

        if (event == null) {
            Toast.makeText(this, "select event to get", Toast.LENGTH_SHORT).show();
            return;
        }

        var id = event.getId();
        EventRepository.getInstance().loadEvent(id, this.printResponseJsonCallback);
    }

    private void onCreateEvent(View view) {
        var input = ((EditText) findViewById(R.id.editTextMultilineEvent)).getText().toString();
        var event = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create()
                .fromJson(input, Event.class);

        EventRepository.getInstance().addEvent(event, this.printResponseJsonCallback);
    }

    private void onDeleteEvent(View view) {
        var event = (Event) this.spinnerEvents.getSelectedItem();

        if (event == null) {
            Toast.makeText(this, "select event to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        var id = event.getId();
        EventRepository.getInstance().deleteEvent(id, this.printResponseJsonCallback);

        model.loadEvents();
    }
}