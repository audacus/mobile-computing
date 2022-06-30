package ch.hftm.mobilecomputing.holder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ch.hftm.mobilecomputing.R;

public class UserViewHolder extends RecyclerView.ViewHolder {

    private final TextView textViewUserId;
    private final TextView textViewUserFirstName;
    private final TextView textViewUserLastName;

    private UserViewHolder(View itemView) {
        super(itemView);
        this.textViewUserId = itemView.findViewById(R.id.textViewUserId);
        this.textViewUserFirstName = itemView.findViewById(R.id.textViewUserFirstName);
        this.textViewUserLastName = itemView.findViewById(R.id.textViewUserLastName);
    }

    public void bind(Integer id, String firstName, String lastName) {
        this.textViewUserId.setText(String.valueOf(id));
        this.textViewUserFirstName.setText(firstName);
        this.textViewUserLastName.setText(lastName);
    }

    public static UserViewHolder create(ViewGroup parent) {
        var view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_user, parent, false);
        return new UserViewHolder(view);
    }
}
