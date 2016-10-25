package by.android.todolist_example.Network;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import java.io.IOException;

import by.android.todolist_example.model.ToDoList;
import by.android.todolist_example.ui.recyclerview.ToDoListAdapter;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Павел on 24.10.2016.
 */
public class ServiceHelper {

    public static final String API_KEY = "5h3juf5";
    public static final String URL = "https://profigroup.by/applicants-tests/mobile/v2/";

    private Subscription subscription;
    private Observable<ToDoList> observable;

    private static ServiceHelper instance = new ServiceHelper();
    private Retrofit mRetrofit;

    private ServiceHelper() {
    }

    public static ServiceHelper getInstance() {
        if (instance == null) {
            instance = new ServiceHelper();
        }
        return instance;
    }


    public Retrofit getRetrofit() {

        if (mRetrofit != null) return mRetrofit;

        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .addCallAdapterFactory(rxAdapter)
                .build();

        return mRetrofit;

    }



    public void downloadToDoList(final ToDoListAdapter toDoListAdapter, final SwipeRefreshLayout swipeRefreshLayout) {
        IApiMethods myApi = getRetrofit().create(IApiMethods.class);
        observable = myApi.getToDoList(ServiceHelper.API_KEY);

        subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ToDoList>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            HttpException response = (HttpException) e;
                            int code = response.code();
                            Log.d("TAG", "Errer code is "+ code);
                        }

                    }

                    @Override
                    public void onNext(ToDoList toDoList) {
                        toDoListAdapter.updateList(toDoList.getUserTasksList());
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });


    }

    public void updateToDoList(final ToDoListAdapter toDoListAdapter, String date) {

        ToDoList toDoList = new ToDoList();
        toDoList.setLastDateUpdating(date);
        toDoList.setUserTasksList(toDoListAdapter.getTasksList());

        IApiMethods myApi = getRetrofit().create(IApiMethods.class);
        observable = myApi.updateToDoList(ServiceHelper.API_KEY, toDoList);

        subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ToDoList>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            HttpException response = (HttpException) e;
                            int code = response.code();
                            Log.d("TAG", "Errer code is "+ code);
                        }

                    }

                    @Override
                    public void onNext(ToDoList toDoList) {
                        toDoListAdapter.updateList(toDoList.getUserTasksList());
                    }
                });

    }


}
