package by.android.todolist_example.Network;

import by.android.todolist_example.model.ToDoList;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Павел on 24.10.2016.
 */
public interface IApiMethods {


    @GET("{key}/")
    Observable<ToDoList> getToDoList (@Path("key") String key);

    @PUT("{key}/")
    Observable<ToDoList> updateToDoList (@Path("key") String key, @Body ToDoList toDoList);


}
