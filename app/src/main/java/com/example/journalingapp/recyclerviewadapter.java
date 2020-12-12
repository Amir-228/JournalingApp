package com.example.journalingapp;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class recyclerviewadapter extends RecyclerView.Adapter<recyclerviewadapter.ViewHolder>{

    private ArrayList<Entry> data;
    private LayoutInflater inf;
    private int selectedEntryIndex;
    Context ctx;
    public OnItemClickListener onClickerListener;


    public recyclerviewadapter(ArrayList<Entry> data, Context ctx) {
        this.inf = LayoutInflater.from(ctx);
        this.data = data;
        this.ctx = ctx;
    }

    public interface OnItemClickListener{ //my own interface to facilitate clicking
        void onEditClick(int position);
        void onDeleteClick(int position);
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
        SimpleDateFormat spf = new SimpleDateFormat("MMM dd, yyyy");
        String date = spf.format(data.get(position).getDate());
        holder.date.setText(date);

        if(entry.getOwner().equals("")){
            holder.owner.setText("No name set.");
        }
        else{
            holder.owner.setText(entry.getOwner());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateEntry(Entry entry){
        data.get(selectedEntryIndex).setTitle(entry.getTitle());
        data.get(selectedEntryIndex).setContent(entry.getContent());
        data.get(selectedEntryIndex).setDate(entry.getDate());
        data.get(selectedEntryIndex).setOwner(entry.getOwner());
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener hear){
        onClickerListener = hear;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, date, owner;
        ImageButton delete;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            title = itemView.findViewById(R.id.titleHolder);
            date = itemView.findViewById(R.id.dateHolder);
            owner = itemView.findViewById(R.id.ownerHolder);
            delete = itemView.findViewById(R.id.deleteBtn);


            itemView.setOnClickListener(new View.OnClickListener() { //allows the app to recognize which item is being clicked.
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onEditClick(position);
                        }
                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

        }
    }
}
