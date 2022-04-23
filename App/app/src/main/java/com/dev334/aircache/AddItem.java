package com.dev334.aircache;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddItem extends AppCompatActivity {


    private EditText ItemName,SecurityMoney,RentPrice;
    private Spinner categorySpinner;
    private ArrayAdapter<CharSequence> categoryAdapter;
    StorageReference storageReference;
    private Button submit;
    private int REQ_IMG=21;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        storageReference= FirebaseStorage.getInstance().getReference();

         ItemName=findViewById(R.id.addItemName);
         SecurityMoney=findViewById(R.id.addItemSecurityMoney);
         RentPrice=findViewById(R.id.addItemRentPrice);

         categorySpinner=findViewById(R.id.addItemCategory);
         categoryAdapter=ArrayAdapter.createFromResource(getApplicationContext(),R.array.array_categories,R.layout.spinner_layout);
         categoryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
         categorySpinner.setAdapter(categoryAdapter);

         submit=findViewById(R.id.button_submit);

         submit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                   Intent chooseFile=new Intent(Intent.ACTION_GET_CONTENT);
                   chooseFile.setType("image/jpg");
                   chooseFile=Intent.createChooser(chooseFile,"Choose the item image");
                   startActivityForResult(chooseFile,REQ_IMG);

             }
         });




    }
}