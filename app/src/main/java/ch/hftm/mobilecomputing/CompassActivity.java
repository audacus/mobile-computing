package ch.hftm.mobilecomputing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;

/**
 * https://developer.android.com/guide/topics/sensors/sensors_position
 */
public class CompassActivity extends AppCompatActivity implements SensorEventListener, SeekBar.OnSeekBarChangeListener {

    private TextView textViewCompassValue360;
    private TextView textViewCompassValue180;
    private ImageView imageViewCompass;
    private TextView textViewSeekBar;

    private SensorManager sensorManager;

    private final float[] accelerometerReading = new float[3];
    private final float[] magnetometerReading = new float[3];

    private final float[] rotationMatrix = new float[9];
    private final float[] orientationAngles = new float[3];

    private double smoothingFactor = 0.9;
    private double lastSin = 0.0f;
    private double lastCos = 0.0f;

    private static final int SEEK_BAR_MIN_VALUE = 1;
    private static final int SEEK_BAR_MAX_VALUE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        this.textViewCompassValue360 = findViewById(R.id.textViewCompassValue360);
        this.textViewCompassValue180 = findViewById(R.id.textViewCompassValue180);
        this.imageViewCompass = findViewById(R.id.imageViewCompass);
        this.textViewSeekBar = findViewById(R.id.textViewSeekBar);
        SeekBar seekBarSmoothingFactor = findViewById(R.id.seekBarSmoothingFactor);

        seekBarSmoothingFactor.setMax(SEEK_BAR_MAX_VALUE - SEEK_BAR_MIN_VALUE);
        seekBarSmoothingFactor.setOnSeekBarChangeListener(this);
        seekBarSmoothingFactor.setProgress((int) (seekBarSmoothingFactor.getMax() * 0.6));

        // set different compass image depending on night mode on / off
        var nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            this.imageViewCompass.setImageResource(R.drawable.compass_white);
        } else {
            this.imageViewCompass.setImageResource(R.drawable.compass_black);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // listen for acceleration
        var accelerometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) this.sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);

        // listen for magnetic field
        var magneticField = this.sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticField != null) this.sensorManager.registerListener(this, magneticField, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();

        this.sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) System.arraycopy(event.values, 0, this.accelerometerReading, 0, this.accelerometerReading.length);
        else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) System.arraycopy(event.values, 0, this.magnetometerReading, 0, this.magnetometerReading.length);

        this.updateOrientationAngles();
    }

    private void updateOrientationAngles() {
        SensorManager.getRotationMatrix(this.rotationMatrix, null, this.accelerometerReading, this.magnetometerReading);
        SensorManager.getOrientation(this.rotationMatrix, this.orientationAngles);

        var angle = this.orientationAngles[0];

        // remove sensor noise with low pass filter
        // https://christine-coenen.de/blog/2014/07/02/smooth-compass-needle-in-android-or-any-angle-with-low-pass-filter/
        this.lastSin = this.smoothingFactor * this.lastSin + (1.0 - this.smoothingFactor) * Math.sin(angle);
        this.lastCos = this.smoothingFactor * this.lastCos + (1.0 - this.smoothingFactor) * Math.cos(angle);
        var flattenedAngle = Math.atan2(this.lastSin, lastCos);

        var flattenedDegrees180 = (float) Math.toDegrees(flattenedAngle);
        var flattenedDegrees360 = (flattenedDegrees180 + 360.0f) % 360.0f;

        this.textViewCompassValue360.setText(String.format(Locale.ENGLISH, "%d°", (int) flattenedDegrees360));
        this.textViewCompassValue180.setText(String.format(Locale.ENGLISH, "%d°", (int) flattenedDegrees180));

        // rotate compass image
        this.imageViewCompass.setRotation(flattenedDegrees180 * (-1.0f));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        progress = progress + SEEK_BAR_MIN_VALUE;

        // https://mathcracker.com/logarithmic-function-calculator#results
        // t1 = 1, y1 = 1, t2 = 100, y2 = 99
        this.smoothingFactor = 21.2804 * Math.log(1.0481 * progress) / 100.0;

        this.textViewSeekBar.setText(String.format(Locale.ENGLISH, "smoothing: %d%% / %.2f", progress, this.smoothingFactor));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}