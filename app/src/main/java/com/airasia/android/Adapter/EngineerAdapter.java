package com.airasia.android.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.airasia.android.Data.ModelItem;
import com.airasia.android.R;

import java.util.List;

public class EngineerAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    private Context mContext;
    private List<ModelItem> data;
    private ModelItem mItemList;

    public EngineerAdapter(Context context, List<ModelItem> engineerData) {
        mContext = context;
        data = engineerData;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        if (data.size() <= 0)
            return 0;
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_engineers, null);
            holder.mEmpId = (TextView) view.findViewById(R.id.emp_id);
            holder.mEmpName = (TextView) view.findViewById(R.id.emp_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (data.size() > 0) {
            mItemList = null;
            mItemList = data.get(position);
            String empId = mItemList.getEmpId();
            String empName = mItemList.getEmpName();
            holder.mEmpId.setText("Emp ID : " + empId);
            holder.mEmpName.setText(empName);
        }
        return view;
    }

    class ViewHolder {
        public TextView mEmpId;
        public TextView mEmpName;
    }
}

