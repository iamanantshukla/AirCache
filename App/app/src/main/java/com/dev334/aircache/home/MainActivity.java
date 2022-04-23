package com.dev334.aircache.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dev334.aircache.R;
import com.dev334.aircache.scan.ScanActivity;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private Integer FRAGMENT = 0;
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private BrowseFragment browseFragment;
    private FragmentManager fragmentManager;
    private TextView navHome, navProfile, navBrowse;
    private FloatingActionButton addItem, grabItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment();
        browseFragment = new BrowseFragment();

        fragmentManager=getSupportFragmentManager();

        navHome = findViewById(R.id.nav_home);
        navProfile = findViewById(R.id.nav_profile);
        navBrowse = findViewById(R.id.nav_browse);

        updateUI();

        navHome.setOnClickListener(v->{
            FRAGMENT=0;
            updateUI();
        });

        navProfile.setOnClickListener(v->{
            FRAGMENT=1;
            updateUI();
        });

        navBrowse.setOnClickListener(v->{
            FRAGMENT=2;
            updateUI();
        });

        addItem = findViewById(R.id.menu_item1);
        grabItem = findViewById(R.id.menu_item2);

        grabItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ScanActivity.class);
                startActivity(i);
            }
        });


    }
    private void updateUI(){
        if(FRAGMENT==0){
            makeColorGrey();
            navHome.setTextColor(getResources().getColor(R.color.black));
            replaceFragment(homeFragment);
        }else if(FRAGMENT==1){
            makeColorGrey();
            navProfile.setTextColor(getResources().getColor(R.color.black));
            replaceFragment(profileFragment);
        }else if(FRAGMENT==2){
            makeColorGrey();
            navBrowse.setTextColor(getResources().getColor(R.color.black));
            replaceFragment(browseFragment);
        }
    }

    private void replaceFragment(Fragment fragmentToShow) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        // Hide all of the fragments
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            transaction.hide(fragment);
        }

        if (fragmentToShow.isAdded()) {
            // When fragment was previously added - show it
            transaction.show(fragmentToShow);
        } else {
            // When fragment is adding first time - add it
            transaction.add(R.id.HomeContainer, fragmentToShow);
        }

        transaction.commit();
    }

    private void makeColorGrey(){
        navBrowse.setTextColor(getResources().getColor(R.color.grey));
        navProfile.setTextColor(getResources().getColor(R.color.grey));
        navHome.setTextColor(getResources().getColor(R.color.grey));
    }
}