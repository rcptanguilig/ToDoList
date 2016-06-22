package com.example.rcptanguilig.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by rcptanguilig on 21/06/2016.
 */
public class ToDoListDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION    = 1; // change this when db is changed

    // Database and table name
    private static final String DATABASE_NAME   = "ToDoListData.db";
    private static final String TABLE_NAME_TO_DO= "ToDoList";

    // field names
    public static final String	KEY_ID				= "_id";
    public static final String	KEY_TODO_ITEM	    = "todo_item";
    public static final String	KEY_DATE_CREATED    = "date_created";
    public static final String	KEY_ARCHIVED		= "archived";

    // column numbers
    private static final int COL_ID 		    = 0;
    private static final int COL_TODO_ITEM	    = 1;
    private static final int COL_DATE_CREATED   = 2;
    private static final int COL_ARCHIVED		= 3;

    // static strings for table creation
    private static final String SQL_CREATE_TODO_TABLE =
            "CREATE TABLE " + TABLE_NAME_TO_DO + "("
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_TODO_ITEM + " TEXT  NOT NULL, "
                    + KEY_DATE_CREATED + " BIGINT  NOT NULL, "
                    + KEY_ARCHIVED + " TINYINT NOT NULL "
                    + ");";

    // query statement builder
    private static final String AND = " AND ";
    private static final String SELECTION = " = ? ";
    private static final String DOT = ".";
    private static final String COMMA = ", ";

    private static final int NO_LIMIT = -1;

    /**
     * Database constructor
     */
    public ToDoListDBHelper (Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public ToDoListDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public ToDoListDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    /**
     * Create / Destroy database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TO_DO);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    /**
     * CRUD Operations
     */

    // Insert to do list (null values are not allowed in this insert)
    public long addToDoList(ArrayList<ToDoItem> todoList) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        long result = 0;
        try {
            for (ToDoItem newItem : todoList) {
                ContentValues values = new ContentValues();
                values.put(KEY_TODO_ITEM, newItem.toDo);
                values.put(KEY_DATE_CREATED, newItem.dateCreated);
                values.put(KEY_ARCHIVED, newItem.archived ? 1 : 0);

                result += db.insert(TABLE_NAME_TO_DO, null, values);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return result;
    }

    // Add new entry
    public long addToDoItem(ToDoItem newItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TODO_ITEM, newItem.toDo);
        values.put(KEY_DATE_CREATED, newItem.dateCreated);
        values.put(KEY_ARCHIVED, newItem.archived ? 1 : 0);

        long resultValue = db.insert(TABLE_NAME_TO_DO, // table name
                null,  // nullColumnHack, used when inserting null values
                values); // value to be inserted
        db.close();
        return resultValue;
    }

    // Edit entry
    public int updateToDoItem(ToDoItem updateItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TODO_ITEM, updateItem.toDo);
        values.put(KEY_DATE_CREATED, updateItem.dateCreated);
        values.put(KEY_ARCHIVED, updateItem.archived ? 1 : 0);

        int resultValue = db.update(TABLE_NAME_TO_DO, // table name
                values, // value to be updated
                KEY_ID + " = ?", // where clause
                new String[] { String.valueOf(updateItem.id) }); // where args
        db.close();
        return resultValue;
    }

    // Delete entry
    public int deleteToDoItem(ToDoItem deleteItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        int resultValue = db.delete(TABLE_NAME_TO_DO, // table name
                KEY_ID + " = ?", // where clause
                new String[] { String.valueOf(deleteItem.id) }); // where args
        db.close();
        return resultValue;
    }

    // Retrieve single item
    public ToDoItem getToDoItem(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT  * FROM " + TABLE_NAME_TO_DO + " WHERE _id=" + id;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor == null || cursor.getCount() < 1) {
            cursor.close();
            db.close();
            return null;
        }
        cursor.moveToFirst();

        ToDoItem retrievedItem = new ToDoItem (
                cursor.getLong(COL_ID),
                cursor.getString(COL_TODO_ITEM),
                cursor.getLong(COL_DATE_CREATED),
                cursor.getInt(COL_ARCHIVED));

        cursor.close();
        db.close();
        return retrievedItem;
    }

    public ArrayList<ToDoItem> retrieveAllToDoItems (boolean ascending) {
        ArrayList<ToDoItem> todoList = new ArrayList<ToDoItem>();
        SQLiteDatabase db = this.getReadableDatabase();
        String order = ascending ? " ASC" : " DESC";

        String query = "SELECT  * FROM " + TABLE_NAME_TO_DO +
                " ORDER BY " + KEY_ID + order;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor == null || cursor.getCount() < 1) {
            cursor.close();
            db.close();
            return null;
        }

        if (cursor.moveToFirst()) {
            Log.v("TEST", "Total count: " + cursor.getCount());
            do {
                ToDoItem retrievedItem = new ToDoItem(
                        cursor.getLong(COL_ID),
                        cursor.getString(COL_TODO_ITEM),
                        cursor.getLong(COL_DATE_CREATED),
                        cursor.getInt(COL_ARCHIVED));
                        todoList.add(retrievedItem);

                Log.v("TEST", "Check if edited:\n" + retrievedItem.printToDoString());

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return todoList;
    }

    public int toDoItemCount() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME_TO_DO;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor == null || cursor.getCount() < 1) {
            cursor.close();
            db.close();
            return 0;
        }

        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

}
