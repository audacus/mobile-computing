package ch.hftm.mobilecomputing.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ch.hftm.mobilecomputing.entity.User;

@Dao
public interface UserDao {

    @Query("select * from user order by id")
    LiveData<List<User>> getAll();

    @Query("select count(*) from user")
    LiveData<Integer> getCount();

    @Query("select * from user where id in (:ids) order by id")
    List<User> loadAllByIds(int[] ids);

    @Query("select * from user where first_name like '%' || :name || '%' or last_name like '%' || :name || '%' order by id limit 1")
    User find(String name);

    @Query("select * from user where first_name like :firstName and last_name like :lastName order by id limit 1")
    User findByNames(String firstName, String lastName);

    @Query("select * from user order by id desc limit 1")
    LiveData<User> getLast();

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);

    @Query("delete from user")
    void deleteAll();

}
