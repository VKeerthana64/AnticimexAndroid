package com.surveyor.app.surveyorapp.survey_form;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.surveyor.app.surveyorapp.R;
import com.surveyor.app.surveyorapp.bean.OptionBean;

import java.util.ArrayList;

public class OptionsSelectedFindingsAdapter extends RecyclerView.Adapter<OptionsSelectedFindingsAdapter.ViewHolder> {
    ArrayList<String> list;
    Context context;
    OnValueSelectedListener onValueSelectedListener;
    public OptionsSelectedFindingsAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_tag_for_display, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final String bean = list.get(position);

        if (!TextUtils.isEmpty(bean)){
            holder.txtTag.setText(bean);
        }

    }


    public String selectedItem() {
       /* String selected = "";
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelected()) {
                selected += list.get(i).getOptionname()+",";
            }
        }
        if (!TextUtils.isEmpty(selected)){
            selected = selected.substring(0,selected.length()-1) ;
        }
        return selected;*/
        return "";
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public ArrayList<String> adapterList() {
        return list;
    }

    public String getItem(int position) {
        return list.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTag;
        public ViewHolder(View itemView) {
            super(itemView);
            txtTag = itemView.findViewById(R.id.txtTag);
        }
    }

}



