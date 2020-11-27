package com.example.journalingapp;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class recyclerviewadapter extends RecyclerView.Adapter<recyclerviewadapter.ViewHolder>{

    private ArrayList<Entry> data;
    private LayoutInflater inf;
    Context ctx;
    public OnItemClickListener onClickerListener;


    public recyclerviewadapter(ArrayList<Entry> data, Context ctx) {
        this.inf = LayoutInflater.from(ctx);
        this.data = data;
        this.ctx = ctx;
    }

    public interface OnItemClickListener{ //my own interface to facilitate clicking
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public recyclerviewadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inf.inflate(R.layout.activity_entry_item, parent, false);
        return new ViewHolder(v, onClickerListener);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerviewadapter.ViewHolder holder, int position) {
        Entry entry = data.get(position);
        holder.title.setText(entry.getTitle());
        holder.date.setText("" + entry.getDate());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, date;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            title = itemView.findViewById(R.id.titleHolder);
            date = itemView.findViewById(R.id.dateHolder);

            itemView.setOnClickListener(new View.OnClickListener() { //allows the app to recognize which item is being clicked.
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
