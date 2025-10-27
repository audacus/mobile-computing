package ch.hftm.mobilecomputing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ch.hftm.mobilecomputing.R;
import ch.hftm.mobilecomputing.entity.Event;

public class EventIdsSpinnerAdapter extends ArrayAdapter<Event> {

    public EventIdsSpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Event> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @NonNull
    private View initView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event_spinner, parent, false);

        TextView textViewEventItemId = convertView.findViewById(R.id.textViewEventItemText);
        var currentItem = getItem(position);

        if (currentItem != null) textViewEventItemId.setText(currentItem.getDescription());

        return convertView;
    }
}
