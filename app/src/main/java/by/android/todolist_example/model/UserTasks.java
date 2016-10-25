package by.android.todolist_example.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Павел on 24.10.2016.
 */
public class UserTasks implements Parcelable {


    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.description);
    }

    public UserTasks() {
    }

    protected UserTasks(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<UserTasks> CREATOR = new Parcelable.Creator<UserTasks>() {
        @Override
        public UserTasks createFromParcel(Parcel source) {
            return new UserTasks(source);
        }

        @Override
        public UserTasks[] newArray(int size) {
            return new UserTasks[size];
        }
    };
}
