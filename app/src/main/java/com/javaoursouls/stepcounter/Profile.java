package com.javaoursouls.stepcounter;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class Profile extends AppCompatActivity implements GoogleSignInApi, View.OnClickListener{

    private static final String TAG = null;
    private Button back;
    TextView username, usermail, accountid;
    ImageView profileimage;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        back = (Button) findViewById(R.id.profile_back);
        username = (TextView) findViewById(R.id.name);
        usermail = (TextView) findViewById(R.id.mail);
        accountid = (TextView) findViewById(R.id.accountid);
        profileimage = (ImageView) findViewById(R.id.profileImage);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        back.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(Profile.this);
        if (acct != null) {
            username.setText(acct.getDisplayName());
            //String personGivenName = acct.getGivenName();
            //String personFamilyName = acct.getFamilyName();
            usermail.setText(acct.getEmail());
            accountid.setText(acct.getId());
            Glide.with(this).load(acct.getPhotoUrl()).into(profileimage);

        }

    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            gotoSignIn();
                        }
                    }
                });
    }

    private void gotoSignIn() {
        Intent intent = new Intent(Profile.this, SignIn.class);
        startActivity(intent);
    }


    @Override
    public Intent getSignInIntent(GoogleApiClient googleApiClient) {
        return null;
    }

    @Override
    public OptionalPendingResult<GoogleSignInResult> silentSignIn(GoogleApiClient googleApiClient) {
        return null;
    }

    @Override
    public PendingResult<Status> signOut(GoogleApiClient googleApiClient) {
        return null;
    }

    @Override
    public PendingResult<Status> revokeAccess(GoogleApiClient googleApiClient) {
        return null;
    }

    @Nullable
    @Override
    public GoogleSignInResult getSignInResultFromIntent(Intent intent) {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.profile_back :
                finish();
        }
    }
}
