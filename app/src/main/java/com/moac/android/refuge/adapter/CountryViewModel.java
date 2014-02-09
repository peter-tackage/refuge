package com.moac.android.refuge.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moac.android.refuge.R;
import com.moac.android.refuge.model.Country;

public class CountryViewModel implements CountryAdapter.ViewModel {

    private final long mTotalIntake;
    private final Country mCountry;
    private final int mColor;

    public CountryViewModel(Country country, int color, long totalIntake) {
        mCountry = country;
        mTotalIntake = totalIntake;
        mColor = color;
    }

    @Override
    public View getView(Context context, View convertView, ViewGroup parent) {
        View view = convertView;
        TextView countryNameTextView;
        TextView totalIntakeTextView;
        View colorIndicatorView;

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.country_info_row, parent, false);
            countryNameTextView = (TextView) view.findViewById(R.id.country_name_textView);
            totalIntakeTextView = (TextView) view.findViewById(R.id.total_intake_textView);
            colorIndicatorView = (View) view.findViewById(R.id.country_item_check_indicator);
            view.setTag(R.id.country_name_textView, countryNameTextView);
            view.setTag(R.id.total_intake_textView, totalIntakeTextView);
            view.setTag(R.id.country_item_check_indicator, colorIndicatorView);
        } else {
            countryNameTextView = (TextView) view.getTag(R.id.country_name_textView);
            totalIntakeTextView = (TextView) view.getTag(R.id.total_intake_textView);
            colorIndicatorView = (View) view.getTag(R.id.country_item_check_indicator);
        }

        countryNameTextView.setText(mCountry.getName());
        totalIntakeTextView.setText(String.valueOf(mTotalIntake));
        colorIndicatorView.setBackgroundColor(mColor);

        return view;
    }

    @Override
    public long getId() {
       return mCountry.getId();
    }
}
