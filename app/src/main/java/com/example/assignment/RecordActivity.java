package com.example.assignment;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class RecordActivity extends AppCompatActivity {

    ListView lvRecordList;
    TextView tvTitle;
    boolean easyMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_record);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SharedPreferences settings = getSharedPreferences("assignment_setting", 0);
        easyMode = settings.getBoolean("easyMode", false);


        lvRecordList = findViewById(R.id.RecordList);
        tvTitle = findViewById(R.id.gameTitle);
        tvTitle.setText(easyMode ? getString(R.string.YourRecordsEasy) : getString(R.string.YourRecordsNormal));


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.text_view, readData());
        lvRecordList.setAdapter(adapter);
    }

    public ArrayList<String> readData() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.example.assignment/GameDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);
        ArrayList<String> data = new ArrayList<String>();
        try {

            Cursor c = db.rawQuery("SELECT * FROM GamesLog Where easyMode = " + (easyMode ? 1 : 0), null);
            if (c.moveToFirst()) {
                do {
                    data.add(c.getString(1) + ", " + c.getString(2) + ", " + c.getInt(4) + getString(R.string.corrects) + ", " + c.getString(3) + getString(R.string.sec));
                } while (c.moveToNext());
            }
            c.close();

        } catch (Exception e) {
            Log.d("DB", e.getMessage());
        }
        return data;
    }
}