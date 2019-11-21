package com.airasia.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airasia.android.Adapter.EngineerAdapter;
import com.airasia.android.Data.EngineersList;
import com.airasia.android.Data.ModelItem;
import com.airasia.android.Retrofit.RetrofitClient;
import com.airasia.android.Utils.NetworkUtil;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EngineersActivity extends AppCompatActivity {
    private Context mContext;
    private TextView mGenerateSchedule;
    private ListView mEngList;
    private List<ModelItem> mEngineerData = new ArrayList<>();
    private EngineerAdapter mAdapter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engineers);
        mContext = getApplicationContext();
        initUi();
        mProgressDialog = ProgressDialog.show(EngineersActivity.this, "",
                "Fetching data! Please wait...", true);

        // fetch data
        fetchEngineerList();
        mGenerateSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EngineersActivity.this, GenerateScheduleActivity.class);
                intent.putExtra("Data", (Serializable) mEngineerData);
                startActivity(intent);
            }
        });
    }

    private void fetchEngineerList() {
        if (NetworkUtil.checkNetworkConnection(mContext)) {
            Call<EngineersList> call = new RetrofitClient()
                    .getInstance()
                    .getApi()
                    .getEngineers();
            call.enqueue(new Callback<EngineersList>() {
                @Override
                public void onResponse(Call<EngineersList> call, Response<EngineersList> response) {
                    mEngineerData.clear();
                    if (mProgressDialog != null)
                        mProgressDialog.cancel();
                    EngineersList engineersList = response.body();
                    if (engineersList != null) {
                        List<LinkedHashMap<String, String>> engineers = engineersList.getEngineers();
                        if (engineers != null && engineers.size() > 0) {
                            for (int i = 0; i < engineers.size(); i++) {
                                LinkedHashMap<String, String> hashMap = engineers.get(i);
                                if (hashMap != null && hashMap.containsKey("name") && hashMap.containsKey("id")) {
                                    String empID = hashMap.get("id");
                                    String empName = hashMap.get("name");
                                    mEngineerData.add(new ModelItem(empID, empName));
                                }
                            }
                        }
                        setAdapter();
                    }

                }

                @Override
                public void onFailure(Call<EngineersList> call, Throwable t) {
                    Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.no_net_conn),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void setAdapter() {
        if (mEngineerData != null && mEngineerData.size() > 0) {
            mAdapter = new EngineerAdapter(EngineersActivity.this, mEngineerData);
            mEngList.setAdapter(mAdapter);
            mGenerateSchedule.setVisibility(View.VISIBLE);
        }

    }

    private void initUi() {
        mGenerateSchedule = (TextView) findViewById(R.id.generate);
        mEngList = (ListView) findViewById(R.id.listView);
        mGenerateSchedule.setVisibility(View.GONE);
    }
}
