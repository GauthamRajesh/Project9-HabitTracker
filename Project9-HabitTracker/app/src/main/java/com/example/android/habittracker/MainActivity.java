package com.example.android.habittracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.habittracker.data.HabitContract;
import com.example.android.habittracker.data.HabitDbHelper;

public class MainActivity extends AppCompatActivity {
    private HabitDbHelper mDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button dataButton = (Button) findViewById(R.id.dummy);
        dataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertActivity();
            }
        });
        mDbHelper = new HabitDbHelper(this);
        displayDatabaseInfo();
    }
    private void displayDatabaseInfo() {
        HabitDbHelper mDbHelper = new HabitDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                HabitContract.HabitEntry._ID,
                HabitContract.HabitEntry.COLUMN_HABIT_NAME };
        Cursor cursor = db.query(
                HabitContract.HabitEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        TextView displayView = (TextView) findViewById(R.id.database_activity);
        try {
            displayView.setText("You have entered " + cursor.getCount() + " habits.\n\n");
            displayView.append(HabitContract.HabitEntry._ID + " - " +
                    HabitContract.HabitEntry.COLUMN_HABIT_NAME + " \n");
            int idColumnIndex = cursor.getColumnIndex(HabitContract.HabitEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_HABIT_NAME);
            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentHabitName = cursor.getString(nameColumnIndex);
                displayView.append(("\n" + currentID + " - " +
                        currentHabitName));
            }
        } finally {
            cursor.close();
        }
    }
    private void insertActivity() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HabitContract.HabitEntry.COLUMN_HABIT_NAME, "Coding");
        long newRowId = db.insert(HabitContract.HabitEntry.TABLE_NAME, null, values);
    }
}
