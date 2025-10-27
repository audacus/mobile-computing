package ch.hftm.mobilecomputing.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private OnDatePass datePasser;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // use the current date as the default date
        final var c = Calendar.getInstance();
        var year = c.get(Calendar.YEAR);
        var month = c.get(Calendar.MONTH);
        var day = c.get(Calendar.DAY_OF_MONTH);

        // create a new instance of the date picker dialog
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.datePasser.onDatePass(year, month, dayOfMonth);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.datePasser = (OnDatePass) context;
    }

    public interface OnDatePass {

        void onDatePass(int year, int month, int date);
    }
}