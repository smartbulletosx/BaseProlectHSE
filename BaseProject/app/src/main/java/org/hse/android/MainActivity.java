package org.hse.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View studentsSchedule = (Button)findViewById(R.id.studentsSchedule);
        View teacherSchedule = (Button)findViewById(R.id.teacherSchedule);
        View settings=(Button)findViewById(R.id.settings);

        studentsSchedule.setOnClickListener(this);
        teacherSchedule.setOnClickListener(this);
        settings.setOnClickListener(this);
    }

    private void showStudent(){
        Intent intent=new Intent(this,StudentActivity.class);
        startActivity(intent);
    }
    private void showTeacher(){
        Intent intent=new Intent(this,TeacherActivity.class);
        startActivity(intent);
    }
    private void showSettings(){
        Intent intent=new Intent(this,SettingsActivity.class);
        startActivity(intent);
    }
    public void DisplayToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.studentsSchedule:
                DisplayToast("Открытие расписания для студентов");
                Intent intent1 = new Intent(this, StudentActivity.class);
                startActivity(intent1);
                break;
            case R.id.teacherSchedule:
                DisplayToast("Открытие расписания для преподавателя");
                Intent intent2 = new Intent(this, TeacherActivity.class);
                startActivity(intent2);
                break;
            case R.id.settings:
                DisplayToast("Настройки");
                Intent intent3 = new Intent(this, SettingsActivity.class);
                startActivity(intent3);
            default:
                break;
    }
}
}