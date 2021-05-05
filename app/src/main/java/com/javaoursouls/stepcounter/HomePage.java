package com.javaoursouls.stepcounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class HomePage extends AppCompatActivity implements View.OnClickListener,  OnCompleteListener<Void> {


    private Button gotoProfile;
    private Button gotoStepCounter;
    private Button logout;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        gotoProfile = (Button) findViewById(R.id.homepage_goto_profile);
        gotoStepCounter = (Button) findViewById(R.id.homepage_goto_stepcounter);
        logout = (Button) findViewById(R.id.homepage_logout);

        gotoProfile.setOnClickListener(this);
        gotoStepCounter.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.homepage_goto_profile:
                startActivity(new Intent(HomePage.this, Profile.class));
                break;
            case R.id.homepage_goto_stepcounter :
                startActivity(new Intent(HomePage.this, MainActivity.class));
                break;
            case R.id.homepage_logout :
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(this);
                break;
        }
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
            startActivity(new Intent(HomePage.this, SignIn.class));
        }
    }
}