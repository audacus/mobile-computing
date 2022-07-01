package ch.hftm.mobilecomputing.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ch.hftm.mobilecomputing.entity.User;
import ch.hftm.mobilecomputing.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository repository;

    private final LiveData<User> currentUser;
    private final LiveData<List<User>> users;

    private final MutableLiveData<Integer> count;

    public UserViewModel(@NonNull Application application) {
        super(application);
        this.repository = new UserRepository(application);
        this.currentUser = this.repository.getLast();
        this.users = this.repository.getUsers();

        this.count = new MutableLiveData<>();

        this.users.observeForever((users) -> this.count.setValue(users.size()));
    }

    public LiveData<User> getLast() {
        return this.currentUser;
    }

    public LiveData<List<User>> getUsers() {
        return this.users;
    }

    public LiveData<Integer> getCount() {
        return this.count;
    }

    public void insert(User user) {
        this.repository.insert(user);
    }

    public User findByNames(String firstName, String lastName) {
        return this.repository.findByNames(firstName, lastName);
    }
}
