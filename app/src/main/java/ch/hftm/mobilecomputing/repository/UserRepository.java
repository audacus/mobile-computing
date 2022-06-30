package ch.hftm.mobilecomputing.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import ch.hftm.mobilecomputing.dao.UserDao;
import ch.hftm.mobilecomputing.database.AppDatabase;
import ch.hftm.mobilecomputing.entity.User;

public class UserRepository {

    private final UserDao userDao;

    private final LiveData<User> currentUser;
    private final LiveData<List<User>> users;
    private final LiveData<Integer> count;

    public UserRepository(Application application) {
        var database = AppDatabase.getDatabase(application);
        this.userDao = database.userDao();
        this.currentUser = this.userDao.getLast();
        this.users = this.userDao.getAll();
        this.count = this.userDao.getCount();
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
        AppDatabase.executor.execute(() -> this.userDao.insertAll(user));
    }

    public User findByNames(String firstName, String lastName) {
        return this.userDao.findByNames(firstName, lastName);
    }
}
