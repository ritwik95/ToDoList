package com.example.ritwikkaul.todolist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public final String POPUP_LOGIN_TITLE="ADD A NEW TASK";
    public final String TITLE_HINT="---TITLE---";
    public final String DESCRIPTION_HINT="--DESCRIPTION--";
    Map<String, String> tasks = new HashMap<String, String>();
    private ArrayAdapter<String> mAdapter;
    private ListView mTaskListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTaskListView = (ListView) findViewById(R.id.list_todo);

        SharedPreferences keyValues = getApplicationContext().getSharedPreferences("name_icons_list", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = keyValues.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            tasks.put(entry.getKey(),entry.getValue().toString());
        }
        updateUI();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                /*final EditText taskEditText = new EditText(this);
                final EditText descripEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Title of the Note")
                        .setMessage("Title of the Note")
                        .setView(taskEditText)
                        .setMessage("Description of the note")
                        .setView(descripEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                String des = String.valueOf(descripEditText.getText());
                                Log.d(TAG, "Task to add: " + task);
                                Log.d(TAG, "Description of Task: " + des);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;*/
                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle(POPUP_LOGIN_TITLE);

                // Set an EditText view to get user input
                final EditText taskEditText = new EditText(this);
                taskEditText.setHint(TITLE_HINT);
                final EditText  descripEditText= new EditText(this);
                descripEditText.setHint(DESCRIPTION_HINT);
                LinearLayout layout = new LinearLayout(getApplicationContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(taskEditText);
                layout.addView(descripEditText);
                alert.setView(layout);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String task = String.valueOf(taskEditText.getText());
                        String des = String.valueOf(descripEditText.getText());
                        Log.d(TAG, "Task to add: " + task);
                        Log.d(TAG, "Description of Task: " + des);
                        tasks.put(task,des);

                        updateUI();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alert.show();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop(){
        super.onStop();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences keyValues = getApplicationContext().getSharedPreferences("name_icons_list", Context.MODE_PRIVATE);
        keyValues.edit().clear().commit();
        SharedPreferences.Editor keyValuesEditor = keyValues.edit();

        for (String s : tasks.keySet()) {
            // use the name as the key, and the icon as the value
            keyValuesEditor.putString(s, tasks.get(s));
        }
        keyValuesEditor.commit();
    }


    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        int i=0;
        for (String key : tasks.keySet()) {
            taskList.add(key);
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.item_todo,
                    R.id.task_title,
                    taskList);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }


    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        Log.d(TAG,task);
        tasks.remove(task);
        updateUI();
    }

}
