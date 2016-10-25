package by.android.todolist_example.helper;

/**
 * Created by Павел on 24.10.2016.
 */
public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
