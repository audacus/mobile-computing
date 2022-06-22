package ch.hftm.mobilecomputing.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ch.hftm.mobilecomputing.entity.Event;
import ch.hftm.mobilecomputing.repository.EventRepository;

public class EventListViewModel extends ViewModel {

    private EventRepository repository;
    private Observer<List<Event>> observer;

    private MutableLiveData<List<Event>> events;

    public LiveData<List<Event>> getEvents() {
        if (this.events == null) {
            this.events = new MutableLiveData<>();
        }
        return this.events;
    }

    public void loadEvents() {
        if (this.repository == null) {
            this.repository = EventRepository.getInstance();

            this.observer = this.events::setValue;

            // add observer to get updates if the api loads the events
            this.repository.getEvents().observeForever(this.observer);
        }

        this.repository.loadEvents();
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if (this.repository == null || this.observer == null) return;

        this.repository.getEvents().removeObserver(this.observer);
    }
}
