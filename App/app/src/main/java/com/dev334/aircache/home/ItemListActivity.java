package com.dev334.aircache.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.dev334.aircache.R;
import com.dev334.aircache.adapter.itemAdapter;
import com.dev334.aircache.model.Item;

import java.util.List;

public class ItemListActivity extends AppCompatActivity {

    private RecyclerView itemRecyclerView;
    private com.dev334.aircache.adapter.itemAdapter itemAdapter;
    private List<Item> Items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        itemRecyclerView = findViewById(R.id.itemlist_recyclerview);
        setUpRecycler();
    }

    private void setUpRecycler() {
        itemAdapter=new itemAdapter(Items);
        itemRecyclerView.setAdapter(itemAdapter);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
        itemRecyclerView.setHasFixedSize(true);
    }
}