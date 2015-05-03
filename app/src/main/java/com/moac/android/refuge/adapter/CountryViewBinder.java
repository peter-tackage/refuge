package com.moac.android.refuge.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moac.android.refuge.R;

public class CountryViewBinder implements CountryAdapter.ViewModelBinder {

    private final int resourceId;

    public CountryViewBinder(int resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public View getView(Context context, View convertView, ViewGroup parent, final CountryViewModel item) {
        View view = convertView;
        TextView countryNameTextView;
        TextView totalIntakeTextView;
        final View colorIndicatorView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(resourceId, parent, false);
            countryNameTextView = (TextView) view.findViewById(R.id.country_name_textView);
            totalIntakeTextView = (TextView) view.findViewById(R.id.total_intake_textView);
            colorIndicatorView = view.findViewById(R.id.country_item_check_indicator);
            view.setTag(R.id.country_name_textView, countryNameTextView);
            view.setTag(R.id.total_intake_textView, totalIntakeTextView);
            view.setTag(R.id.country_item_check_indicator, colorIndicatorView);
        } else {
            countryNameTextView = (TextView) view.getTag(R.id.country_name_textView);
            totalIntakeTextView = (TextView) view.getTag(R.id.total_intake_textView);
            colorIndicatorView = (View) view.getTag(R.id.country_item_check_indicator);
        }

        countryNameTextView.setText(item.getCountryName());
        totalIntakeTextView.setText(String.valueOf(item.getTotalIntake()));
        colorIndicatorView.setBackgroundColor(item.getColor());
        return view;
    }
}
