package org.hse.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends BaseActivity implements OnItemClick {

    public static String ARG_ID = "ARG_ID";
    public static String ARG_TYPE = "ARG_TYPE";
    public static String ARG_MODE = "ARG_MODE";
    public static String ARG_EXTRA = "ARG_EXTRA";
    private BaseActivity.ScheduleType type;
    private BaseActivity.ScheduleMode mode;
    private Integer id;
    private String mExtra;
    private Integer DEFAULT_ID = 0;
    private TextView mTitleTextView;

    private RecyclerView recyclerView;
    private ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        initTime();

        type = (BaseActivity.ScheduleType) getIntent().getSerializableExtra(ARG_TYPE);
        mode = (BaseActivity.ScheduleMode) getIntent().getSerializableExtra(ARG_MODE);
        id = getIntent().getIntExtra(ARG_ID, DEFAULT_ID);
        mExtra = getIntent().getStringExtra(ARG_EXTRA);

        mTitleTextView = findViewById(R.id.title);
        recyclerView = findViewById(R.id.listView);
        time = findViewById(R.id.dateTime);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new ItemAdapter(this::onClick);
        recyclerView.setAdapter(adapter);
        initData();
        setExtraInfo();
    }

    private void initData() {
        List<ScheduleItem> list = new ArrayList<>();

        ScheduleItemHeader header = new ScheduleItemHeader();
        header.setTitle("Понедельник, 28 января");
        list.add(header);

        ScheduleItem item = new ScheduleItem();
        item.setStart("10:00");
        item.setEnd("11::00");
        item.setType("Практическое занятие");
        item.setName("Анализ данных");
        item.setPlace("Ауд.503, Кочновский пр-д, д.3");
        item.setTeacher("Пред. Гущим Михаил Иванович");
        list.add(item);

        item = new ScheduleItem();
        item.setStart("12:00");
        item.setEnd("13::00");
        item.setType("Практическое занятие");
        item.setName("Анализ данных (анг)");
        item.setPlace("Ауд.503, Кочновский пр-д, д.3");
        item.setTeacher("Пред. Гущим Михаил Иванович");
        list.add(item);
        adapter.setDataList(list);
    }

    @SuppressLint("SetTextI18n")
    private void setExtraInfo() {
        if (mExtra.isEmpty()) {
            mTitleTextView.setVisibility(View.GONE);
        } else {
            String extra;
            if (mode == BaseActivity.ScheduleMode.TEACHER) {
                extra = "Преподаватель: ";
            } else {
                extra = "Номер группы: ";
            }
            mTitleTextView.setText(extra + mExtra);
        }
    }

    @Override
    public void onClick(ScheduleItem data) {

    }

    public final static class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

        private final static int TYPE_ITEM = 0;
        private final static int TYPE_HEADER = 1;

        private List<ScheduleItem> dataList = new ArrayList<>();
        private OnItemClick onItemClick;

        public void setDataList(List<ScheduleItem> dataList) {
            this.dataList = dataList;
        }

        public ItemAdapter(OnItemClick onItemClick) {
            this.onItemClick = onItemClick;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            if (viewType == TYPE_ITEM) {
                View contactView = inflater.inflate(R.layout.item_shedule, parent, false);
                return new ViewHolder(contactView, context, onItemClick);
            } else if (viewType == TYPE_HEADER) {
                View contactView = inflater.inflate(R.layout.item_schedule_header, parent, false);
                return new ViewHolderHeader(contactView, context, onItemClick);
            }
            throw new IllegalStateException("Invalid view type");
        }

        public int getItemViewType(int position) {
            ScheduleItem data = dataList.get(position);
            if (data instanceof ScheduleItemHeader) {
                return TYPE_HEADER;
            }
            return TYPE_ITEM;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            ScheduleItem data = dataList.get(position);
            if (viewHolder instanceof ViewHolder) {
                ((ViewHolder) viewHolder).bind(data);
            } else if (viewHolder instanceof ViewHolderHeader) {
                ((ViewHolderHeader) viewHolder).bind((ScheduleItemHeader) data);
            }
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            private Context context;
            private OnItemClick onItemClick;
            private TextView start;
            private TextView end;
            private TextView type;
            private TextView name;
            private TextView place;
            private TextView teacher;

            public ViewHolder(View itemView, Context context, OnItemClick onItemClick) {
                super(itemView);
                this.context = context;
                this.onItemClick = onItemClick;
                start = itemView.findViewById(R.id.start);
                end = itemView.findViewById(R.id.end);
                type = itemView.findViewById(R.id.type);
                name = itemView.findViewById(R.id.name);
                place = itemView.findViewById(R.id.place);
                teacher = itemView.findViewById(R.id.teacher);
            }

            public void bind(final ScheduleItem data) {
                start.setText(data.getStart());
                end.setText(data.getEnd());
                type.setText(data.getType());
                name.setText(data.getName());
                place.setText(data.getPlace());
                teacher.setText(data.getTeacher());
            }
        }

        public static class ViewHolderHeader extends ViewHolder {

            private TextView title;

            public ViewHolderHeader(View itemView, Context context, OnItemClick onItemClick) {
                super(itemView, context, onItemClick);
                title = itemView.findViewById(R.id.title);
            }

            @Override
            public void bind(ScheduleItem data) {
                ScheduleItemHeader scheduleItemHeader = (ScheduleItemHeader) data;
                title.setText(scheduleItemHeader.getTitle());
            }
        }
    }
}

class ScheduleItem {
    private String start;
    private String end;
    private String type;
    private String name;
    private String place;
    private String teacher;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}

class ScheduleItemHeader extends ScheduleItem {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

interface OnItemClick {
    void onClick(ScheduleItem data);
}