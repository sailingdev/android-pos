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

public class TransactionListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    private List<Transaction> mListTransaction = new ArrayList<>();

    private OnTransactionItemLongClickListener onTransactionItemLongClickListener;

    public TransactionListAdapter(Context context, OnTransactionItemLongClickListener onTransactionItemLongClickListener){
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.onTransactionItemLongClickListener = onTransactionItemLongClickListener;
    }


    public interface OnTransactionItemLongClickListener{
        void longClicked(int pos);
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
        return getItem(position).transaction_id;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.transaction_item, null);
        }

        Transaction transaction = getItem(position);

        TextView mTextProductName = convertView.findViewById(R.id.product_name);
        TextView mTextProductStatus = convertView.findViewById(R.id.product_status);
        TextView mTextUnitPrice = convertView.findViewById(R.id.product_unit_price);

        mTextProductName.setText(transaction.product_name);

        mTextProductStatus.setText(transaction.status);

        if (transaction.status.equals("Served")){
            mTextProductStatus.setTextColor(context.getResources().getColor(R.color.blue));
        } else if (transaction.status.equals("Cancel")){
            mTextProductStatus.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            mTextProductStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }



        mTextUnitPrice.setText(String.valueOf(transaction.unit_price));

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onTransactionItemLongClickListener.longClicked(position);
                return false;
            }
        });

        return convertView;
    }


    public void addTransactionItem(Transaction transaction){
        this.mListTransaction.add(transaction);
        notifyDataSetInvalidated();
    }

    public void addAllTransaction(List<Transaction> mListTransaction){
        this.mListTransaction.addAll(mListTransaction);
        notifyDataSetChanged();
    }

    public void removeAll(){
        mListTransaction.clear();
        notifyDataSetChanged();
    }


    public void removeTransacitionItem(Transaction transaction){
        mListTransaction.remove(transaction);
        notifyDataSetChanged();
    }

    public void removeItem(int pos){
        mListTransaction.remove(pos);
        notifyDataSetChanged();
    }

    public List<Transaction> getAllTransactions(){
        return mListTransaction;
    }

}
