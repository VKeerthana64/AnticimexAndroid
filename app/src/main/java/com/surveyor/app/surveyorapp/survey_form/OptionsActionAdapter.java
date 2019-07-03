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

public class OptionsActionAdapter extends RecyclerView.Adapter<OptionsActionAdapter.ViewHolder> {

    ArrayList<OptionBean.ActionTaken> list;
    Context context;
    OnItemClickListener onItemClickListener;
    OnValueSelectedListener onValueSelectedListener;

    public OptionsActionAdapter(ArrayList<OptionBean.ActionTaken> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnValueSelectedListener(OnValueSelectedListener onValueSelectedListener) {
        this.onValueSelectedListener = onValueSelectedListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_tag, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final OptionBean.ActionTaken bean = list.get(position);

        if (!TextUtils.isEmpty(bean.getOptionname())){
            holder.txtTag.setText(bean.getOptionname());
        }

        holder.cbSelection.setChecked(bean.isSelected());
        holder.cbSelection.setTag(list.get(position));

        if (bean.isSelected()) {
            holder.cbSelection.setChecked(true);
        } else {
            holder.cbSelection.setChecked(false);
        }

        holder.cbSelection.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                OptionBean.ActionTaken bean = (OptionBean.ActionTaken) cb.getTag();

                bean.setSelected(cb.isChecked());
                list.get(position).setSelected(cb.isChecked());
                onValueSelectedListener.onValueSelectedListener(selectedItem());
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

    public int selectedCount() {
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelected()) {
                count++;
            }
        }
        return count;
    }

    public String selectedItem() {
        String selected = "";
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelected()) {
                selected += list.get(i).getOptionname()+"\n";
            }
        }
        if (!TextUtils.isEmpty(selected)){
            selected = selected.substring(0,selected.length()-1) ;
        }

        return selected;
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
        void onItemClickListener(int position, OptionBean.ActionTaken bean);
    }

    public ArrayList<OptionBean.ActionTaken> adapterList() {
        return list;
    }

    public OptionBean.ActionTaken getItem(int position) {
        return list.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTag;
        CheckBox cbSelection;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTag = itemView.findViewById(R.id.txtTag);
            cbSelection = itemView.findViewById(R.id.cbSelection);
        }
    }

}



