package com.jesuraj.java.businesscard;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProductAdaper extends RecyclerView.Adapter<ProductAdaper.CardViewHoldeer> {
    private List<ProductData> pathList = new ArrayList<>();
    private RecyclerViewClickListener recyclerViewClickListener;


    @NonNull
    @Override
    public CardViewHoldeer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new CardViewHoldeer(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false), recyclerViewClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHoldeer holder, int position) {

        holder.imageView.setImageBitmap(pathList.get(position).getBitmap());
    }

    public ProductAdaper(RecyclerViewClickListener recyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener;


    }

    public void clearData() {
        pathList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (pathList != null)
            return pathList.size();
        else return 0;
    }

    public void addData(ProductData mData) {
        pathList.add(mData);
        notifyDataSetChanged();

    }

    public List<ProductData> getPathList() {
        return pathList;
    }

    public void setPathList(List<ProductData> pathList) {
        this.pathList = pathList;
        notifyDataSetChanged();
    }

    public void removeData(int position){
        pathList.remove(position);
        notifyDataSetChanged();
    }

    class CardViewHoldeer extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        private ImageView imageView;
        private RecyclerViewClickListener recyclerViewClickListener;

        public CardViewHoldeer(@NonNull View itemView, RecyclerViewClickListener recyclerViewClickListener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivProduct);
            this.recyclerViewClickListener = recyclerViewClickListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);


        }

        @Override
        public void onClick(View v) {
            recyclerViewClickListener.onClick(v, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            recyclerViewClickListener.onLongClick(v,getAdapterPosition());
            return true;
        }
    }
}
