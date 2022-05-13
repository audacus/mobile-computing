package ch.hftm.mobilecomputing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        // get intent
        var intent = getIntent();

        // check intent type
        if (intent.getType().equals(getString(R.string.mime_text_plain))) {
            // get text from intent and set to view
            ((TextView) findViewById(R.id.textViewIntentText)).setText(intent.getStringExtra(Intent.EXTRA_TEXT));
        }
    }
}