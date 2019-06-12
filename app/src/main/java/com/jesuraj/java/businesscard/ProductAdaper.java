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
    private List<ProductData> pathList=new ArrayList<>();


    @NonNull
    @Override
    public CardViewHoldeer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new CardViewHoldeer(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHoldeer holder, int position) {

        holder.imageView.setImageBitmap(pathList.get(position).getBitmap());
    }

    public ProductAdaper() {

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

    public void setPathList(List<ProductData> pathList) {
        this.pathList = pathList;
    }

    class CardViewHoldeer extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public CardViewHoldeer(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivProduct);

        }
    }
}
