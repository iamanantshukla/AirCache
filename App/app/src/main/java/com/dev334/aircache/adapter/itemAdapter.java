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

    public itemAdapter(List<Item> itemModels){
        this.itemModels=itemModels;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new itemAdapter.mViewHolder(view);
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

    public class mViewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView itemName,itemPrice,itemLockNumer;
        Button itemViewDetails;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setItemName(String name) {
            itemName = view.findViewById(R.id.textView_item_name);
            itemName.setText(name);
        }

        public void setItemPrice(Double rentPrice) {
            itemPrice = view.findViewById(R.id.textView_item_price);
            itemPrice.setText(rentPrice.toString());
        }

        public void setLockNo(String lockerID) {
            itemLockNumer = view.findViewById(R.id.textView_item_lockno);
            itemLockNumer.setText(lockerID);
        }
    }
}
