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
import android.widget.TextView;

import java.util.Locale;

public class CompassActivity extends AppCompatActivity implements SensorEventListener {

    private TextView textViewCompassValue360;
    private TextView textViewCompassValue180;
    private ImageView imageViewCompass;

    private SensorManager sensorManager;

    private final float[] accelerometerReading = new float[3];
    private final float[] magnetometerReading = new float[3];

    private final float[] rotationMatrix = new float[9];
    private final float[] orientationAngles = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        this.textViewCompassValue360 = findViewById(R.id.textViewCompassValue360);
        this.textViewCompassValue180 = findViewById(R.id.textViewCompassValue180);
        this.imageViewCompass = findViewById(R.id.imageViewCompass);

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

        var accelerometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) this.sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        var magneticField = this.sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticField != null) this.sensorManager.registerListener(this, magneticField, SensorManager.SENSOR_DELAY_NORMAL);
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

        double angle180 = Math.toDegrees(this.orientationAngles[0]);
        double angle360 = (angle180 + 360.0) % 360.0;

        this.textViewCompassValue360.setText(String.format(Locale.ENGLISH, "%.2f°", angle360));
        this.textViewCompassValue180.setText(String.format(Locale.ENGLISH, "%.2f°", angle180));

        // rotate compass image
        this.imageViewCompass.setRotation((float) angle360 * (-1));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}