package by.android.todolist_example.ui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import by.android.todolist_example.R;
import by.android.todolist_example.helper.ItemTouchHelperAdapter;
import by.android.todolist_example.model.UserTasks;


/**
 * Created by Павел on 24.10.2016.
 */
public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoListAdapterHolder> implements ItemTouchHelperAdapter {


    private List<UserTasks> tasksList = new ArrayList<>();

    public List<UserTasks> getTasksList() {
        return tasksList;
    }

    public void updateList(List<UserTasks> list) {
        tasksList.clear();
        tasksList.addAll(list);
        notifyDataSetChanged();
    }

    public void addItem(UserTasks userTasks, int position) {
        if (position == -1) {
            tasksList.add(userTasks);
        } else {
            tasksList.get(position).setTitle(userTasks.getTitle());
            tasksList.get(position).setDescription(userTasks.getDescription());
        }
        notifyDataSetChanged();
    }

    @Override
    public ToDoListAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_do_list_item, parent, false);
        return new ToDoListAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(final ToDoListAdapterHolder holder, final int position) {
        holder.title.setText(tasksList.get(position).getTitle());
        holder.description.setText(tasksList.get(position).getDescription());
    }


    @Override
    public int getItemCount() {
        return tasksList == null ? 0 : tasksList.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(tasksList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(tasksList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        tasksList.remove(position);
        notifyItemRemoved(position);
    }

    public static class ToDoListAdapterHolder extends RecyclerView.ViewHolder {

        View view;
        public TextView title;
        public TextView description;


        public ToDoListAdapterHolder(View itemView) {
            super(itemView);
            view = itemView;
            title = (TextView) itemView.findViewById(R.id.to_do_item_title);
            description = (TextView) itemView.findViewById(R.id.to_do_item_description);

        }
    }

}
