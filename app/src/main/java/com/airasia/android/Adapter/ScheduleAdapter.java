package com.airasia.android.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.airasia.android.Data.ModelItem;
import com.airasia.android.R;

import java.util.ArrayList;
import java.util.List;

public class ScheduleAdapter extends BaseAdapter implements Filterable {
    private static LayoutInflater inflater = null;
    private Context mContext;
    private List<ModelItem> data;
    private List<ModelItem> filterData;
    private CustomFilter filter;
    private ModelItem mItemList;

    public ScheduleAdapter(Context context, List<ModelItem> engineerData) {
        mContext = context;
        data = engineerData;
        filterData = engineerData;
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
        return data.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_engineers_schedules, null);
            holder.mEmpId = (TextView) view.findViewById(R.id.emp_id);
            holder.mEmpName = (TextView) view.findViewById(R.id.emp_name);
            holder.mDay = (TextView) view.findViewById(R.id.day);
            holder.mShift = (TextView) view.findViewById(R.id.shift);
            holder.mWeek = (TextView) view.findViewById(R.id.week);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (data.size() > 0) {
            mItemList = null;
            mItemList = data.get(position);
            String empId = mItemList.getEmpId();
            String empName = mItemList.getEmpName();
            holder.mEmpId.setText(empId);
            holder.mEmpName.setText(empName);
            holder.mDay.setText(mItemList.getDay().split(" ")[0]);
            holder.mShift.setText(mItemList.getShift());
            holder.mWeek.setText(mItemList.getDay().split(" ")[1]);
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilter();
        }

        return filter;
    }

    class ViewHolder {
        public TextView mEmpId;
        public TextView mEmpName;
        public TextView mDay;
        public TextView mShift;
        public TextView mWeek;
    }


    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {

                ArrayList<ModelItem> filters = new ArrayList<>();

                //get specific items
                for (int i = 0; i < filterData.size(); i++) {
                    if (filterData.get(i).getEmpId().contains(constraint)) {
                        ModelItem p = new ModelItem(filterData.get(i).getEmpId(),
                                filterData.get(i).getEmpName(),
                                filterData.get(i).getDay(), filterData.get(i).getShift()
                        );
                        filters.add(p);
                    }
                }

                results.count = filters.size();
                results.values = filters;

            } else {
                results.count = filterData.size();
                results.values = filterData;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data = (ArrayList<ModelItem>) results.values;
            notifyDataSetChanged();

        }
    }
}

