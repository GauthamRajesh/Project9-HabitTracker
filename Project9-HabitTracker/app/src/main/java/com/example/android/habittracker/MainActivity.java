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
                displayDatabaseInfo(readDatabase());
            }
        });
        mDbHelper = new HabitDbHelper(this);
        displayDatabaseInfo(readDatabase());
    }
    private Cursor readDatabase() {
        HabitDbHelper mDbHelper = new HabitDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                HabitContract.HabitEntry._ID,
                HabitContract.HabitEntry.COLUMN_HABIT_NAME,
                HabitContract.HabitEntry.COLUMN_DATE };
        return db.query(
                HabitContract.HabitEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);
    }
    private void displayDatabaseInfo(Cursor cursor) {
        TextView displayView = (TextView) findViewById(R.id.database_activity);
        try {
            displayView.setText("You have entered " + cursor.getCount() + " habits.\n\n");
            displayView.append(HabitContract.HabitEntry._ID + " - " +
                    HabitContract.HabitEntry.COLUMN_HABIT_NAME +
                    " - " + HabitContract.HabitEntry.COLUMN_DATE + " \n");
            int idColumnIndex = cursor.getColumnIndex(HabitContract.HabitEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_HABIT_NAME);
            int dateColumnIndex = cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_DATE);
            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentHabitName = cursor.getString(nameColumnIndex);
                String currentDate = cursor.getString(dateColumnIndex);
                displayView.append(("\n" + currentID + " - " +
                        currentHabitName + " - " + currentDate));
            }
        } finally {
            cursor.close();
        }
    }
    private void insertActivity() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HabitContract.HabitEntry.COLUMN_HABIT_NAME, "Coding");
        values.put(HabitContract.HabitEntry.COLUMN_DATE, "01-12-2121");
        long newRowId = db.insert(HabitContract.HabitEntry.TABLE_NAME, null, values);
    }
}
