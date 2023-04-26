package com.example.project.Helper;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import java.util.ArrayList;

public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.ViewHolder>{
    Context context;
    ArrayList<Log> logs;

    public LogsAdapter(Context context, ArrayList<Log> logs) {
        this.context = context;
        this.logs = logs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recycle_view_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log log = logs.get(position);
        holder.name.setText(log.getItem().getName());
        holder.qty.setText(""+log.getItem().getQty());
        holder.sp.setText(""+log.getItem().getSelling_price());
        holder.date.setText(log.getDate());
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView qty;
        TextView sp;
        TextView date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.invoiceRecyclerViewItemName);
            qty = itemView.findViewById(R.id.invoiceRecyclerViewQty);
            sp = itemView.findViewById(R.id.invoiceRecyclerViewPrice);
            date = itemView.findViewById(R.id.invoiceRecyclerViewRate);
            TextView dateLabel = itemView.findViewById(R.id.rateLabel);
            dateLabel.setText("");
            date.setTextColor(Color.BLACK);

        }
    }
}
