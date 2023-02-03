package com.example.rexpos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rexpos.R;
import com.example.rexpos.models.Table;

import java.util.ArrayList;
import java.util.List;

public class TableSpinnerAdater extends BaseAdapter {


    private Context context;
    private LayoutInflater inflter;
    private List<Table> mListTable = new ArrayList<>();


    public TableSpinnerAdater(Context applicationContext) {
        this.context = applicationContext;
        inflter = (LayoutInflater.from(applicationContext));
    }


    public interface OnTableItemClickListener{
        void onTableItemClicked(int pos);
    }


    @Override
    public int getCount() {
        return mListTable.size();
    }

    @Override
    public Table getItem(int position) {
        return mListTable.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).table_id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflter.inflate(R.layout.spinner_table_item, null);

        Table mTable = mListTable.get(position);
        TextView mTextTableItemInSpinner = convertView.findViewById(R.id.txtTableName);
        mTextTableItemInSpinner.setText(mTable.table_name);


        return convertView;
    }

    public void addAllTable(List<Table> mListTable){
        this.mListTable = mListTable;
        notifyDataSetChanged();
    }
}
