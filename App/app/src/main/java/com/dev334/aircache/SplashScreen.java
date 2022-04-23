package com.dev334.aircache;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.dev334.aircache.home.MainActivity;
import com.dev334.aircache.login.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreen extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseAuth mAuth;
    private String TAG = "SplashScreenLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        if(mAuth.getCurrentUser().getUid().isEmpty()){
            //open signUp
            Intent i = new Intent(SplashScreen.this, LoginActivity.class);
            i.putExtra("LoginCode", 0);
            startActivity(i);
        }else{
            firestore.collection("Users").document().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        //open homePage
                        Intent i = new Intent(SplashScreen.this, MainActivity.class);
                        i.putExtra("LoginCode", 0);
                        startActivity(i);
                    }else{
                        //open create Profile
                        Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                        i.putExtra("LoginCode", 2);
                        startActivity(i);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(TAG, "onFailure: "+e.getMessage());
                }
            });
        }

    }
}