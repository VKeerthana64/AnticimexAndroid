package com.surveyor.app.surveyorapp.jobs_team_leader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.surveyor.app.surveyorapp.R;
import com.surveyor.app.surveyorapp.bean.JobAllSyncDataBean;

import java.util.ArrayList;

public class JobTeamLeaderAdapter extends RecyclerView.Adapter<JobTeamLeaderAdapter.ViewHolder> {

    ArrayList<JobAllSyncDataBean> list;
    Context context;
    OnItemClickListener onItemClickListener;
    OnStatusClickListener onStatusClickListener;

    public JobTeamLeaderAdapter(ArrayList<JobAllSyncDataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnStatusClickListener(OnStatusClickListener onStatusClickListener) {
        this.onStatusClickListener = onStatusClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_job_team_leader, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final JobAllSyncDataBean bean = list.get(position);

        if (!TextUtils.isEmpty(bean.getCustomername())) {
            holder.txtClientName.setText(bean.getCustomername());
        }

        if (!TextUtils.isEmpty(bean.getJobtypename())) {
            holder.txtName.setText(bean.getJobtypename());
        }
        if (!TextUtils.isEmpty(bean.getLocation())) {
            holder.txtAddress.setText(bean.getLocation());
        }

        if (bean.getSurveyteammember() != null && bean.getSurveyteammember().size() > 0) {
            String names = "";
            for (int i = 0; i < bean.getSurveyteammember().size(); i++) {
                if (bean.getSurveyteammember().get(i).isAssigned()){
                    names += bean.getSurveyteammember().get(i).getUsername() + ",";
                }
            }
            if (!TextUtils.isEmpty(names)){
                holder.txtAssignTo.setText(names.substring(0, names.length() - 1));
                holder.txtStatus.setText(context.getString(R.string.reassign));
            }else{
                holder.txtStatus.setText(context.getString(R.string.assign));
                holder.txtAssignTo.setText("");
            }
        } else {
            holder.txtStatus.setText(context.getString(R.string.assign));
            holder.txtAssignTo.setText("");
        }

        if (!TextUtils.isEmpty(bean.getScheduledate())) {
            holder.txtServiceDate.setText(bean.getScheduledate());

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(position, list.get(position));
                }
            }
        });

        holder.txtStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onStatusClickListener != null) {
                    onStatusClickListener.onStatusClickListener(position, list.get(position));
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

    public interface OnStatusClickListener {
        void onStatusClickListener(int position, JobAllSyncDataBean bean);
    }

    public ArrayList<JobAllSyncDataBean> adapterList() {
        return list;
    }

    public JobAllSyncDataBean getItem(int position) {
        return list.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtClientName, txtServiceDate, txtStatus, txtAssignTo, txtAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtName = itemView.findViewById(R.id.txtName);
            txtClientName = itemView.findViewById(R.id.txtClientName);
            txtServiceDate = itemView.findViewById(R.id.txtServiceDate);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtAssignTo = itemView.findViewById(R.id.txtAssignTo);
        }
    }
}



