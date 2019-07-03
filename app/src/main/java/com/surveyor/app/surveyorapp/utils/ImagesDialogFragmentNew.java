package com.surveyor.app.surveyorapp.utils;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.surveyor.app.surveyorapp.R;
import com.surveyor.app.surveyorapp.bean.JobAllSyncImageslistBean;

import java.util.List;

/**
 * Created by cd on 09-08-2017.
 */

public class ImagesDialogFragmentNew extends DialogFragment {

    private List<JobAllSyncImageslistBean> images;
    private int defaultPosition = 0;

    private ViewPager imagesPager;

    public static ImagesDialogFragmentNew newInstance(List<JobAllSyncImageslistBean> images, int defaultPosition) {
        if (images == null || images.isEmpty()) return null;
        ImagesDialogFragmentNew frag = new ImagesDialogFragmentNew();
        frag.images = images;
        frag.defaultPosition = defaultPosition;
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialogFullscreen);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("onStart","onStart");
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Window window = d.getWindow();
            window.setLayout(width, height);

            window.setWindowAnimations(R.style.dialogFragmentAnimationNew);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //  Timber.d("%s - OnCreateView", this.getClass().getSimpleName());
        View view = inflater.inflate(R.layout.dialog_image_viewer, container, false);

        imagesPager = (ViewPager) view.findViewById(R.id.dialog_product_detail_images_pager);
        // Prepare endless image adapter
        PagerAdapter mPagerAdapter = new ImagesPagerAdapter(getActivity(), images);
        imagesPager.setAdapter(mPagerAdapter);

        if (defaultPosition > 0 && defaultPosition < images.size()) {
            imagesPager.setCurrentItem(defaultPosition);
        } else {
            imagesPager.setCurrentItem(0);
        }

        final ImageView goLeft = (ImageView) view.findViewById(R.id.dialog_product_detail_images_left);
        goLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = imagesPager.getCurrentItem();
                imagesPager.setCurrentItem(position - 1, true);
            }
        });

        final ImageView goRight = (ImageView) view.findViewById(R.id.dialog_product_detail_images_right);
        goRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = imagesPager.getCurrentItem();
                imagesPager.setCurrentItem(position + 1, true);
            }
        });

        imagesPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position == 0){
                    goLeft.setVisibility(View.INVISIBLE);
                    goRight.setVisibility(View.VISIBLE);
                }else if(position == images.size()-1){
                    goLeft.setVisibility(View.VISIBLE);
                    goRight.setVisibility(View.INVISIBLE);
                }else{
                    goLeft.setVisibility(View.VISIBLE);
                    goRight.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Button cancel = (Button) view.findViewById(R.id.pager_close);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }
}
