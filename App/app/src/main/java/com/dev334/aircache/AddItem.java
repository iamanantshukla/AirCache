package com.dev334.aircache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AddItem extends AppCompatActivity {


    private EditText ItemName,SecurityMoney,RentPrice;
    private TextView ItemImage;
    private Spinner categorySpinner;
    private ArrayAdapter<CharSequence> categoryAdapter;
    StorageReference storageReference;
    private Button submit;
    private int REQ_IMG=21;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private String TAG = "AddItemLog";


    private String image;
    private String UserUID;

    private boolean pdfUploadFlag=false;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQ_IMG && resultCode==RESULT_OK && data!=null)
        {
            uploadToStorage(data.getData(),data);
        }


    }

    private void uploadToStorage(Uri data, Intent data1) {

        final ProgressDialog progressDialog=new ProgressDialog(getApplicationContext());
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        StorageReference reference=storageReference.child("ItemImage"+System.currentTimeMillis()+".pdf");

        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri uri=uriTask.getResult();


                File file= new File(uri.getPath());
                image=uri.toString();

                ItemImage.setText(file.getName().toString());
                pdfUploadFlag=true;
                progressDialog.dismiss();


            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                double progress=(100.0*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                progressDialog.setMessage("File Uploading ..."+(int)progress);

            }
        });



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        storageReference= FirebaseStorage.getInstance().getReference();

         ItemName=findViewById(R.id.addItemName);
         SecurityMoney=findViewById(R.id.addItemSecurityMoney);
         RentPrice=findViewById(R.id.addItemRentPrice);
         ItemImage=findViewById(R.id.addItemImage);

         categorySpinner=findViewById(R.id.addItemCategory);
         categoryAdapter=ArrayAdapter.createFromResource(getApplicationContext(),R.array.array_categories,R.layout.spinner_layout);
         categoryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
         categorySpinner.setAdapter(categoryAdapter);
         firestore = FirebaseFirestore.getInstance();


        submit=findViewById(R.id.button_submit);
         mAuth=FirebaseAuth.getInstance();
         UserUID=mAuth.getUid();
         ItemImage.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                   Intent chooseFile=new Intent(Intent.ACTION_GET_CONTENT);
                   chooseFile.setType("image/jpg");
                   chooseFile=Intent.createChooser(chooseFile,"Choose the item image");
                   startActivityForResult(chooseFile,REQ_IMG);

             }
         });


         submit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                    String name=ItemName.getText().toString();
                    String category=categorySpinner.getSelectedItem().toString();
                    //image
                    String ownerName="Random";
                    String rentPrice=RentPrice.getText().toString();
                    String securityMoney=SecurityMoney.getText().toString();

                 Map<String,Object> item=new HashMap<>();
                 item.put("name",name);
                 item.put("category",category);
                 item.put("image",image);
                 item.put("ownerName",ownerName);
                 item.put("OwnerID",UserUID);
                 item.put("rentPrice",rentPrice);
                 item.put("securityMoney",securityMoney);

                findFreeLocker(item);

             }
         });



    }

    private void findFreeLocker(Map<String, Object> item) {
        firestore.collection("Lockers").whereEqualTo("Status", true).limit(1).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()){
                            //no free
                        }else {
                            DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);
                            String locker = doc.get("Code").toString();
                            item.put("LockerID",locker);
                            AddItemToInventory(item);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: "+e.getMessage());
            }
        });
    }

    private void AddItemToInventory(Map<String, Object> item) {
        firestore.collection("Items").document().set(item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Item Added", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: "+e.getMessage());
            }
        });
    }

}