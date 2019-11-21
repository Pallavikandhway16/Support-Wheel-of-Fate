package com.airasia.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.airasia.android.Adapter.ScheduleAdapter;
import com.airasia.android.Data.ModelItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GenerateScheduleActivity extends AppCompatActivity {
    private Context mContext;
    private ListView mList;
    private SearchView mFilter;
    private TextView mTitle;
    private Toolbar toolbar;
    private List<ModelItem> mEngineerData = new ArrayList<>();
    // all emp
    private List<String> mEmpIdList;
    // all emp id and name
    private HashMap<String, String> mEmpIdHashMap;
    // Non selected emp list
    private List<String> mNonSelectedEmpList = new ArrayList<>();
    private HashMap<String, Integer> mSelectedEmpHm = new HashMap<>();
    private ScheduleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_schedule);
        mContext = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        initUi();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mEngineerData = (List<ModelItem>) bundle.getSerializable("Data");
        }
        if (mEngineerData != null && mEngineerData.size() > 0) {
            mFilter.setVisibility(View.VISIBLE);
            mEmpIdList = new ArrayList<>();
            mEmpIdHashMap = new HashMap<>();
            for (int i = 0; i < mEngineerData.size(); i++) {
                mEmpIdList.add(mEngineerData.get(i).getEmpId());
                mEmpIdHashMap.put(mEngineerData.get(i).getEmpId(),
                        mEngineerData.get(i).getEmpName());
            }
            mNonSelectedEmpList.addAll(mEmpIdList);
            generateSchedule();
        }

        mFilter.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void generateSchedule() {
        mEngineerData.clear();
        // start from monday and skip sat or sunday
        Calendar c = Calendar.getInstance();
        // Set the calendar to monday of the current week
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        List<String> selected = null;
        for (int day = 0; day <= 10; day++) {
            // non selected emp
            mNonSelectedEmpList.clear();
            mNonSelectedEmpList.addAll(mEmpIdList);
            String key;
            String day_of_week = getNextDay(c);
            // remove selected emp for consecutive day
            if (selected != null && selected.size() > 0) {
                for (int i = 0; i < selected.size(); i++) {
                    mNonSelectedEmpList.remove(selected.get(i));
                }
            }
            if (mSelectedEmpHm != null && mSelectedEmpHm.size() > 0 && day < 10) {
                for (Map.Entry<String, Integer> entry : mSelectedEmpHm.entrySet()) {
                    if (entry.getValue() == 2)
                        mNonSelectedEmpList.remove(entry.getKey());
                }
            }
            // select 2 random emp for one day shift
            Random random = new Random();
            // selected emp
            selected = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                if (!mNonSelectedEmpList.isEmpty()) {
                    int randomIndex = random.nextInt(mNonSelectedEmpList.size());
                    key = mNonSelectedEmpList.get(randomIndex);
                    checkShiftCount(key);
                    // add element in temporary list
                    selected.add(key);
                    String shift = i == 0 ? "Day" : "Night";
                    mEngineerData.add(new ModelItem(key, mEmpIdHashMap.get(key),
                            day_of_week, shift));
                    // Remove selected element
                    mNonSelectedEmpList.remove(randomIndex);
                }
            }
        }
        setAdapter();
    }

    private boolean checkShiftCount(String key) {
        int count = 1;
        boolean isCompleted = true;
        if (mSelectedEmpHm != null && mSelectedEmpHm.containsKey(key)) {
            int shiftCount = mSelectedEmpHm.get(key);
            if (shiftCount < 2) {
                mSelectedEmpHm.put(key, count + 1);
            } else {
                isCompleted = false;
            }
        } else {
            mSelectedEmpHm.put(key, count);
        }
        return isCompleted;
    }

    private String getNextDay(Calendar c) {
        // Print dates of the current week starting on Monday to Friday
        DateFormat df = new SimpleDateFormat("EEE dd/MM/yyyy");
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.FRIDAY) {
            c.add(Calendar.DATE, 3);
        } else if (dayOfWeek == Calendar.SATURDAY) {
            c.add(Calendar.DATE, 2);
        } else {
            c.add(Calendar.DATE, 1);
        }
        return df.format(c.getTime());
    }

    private void setAdapter() {
        if (mEngineerData != null && mEngineerData.size() > 0) {
            mAdapter = new ScheduleAdapter(GenerateScheduleActivity.this, mEngineerData);
            mList.setAdapter(mAdapter);
        }
    }

    private void initUi() {
        mTitle = (TextView) findViewById(R.id.toolbar_title);
        mList = (ListView) findViewById(R.id.listView);
        mFilter = (SearchView) findViewById(R.id.searchView1);
        mFilter.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
