package com.dev334.aircache.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dev334.aircache.MainActivity;
import com.dev334.aircache.R;
import com.dev334.aircache.database.TinyDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }

    private View view;
    private Button Login;
    private EditText EditEmail,EditPassword;
    private TinyDB tinyDB;
    private FirebaseAuth mAuth;
    private TextView newUser;
    private String Email,Password;
    private ProgressBar loading;
    private FirebaseFirestore firestore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_login, container, false);
        tinyDB=new TinyDB(getContext());
        mAuth=FirebaseAuth.getInstance();
        newUser=view.findViewById(R.id.LoginTextSignIn);
        EditEmail=view.findViewById(R.id.editEmailSignin);
        EditPassword=view.findViewById(R.id.editPasswordSignin);
        Login=view.findViewById(R.id.signInButton);
        loading=view.findViewById(R.id.signInLoading);
        firestore=FirebaseFirestore.getInstance();

        loading.setVisibility(View.INVISIBLE);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email=EditEmail.getText().toString();
                Password=EditPassword.getText().toString();
                if(check(Email,Password))
                {
                   SigninUser();
                }
                else
                {
                   loading.setVisibility(View.INVISIBLE);
                }
            }
        });


        return view;

    }

    private void SigninUser() {
        mAuth.signInWithEmailAndPassword(Email, Password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user=mAuth.getCurrentUser();
                        if(user.isEmailVerified()){
                            checkProfileCreated();
                        }else{
                            //email verify fragment
                            loading.setVisibility(View.INVISIBLE);
                            ((LoginActivity)getActivity()).setSignUpCredentials(Email, Password);
                            ((LoginActivity)getActivity()).openVerifyEmail();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(),"Login Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkProfileCreated() {
        String UserUID=mAuth.getCurrentUser().getUid();
        firestore.collection("Users").document(UserUID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        loading.setVisibility(View.INVISIBLE);
                        if(task.isSuccessful()){
                            Intent i = new Intent(getActivity(), MainActivity.class);
                            startActivity(i);
                            getActivity().finish();
                        }else{
                            ((LoginActivity)getActivity()).openCreateProfile();
                        }
                    }
                });


    }

    private boolean check(String email, String password) {
        if(email.isEmpty()){
            EditEmail.setError("Email is empty");
            return false;
        }else if(password.isEmpty()){
            EditPassword.setError("Password is empty");
            return  false;
        }else {
            if (password.length() < 6) {
                EditPassword.setError("Password is too short");
                return false;
            } else {
                return true;
            }
        }
    }
}