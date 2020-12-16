package com.alphaverse.inventorymanagementsystem.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alphaverse.inventorymanagementsystem.R;
import com.alphaverse.inventorymanagementsystem.ui.auth.AuthenticationActivity;
import com.alphaverse.inventorymanagementsystem.util.CurrentUserAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity {
    public final String TAG = getClass().getSimpleName();
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ProgressBar progressBar;
    private FirebaseUser mUser;
    private String user;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firestore.collection("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.splash_pb);
        authStateListener = firebaseAuth -> {
            mUser = firebaseAuth.getCurrentUser();
            if (mUser != null) {
                user=mUser.getUid();
                        progressBar.setVisibility(View.VISIBLE);
                        collectionReference.document(user).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Log.d(TAG, "onEvent: "+task.getResult().get("userName"));
                                CurrentUserAPI currentUserAPI= CurrentUserAPI.getInstance();
                                currentUserAPI.setUserId(String.valueOf(Objects.requireNonNull(task.getResult()).get("userId")));
                                currentUserAPI.setUserName(String.valueOf(Objects.requireNonNull(task.getResult()).get("userName")));
                                currentUserAPI.setUserStoreName(String.valueOf(Objects.requireNonNull(task.getResult()).get("userStoreName")));
                                startActivity(new Intent(SplashScreenActivity.this,HomeScreenActivity.class));
                                finish();
                                }
                            }
                        }).addOnFailureListener(e -> {

                        });

//                collectionReference.get().addOnCompleteListener(task -> {
//
//
//                    if (task.isSuccessful()){
//                        for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
//                            if (documentSnapshot.exists() && documentSnapshot.get("userId").equals(user)) {
//                                Log.d(TAG, "onEvent: "+documentSnapshot.get("userName"));
//                                progressBar.setVisibility(View.INVISIBLE);
//                                CurrentUserAPI currentUserAPI= CurrentUserAPI.getInstance();
//                                currentUserAPI.setUserId(String.valueOf(documentSnapshot.get("userId")));
//                                currentUserAPI.setUserName(String.valueOf(documentSnapshot.get("userName")));
//                                currentUserAPI.setUserStoreName(String.valueOf(documentSnapshot.get("userStoreName")));
//                                startActivity(new Intent(SplashScreenActivity.this,HomeScreenActivity.class));
//                                finish();
//                            }
//                        }
//                    }
//                }).addOnFailureListener(e -> {
//
//                });

            } else {
                startActivity(new Intent(SplashScreenActivity.this, AuthenticationActivity.class));
                finish();
            }

        };
        firebaseAuth.addAuthStateListener(authStateListener);
    }


}

