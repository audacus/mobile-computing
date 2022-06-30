package ch.hftm.mobilecomputing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;

import ch.hftm.mobilecomputing.adapter.UserListAdapter;
import ch.hftm.mobilecomputing.database.AppDatabase;
import ch.hftm.mobilecomputing.entity.User;
import ch.hftm.mobilecomputing.viewmodel.UserViewModel;

public class EditorActivity extends AppCompatActivity {

    private EditText editTextEditorContent;
    private TextView textViewDatabaseCount;

    private SharedPreferences preferences;
    private AppDatabase database;

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        this.editTextEditorContent = findViewById(R.id.editTextEditorContent);
        this.textViewDatabaseCount = findViewById(R.id.textViewDatabaseCount);

        findViewById(R.id.buttonLoadFromPreferences).setOnClickListener(this::onLoadFromPreferences);
        findViewById(R.id.buttonLoadFromFile).setOnClickListener(this::onLoadFromFile);
        findViewById(R.id.buttonLoadFromDatabase).setOnClickListener(this::onLoadFromDatabase);

        findViewById(R.id.buttonSaveToPreferences).setOnClickListener(this::onSaveToPreferences);
        findViewById(R.id.buttonSaveToFile).setOnClickListener(this::onSaveToFile);
        findViewById(R.id.buttonSaveToDatabase).setOnClickListener(this::onSaveToDatabase);
        findViewById(R.id.buttonClearDatabase).setOnClickListener(this::onClearDatabase);

        this.preferences = getSharedPreferences(getString(R.string.editor_shared_preference), Context.MODE_PRIVATE);
        this.database = AppDatabase.getDatabase(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewUsers);
        var adapter = new UserListAdapter(new UserListAdapter.UserDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        this.userViewModel.getUsers().observe(this, adapter::submitList);
        this.userViewModel.getLast().observe(this, (u) -> {
            if (u == null) return;

            var content = u.firstName + " " + u.lastName;
            this.editTextEditorContent.setText(content);
        });
        this.userViewModel.getCount().observe(this, (c) -> this.textViewDatabaseCount.setText(String.format(Locale.GERMAN, "Die Datenbank enthält %d Einträge", c)));
    }

    private void onLoadFromPreferences(View view) {
        var content = this.preferences.getString(getString(R.string.editor_content), "preferences default value");
        this.editTextEditorContent.setText(content);

        Toast.makeText(this, "loaded from preferences", Toast.LENGTH_SHORT).show();
    }

    private void onLoadFromFile(View view) {
        var contentBuilder = new StringBuilder();
        var content = "file default value";
        try {
            var reader = new BufferedReader(
                    new InputStreamReader(
                            openFileInput(getString(R.string.editor_file)), StandardCharsets.UTF_8));

            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }

            reader.close();
            content = contentBuilder.toString().trim();

            Toast.makeText(this, "loaded from file", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(MainActivity.TAG, e.getMessage());
            Toast.makeText(this, Objects.requireNonNull(e.getCause()).getMessage(), Toast.LENGTH_SHORT).show();
        }

        this.editTextEditorContent.setText(content);
    }

    private void onLoadFromDatabase(View view) {
        AppDatabase.executor.execute(() -> {
            var content = "database default content";
            var user = this.userViewModel.getLast().getValue();

            if (user != null) {
                content = user.firstName + " " + user.lastName;
            }

            this.editTextEditorContent.setText(content);
        });

    }

    private void onSaveToPreferences(View view) {
        var content = this.editTextEditorContent.getText().toString();
        this.preferences.edit().putString(getString(R.string.editor_content), content).apply();

        Toast.makeText(this, "saved to preferences", Toast.LENGTH_SHORT).show();
    }

    private void onSaveToFile(View view) {
        var content = this.editTextEditorContent.getText().toString();
        try {
            var outputStreamWriter = new OutputStreamWriter(
                    openFileOutput(getString(R.string.editor_file), Context.MODE_PRIVATE));

            outputStreamWriter.write(content);
            outputStreamWriter.close();

            Toast.makeText(this, "saved to file", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(MainActivity.TAG, e.getMessage());
            Toast.makeText(this, Objects.requireNonNull(e.getCause()).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void onSaveToDatabase(View view) {
        AppDatabase.executor.execute(() -> {
            var content = this.editTextEditorContent.getText().toString();

            var parts = content.split("\\s");
            var firstNameLength = parts.length / 2;

            var firstNameBuilder = new StringBuilder();
            var lastNameBuilder = new StringBuilder();
            for (var i = 0; i < parts.length; i++) {
                if (i < firstNameLength)
                    firstNameBuilder.append(parts[i]).append(" ");
                else
                    lastNameBuilder.append(parts[i]).append(" ");
            }

            var user = new User();

            // generate new id
            var id = 1;
            var last = this.userViewModel.getLast().getValue();
            if (last != null) id += last.id;

            user.id = id;
            user.firstName = firstNameBuilder.toString().trim();
            user.lastName = lastNameBuilder.toString().trim();

            this.userViewModel.insert(user);
        });
    }

    private void onClearDatabase(View view) {
        AppDatabase.executor.execute(() -> this.database.clearAllTables());
    }
}