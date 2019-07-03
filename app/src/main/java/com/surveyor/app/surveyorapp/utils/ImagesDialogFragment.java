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

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.surveyor.app.surveyorapp.R;
import com.surveyor.app.surveyorapp.bean.FormImages;
import com.surveyor.app.surveyorapp.bean.JobAllSyncImageslistBean;

import java.util.List;

public class ImagesDialogFragment extends DialogFragment {

    private List<String> images;
    private int defaultPosition = 0;
    JobAllSyncImageslistBean bean;
    TouchImageView imageView;

    public static ImagesDialogFragment newInstance(JobAllSyncImageslistBean bean, int defaultPosition) {
        if (bean == null) return null;
        ImagesDialogFragment frag = new ImagesDialogFragment();
       // frag.images = images;
        frag.bean = bean;
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
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Window window = d.getWindow();
            window.setLayout(width, height);
            Log.e("onStart","called");
//            window.setWindowAnimations(R.style.dialogFragmentAnimation);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //  Timber.d("%s - OnCreateView", this.getClass().getSimpleName());
        View view = inflater.inflate(R.layout.layout_full_screen_image, container, false);

        imageView = (TouchImageView) view.findViewById(R.id.fullscreen_image);

        Picasso.with(getActivity()).load(ImageStorage.getImage(bean.getImagename())).
                placeholder(R.drawable.avatar).error(R.drawable.avatar).into(imageView);

      /*  if (bean.isFromServer()) {
            Picasso.with(getActivity()).load(bean.getImgpath()).placeholder(R.drawable.avatar).error(R.drawable.avatar).into(imageView);
        } else {
            Picasso.with(getActivity()).load(bean.getImgFile()).
                    placeholder(R.drawable.avatar).error(R.drawable.avatar).into(imageView);
        }
*/

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