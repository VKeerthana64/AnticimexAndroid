package com.surveyor.app.surveyorapp.utils;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.surveyor.app.surveyorapp.R;
import com.surveyor.app.surveyorapp.bean.JobAllSyncImageslistBean;

import java.util.List;

/**
 * Created by cd on 09-08-2017.
 */

public class ImagesPagerAdapter extends PagerAdapter {
    private Context context;
    private List<JobAllSyncImageslistBean> images;

    /**
     * Creates an adapter for viewing product images.
     *
     * @param context activity context.
     * @param images  list of product images.
     */
    public ImagesPagerAdapter(Context context, List<JobAllSyncImageslistBean> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return this.images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TouchImageView imgDisplay;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container, false);
        imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.fullscreen_image);

        Picasso.with(context)
                .load(ImageStorage.getImage(images.get(position).getImagename()))
                .into(imgDisplay);

        container.addView(viewLayout);
        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}