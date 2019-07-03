package com.surveyor.app.surveyorapp.jobs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.surveyor.app.surveyorapp.DashboardActivity;
import com.surveyor.app.surveyorapp.R;
import com.surveyor.app.surveyorapp.bean.JobAllSyncBean;
import com.surveyor.app.surveyorapp.bean.JobAllSyncDataBean;
import com.surveyor.app.surveyorapp.bean.JobBean;
import com.surveyor.app.surveyorapp.utils.AppConstants;
import com.surveyor.app.surveyorapp.utils.SharedObjects;

import java.util.ArrayList;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> {

    ArrayList<JobAllSyncDataBean> list;
    Context context;
    OnItemClickListener onItemClickListener;

    public JobsAdapter(ArrayList<JobAllSyncDataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_jobslist, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final JobAllSyncDataBean bean = list.get(position);

        if (!TextUtils.isEmpty(bean.getScheduledate())) {

            holder.txtDate.setText(bean.getScheduledate());
        }

        if (!TextUtils.isEmpty(bean.getDeadlinedate())) {
            holder.txtDeadLine.setText(bean.getDeadlinedate());
        }

        if (!TextUtils.isEmpty(bean.getJobtypename())) {
            holder.txtJobType.setText(bean.getJobtypename());
        }

        if (!TextUtils.isEmpty(bean.getCustomername())) {
            holder.txtClientName.setText(bean.getCustomername());
        }

        if (!TextUtils.isEmpty(bean.getConstituency())) {
            holder.txtConstituency.setText(bean.getConstituency());
        }

        if (!TextUtils.isEmpty(bean.getTowncouncil())) {
            holder.txtZoneNumbers.setText(bean.getTowncouncil());
        }

        if (!TextUtils.isEmpty(bean.getLocation())) {
            holder.txtAddress.setText(bean.getLocation());
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bean.getIscomplete().equalsIgnoreCase("true")) {
                    showDialog(context, "");
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(AppConstants.INTENT_BUNDLE, bean);
                    ((DashboardActivity) context).selectDrawerItem(AppConstants.MenuPosition.SERVICE_REPORTING, bundle);
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
        void onItemClickListener(int position, JobAllSyncDataBean bean);
    }

    public ArrayList<JobAllSyncDataBean> adapterList() {
        return list;
    }

    public JobAllSyncDataBean getItem(int position) {
        return list.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate, txtJobType, txtClientName, txtConstituency, txtZoneNumbers, txtAddress, txtDeadLine;
        CardView card_view;

        public ViewHolder(View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtJobType = itemView.findViewById(R.id.txtJobType);
            txtClientName = itemView.findViewById(R.id.txtClientName);
            txtConstituency = itemView.findViewById(R.id.txtConstituency);
            txtZoneNumbers = itemView.findViewById(R.id.txtZoneNumbers);
            txtDeadLine = itemView.findViewById(R.id.txtDeadLine);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            card_view = itemView.findViewById(R.id.card_view);
        }
    }

    public void showDialog(Context activity, String msg) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_job_completed);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.CENTER);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}



