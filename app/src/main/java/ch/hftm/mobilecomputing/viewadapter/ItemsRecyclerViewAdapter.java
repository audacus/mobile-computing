package ch.hftm.mobilecomputing.viewadapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.hftm.mobilecomputing.MainActivity;
import ch.hftm.mobilecomputing.R;

public class ItemsRecyclerViewAdapter extends RecyclerView.Adapter<ItemsRecyclerViewAdapter.ItemsViewHolder> {

    private ItemClickListener itemClickListener;
    private List<Map<DataType, String>> data;

    public ItemsRecyclerViewAdapter(List<Map<DataType, String>> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        var view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);

        return new ItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        var entry = this.data.get(position);

        if (entry.containsKey(DataType.NUMBER)) holder.textViewItemNumber.setText(this.data.get(position).get(DataType.NUMBER));
        if (entry.containsKey(DataType.TITLE)) holder.textViewItemTitle.setText(this.data.get(position).get(DataType.TITLE));
        if (entry.containsKey(DataType.DESCRIPTION)) holder.textViewItemDescription.setText(this.data.get(position).get(DataType.DESCRIPTION));
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {

        void onItemClick(View view, int position);
    }

    public enum DataType {
        NUMBER,
        TITLE,
        DESCRIPTION
    }

    public class ItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textViewItemNumber;
        public TextView textViewItemTitle;
        public TextView textViewItemDescription;

        public ItemsViewHolder(View itemView) {
            super(itemView);

            this.textViewItemNumber = itemView.findViewById(R.id.textViewItemNumber);
            this.textViewItemTitle = itemView.findViewById(R.id.textViewItemTitle);
            this.textViewItemDescription = itemView.findViewById(R.id.textViewItemDescription);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, getAdapterPosition());
            } else {
                Log.w(MainActivity.TAG, "itemClickListener is null!");
            }
        }
    }
}
