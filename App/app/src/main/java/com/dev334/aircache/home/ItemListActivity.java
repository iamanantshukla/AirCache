package com.dev334.aircache.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev334.aircache.R;
import com.dev334.aircache.adapter.itemAdapter;
import com.dev334.aircache.model.Item;
import com.dev334.aircache.scan.ScanActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

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

//        AlertDialog.Builder alert=new AlertDialog.Builder(getApplicationContext());
//        View view=getLayoutInflater().inflate(R.layout.dialog_view_item,null);
//        alert.setView(view);
//        AlertDialog show=alert.show();
//        alert.setCancelable(true);
//        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        showItemDialog(items.get(position));

    }

    private void showItemDialog(Item item) {
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(ItemListActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_view_item, null);

        TextView itemName = view.findViewById(R.id.item_name_Card);
        TextView itemRent = view.findViewById(R.id.item_rate_card);
        TextView itemMin = view.findViewById(R.id.item_mins_card);
        Button itemBtn = view.findViewById(R.id.item_btn_card);
        ImageView itemImage = view.findViewById(R.id.item_image_card);

        if(item.getCurUserID() == FirebaseAuth.getInstance().getCurrentUser().getUid()){
            itemBtn.setText("Return");
        }else{
            itemBtn.setText("borrow");
        }

        itemName.setText(item.getName());
        itemRent.setText("Rental rate: ₹"+ item.getRentPrice());
        itemMin.setText("Minimum security deposit: ₹"+ item.getSecurityMoney());
        itemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getCurUserID() == FirebaseAuth.getInstance().getCurrentUser().getUid()){
                    Intent i = new Intent(ItemListActivity.this, ScanActivity.class);
                    i.putExtra("Type", 1);
                    i.putExtra("ItemName", item.getName());
                    startActivity(i);
                }else{
                    Intent i = new Intent(ItemListActivity.this, ScanActivity.class);
                    i.putExtra("Type", 0);
                    i.putExtra("ItemName", item.getName());
                    startActivity(i);
                }
            }
        });

        StorageReference storageReference= FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageReference.child("items/" + item.getImage());
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(itemImage);

            }
        });

       // Button confirmBtn = view.findViewById(R.id.dialog_confirmBtn);

        alert.setView(view);
        android.app.AlertDialog show = alert.show();

        alert.setCancelable(true);
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

}