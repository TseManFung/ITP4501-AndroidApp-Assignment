package com.example.assignment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingActivity extends AppCompatActivity {

    Switch SwEasyMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SwEasyMode = findViewById(R.id.easyModeSwitch);
        SharedPreferences settings = getSharedPreferences("assignment_setting", 0);
        SwEasyMode.setChecked(settings.getBoolean("easyMode", false));
        if(SwEasyMode.isChecked()){
            SwEasyMode.setText(R.string.on);

        }else{
            SwEasyMode.setText(R.string.off);

        }
    }

    public void clickEasyMode(View v){
        if(SwEasyMode.isChecked()){
            SwEasyMode.setText(R.string.on);

        }else{
            SwEasyMode.setText(R.string.off);

        }
        SharedPreferences settings = getSharedPreferences("assignment_setting", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("easyMode", SwEasyMode.isChecked());
        editor.commit();
    }
}