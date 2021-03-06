package org.hse.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    private Spinner spinner;

    static class Group {
        private Integer id;
        private Number name;
        private Number year;
        private String op;

        public Group(Integer id, String op, Number year, Number name) {
            this.id = id;
            this.name = name;
            this.year = year;
            this.op = op;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return op.toString() + "-" + year.toString() + "-" + name.toString();
        }

        public String getName() {
            return op.toString() + "-" + year.toString() + "-" + name.toString();
        }

        public void setName(String op, Number year, Number name) {
            this.op = op;
            this.year = year;
            this.name = name;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        spinner = findViewById(R.id.groupList);
        List<Group> groups = new ArrayList();
        initGroupList(groups);

        ArrayAdapter<?> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition, long selectedId) {
                Object item = adapter.getItem(selectedItemPosition);
                Log.d(TAG, "selectedItem: " + item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //
            }
        });

        time = findViewById(R.id.time_student);
        initTime();
        status = findViewById(R.id.status_student);
        subject = findViewById(R.id.subject_student);
        cabinet = findViewById(R.id.cabinet_student);
        corp = findViewById(R.id.corp_student);
        teacher = findViewById(R.id.teacher_student);
        initData();
        View scheduleDay = findViewById(R.id.dailySchedule);
        scheduleDay.setOnClickListener(v -> showShedule(BaseActivity.ScheduleType.DAY));
        View scheduleWeek = findViewById(R.id.weeklySchedule);
        scheduleWeek.setOnClickListener(v -> showShedule(BaseActivity.ScheduleType.WEEK));
    }

    private void initData() {
        status.setText("?????? ??????");
        subject.setText("????????????????????");
        cabinet.setText("??????????????");
        corp.setText("????????????");
        teacher.setText("??????????????????????????");
    }

    private void initTime() {
        Date currentTime = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", new Locale("ru", "RU"));
        time.setText(simpleDateFormat.format(currentTime) + ", " + sdf.format(currentTime));
    }

    private void initGroupList(List<StudentActivity.Group> groups) {
        String[] OP = new String[]{"????", "????", "??", "??", "??", "????"};
        Number[] year = new Number[]{18, 19, 20, 21};
        Number[] name = new Number[]{1, 2};
        for (int o = 0; o < OP.length; o++) {
            for (int y = 0; y < year.length; y++) {
                for (int n = 0; n < name.length; n++) {
                    String oP = OP[o];
                    Number yeaR = year[y];
                    Number namE = name[n];
                    groups.add(new StudentActivity.Group(1, oP, yeaR, namE));
                }
            }
        }
    }

    private void showShedule(BaseActivity.ScheduleType type) {
        Object selectedItem = spinner.getSelectedItem();
        if (!(selectedItem instanceof Group)) {
            return;
        }
        showScheduleImpl(BaseActivity.ScheduleMode.STUDENT, type, (Group) selectedItem);
    }

    private void showScheduleImpl(BaseActivity.ScheduleMode mode, BaseActivity.ScheduleType type, Group group) {
        Intent intent = new Intent(this, ScheduleActivity.class);
        intent.putExtra(ScheduleActivity.ARG_ID, group.getId());
        intent.putExtra(ScheduleActivity.ARG_TYPE, type);
        intent.putExtra(ScheduleActivity.ARG_MODE, mode);
        intent.putExtra(ScheduleActivity.ARG_EXTRA, group.getName());
        startActivity(intent);
    }
}