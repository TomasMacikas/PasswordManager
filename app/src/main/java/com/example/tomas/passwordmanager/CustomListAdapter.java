package com.example.tomas.passwordmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {
    private ArrayList<NewItem> listData;
    private LayoutInflater layoutInflater;

    public CustomListAdapter(Context aContext, ArrayList<NewItem> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row_layout, null);
            holder = new ViewHolder();
            holder.siteView = (TextView) convertView.findViewById(R.id.site);
            holder.usernameView = (TextView) convertView.findViewById(R.id.username);
            holder.passwordView = (TextView) convertView.findViewById(R.id.password);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.siteView.setText(listData.get(position).getSite());
        holder.usernameView.setText(listData.get(position).getUsername());
        holder.passwordView.setText(listData.get(position).getPassword());
        return convertView;
    }

    static class ViewHolder {
        TextView siteView;
        TextView usernameView;
        TextView passwordView;
    }
}