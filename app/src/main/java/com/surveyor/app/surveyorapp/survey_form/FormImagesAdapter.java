package com.surveyor.app.surveyorapp.survey_form;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.surveyor.app.surveyorapp.DashboardActivity;
import com.surveyor.app.surveyorapp.R;
import com.surveyor.app.surveyorapp.bean.JobAllSyncImageslistBean;
import com.surveyor.app.surveyorapp.utils.ImageStorage;
import com.surveyor.app.surveyorapp.utils.ImagesDialogFragmentNew;

import java.util.ArrayList;

public class FormImagesAdapter extends RecyclerView.Adapter<FormImagesAdapter.ViewHolder> {

    ArrayList<JobAllSyncImageslistBean> list;
    Context context;
    OnItemClickListener onItemClickListener;

    public FormImagesAdapter(ArrayList<JobAllSyncImageslistBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_images, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final JobAllSyncImageslistBean bean = list.get(position);

        Picasso.with(context).load(ImageStorage.getImage(bean.getImagename())).
                placeholder(R.drawable.avatar).error(R.drawable.avatar).into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagesDialogFragmentNew imagesDialogFragmentNew = ImagesDialogFragmentNew.newInstance(list, position);
                if (imagesDialogFragmentNew != null) {
                    imagesDialogFragmentNew.show(((DashboardActivity) context).getSupportFragmentManager(), ImagesDialogFragmentNew.class.getSimpleName());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position, JobAllSyncImageslistBean bean);
    }

    public ArrayList<JobAllSyncImageslistBean> adapterList() {
        return list;
    }

    public JobAllSyncImageslistBean getItem(int position) {
        return list.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }

}



