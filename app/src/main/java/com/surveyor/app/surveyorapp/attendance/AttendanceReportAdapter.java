package com.surveyor.app.surveyorapp.attendance;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.surveyor.app.surveyorapp.R;
import com.surveyor.app.surveyorapp.bean.AttendanceReportBean;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AttendanceReportAdapter extends RecyclerView.Adapter<AttendanceReportAdapter.ViewHolder> {

    ArrayList<AttendanceReportBean.Data> list;
    Context context;
    OnItemClickListener onItemClickListener;


    public AttendanceReportAdapter(ArrayList<AttendanceReportBean.Data> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_attendance_report, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final AttendanceReportBean.Data bean = list.get(position);
        holder.txtDate.setText(bean.getDate());
        holder.txtTimeIn.setText(bean.getTimein());
        holder.txtTimeOut.setText(bean.getTimeout());

        if (!TextUtils.isEmpty(bean.getAddress())){
            holder.txtAddress.setText(bean.getAddress());
        }

        Picasso.with(context).load(bean.getImagefile()).into(holder.imgUser);

        holder.imgPlusMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.llLocation.getVisibility() == View.VISIBLE){
                    holder.llLocation.setVisibility(View.GONE);
                    holder.imgPlusMinus.setBackground(ContextCompat.getDrawable(context,R.drawable.ic_plus));
                }else{
                    holder.llLocation.setVisibility(View.VISIBLE);
                    holder.imgPlusMinus.setBackground(ContextCompat.getDrawable(context,R.drawable.ic_minus));
                }
            }
        });

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
        void onItemClickListener(int position, AttendanceReportBean.Data bean);
    }

    public ArrayList<AttendanceReportBean.Data> adapterList() {
        return list;
    }

    public AttendanceReportBean.Data getItem(int position) {
        return list.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate,txtTimeIn,txtTimeOut,txtAddress ;
        CircleImageView imgUser;
        ImageView imgPlusMinus;
        LinearLayout llLocation ;

        public ViewHolder(View itemView) {
            super(itemView);
            imgUser = (CircleImageView) itemView.findViewById(R.id.imgUser);
            imgPlusMinus = (ImageView) itemView.findViewById(R.id.imgPlusMinus);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtTimeIn = itemView.findViewById(R.id.txtTimeIn);
            txtTimeOut = itemView.findViewById(R.id.txtTimeOut);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            llLocation = itemView.findViewById(R.id.llLocation);
        }
    }
}



