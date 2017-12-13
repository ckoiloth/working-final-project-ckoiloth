package com.example.chiru.authentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth firebaseAuth;
    private SignInButton signInButton;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final int SIGN_IN = 1;
    public static final String UID = "UID";
    public static final String NAME = "NAME";
    public static final String USER = "USER";
    public static final String CHATLIST = "List Of Chats";

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser= firebaseAuth.getCurrentUser();

        if(currentUser != null){
            rerouteToHomepage();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .enableAutoManage(MainActivity.this, new GoogleApiClient.OnConnectionFailedListener(){

                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(MainActivity.this, "This Failed", Toast.LENGTH_LONG).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();

        signInButton = (SignInButton) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSignIn();
            }
        });

    }


    private void userSignIn(){
        Intent authIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(authIntent , SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == SIGN_IN){
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if(googleSignInResult.isSuccess()){
                GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();

                FirebaseUserAuth(googleSignInAccount);
            }
        }
    }

    public void FirebaseUserAuth(GoogleSignInAccount googleSignInAccount){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);

        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                          rerouteToHomepage();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Sign-In Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private User setNewUser(){
        String currentUser = firebaseAuth.getCurrentUser().getUid();
        User newUser =
                new User(firebaseAuth.getCurrentUser().getDisplayName(),currentUser, new ArrayList<Chat>());
        FirebaseDatabase.getInstance()
                .getReference("/Users/" + currentUser).setValue(newUser);
        return newUser;
    }

    private void rerouteToHomepage(){
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        final Intent rerouteToHomePage = new Intent(MainActivity.this, HomePage.class);

        DatabaseReference userRef
                = firebaseDatabase
                .getReference("/Users/" + currentUser.getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user == null){
                    user = setNewUser();
                }
                rerouteToHomePage.putExtra(USER, user);
                MainActivity.this.startActivity(rerouteToHomePage);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

