package com.dev334.aircache.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dev334.aircache.R;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_home, container, false);

        books = view.findViewById(R.id.booksCategory);
        misc = view.findViewById(R.id.miscCategory);
        sports = view.findViewById(R.id.sportsCategory);
        electronics = view.findViewById(R.id.electCategory);

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


        return view;
    }
}