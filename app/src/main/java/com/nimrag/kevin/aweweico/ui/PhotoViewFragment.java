package com.nimrag.kevin.aweweico.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nimrag.kevin.aweweico.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by sunkevin on 2017/3/27.
 */

public class PhotoViewFragment extends Fragment {

    private ImageView image;
    private String imageUrl;
    private int screenWidth;
    private int screenHeight;

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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.phote_view, container, false);
        image = (ImageView)rootView.findViewById(R.id.photo);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
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
        Picasso.with(getActivity()).load(imageUrl).into(new ImageTarget());

        return rootView;
    }

    private class ImageTarget implements Target {
        public ImageTarget() {
            super();
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            // 根据宽高比来放大图片
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();

            // 以图下的宽度为基准放大图片
            ViewGroup.LayoutParams layoutParams = image.getLayoutParams();
            layoutParams.width = screenWidth;
            layoutParams.height = bitmapHeight * Math.round((float) screenWidth / bitmapWidth);
            image.setLayoutParams(layoutParams);
            image.setImageBitmap(bitmap);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }
    }
}
