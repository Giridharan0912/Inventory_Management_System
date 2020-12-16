package com.alphaverse.inventorymanagementsystem.ui.auth;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alphaverse.inventorymanagementsystem.R;

public class AuthenticationActivity extends AppCompatActivity implements ActionListener {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        fragmentManager = getSupportFragmentManager();
        initLogin();

    }

    private void initLogin() {
        fragmentTransaction = fragmentManager.beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setActionListener(this);
        fragmentTransaction.replace(R.id.auth_framelayout, loginFragment).commit();
    }
    private void initSignUp(){
        fragmentTransaction=fragmentManager.beginTransaction();
        SignUpFragment signUpFragment=new SignUpFragment();
        signUpFragment.setActionListener(this);
        fragmentTransaction.replace(R.id.auth_framelayout,signUpFragment).commit();
    }

    @Override
    public void onSignUpClicked() {
        initSignUp();
    }


    @Override
    public void onLoginClicked() {
        initLogin();
    }
}