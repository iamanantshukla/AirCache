package com.dev334.aircache.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev334.aircache.R;
import com.dev334.aircache.scan.ScanActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private View view;
    private LinearLayout books, misc, sports, electronics;
    private TextView headerBtn1, headerBtn2;
    private Boolean borrowed=true;
    private TextView headerText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_home, container, false);

        books = view.findViewById(R.id.booksCategory);
        misc = view.findViewById(R.id.miscCategory);
        sports = view.findViewById(R.id.sportsCategory);
        electronics = view.findViewById(R.id.electCategory);

        headerBtn1 = view.findViewById(R.id.header_btn_1);
        headerBtn2 = view.findViewById(R.id.header_btn_2);

        headerText = view.findViewById(R.id.header_text);

        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ItemListActivity.class);
                i.putExtra("Category", "Sports");
                startActivity(i);
            }
        });

        electronics.setOnClickListener(v->{
            Intent i = new Intent(getActivity(), ItemListActivity.class);
            i.putExtra("Category", "Electronics");
            startActivity(i);
        });

        misc.setOnClickListener(v->{
            Intent i = new Intent(getActivity(), ItemListActivity.class);
            i.putExtra("Category", "Miscellaneous");
            startActivity(i);
        });

        books.setOnClickListener(v->{
            Intent i = new Intent(getActivity(), ItemListActivity.class);
            i.putExtra("Category", "Books");
            startActivity(i);
        });

        getProfileData();

        headerBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(borrowed){
                    Intent i = new Intent(getActivity(), ScanActivity.class);
                    i.putExtra("Type", 1);
                    startActivity(i);
                }else{
                    Intent i = new Intent(getActivity(), ScanActivity.class);
                    i.putExtra("Type", 0);
                    startActivity(i);
                }
            }
        });

        headerBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });


        return view;
    }

    private void getProfileData() {
        FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //
                borrowed = documentSnapshot.getBoolean("Status");
                if(borrowed) {
                    headerText.setText("You have already borrowed an item.");
                    headerBtn1.setText("return");
                    headerBtn2.setText("view details");
                }else{
                    headerText.setText("Borrow an item now.");
                    headerBtn1.setText("browse");
                    headerBtn2.setText("why borrow");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getProfileData();
    }
}