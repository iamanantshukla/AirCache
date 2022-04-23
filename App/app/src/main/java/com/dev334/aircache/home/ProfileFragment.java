package com.dev334.aircache.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dev334.aircache.R;
import com.dev334.aircache.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private View view;
    private LinearLayout changePassword,myWallet,logOut,deleteAcc;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String TAG = "ProfileFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        changePassword = view.findViewById(R.id.change_password);
        myWallet = view.findViewById(R.id.my_wallet);
        logOut = view.findViewById(R.id.log_out);
        deleteAcc = view.findViewById(R.id.delete_account);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Hii",Toast.LENGTH_SHORT).show();
            }
        });
        myWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        deleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User account deleted.");
                                }
                            }
                        });
            }
        });
        return view;
    }
}