package com.example.rcptanguilig.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ToDoListFragment extends Fragment implements AddTaskFragment.AddTaskListener, View.OnClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";

    private OnListFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter<MyToDoRecyclerViewAdapter.ViewHolder> mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ToDoItem> mDataset;

    private FloatingActionButton addBtn;
    private ToDoListDBHelper mDatabase;



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ToDoListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ToDoListFragment newInstance(int columnCount) {
        ToDoListFragment fragment = new ToDoListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mDataset == null) {
            mDataset = new ArrayList<>();

            mDatabase = new ToDoListDBHelper(this.getActivity());

            ArrayList<ToDoItem> toDoListFrDB = mDatabase.retrieveAllToDoItems(true);

            if (toDoListFrDB != null && toDoListFrDB.size() > 0) {
                mDataset.addAll(toDoListFrDB);
            }

            /*
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String toDoJSONArray = sharedPrefs.getString(ToDoItem.TO_DO_LIST_JSON, null);

            if (toDoJSONArray == null) return;
            try {
                JSONArray toDoArray = new JSONArray(toDoJSONArray);
                if (toDoArray != null) {
                    for (int i=0;i<toDoArray.length();i++){
                        ToDoItem item = new ToDoItem(toDoArray.optJSONObject(i));
                        mDataset.add(item);
                    }
                }
            } catch (JSONException e) {
                Log.i("toDoItem JSON", "Unable to create JSONArray");
                e.printStackTrace();
            }
            */

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.todoListView);

        // set Layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Set the adapter
        mAdapter = new MyToDoRecyclerViewAdapter(mDataset, mListener);
        mRecyclerView.setAdapter(mAdapter);

        addBtn = (FloatingActionButton) view.findViewById(R.id.btn_new_task);
        addBtn.setOnClickListener(this);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();

        /*
        JSONArray toDoArray = new JSONArray();
        for (ToDoItem toDo : mDataset) {
            toDoArray.put(toDo.toJSONObject());
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ToDoItem.TO_DO_LIST_JSON, toDoArray.toString());
        editor.commit();
        */
    }


    @Override
    public void onTaskAdded(ToDoItem task) {
        mDataset.add(task);
        mDatabase.addToDoItem(task);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_new_task) {
            AddTaskFragment newTask = new AddTaskFragment();
            newTask.mAddTaskListener = this;
            newTask.show(getFragmentManager(), "newTask");
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ToDoItem item);
    }
}
