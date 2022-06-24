package ch.hftm.mobilecomputing;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import ch.hftm.mobilecomputing.receiver.PowerManagementReceiver;

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

        findViewById(R.id.buttonGoToLinearLayout).setOnClickListener(this::goToLinear);
        findViewById(R.id.buttonGoToRelativeLayout).setOnClickListener(this::goToRelative);
        findViewById(R.id.buttonGoToConstraintLayout).setOnClickListener(this::goToConstraint);

        this.editTextLogMessage = findViewById(R.id.editTextLogMessage);
        this.editTextIntentMessage = findViewById(R.id.editTextIntentMessage);

        // register receiver
        var receiver = new PowerManagementReceiver();
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_BATTERY_LOW));
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_POWER_CONNECTED));
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_POWER_DISCONNECTED));
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

    public void goToPhoto(View view) {
        startActivity(new Intent(this, PhotoActivity.class));
    }

    public void goToElements(View view) {
        startActivity(new Intent(this, ElementActivity.class));
    }

    public void goToCompass(View view) {
        startActivity(new Intent(this, CompassActivity.class));
    }

    public void goToSensors(View view) {
        startActivity(new Intent(this, SensorActivity.class));
    }

    public void goToLocation(View view) {
        startActivity(new Intent(this, LocationActivity.class));
    }

    public void goToNetwork(View view) {
        startActivity(new Intent(this, NetworkActivity.class));
    }

    public void goToApi(View view) {
        startActivity(new Intent(this, ApiActivity.class));
    }

    public void goToWeb(View view) {
        startActivity(new Intent(this, WebViewActivity.class));
    }

    public void goToLinear(View view) {
        startActivity(new Intent(this, LinearActivity.class));
    }

    public void goToRelative(View view) {
        startActivity(new Intent(this, RelativeActivity.class));
    }

    public void goToConstraint(View view) {
        startActivity(new Intent(this, ConstraintActivity.class));
    }

    private void sendTextIntent() {
        Intent textIntent = new Intent();
        textIntent.setAction(Intent.ACTION_SEND);
        textIntent.putExtra(Intent.EXTRA_TEXT, this.editTextIntentMessage.getText().toString());
        textIntent.setType(getString(R.string.mime_text_plain));
        startActivity(textIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuGoToOther) this.goToOther(null);
        else if (item.getItemId() == R.id.menuGoToPhoto) this.goToPhoto(null);
        else if (item.getItemId() == R.id.menuGoToElements) this.goToElements(null);
        else if (item.getItemId() == R.id.menuGoToCompass) this.goToCompass(null);
        else if (item.getItemId() == R.id.menuGoToSensors) this.goToSensors(null);
        else if (item.getItemId() == R.id.menuGoToLocation) this.goToLocation(null);
        else if (item.getItemId() == R.id.menuGoToNetwork) this.goToNetwork(null);
        else if (item.getItemId() == R.id.menuGoToApi) this.goToApi(null);
        else if (item.getItemId() == R.id.menuGoToWeb) this.goToWeb(null);
        else return super.onOptionsItemSelected(item);

        // return true if menu item was handled
        return true;
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