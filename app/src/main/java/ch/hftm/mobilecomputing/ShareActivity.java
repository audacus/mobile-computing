package ch.hftm.mobilecomputing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import ch.hftm.mobilecomputing.viewadapter.ItemsRecyclerViewAdapter;
import ch.hftm.mobilecomputing.viewadapter.ItemsRecyclerViewAdapter.DataType;

public class ShareActivity extends AppCompatActivity implements ItemsRecyclerViewAdapter.ItemClickListener {

    private List<Map<DataType, String>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        // setup data
        this.data = List.of(
                Map.ofEntries(
                    Map.entry(DataType.NUMBER, "1"),
                    Map.entry(DataType.TITLE, "Title 1"),
                    Map.entry(DataType.DESCRIPTION, "Description 1")
                ),
                Map.ofEntries(
                    Map.entry(DataType.NUMBER, "2"),
                    Map.entry(DataType.TITLE, "Title 2"),
                    Map.entry(DataType.DESCRIPTION, "Description 2")
                ),
                Map.ofEntries(
                    Map.entry(DataType.NUMBER, "3"),
                    Map.entry(DataType.TITLE, "Title 3"),
                    Map.entry(DataType.DESCRIPTION, "Description 3")
                )
        );

        // setup recycler view adapter
        var adapter = new ItemsRecyclerViewAdapter(data);
        adapter.setItemClickListener(this);

        // setup recycler view
        var recyclerView = (RecyclerView) findViewById(R.id.recyclerViewItems);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // get intent
        var intent = getIntent();

        // check intent type
        if (intent.getType().equals(getString(R.string.mime_text_plain))) {
            // get text from intent and set to view
            ((TextView) findViewById(R.id.textViewIntentText)).setText(intent.getStringExtra(Intent.EXTRA_TEXT));
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this,
                String.format(Locale.ENGLISH, "You clicked '%s' on row number %d", this.data.get(position).get(DataType.TITLE), position),
                Toast.LENGTH_SHORT).show();
    }
}