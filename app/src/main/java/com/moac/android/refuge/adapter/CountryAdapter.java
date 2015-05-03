package com.moac.android.refuge.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class CountryAdapter extends BaseAdapter {

    private final Context context;
    private List<CountryViewModel> items;
    private final ViewModelBinder binder;

    public CountryAdapter(Context context, List<CountryViewModel> items, ViewModelBinder binder) {
        this.context = context;
        this.items = items;
        this.binder = binder;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public CountryViewModel getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getCountryId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return binder.getView(context, convertView, parent, items.get(position));
    }

    public interface ViewModelBinder {
        View getView(Context context, View convertView, ViewGroup parent, CountryViewModel item);
    }
}