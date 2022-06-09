package ch.hftm.mobilecomputing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import ch.hftm.mobilecomputing.fragment.DatePickerFragment;

public class ElementActivity extends AppCompatActivity implements DatePickerFragment.OnDatePass {

    private TextView textViewDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element);

        findViewById(R.id.buttonDatePicker).setOnClickListener(this::showDatePickerDialog);

        this.textViewDate = findViewById(R.id.textViewDate);
    }

    public void showDatePickerDialog(View view) {
        var fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDatePass(int year, int month, int date) {
        this.textViewDate.setText(String.format(Locale.ENGLISH, "%02d.%02d.%04d", date, month, year));
    }
}