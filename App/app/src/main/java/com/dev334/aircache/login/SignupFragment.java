package com.dev334.aircache.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dev334.aircache.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupFragment extends Fragment {
       private View view;
       private Button SignUp;
       private EditText EditEmail, EditPassword,EditName;
       private TextView Login;
       private String Email,Password,Name;
       private ProgressBar loading;
       private ConstraintLayout parentLayout;
       private FirebaseAuth mAuth;
       private FirebaseFirestore firestore;
       private String TAG = "SignUpFragmentError";


    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_signup, container, false);
        EditEmail= view.findViewById(R.id.editEmailSignup);
        EditPassword=view.findViewById(R.id.editPasswordSignUp);
        loading=view.findViewById(R.id.SignUpLoading);
        Login=view.findViewById(R.id.LoginTextSignup);
        EditName=view.findViewById(R.id.editUserName);

        mAuth = FirebaseAuth.getInstance();

//        Login.setOnClickListener(v->{
//            ((LoginActivity)getActivity()).openLogin();
//        });

           loading.setVisibility(View.INVISIBLE);
           SignUp=view.findViewById(R.id.SignUpButton);
         parentLayout=view.findViewById(R.id.signUpFragmentLayout);

          SignUp.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  loading.setVisibility(View.VISIBLE);
                  Email=EditEmail.getText().toString();
                  Name=EditName.getText().toString();
                  Password=EditPassword.getText().toString();

                  if(check(Name,Email,Password)){
                      registerUser();

                  }
              }
          });


        return view;

    }

    private void registerUser() {

          mAuth.createUserWithEmailAndPassword(Email,Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
              @Override
              public void onSuccess(AuthResult authResult) {
                   sendVerificationEmail();
              }
          }).addOnFailureListener(new OnFailureListener() {
              @Override
               public void onFailure(@NonNull Exception e) {
                  Toast.makeText(getContext(),"Signup Failed",Toast.LENGTH_SHORT).show();
                  Log.i(TAG, "onFailure: "+e.getMessage());
              }
          });



    }

    private void sendVerificationEmail() {
        mAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                ((LoginActivity)getActivity()).setSignUpCredentials(Email, Password);
                Toast.makeText(getContext(),"Email Sent",Toast.LENGTH_SHORT).show();
                ((LoginActivity)getActivity()).openVerifyEmail();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Failed to send email",Toast.LENGTH_SHORT).show();
               // Log.i(TAG, "onFailure: "+e.getMessage());
            }
        });
    }

    private boolean check(String name, String email, String password) {


        if(name.isEmpty()){
            EditName.setError("Name is empty");
            return false;
        }
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