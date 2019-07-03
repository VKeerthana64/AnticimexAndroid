package com.surveyor.app.surveyorapp.jobs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.surveyor.app.surveyorapp.DashboardActivity;
import com.surveyor.app.surveyorapp.R;
import com.surveyor.app.surveyorapp.bean.JobAllSyncAllSurveyBean;
import com.surveyor.app.surveyorapp.bean.JobAllSyncDataBean;
import com.surveyor.app.surveyorapp.bean.JobBean;
import com.surveyor.app.surveyorapp.bean.ServiceReportBean;
import com.surveyor.app.surveyorapp.utils.AppConstants;
import com.surveyor.app.surveyorapp.utils.SharedObjects;

import java.util.ArrayList;

public class ServiceReportAdapter extends RecyclerView.Adapter<ServiceReportAdapter.ViewHolder> {

    ArrayList<JobAllSyncAllSurveyBean> list;
    Context context;
    OnItemClickListener onItemClickListener;
    OnItemDeleteListener onItemDeleteListener;
    JobAllSyncDataBean beandata;

    public ServiceReportAdapter(ArrayList<JobAllSyncAllSurveyBean> list, Context context, JobAllSyncDataBean beandata) {
        this.list = list;
        this.context = context;
        this.beandata = beandata;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemDeleteListener(OnItemDeleteListener onItemDeleteListener) {
        this.onItemDeleteListener = onItemDeleteListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_service_report, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final JobAllSyncAllSurveyBean bean = list.get(position);

        if (!TextUtils.isEmpty(bean.getDate_of_finding())) {
            holder.txtServiceDate.setText(bean.getDate_of_finding());
        }


        if (beandata != null) {
            holder.txtClientName.setText(beandata.getCustomername());
        }

        if (!TextUtils.isEmpty(bean.getGeoaddress())) {
            holder.txtServiceAddress.setText(bean.getGeoaddress());
        }

        holder.llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(list.get(position).getIsdraft()) && list.get(position).getIsdraft().equalsIgnoreCase("true")) {

                    if (onItemDeleteListener != null) {
                        AppConstants.showConfirmDialog(context.getString(R.string.msg_service_report_delete), context,
                                context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        onItemDeleteListener.onItemDeleteListener(position, list.get(position));
                                    }
                                }, context.getString(R.string.no), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });

                    }
                } else {
                    AppConstants.showAlertDialog(context, context.getString(R.string.delete_form),
                            context.getString(R.string.cant_delete_uploaded_or_completed_form));
                }


            }
        });

        if (!TextUtils.isEmpty(bean.getIscomplete()) && bean.getIscomplete().equalsIgnoreCase("true")) {
            holder.card_view.setCardBackgroundColor(ContextCompat.getColor(context, R.color.green));
        } else if (!TextUtils.isEmpty(bean.getIsdraft()) && bean.getIsdraft().equalsIgnoreCase("true")) {
            holder.card_view.setCardBackgroundColor(ContextCompat.getColor(context, R.color.orange));
        } else {
            holder.card_view.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(position, list.get(position));
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
        void onItemClickListener(int position, JobAllSyncAllSurveyBean bean);
    }

    public interface OnItemDeleteListener {
        void onItemDeleteListener(int position, JobAllSyncAllSurveyBean bean);
    }

    public ArrayList<JobAllSyncAllSurveyBean> adapterList() {
        return list;
    }

    public JobAllSyncAllSurveyBean getItem(int position) {
        return list.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtClientName, txtServiceDate, txtServiceAddress;
        LinearLayout llDelete;
        CardView card_view;

        public ViewHolder(View itemView) {
            super(itemView);
            card_view = itemView.findViewById(R.id.card_view);
            txtClientName = itemView.findViewById(R.id.txtClientName);
            txtServiceDate = itemView.findViewById(R.id.txtServiceDate);
            txtServiceAddress = itemView.findViewById(R.id.txtServiceAddress);
            llDelete = itemView.findViewById(R.id.llDelete);
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



