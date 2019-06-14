package com.jesuraj.java.businesscard;

import android.content.Context;
import android.util.Log;
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
    private CardClickListener cardClickListener;

    public CardListAdapter(Context context, CardClickListener cardClickListener) {

        layoutInflater = LayoutInflater.from(context);
        this.cardClickListener = cardClickListener;
    }


    @NonNull
    @Override
    public CardListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CardListViewHolder(layoutInflater.inflate(R.layout.list_item, parent, false), cardClickListener);
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

    public Card getItem(int position) {
        return cardList.get(position);
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
        notifyDataSetChanged();
    }

    public List<Card> getCardList() {
        return cardList;
    }

    @Override
    public int getItemCount() {
        if (cardList == null)
            return 0;
        else return cardList.size();
    }

    class CardListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvName, tvTime, tvDesc;

        private CardClickListener cardClickListener;

        public CardListViewHolder(@NonNull View itemView, CardClickListener cardClickListener) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDesc = itemView.findViewById(R.id.tvdesc);
            this.cardClickListener=cardClickListener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            cardClickListener.onClick(getAdapterPosition());

        }
    }

    public interface CardClickListener {
        void onClick(int position);
    }
}
