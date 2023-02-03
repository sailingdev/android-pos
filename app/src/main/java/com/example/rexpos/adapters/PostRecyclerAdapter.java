package com.example.rexpos.adapters;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rexpos.DemoActivity;
import com.example.rexpos.KitchenSetStatusActivity;
import com.example.rexpos.R;
import com.example.rexpos.common.GlobalConst;
import com.example.rexpos.models.KitchenTableTransaction;
import com.example.rexpos.models.PostItem;
import com.example.rexpos.models.Table;
import com.example.rexpos.models.Transaction;
import com.example.rexpos.utils.DateTimeUtils;

import java.io.Serializable;
import java.util.List;


public class PostRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    private List<KitchenTableTransaction> mPostItems;
    private Context context;

    public PostRecyclerAdapter(Context context, List<KitchenTableTransaction> postItems) {
        this.context = context;
        this.mPostItems = postItems;
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false));
            case VIEW_TYPE_LOADING:
                return new FooterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == mPostItems.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return mPostItems == null ? 0 : mPostItems.size();
    }

    public void add(KitchenTableTransaction response) {
        mPostItems.add(response);
        notifyItemInserted(mPostItems.size() - 1);
    }

    public void addAll(List<KitchenTableTransaction> postItems) {
        for (KitchenTableTransaction response : postItems) {
            add(response);
        }
    }


    private void remove(KitchenTableTransaction postItems) {
        int position = mPostItems.indexOf(postItems);
        if (position > -1) {
            mPostItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addLoading() {
        isLoaderVisible = true;
        add(new KitchenTableTransaction());
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = mPostItems.size() - 1;
        KitchenTableTransaction item = getItem(position);
        if (item != null) {
            mPostItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }


    KitchenTableTransaction getItem(int position) {
        return mPostItems.get(position);
    }


    public class ViewHolder extends BaseViewHolder {

        //TextView textViewTitle;
        LinearLayout itemHeader;
        private Spinner spinner;
        private TextView mTextTableName;
        private TextView mTextdateTransaction;

        public LinearLayout mLinearContainerView;


        ViewHolder(View itemView) {
            super(itemView);
            ViewGroup.LayoutParams params = itemView.getLayoutParams();
            params.width = (int)(GlobalConst.WITH_SCREEN / 4);
            itemView.setLayoutParams(params);
            itemHeader = itemView.findViewById(R.id.item_header);
            mTextTableName = itemView.findViewById(R.id.txt_table_name);
            mTextdateTransaction = itemView.findViewById(R.id.date_transaction);
            mLinearContainerView = itemView.findViewById(R.id.kitchen_table_item_container);
        }


        protected void clear() {}






        public void onBind(final int position) {
            super.onBind(position);
            final KitchenTableTransaction item = mPostItems.get(position);
            //textViewTitle.setText(item.getTitle());


            String tableName = item.table_name;
            String date_transaction = item.date_transaction;
            long tableId = item.table_id;

            mTextTableName.setText(tableName);

            mTextdateTransaction.setText(DateTimeUtils.convertFormat(DateTimeUtils.FMT_FULL, DateTimeUtils.FMT_MONTH_DAY, date_transaction));
            addProductList(item.transactions);

            if (item.transactions.get(0).status.equals("BILLED")){
                itemHeader.setBackgroundColor(context.getResources().getColor(R.color.complete));
            } else {
                itemHeader.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            }

//            itemHeader.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    showCustomDialog();
//                }
//            });


            mLinearContainerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Activity mActivity = (DemoActivity)context;
                    Intent intent = new Intent(mActivity, KitchenSetStatusActivity.class);
                    intent.putExtra("tableName", tableName);
                    intent.putExtra("tableId", tableId);
                    intent.putExtra("date_transaction", date_transaction);

                    mPostItems.clear();
                    notifyDataSetChanged();

                    mActivity.startActivity(intent);
                }
            });
        }




        private void addProductList(List<Transaction> mListTransactions){

            if (mLinearContainerView.getChildCount() > 0)
                mLinearContainerView.removeAllViews();

            for (Transaction item: mListTransactions) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View converterview = layoutInflater.inflate(R.layout.kitchen_table_item_row, null);

                TextView mTextProductName = converterview.findViewById(R.id.kitchen_product_name);

                mTextProductName.setText(item.product_name);

                if (item.status.equals("Open"))
                    mTextProductName.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                if (item.status.equals("Served"))
                    mTextProductName.setTextColor(context.getResources().getColor(R.color.blue));
                if (item.status.equals("Cancel"))
                    mTextProductName.setTextColor(context.getResources().getColor(R.color.red));
                if (item.status.equals("BILLED"))
                    mTextProductName.setTextColor(context.getResources().getColor(R.color.complete));

                mLinearContainerView.addView(converterview);
            }
        }
    }






    private void showCustomDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = ((DemoActivity)context).findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(context).inflate(R.layout.pop_up_xml, viewGroup, false);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

    }







public class FooterHolder extends BaseViewHolder {

        ProgressBar mProgressBar;

        FooterHolder(View itemView) {
            super(itemView);
            mProgressBar = itemView.findViewById(R.id.progressBar);
        }

        @Override
        protected void clear() {

        }

    }

}