package com.dev334.aircache.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.dev334.aircache.R;
import com.dev334.aircache.adapter.itemAdapter;
import com.dev334.aircache.model.Item;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ItemListActivity extends AppCompatActivity implements itemAdapter.onNoteListener {

    private RecyclerView itemRecyclerView;
    private com.dev334.aircache.adapter.itemAdapter itemAdapter;
    private List<Item> items;
    private String itemType;
    private FirebaseFirestore firestore;
    private String TAG = "ItemListActivityLog";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        itemType = getIntent().getStringExtra("Category");

        textView = findViewById(R.id.textView21);
        textView.setText(itemType);

        itemRecyclerView = findViewById(R.id.itemlist_recyclerview);

        items = new ArrayList<>();

        firestore = FirebaseFirestore.getInstance();

        firestore.collection("Items").whereEqualTo("category", itemType).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                        for(int i=0;i<docs.size();i++){
                            items.add(docs.get(i).toObject(Item.class));
                        }
                        setUpRecycler();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: "+e.getMessage());
            }
        });
    }

    private void setUpRecycler() {
        itemAdapter=new itemAdapter(items, this);
        itemRecyclerView.setAdapter(itemAdapter);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
        itemRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onNoteClick(int position) {

        AlertDialog.Builder alert=new AlertDialog.Builder(getApplicationContext());
        View view=getLayoutInflater().inflate(R.layout.dialog_view_item,null);
        alert.setView(view);
        AlertDialog show=alert.show();
        alert.setCancelable(true);
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    }
}