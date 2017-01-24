package com.pushbots.notificationshistory.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import com.pushbots.notificationshistory.model.PushBotsModel;
import com.pushbots.notificationshistory.utils.Constants;

import java.util.ArrayList;

import static com.pushbots.notificationshistory.storage.DatabaseContract.DataEntry.COLUMN_DATE;

/**
 * Created by Muhammad on 9/27/2016.
 */

public class Database extends SQLiteOpenHelper {
    private ArrayList<PushBotsModel> pushBotsModels;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PushBots.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DatabaseContract.DataEntry.TABLE_NAME + " (" +
                    DatabaseContract.DataEntry._ID + " INTEGER PRIMARY KEY" +
                    COMMA_SEP +
                    DatabaseContract.DataEntry.COLUMN_MESSAGE + TEXT_TYPE +
                    COMMA_SEP +
                    COLUMN_DATE + TEXT_TYPE +
                    COMMA_SEP +
                    DatabaseContract.DataEntry.COLUMN_NOTIFICATION_ID + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseContract.DataEntry.TABLE_NAME;


    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        pushBotsModels = new ArrayList<>();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.e("database", SQL_CREATE_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);

    }

    Cursor cursor;

    private int getCursorSize() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String sortOrder =
                    DatabaseContract.DataEntry._ID + " DESC";

            cursor = db.query(
                    DatabaseContract.DataEntry.TABLE_NAME,                     // The table to query
                    null,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );
            cursor.moveToFirst();

            return cursor.getCount();
        } catch (Exception e) {
            e.printStackTrace();
            return cursor.getCount();
        }
    }

    public ArrayList<PushBotsModel> readNotificationData() {
        try {
            pushBotsModels.clear();
            SQLiteDatabase db = this.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
            String[] projection = {
                    DatabaseContract.DataEntry._ID,
                    DatabaseContract.DataEntry.COLUMN_MESSAGE,
                    DatabaseContract.DataEntry.COLUMN_DATE,
                    DatabaseContract.DataEntry.COLUMN_NOTIFICATION_ID
            };

// Filter results WHERE "title" = 'My Title'
            String selection = DatabaseContract.DataEntry._ID + " = ?";
            String[] selectionArgs = {"My Title"};

// How you want the results sorted in the resulting Cursor
            String sortOrder =
                    DatabaseContract.DataEntry._ID + " DESC";

            Cursor c = db.query(
                    DatabaseContract.DataEntry.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );
            c.moveToFirst();

            String[] str = new String[getCursorSize()];
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    addDataForRead(c);
                    c.moveToNext();
                }
            }
            c.close();
            db.close();
            return pushBotsModels;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertNotificationData(Bundle bundle) {

        try {
            Log.e("BundleDB", bundle.toString());
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.DataEntry.COLUMN_NOTIFICATION_ID, bundle.getString(Constants.NOTIFICATION_ID, ""));
            values.put(DatabaseContract.DataEntry.COLUMN_MESSAGE, bundle.getString(Constants.MESSAGE, ""));
            values.put(COLUMN_DATE, bundle.getString(Constants.SENT_TIME, ""));
// Insert the new row, returning the primary key value of the new row
            db.insert(DatabaseContract.DataEntry.TABLE_NAME, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteNotifications() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Define 'where' part of query.
        String selection = (DatabaseContract.DataEntry.COLUMN_MESSAGE + " LIKE ?");
// Specify arguments in placeholder order.
        String[] selectionArgs = {"MyTitle"};
// Issue SQL statement.
        db.delete(DatabaseContract.DataEntry.TABLE_NAME, null, null);

    }

    private void addDataForRead(Cursor c) {
        PushBotsModel notificationsDatabaseModel = new PushBotsModel();
        notificationsDatabaseModel.setDate(c.getString(c.getColumnIndexOrThrow(COLUMN_DATE)));
        notificationsDatabaseModel.setMessage(c.getString(c.getColumnIndexOrThrow(DatabaseContract.DataEntry.COLUMN_MESSAGE)));

        notificationsDatabaseModel.setNotificationID(c.getString(c.getColumnIndexOrThrow(DatabaseContract.DataEntry.COLUMN_NOTIFICATION_ID)));
        pushBotsModels.add(notificationsDatabaseModel);
    }
}
