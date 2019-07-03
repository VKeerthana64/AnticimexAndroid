package com.surveyor.app.surveyorapp.jobs_team_leader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.surveyor.app.surveyorapp.R;
import com.surveyor.app.surveyorapp.bean.JobTeamLeaderBean;
import com.surveyor.app.surveyorapp.bean.TeamMemberBean;
import com.surveyor.app.surveyorapp.bean.TeamMemberDataBean;
import com.surveyor.app.surveyorapp.retrofit.ApiService;

import java.util.ArrayList;

public class TeamMemberAdapter extends RecyclerView.Adapter<TeamMemberAdapter.ViewHolder> implements Filterable {

    ArrayList<TeamMemberDataBean> list;
    ArrayList<TeamMemberDataBean> listFiltered;
    Context context;
    OnItemClickListener onItemClickListener;

    public TeamMemberAdapter(ArrayList<TeamMemberDataBean> list, Context context) {
        this.list = list;
        this.listFiltered = list;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_team_member, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final TeamMemberDataBean bean = listFiltered.get(position);

        String fName = "", lName = "";
        if (!TextUtils.isEmpty(bean.getFirstname())) {
            fName = bean.getFirstname();
        }
        if (!TextUtils.isEmpty(bean.getLastname())) {
            lName = bean.getLastname();
        }
        holder.txtName.setText(fName + " " + lName);

        if (!TextUtils.isEmpty(bean.getProfileimage())) {
            String imageUrl = ApiService.BASEURL_PROFILE + bean.getProfileimage();
            Picasso.with(context).load(imageUrl).into(holder.imgUser);
        }

        if (bean.isAssigned()) {
            holder.rlTick.setVisibility(View.VISIBLE);
        } else {
            holder.rlTick.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(position, listFiltered.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listFiltered.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listFiltered = list;
                } else {
                    ArrayList<TeamMemberDataBean> filteredList = new ArrayList<>();
                    for (TeamMemberDataBean row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getFirstname().toLowerCase().contains(charString.toLowerCase())
                                || row.getLastname().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    listFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listFiltered = (ArrayList<TeamMemberDataBean>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position, TeamMemberDataBean bean);
    }

    public ArrayList<TeamMemberDataBean> adapterList() {
        return listFiltered;
    }

    public TeamMemberDataBean getItem(int position) {
        return listFiltered.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        ImageView imgUser;
        RelativeLayout rlTick;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            imgUser = itemView.findViewById(R.id.imgUser);
            rlTick = itemView.findViewById(R.id.rlTick);
        }
    }
}



