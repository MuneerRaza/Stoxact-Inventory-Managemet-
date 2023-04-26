package com.example.project.Helper;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.ViewHolder>{

    int[] imgs = {R.drawable.slider_img1,R.drawable.completed,R.drawable.completed};
    int[] headings = {R.string.welcome_to_stoxact, R.string.low_stocks, R.string.setup_completed};
    int[] desc = {R.string.slider_string1, R.string.slider_string2, R.string.slider_string3};
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView heading;
        TextView description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.slider_img1);
            heading = itemView.findViewById(R.id.slider_heading1);
            description = itemView.findViewById(R.id.Title_sign_up);
        }
    }
    public SliderAdapter(Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.slides, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(imgs[position]);
        holder.heading.setText(headings[position]);
        Typeface h = ResourcesCompat.getFont(context, R.font.poppins_semibold);
        holder.heading.setTypeface(h);
        holder.description.setText(desc[position]);
        holder.description.setTypeface(h);
    }

    @Override
    public int getItemCount() {
        return headings.length;
    }

}
