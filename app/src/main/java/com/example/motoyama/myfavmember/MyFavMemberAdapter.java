package com.example.motoyama.myfavmember;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by motoyama on 2017/11/10.
 */

public class MyFavMemberAdapter extends RealmBaseAdapter<MyFavMember> {

    private static class ViewHolder{
        TextView date;
        TextView title;
    }
    public MyFavMemberAdapter(@Nullable OrderedRealmCollection<MyFavMember> data) {
        super(data);
    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    android.R.layout.simple_list_item_2,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.date =
                    (TextView) convertView.findViewById(android.R.id.text1);
            viewHolder.title =
                    (TextView) convertView.findViewById(android.R.id.text2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        MyFavMember myfavmember = adapterData.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy//MM/dd");
        String formatDate = sdf.format(myfavmember.getDate());
        viewHolder.date.setText(formatDate);
        viewHolder.title.setText(myfavmember.getTitle());
        return convertView;
    }
}
