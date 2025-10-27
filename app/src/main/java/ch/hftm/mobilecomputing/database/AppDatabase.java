package ch.hftm.mobilecomputing.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ch.hftm.mobilecomputing.dao.UserDao;
import ch.hftm.mobilecomputing.entity.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    private static final String DATABASE_NAME = "app_database";
    public static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static final RoomDatabase.Callback roomDatabaseCalback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            executor.execute(() -> {
                var dao = INSTANCE.userDao();
                dao.deleteAll();

                var data = Map.of(
                        "Tick", "Duck",
                        "Trick", "Duck",
                        "Track", "Duck"
                ).entrySet().toArray();

                var users = new User[data.length];
                for (var i = 0; i < data.length; i++) {
                    @SuppressWarnings("unchecked")
                    var set = (Map.Entry<String, String>) data[i];

                    var user = new User();
                    user.id = i + 1;
                    user.firstName = set.getKey();
                    user.lastName = set.getValue();

                    users[i] = user;
                }
                dao.insertAll(users);
            });
        }
    };

    public abstract UserDao userDao();

    public static AppDatabase getDatabase(final Context context) {
        synchronized (AppDatabase.class) {
            if (INSTANCE == null) {
                INSTANCE = Room
                        .databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                        .addCallback(roomDatabaseCalback)
                        .build();
            }
        }
        return INSTANCE;
    }
}
