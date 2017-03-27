package com.nimrag.kevin.aweweico.ui;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nimrag.kevin.aweweico.R;
import com.squareup.picasso.Picasso;

/**
 * Created by sunkevin on 2017/3/27.
 */

public class PhotoViewFragment extends Fragment {

    private String imageUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            imageUrl = args.getString("imageUrl");
        }
    }

    /**
     * 使用这种方式传递参数
     */
    public static PhotoViewFragment newInstance(String imageUrl) {
        PhotoViewFragment f = new PhotoViewFragment();
        Bundle b = new Bundle();
        b.putString("imageUrl", imageUrl);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.phote_view, container, false);
        ImageView image = (ImageView)rootView.findViewById(R.id.photo);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });

        // 加载图片
        Log.d("haha", "PhotoViewFragment imageUrl: " + imageUrl);
        Picasso.with(getActivity()).load(imageUrl).into(image);

        return rootView;
    }
}
