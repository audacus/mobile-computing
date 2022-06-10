package ch.hftm.mobilecomputing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Locale;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private TextView textViewTemperature;
    private TextView textViewLight;

    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        this.textViewTemperature = findViewById(R.id.textViewTemperature);
        this.textViewLight = findViewById(R.id.textViewLight);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // listen for temperature
        var temperature = this.sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if (temperature != null) this.sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL);

        // listen for light
        var light = this.sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (light != null) this.sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        this.sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) this.textViewTemperature.setText(String.format(Locale.ENGLISH, "%.2f °C", event.values[0]));
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) this.textViewLight.setText(String.format(Locale.ENGLISH, "%,.2f lx", event.values[0]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}