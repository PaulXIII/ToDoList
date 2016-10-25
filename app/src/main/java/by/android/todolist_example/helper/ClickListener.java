package by.android.todolist_example.helper;

import android.view.View;

/**
 * Created by Павел on 25.10.2016.
 */
public interface ClickListener {
    public void onClick(View view,int position);
    public void onLongClick(View view, int position);

}
