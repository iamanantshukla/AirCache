package com.dev334.aircache.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dev334.aircache.R;
import com.dev334.aircache.home.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class CreateProfileFragment extends Fragment {

      private FirebaseFirestore firestore;
      private FirebaseAuth mAuth;

    public CreateProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private View view;
    private TextView profileName, profileMobile, profileID, nextBtn;
    private String name, mobile, collegeID;
    private String UserUID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_create_profile, container, false);

        profileID=view.findViewById(R.id.profile_collegeid);
        profileMobile=view.findViewById(R.id.profile_mobile);
        profileName=view.findViewById(R.id.profile_name);
        mAuth=FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        UserUID=mAuth.getUid();
        nextBtn=view.findViewById(R.id.profile_next);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=profileName.getText().toString();
                mobile=profileMobile.getText().toString();
                collegeID=profileID.getText().toString();

                if(name.isEmpty()){
                    profileName.setError("Field is empty");
                }else if(mobile.isEmpty()){
                    profileMobile.setError("Field is empty");
                }else if(collegeID.isEmpty()){
                    profileID.setError("Field is empty");
                }else{
                    Map<String,Object> user=new HashMap<>();
                    user.put("Name",name);
                    user.put("mobile",mobile);
                    user.put("collegeId",collegeID);

                    firestore.collection("Users").document(UserUID).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(),"Welcome "+name+"!!",Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(getContext(), MainActivity.class);
                            startActivity(i);
                            getActivity().finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),"An Error Occurred",Toast.LENGTH_SHORT).show();

                        }
                    });


                }

            }
        });

        return view;
    }
}