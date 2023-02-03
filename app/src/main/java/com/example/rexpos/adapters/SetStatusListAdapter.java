package com.example.rexpos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rexpos.R;
import com.example.rexpos.models.Transaction;

import java.util.ArrayList;
import java.util.List;

public class SetStatusListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    private List<Transaction> mListTransaction = new ArrayList<>();

    private OnItemClickListener onItemClickListener;


    public SetStatusListAdapter(Context context, OnItemClickListener onItemClickListener){
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onBtnServedClicked(int pos);
        void onBtnCancelClicked(int pos);
        void onBtnPrintClicked(int pos);
    }



    @Override
    public int getCount() {
        return mListTransaction.size();
    }

    @Override
    public Transaction getItem(int position) {
        return mListTransaction.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).product_id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.kitchen_table_transaction_item_row, null);
        }

        Transaction kitchenTableTransaction = getItem(position);

        TextView mTextViewProductName = convertView.findViewById(R.id.kitchen_transaction_product_name);
        mTextViewProductName.setText(kitchenTableTransaction.product_name);


        TextView btnServed = convertView.findViewById(R.id.btn_served);
        TextView btnCancel = convertView.findViewById(R.id.btn_cancel);
        TextView btnPrint = convertView.findViewById(R.id.btn_print);


        if (kitchenTableTransaction.status.equals("BILLED")){
            btnServed.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            btnPrint.setVisibility(View.GONE);
        } else {
            btnCancel.setVisibility(View.VISIBLE);
            btnServed.setVisibility(View.VISIBLE);
            btnPrint.setVisibility(View.VISIBLE);
        }



        if (kitchenTableTransaction.status.equals("Served") || kitchenTableTransaction.status.equals("BILLED")) {
            btnServed.setBackground(context.getResources().getDrawable(R.drawable.back_blue_round));
            btnPrint.setVisibility(View.VISIBLE);
        }else{
            btnServed.setBackground(context.getResources().getDrawable(R.drawable.back_primary_round));
            btnPrint.setVisibility(View.GONE);
        }


        if (kitchenTableTransaction.status.equals("Cancel")) {
            btnCancel.setBackground(context.getResources().getDrawable(R.drawable.back_red_round));
        }else{
            btnCancel.setBackground(context.getResources().getDrawable(R.drawable.back_primary_round));
        }


        btnServed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onBtnServedClicked(position);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onBtnCancelClicked(position);
            }
        });

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onBtnPrintClicked(position);
            }
        });



        return convertView;
    }


    public void addAllList(List<Transaction> mListTransaction){
        this.mListTransaction = mListTransaction;
        notifyDataSetChanged();
    }

    public List<Transaction> getAllData(){
        return this.mListTransaction;
    }

    public void updateItem(int pos, Transaction transaction){
        this.mListTransaction.get(pos).status = transaction.status;
        notifyDataSetChanged();
    }
}
