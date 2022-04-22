package com.dev334.aircache.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.dev334.aircache.R;
import com.dev334.aircache.database.TinyDB;

public class LoginActivity extends AppCompatActivity {

       private LoginFragment loginFragment;
       private SignupFragment signupFragment;
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
        verificationFragment=new VerificationFragment();
        fragmentManager=getSupportFragmentManager();
        if(FRAGMENT==0){
            replaceFragment(loginFragment);
        }else if(FRAGMENT==1){
            replaceFragment(CreateProfileFragment);
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
          replaceFragment(CreateProfileFragment);
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