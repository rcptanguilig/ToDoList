package com.example.rcptanguilig.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity  implements
        ToDoListFragment.OnListFragmentInteractionListener {

    ArrayList<ToDoItem> toDoListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (toDoListItems == null) {
            toDoListItems = new ArrayList<>();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root_layout, ToDoListFragment.newInstance(toDoListItems.size()), "toDoList")
                .commit();

    }

    @Override
    public void onListFragmentInteraction(ToDoItem item) {
        // someday...
    }
}
