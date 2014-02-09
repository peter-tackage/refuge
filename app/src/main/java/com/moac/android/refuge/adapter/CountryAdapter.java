package com.moac.android.refuge.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class CountryAdapter extends ArrayAdapter<CountryAdapter.ViewModel> {

    public CountryAdapter(Context _context) {
        super(_context, 0);
    }

    public interface ViewModel {
        View getView(Context context, View convertView, ViewGroup parent);
        long getId();
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int _position, View _convertView, ViewGroup _parent) {
        return getItem(_position).getView(getContext(), _convertView, _parent);
    }
}