package ch.hftm.mobilecomputing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "my_log_tag";

    private EditText editTextLogMessage;
    private EditText editTextIntentMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "onCreate()");

        findViewById(R.id.buttonLogSomething).setOnClickListener(this::logSomething);
        findViewById(R.id.imageButtonSendIntent).setOnClickListener(this::sendIntent);
        findViewById(R.id.buttonGoToOther).setOnClickListener(this::goToOther);

        this.editTextLogMessage = findViewById(R.id.editTextLogMessage);
        this.editTextIntentMessage = findViewById(R.id.editTextIntentMessage);
    }

    public void logSomething(View view) {
        Log.i(TAG, this.editTextLogMessage.getText().toString());
    }

    public void sendIntent(View view) {
        this.sendTextIntent();
    }

    public void goToOther(View view) {
        startActivity(new Intent(this, OtherActivity.class));
    }

    private void sendTextIntent() {
        Intent textIntent = new Intent();
        textIntent.setAction(Intent.ACTION_SEND);
        textIntent.putExtra(Intent.EXTRA_TEXT, this.editTextIntentMessage.getText().toString());
        textIntent.setType(getString(R.string.mime_text_plain));
        startActivity(textIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
    }
}