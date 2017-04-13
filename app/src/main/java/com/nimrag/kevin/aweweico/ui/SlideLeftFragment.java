package com.nimrag.kevin.aweweico.ui;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nimrag.kevin.aweweico.R;

import java.util.List;

/**
 * Created by sunkevin on 2017/4/13.
 * 侧边栏
 */

public class SlideLeftFragment extends Fragment{

    private ListView groupListView;
    private GroupListAdapter<String> groupListAdapter;
    private ImageView userProfileImage;
    private String[] groupTexts;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.slide_fragment_layout, container, false);
        groupListView = (ListView)v.findViewById(R.id.index_left_listview);
        // 获取桌面背景作为slide fragment的背景
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(getActivity());
        final Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        groupListView.setBackground(wallpaperDrawable);
        groupListAdapter = new GroupListAdapter<String>(getActivity(), R.layout.slide_group_list_item, groupTexts);
        groupListView.setAdapter(groupListAdapter);
        groupListView.setSelection(0);

        userProfileImage = (ImageView)v.findViewById(R.id.current_avatar);
        groupTexts = getResources().getStringArray(R.array.fixed_group_names);
        return v;
    }

    private class GroupListAdapter<T> extends ArrayAdapter {
        GroupListAdapter(Context context, int resource, T[] objects){
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String groupText = groupTexts[position];
            if(convertView == null){
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.slide_group_list_item, parent);
                convertView.setBackgroundResource(R.drawable.list_item_selector);
            }
            TextView groupTextView = (TextView)convertView.findViewById(R.id.item_text);
            groupTextView.setText(groupText);

            return convertView;
        }

        @Override
        public int getCount() {
            return groupTexts.length;
        }


    }
}
