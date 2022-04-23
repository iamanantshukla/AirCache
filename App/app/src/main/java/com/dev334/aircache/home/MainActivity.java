package com.dev334.aircache.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.dev334.aircache.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }
}