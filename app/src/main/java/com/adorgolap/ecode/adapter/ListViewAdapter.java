package com.adorgolap.ecode.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.adorgolap.ecode.R;
import com.adorgolap.ecode.helper.ECodeData;

import java.util.ArrayList;

/**
 * Created by ifta on 11/3/16.
 */

public class ListViewAdapter extends ArrayAdapter<ECodeData> {
    private static class ViewHolder {
        TextView tvCode;
        TextView tvName;
    }
    public ListViewAdapter(Context context,  ArrayList<ECodeData> data) {
        super(context, 0,data);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ECodeData listItem = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_list_item, parent, false);
            viewHolder.tvCode = (TextView) convertView.findViewById(R.id.tvCodeListItem);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvNameListItem);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvCode.setText(listItem.code);
        viewHolder.tvName.setText(listItem.name);
        return convertView;

    }
}
