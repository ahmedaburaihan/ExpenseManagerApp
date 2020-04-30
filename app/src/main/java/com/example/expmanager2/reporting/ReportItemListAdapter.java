package com.example.expmanager2.reporting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expmanager2.R;

import java.util.List;

public class ReportItemListAdapter extends RecyclerView.Adapter<ReportItemListAdapter.ViewHolder> {
    private List<ReportDataItems> itemList;
    private Context context;
    private ItemClickListener clickListener;

    //VIEW HOLDER
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemDate, itemName, itemDesc, itemAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemDate = itemView.findViewById(R.id.card_date);
            itemName = itemView.findViewById(R.id.card_item);
            itemDesc = itemView.findViewById(R.id.card_desc);
            itemAmount = itemView.findViewById(R.id.card_amount);

        }
    }

    public ReportItemListAdapter(List<ReportDataItems> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReportItemListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repor_card, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ReportItemListAdapter.ViewHolder holder, final int position) {
        holder.itemDate.setText(itemList.get(position).getItemDate());
        holder.itemName.setText(itemList.get(position).getItemName());
        holder.itemDesc.setText(itemList.get(position).getItemDesc());
        //holder.itemAmount.setText(itemList.get(position).getItemAmount());





    }
    public void setClickListener(ItemClickListener itemClickListener){
        this.clickListener = itemClickListener;
    }


    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public interface  ItemClickListener {
        public void itemClick(View view, int position);
    }

}

