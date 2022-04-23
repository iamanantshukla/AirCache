package com.dev334.aircache.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.dev334.aircache.home.MainActivity;
import com.dev334.aircache.R;
import com.dev334.aircache.database.TinyDB;

public class LoginActivity extends AppCompatActivity {

       private LoginFragment loginFragment;
       private SignupFragment signupFragment;
       private CreateProfileFragment createProfileFragment;
       private LoginHomeFragment loginHomeFragment;
       private FragmentManager fragmentManager;
       private VerificationFragment verificationFragment;
       private int FRAGMENT=0;
       private String UserId;
       private TinyDB tinyDB;
       private final String TAG="LoginActivityLog";
       private String email,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginFragment=new LoginFragment();
        signupFragment=new SignupFragment();
        createProfileFragment = new CreateProfileFragment();
        loginHomeFragment = new LoginHomeFragment();

        verificationFragment=new VerificationFragment();
        fragmentManager=getSupportFragmentManager();

        FRAGMENT = getIntent().getIntExtra("LoginCode", 0);

        if(FRAGMENT==0){
            replaceFragment(loginHomeFragment);
        }else if(FRAGMENT==2){
            replaceFragment(loginFragment);
        }else if(FRAGMENT==1){
            replaceFragment(createProfileFragment);
        }else{
            replaceFragment(signupFragment);
        }





    }

    public void setSignUpCredentials(String email,String password)
    {
         this.email=email;
         this.password=password;
    }

    public void openVerifyEmail()
    {
        replaceFragment(verificationFragment);
    }

    public String getSignUpEmail(){
        return email;
    }
    public String getSignUpPassword(){
        return password;
    }

    public void openCreateProfile(){
        FRAGMENT=1;
        replaceFragment(createProfileFragment);
    }

    public void openSignUp(){
        FRAGMENT = 4;
        replaceFragment(signupFragment);
    }

    public void openHomePage(){
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
    }

    public void openSignIn(){
        FRAGMENT = 2;
        replaceFragment(loginFragment);
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
            transaction.add(R.id.LoginContainer, fragmentToShow);
        }

        transaction.commit();
    }
}