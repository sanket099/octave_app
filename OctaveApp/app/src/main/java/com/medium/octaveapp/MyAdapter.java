package com.medium.octaveapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<MusicClass> arrayList;
    private LayoutInflater inflater;
    private OnNoteList onNoteList;

    public MyAdapter(Context context, ArrayList<MusicClass> arrayList, OnNoteList onNoteList) {
        this.context = context;
        this.arrayList = arrayList;
        this.onNoteList = onNoteList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_layout, parent, false);
        MyAdapter.MyViewHolder holder = new MyAdapter.MyViewHolder(view,onNoteList);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        holder.title.setText(arrayList.get(position).getTitle());
        holder.artist.setText(arrayList.get(position).getArtist());


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        OnNoteList onNoteList;

        TextView title, artist;

        public MyViewHolder(@NonNull View itemView, OnNoteList onNoteList) {
            super(itemView);

            title= itemView.findViewById(R.id.tv_title);
            artist= itemView.findViewById(R.id.tv_artist);
            this.onNoteList = onNoteList;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            onNoteList.OnnoteClick(arrayList.get(getAdapterPosition()), getAdapterPosition());

        }
    }
    public interface OnNoteList {
        void OnnoteClick(MusicClass userClass, int position);


    }
    public void filteredlist(ArrayList<MusicClass> filterlist){
        arrayList = filterlist;
        notifyDataSetChanged();
    }
}

