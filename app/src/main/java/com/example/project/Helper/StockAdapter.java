package com.example.project.Helper;


import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Dashboard;
import com.example.project.R;

import java.util.ArrayList;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder> {

    Context context;
    PopupMenu popupMenu;
    Dialog dialog;
    EditText dialogQty, dialogSP, dialogCP, dialogName;
    Button dialogBtn;
    ArrayList<Item> items;
    public StockAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = new ArrayList<>(items);
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dilogue_box);
        dialogName = dialog.findViewById(R.id.dialougeName);
        dialogQty = dialog.findViewById(R.id.dialougeQty);
        dialogCP = dialog.findViewById(R.id.dialougeCP);
        dialogSP = dialog.findViewById(R.id.dialougeSP);
        dialogBtn = dialog.findViewById(R.id.dialogBtn);
        TextView title = dialog.findViewById(R.id.dialougeTitle);
        title.setText("Update Item");
        dialogBtn.setText("Update");
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recycle_view_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item  = items.get(position);
        holder.name.setText(item.getName());
        holder.qty.setText(""+item.getQty());
        holder.cp.setText(""+item.getCost_price());
        holder.sp.setText(""+item.getSelling_price());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, PopupMenu.OnMenuItemClickListener {
        TextView name;
        TextView qty;
        TextView sp;
        TextView cp;
        CardView card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.invoiceRecyclerViewItemName);
            qty = itemView.findViewById(R.id.invoiceRecyclerViewQty);
            sp = itemView.findViewById(R.id.invoiceRecyclerViewPrice);
            cp = itemView.findViewById(R.id.invoiceRecyclerViewRate);
            TextView cpLabel = itemView.findViewById(R.id.rateLabel);
            cpLabel.setText("Cost Price :");

            TextView spLabel = itemView.findViewById(R.id.priceLabel);
            spLabel.setText("Selling Price :");

            card = itemView.findViewById(R.id.CardInvoice);
            card.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.pop_up_menu);
            popupMenu.setOnMenuItemClickListener(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                popupMenu.setForceShowIcon(true);
            }
            popupMenu.show();
            return true;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            int itemId = menuItem.getItemId();
            int pos = getAdapterPosition();
            if (itemId == R.id.edit) {
                Item item = items.get(pos);
                dialogName.setText(item.getName());
                dialogQty.setText(""+item.getQty());
                dialogSP.setText(""+item.getSelling_price());
                dialogCP.setText(""+item.getCost_price());
                dialogBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dialogName.getText().toString().equals("")) {
                            dialogName.setError("Enter Name");
                        } else if (dialogQty.getText().toString().equals("")) {
                            dialogQty.setError("Enter Quantity");
                        } else if (dialogCP.getText().toString().equals("")) {
                            dialogCP.setError("Enter Cost Price");
                        } else if(dialogSP.getText().toString().equals("")){
                            dialogSP.setError("Enter Selling Price");
                        } else{

                            Item i = new Item(
                                    item.getId(),
                                    dialogName.getText().toString().trim(),
                                    Integer.parseInt(dialogQty.getText().toString().trim()),
                                    Long.parseLong(dialogSP.getText().toString().trim()),
                                    Long.parseLong(dialogCP.getText().toString().trim())
                            );

                            Dashboard.userData.updateItem(i);
                            items.set(pos, i);
                            notifyItemChanged(pos);
                            dialogName.setText("");
                            dialogQty.setText("");
                            dialogSP.setText("");
                            dialogCP.setText("");
                            dialog.dismiss();

                        }
                    }
                });
                dialogName.requestFocus();
                dialog.show();
            } else if (itemId == R.id.delete) {
                Dashboard.userData.deleteItem(items.get(pos));
                items.remove(pos);
                notifyItemRemoved(pos);
            }
            return itemId == R.id.edit || itemId == R.id.delete;
        }
    }
    public int ItemInserted(Item item){
        items.add(item);
        int x = items.size()-1;
        notifyItemInserted(x);
        return x;
    }
}