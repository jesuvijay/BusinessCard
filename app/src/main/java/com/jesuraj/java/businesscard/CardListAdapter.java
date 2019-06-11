package com.jesuraj.java.businesscard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.CardListViewHolder> {

    private final LayoutInflater layoutInflater;
    private List<Card> cardList;

    public CardListAdapter(Context context) {

        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CardListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CardListViewHolder(layoutInflater.inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CardListViewHolder holder, int position) {
        if (cardList != null) {
            Card card = cardList.get(position);
            holder.tvTime.setText(card.getDatetime());
            holder.tvName.setText(card.getCmpyname());
            holder.tvDesc.setText(card.getDescription());
        } else
            holder.tvName.setText("No Name");
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (cardList == null)
            return 0;
        else return cardList.size();
    }

    class CardListViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvTime, tvDesc;

        public CardListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDesc = itemView.findViewById(R.id.tvdesc);
        }


    }
}
