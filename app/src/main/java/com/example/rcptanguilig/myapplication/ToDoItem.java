package com.example.rcptanguilig.myapplication;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rcptanguilig on 15/06/2016.
 */
public class ToDoItem implements Parcelable {
    long id;
    String toDo;
    long dateCreated;
    boolean archived;

    public static final String TO_DO_LIST_JSON = "ToDoJSONList";

    public ToDoItem(String toDoItem) {
        this.id = -1;
        this.toDo = toDoItem;
        this.dateCreated = Calendar.getInstance().getTimeInMillis();
        this.archived = false;
    }

    public ToDoItem(long id, String toDoItem, long dateCreated, int isArchived) {
        this.id = id;
        this.toDo = toDoItem;
        this.dateCreated = dateCreated;
        this.archived = isArchived == 0 ? false : true;
    }

    public final String toJSONString() {
        return this.toJSONObject().toString();
    }

    public ToDoItem(JSONObject toDoObject) {
        try {
            toDo = toDoObject.getString("ToDoItem");
            dateCreated = toDoObject.getLong("DateCreated");
            archived = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public final JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        try {
            json.put("ToDoItem", toDo);
            json.put("DateCreated", dateCreated);
            Log.i("toDoItem JSON", json.toString());
        } catch (JSONException e) {
            Log.i("toDoItem JSON", "Unable to create JSONString");
            e.printStackTrace();
        }
        return json;
    }

    public String printToDoString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String callDayTime = format.format(new Date(this.dateCreated));

        StringBuilder toDoString = new StringBuilder("ToDo item: \n");
        toDoString.append("To Do item: ").append(this.toDo)
                .append("\n Date created: ").append(callDayTime)
                .append("\n archived?: ").append(this.archived);
        return toDoString.toString();
    }

    public ToDoItem(Parcel parcel) {
        this.toDo = parcel.readString();
        this.dateCreated = parcel.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(toDo);
        dest.writeLong(dateCreated);
    }

    public static final Creator<ToDoItem> CREATOR = new Creator<ToDoItem>() {
        @Override
        public ToDoItem createFromParcel(Parcel parcel) {
            return new ToDoItem(parcel);
        }

        @Override
        public ToDoItem[] newArray(int size) {
            return new ToDoItem[size];
        }
    };

}
