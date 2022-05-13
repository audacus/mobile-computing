package ch.hftm.mobilecomputing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText editTextLogMessage;
    private EditText editTextIntentMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.buttonLogSomething).setOnClickListener(this::logSomething);
        findViewById(R.id.imageButtonSendIntent).setOnClickListener(this::sendIntent);
        findViewById(R.id.buttonGoToOther).setOnClickListener(this::goToOther);

        this.editTextLogMessage = findViewById(R.id.editTextLogMessage);
        this.editTextIntentMessage = findViewById(R.id.editTextIntentMessage);
    }

    public void logSomething(View view) {
        Log.i(getString(R.string.log_tag), this.editTextLogMessage.getText().toString());
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
}