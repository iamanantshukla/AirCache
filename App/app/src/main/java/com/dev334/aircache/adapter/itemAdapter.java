package com.dev334.aircache.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev334.aircache.R;
import com.dev334.aircache.model.Item;

import java.util.List;

public class itemAdapter extends RecyclerView.Adapter<itemAdapter.mViewHolder> {

    private List<Item> itemModels;
    private onNoteListener mOnNoteListener;

    public itemAdapter(List<Item> itemModels, onNoteListener onNoteListener){
        this.itemModels=itemModels;
        this.mOnNoteListener=onNoteListener;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new itemAdapter.mViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        holder.setItemName(itemModels.get(position).getName());
        holder.setItemPrice(itemModels.get(position).getRentPrice());
        holder.setLockNo(itemModels.get(position).getLockerID());
    }


    @Override
    public int getItemCount() {
        return itemModels.size();
    }

    public interface onNoteListener{
        void onNoteClick(int position);
    }

    public class mViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        View view;
        TextView itemName,itemPrice,itemLockNumer;
        Button itemViewDetails;
        onNoteListener onNotelistener;



        public mViewHolder(@NonNull View itemView, onNoteListener monNoteListener) {
            super(itemView);
            view = itemView;
            view.setOnClickListener(this);
            onNotelistener = monNoteListener;
        }

        public void setItemName(String name) {
            itemName = view.findViewById(R.id.textView_item_name);
            itemName.setText(name);
        }

        public void setItemPrice(String rentPrice) {
            itemPrice = view.findViewById(R.id.textView_item_price);
            itemPrice.setText("₹ " + rentPrice);
        }

        public void setLockNo(String lockerID) {
            itemLockNumer = view.findViewById(R.id.textView_item_lockno);
            itemLockNumer.setText(lockerID);
        }

        @Override
        public void onClick(View v) {
            onNotelistener.onNoteClick(getAdapterPosition());
        }
    }
}
