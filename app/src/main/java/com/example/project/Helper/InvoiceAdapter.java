package com.example.project.Helper;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Dashboard;
import com.example.project.R;

import java.util.ArrayList;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder>{
    Context context;
    ArrayList<Item> items;
    PopupMenu popupMenu;
    Dialog dialog;
    EditText dialogQty, dialogSP;
    Button dialogBtn;
    View viewFragment;

    public InvoiceAdapter(Context context, ArrayList<Item> items,View view) {
        this.context = context;
        this.items = items;
        this.viewFragment = view;
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dilogue_box);
        TextView dialogName = dialog.findViewById(R.id.dialougeName);
        dialogQty = dialog.findViewById(R.id.dialougeQty);
        EditText dialogRate = dialog.findViewById(R.id.dialougeCP);
        dialogSP = dialog.findViewById(R.id.dialougeSP);
        dialogBtn = dialog.findViewById(R.id.dialogBtn);
        TextView title = dialog.findViewById(R.id.dialougeTitle);
        title.setText("Update Item");
        dialogName.setVisibility(View.GONE);
        dialogRate.setVisibility(View.GONE);
        dialogBtn.setText("Update");
        dialogSP.setHint("Enter Rate");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recycle_view_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        long total = 0;
        if(!items.isEmpty()){
            holder.name.setText(items.get(position).getName());
            holder.qty.setText(Long.toString(items.get(position).getQty()));
            holder.rate.setText(Long.toString(items.get(position).getSelling_price()));
            long i = Long.parseLong(holder.rate.getText().toString()) * Integer.parseInt(holder.qty.getText().toString());
            holder.price.setText(Long.toString(i));
            for (Item item: items) {
                total+=(item.getSelling_price()*item.getQty());
            }
        }
        holder.total.setText("Total: "+total+" Rs");

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public Item getItem(int pos){
        return items.get(pos);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, PopupMenu.OnMenuItemClickListener {
        TextView name;
        TextView qty;
        TextView total;
        TextView price;
        TextView rate;
        CardView card;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.invoiceRecyclerViewItemName);
            qty = itemView.findViewById(R.id.invoiceRecyclerViewQty);
            price = itemView.findViewById(R.id.invoiceRecyclerViewPrice);
            rate = itemView.findViewById(R.id.invoiceRecyclerViewRate);
            card = itemView.findViewById(R.id.CardInvoice);
            total = viewFragment.findViewById(R.id.total);
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
                dialogSP.setText(Long.toString(getItem(pos).getSelling_price()));
                dialogBtn.setOnClickListener(view -> {
                    if(dialogSP.getText().toString().equals("")){
                        dialogSP.setError("Enter Rate");
                    } else if (dialogQty.getText().toString().equals("")) {
                        dialogQty.setError("Enter Quantity");
                    } else if (Integer.parseInt(dialogQty.getText().toString())
                            > Dashboard.userData.getItem(getItem(pos).getName()).getQty()) {
                        dialogQty.setError("Stock limit exceeded");
                    } else{

                        long sp = Long.parseLong(dialogSP.getText().toString());
                        long cp = getItem(pos).getCost_price();
                        if(sp < cp){
                            Drawable warningIcon = ContextCompat.getDrawable(context, R.drawable.ic_warning);
                            Toast warningToast = Toast.makeText(context, R.string.warning, Toast.LENGTH_LONG);
                            TextView textView = warningToast.getView().findViewById(android.R.id.message);
                            // Define the margins for the drawable
                            textView.setCompoundDrawablesWithIntrinsicBounds(warningIcon, null, null, null);
                            textView.setCompoundDrawablePadding(30);
                            warningToast.show();
                        }
                        items.set(pos,new Item(getItem(pos).getId(),getItem(pos).getName(), Integer.parseInt(dialogQty.getText().toString()), sp,cp));
                        notifyItemChanged(pos);
                        dialogSP.setText("");
                        dialogQty.setText("");
                        dialog.dismiss();
                    }
                });
                dialog.show();
            } else if (itemId == R.id.delete) {
                items.remove(pos);
                notifyItemRemoved(pos);
                onBindViewHolder(this, 0);
            }
            return itemId == R.id.edit || itemId == R.id.delete;
        }
    }
}
