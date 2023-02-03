package com.example.rexpos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rexpos.R;
import com.example.rexpos.models.Category;
import com.example.rexpos.models.Discount;

import java.util.ArrayList;
import java.util.List;

public class DiscountAdapter extends BaseAdapter {


    private Context context;
    private LayoutInflater inflter;
    private List<Discount> mListDiscount = new ArrayList<>();


    public DiscountAdapter(Context context){
        this.context = context;
        inflter = (LayoutInflater.from(context));


        Discount discount2 = new Discount();
        discount2.discountKey = " ";
        discount2.discountValue = 0;
        mListDiscount.add(discount2);

        Discount discount = new Discount();
        discount.discountKey = "10%";
        discount.discountValue = 10;
        mListDiscount.add(discount);

        Discount discount1 = new Discount();
        discount1.discountKey = "20%";
        discount1.discountValue = 20;
        mListDiscount.add(discount1);

    }


    @Override
    public int getCount() {
        return mListDiscount.size();
    }

    @Override
    public Discount getItem(int position) {
        return mListDiscount.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflter.inflate(R.layout.spinner_discount_item, null);

        Discount mDiscount = mListDiscount.get(position);
        TextView mTextDiscountItemInSpinner = convertView.findViewById(R.id.txtDiscountKey);
        mTextDiscountItemInSpinner.setText(mDiscount.discountKey);

        return convertView;
    }



}
