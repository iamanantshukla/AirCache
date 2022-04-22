package com.dev334.aircache;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CreateProfileFragment extends Fragment {

    public CreateProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private View view;
    private TextView profileName, profileMobile, profileID;
    private Button nextBtn;
    private String name, mobile, collegeID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_create_profile, container, false);

        profileID=view.findViewById(R.id.profile_collegeid);
        profileMobile=view.findViewById(R.id.profile_mobile);
        profileName=view.findViewById(R.id.profile_name);

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
                    //create Profile
                }

            }
        });

        return view;
    }
}