package by.android.todolist_example.ui.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import by.android.todolist_example.Network.ServiceHelper;
import by.android.todolist_example.R;
import by.android.todolist_example.helper.ClickListener;
import by.android.todolist_example.helper.RecyclerTouchListener;
import by.android.todolist_example.model.UserTasks;
import by.android.todolist_example.ui.recyclerview.ItemTouchHelperCallback;
import by.android.todolist_example.ui.recyclerview.ToDoListAdapter;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ToDoListAdapter toDoListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<UserTasks> toDoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toDoList = new ArrayList<>();
        toDoListAdapter = new ToDoListAdapter();
        linearLayoutManager = new LinearLayoutManager(this);

        initComponents();
    }

    private void initComponents() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTaskToList(null, null, -1);
                updateToDoList();
            }
        });
        initRefreshLayout();
        initRecyclerView();
        downloadToDoList();


    }

    private void initRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED);

    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_user_tasks);

        recyclerView.swapAdapter(toDoListAdapter, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(toDoListAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        toDoListAdapter.updateList(toDoList);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String title = toDoListAdapter.getTasksList().get(position).getTitle();
                String description = toDoListAdapter.getTasksList().get(position).getDescription();
                addTaskToList(title, description, position);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }


    public void addTaskToList(String title, String description, final int position) {
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_task_to_list_dialog, null);

        final EditText edtTitle = (EditText) dialogView.findViewById(R.id.edit_text_title);
        final EditText edtDescription = (EditText) dialogView.findViewById(R.id.edit_text_description);

        final TextView tvTitleCounter = (TextView) dialogView.findViewById(R.id.tv_title_counter);
        final TextView tvDescriptionCounter = (TextView) dialogView.findViewById(R.id.tv_description_counter);

        if (!TextUtils.isEmpty(title) || !TextUtils.isEmpty(description)) {
            edtTitle.setText(title);
            edtDescription.setText(description);
        }
        changeCounter(edtTitle, tvTitleCounter, 250);
        changeCounter(edtDescription, tvDescriptionCounter, 1000);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Add task dialog");
        dialogBuilder.setMessage("Enter title and description below");
        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                UserTasks userTasks = new UserTasks();
                userTasks.setTitle(edtTitle.getText().toString());
                userTasks.setDescription(edtDescription.getText().toString());
                if (position == -1) {
                    toDoListAdapter.addItem(userTasks, -1);
                } else {
                    toDoListAdapter.addItem(userTasks, position);
                }
                updateToDoList();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();

    }


    private void changeCounter(final EditText editText, final TextView textViewCounter, final int maxLenght) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int titleLenght = editText.getText().length();
                textViewCounter.setText(String.valueOf(titleLenght) + "/" + maxLenght);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {

        mSwipeRefreshLayout.setRefreshing(true);
        downloadToDoList();

    }


    private String getCurrentDate() {
        DateFormat df = new SimpleDateFormat("dd:MM:yyyy, HH:mm");
        return df.format(Calendar.getInstance().getTime());
    }

    private void downloadToDoList() {
        ServiceHelper.getInstance().downloadToDoList(toDoListAdapter, mSwipeRefreshLayout);
    }

    private void updateToDoList() {
        ServiceHelper.getInstance().updateToDoList(toDoListAdapter, getCurrentDate());
    }
}
