package com.dev334.aircache.scan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.dev334.aircache.R;
import com.dev334.aircache.home.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.Document;

import java.util.HashMap;
import java.util.Map;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class ScanActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_QR_SCAN = 101;
    private Button ScanQrBtn, nextBtn;
    private String TAG = "ScanActivityLog";
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private OtpTextView otpTextView;
    private Boolean otpFilled = false;
    private FirebaseFirestore firestore;
    private String CODE = "";
    private WebView webView;
    private Integer USER_STATUS = 0;
    private String DocumentId= "";
    private DocumentSnapshot doc;
    private String UserId;
    private TextView scanTextHead;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        ScanQrBtn = findViewById(R.id.scanQrBtn);
        otpTextView = findViewById(R.id.otp_view);
        nextBtn = findViewById(R.id.nextBtn);

        scanTextHead = findViewById(R.id.scanItemHead);

        USER_STATUS = getIntent().getIntExtra("Type", 0);

        if(USER_STATUS==1){
            scanTextHead.setText("Return Item");
        }else{
            scanTextHead.setText("Borrow Item");
        }

        webView = findViewById(R.id.web_view_scan);
        UserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firestore = FirebaseFirestore.getInstance();

        ScanQrBtn.setOnClickListener(v->{
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            }else {
                openQRScanner();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otpFilled){
                    //show item and unlock
                    getItemDetail();
                }else{
                    Toast.makeText(getApplicationContext(), "Enter Lock code to unlock", Toast.LENGTH_LONG).show();
                }
            }
        });

        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }
            @Override
            public void onOTPComplete(String otp) {
                // fired when user has entered the OTP fully.
                otpFilled = true;
                CODE = otp;

            }
        });

    }

    private void getItemDetail() {
        firestore.collection("Items").whereEqualTo("LockerID", CODE).limit(1).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()){
                            //no item found
                        }else{
                            doc = queryDocumentSnapshots.getDocuments().get(0);
                            String itemName = doc.get("name").toString();
                            DocumentId = doc.getId();
                            //Toast.makeText(getApplicationContext(), "Hello"+ itemName, Toast.LENGTH_SHORT).show();

                            showItemDialog(itemName);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure: "+e.getMessage());
            }
        });
    }

    private void openQRScanner() {
        Intent i = new Intent(ScanActivity.this, QrCodeActivity.class);
        startActivityForResult(i, REQUEST_CODE_QR_SCAN);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openQRScanner();
            } else {
                //access denied
                //Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            Log.d(TAG, "COULD NOT GET A GOOD RESULT.");
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (result != null) {
                AlertDialog alertDialog = new AlertDialog.Builder(ScanActivity.this).create();
                alertDialog.setTitle("Scan Error");
                alertDialog.setMessage("QR Code could not be scanned");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;

        }
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            Log.d(TAG, "Have scan result in your app activity :" + result);
//            AlertDialog alertDialog = new AlertDialog.Builder(ScanActivity.this).create();
//            alertDialog.setTitle("Scan result");
//            alertDialog.setMessage(result);
//            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//            alertDialog.show();

            otpTextView.setOTP(result);

        }
    }

    private void showItemDialog(String item) {
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(ScanActivity.this);
        View view = getLayoutInflater().inflate(R.layout.confirm_item_dialog, null);

        TextView itemName = view.findViewById(R.id.dialog_item_name);
        itemName.setText(item);

        Button confirmBtn = view.findViewById(R.id.dialog_confirmBtn);

        alert.setView(view);
        android.app.AlertDialog show = alert.show();

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {
                //confirm item
                firestore.collection("Lockers").whereEqualTo("Code", CODE).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if(queryDocumentSnapshots.isEmpty()){

                                }else{
                                    String name = queryDocumentSnapshots.getDocuments().get(0).get("Name").toString();
                                    String status = "0";
                                    if(USER_STATUS==0){
                                        status = "1";
                                    }else{
                                        status = "0";
                                    }
                                    String URL = "https://cloud.arest.io/"+ name + "/digital/2/"+status;
                                    openInWeb(URL);
                                    show.dismiss();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "onFailure: "+e.getMessage());
                        show.dismiss();
                    }
                });
            }
        });

        alert.setCancelable(true);
        show.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void openInWeb(String url) {
        webView.loadUrl(url);
        if(USER_STATUS==0){
            firestore.collection("Items").document(DocumentId)
                    .update("curUserID", UserId, "status", false)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            firestore.collection("Users").document(UserId).update("Status", true)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //successfully rented the item
                                            Toast.makeText(getApplicationContext(), "Unlocked Successfully", Toast.LENGTH_LONG).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i(TAG, "onFailure: "+e.getMessage());
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(TAG, "onFailure: "+e.getMessage());
                }
            });
        }else{
            firestore.collection("Items").document(DocumentId)
                    .update("curUserID", "", "status", true)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            firestore.collection("Users").document(UserId).update("Status", false)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i(TAG, "onFailure: "+e.getMessage());
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

}