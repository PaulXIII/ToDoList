package by.android.todolist_example.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Павел on 24.10.2016.
 */
public class ToDoList
{

    @SerializedName("lastDateUpdating")
    @Expose
    private String lastDateUpdating;

    @SerializedName("userTasksList")
    @Expose
    private List<UserTasks> userTasksList;


    public String getLastDateUpdating() {
        return lastDateUpdating;
    }

    public void setLastDateUpdating(String lastDateUpdating) {
        this.lastDateUpdating = lastDateUpdating;
    }

    public List<UserTasks> getUserTasksList() {
        return userTasksList;
    }

    public void setUserTasksList(List<UserTasks> userTasksList) {
        this.userTasksList = userTasksList;
    }



}
