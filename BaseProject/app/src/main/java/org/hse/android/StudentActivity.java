package org.hse.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudentActivity extends AppCompatActivity {
    private static final String TAG = "MyTAG";
    private TextView time;
    private TextView status;
    private TextView subject;
    private TextView cabinet;
    private TextView corp;
    private TextView teacher;

    static class Group{
        private Integer id;
        private Number name;
        private Number year;
        private String op;

        public Group(Integer id,String op, Number year, Number name){
            this.id=id;
            this.name=name;
            this.year=year;
            this.op=op;
        }

        public Integer getId() {
            return id;
        }
        public void setId(Integer id){
            this.id=id;
        }
        @Override
        public String toString(){
            return op.toString()+"-"+year.toString()+"-"+name.toString();
        }
        public String getName(){
            return op.toString()+"-"+year.toString()+"-"+name.toString();
        }
        public void setName(String op, Number year, Number name){
            this.op=op;
            this.year=year;
            this.name=name;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        final Spinner spinner=findViewById(R.id.groupList);
        List<Group> groups = new ArrayList();
        initGroupList(groups);

        ArrayAdapter<?> adapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,groups);
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

        time = findViewById(R.id.time_student);
        initTime();
        status=findViewById(R.id.status_student);
        subject=findViewById(R.id.subject_student);
        cabinet=findViewById(R.id.cabinet_student);
        corp=findViewById(R.id.corp_student);
        teacher=findViewById(R.id.teacher_student);
        initData();
    }

    private void initData() {
        status.setText("Нет пар");
        subject.setText("Дисциплина");
        cabinet.setText("Кабинет");
        corp.setText("Корпус");
        teacher.setText("Преподаватель");
    }

    private void initTime() {
        Date currentTime=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat sdf=new SimpleDateFormat("EEEE",new Locale("ru","RU"));
        time.setText(simpleDateFormat.format(currentTime)+", "+sdf.format(currentTime));
    }

    private void initGroupList(List<StudentActivity.Group> groups) {
        String[] OP=new String[]{"ПИ","БИ","Э","Ю","И","ИЯ"};
        Number[] year=new Number[]{18,19,20,21};
        Number[] name=new Number[]{1,2};
        for (int o=0;o<OP.length;o++){
            for(int y=0;y<year.length;y++){
                for(int n=0;n<name.length;n++){
                    String oP=OP[o];
                    Number yeaR=year[y];
                    Number namE=name[n];
                    groups.add(new StudentActivity.Group(1,oP,yeaR,namE));
                }
            }
        }
    }
}