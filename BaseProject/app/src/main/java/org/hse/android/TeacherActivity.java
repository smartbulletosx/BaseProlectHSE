package org.hse.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TeacherActivity extends AppCompatActivity {
    private static final String TAG = "MyTAG";
    private TextView time;
    private TextView status;
    private TextView subject;
    private TextView cabinet;
    private TextView corp;
    private TextView teacher;

    static class Teacher{
        private Integer id;
        private String name;

        public Teacher(Integer id,String name){
            this.id=id;
            this.name=name;
        }
        public Integer getId() {
            return id;
        }
        public void setId(Integer id){
            this.id=id;
        }
        @Override
        public String toString(){ return name;}
        public String getName(){
            return name;
        }
        public void setName(String name){this.name=name;}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        final Spinner spinner=findViewById(R.id.teacherList);
        List<TeacherActivity.Teacher> teachers = new ArrayList();
        initGroupList(teachers);

        ArrayAdapter<?> adapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,teachers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition, long selectedId){
                Object item=adapter.getItem(selectedItemPosition);
                Log.d(TAG, "selectedItem: "+item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //
            }
        });

        time = findViewById(R.id.time_teacher);
        initTime();
        status=findViewById(R.id.status_teacher);
        subject=findViewById(R.id.subject_teacher);
        cabinet=findViewById(R.id.cabinet_teacher);
        corp=findViewById(R.id.corp_teacher);
        teacher=findViewById(R.id.teacher_teacher);
        initData();
    }

    private void initGroupList(List<TeacherActivity.Teacher> teachers) {
        teachers.add(new TeacherActivity.Teacher(1,"Иванов Иван Иванович"));
        teachers.add(new TeacherActivity.Teacher(2,"Петров Петр Петрович"));
    }
    private void initTime(){
        Date currentTime=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat sdf=new SimpleDateFormat("EEEE",new Locale("ru","RU"));
        time.setText(simpleDateFormat.format(currentTime)+", "+sdf.format(currentTime));
    }
    private void initData(){
        status.setText("Нет пар");
        subject.setText("Дисциплина");
        cabinet.setText("Кабинет");
        corp.setText("Корпус");
        teacher.setText("Преподаватель");
    }
}

