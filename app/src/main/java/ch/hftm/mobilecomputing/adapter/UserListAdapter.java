package ch.hftm.mobilecomputing.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import ch.hftm.mobilecomputing.entity.User;
import ch.hftm.mobilecomputing.holder.UserViewHolder;

public class UserListAdapter extends ListAdapter<User, UserViewHolder> {

    public UserListAdapter(@NonNull DiffUtil.ItemCallback<User> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return UserViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        var current = getItem(position);
        holder.bind(current.id, current.firstName, current.lastName);
    }

    public static class UserDiff extends DiffUtil.ItemCallback<User> {

        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.firstName.equals(newItem.firstName) && oldItem.lastName.equals(newItem.lastName);
        }
    }
}
