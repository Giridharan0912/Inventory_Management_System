package com.alphaverse.inventorymanagementsystem.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alphaverse.inventorymanagementsystem.R;
import com.alphaverse.inventorymanagementsystem.ui.HomeScreenActivity;
import com.alphaverse.inventorymanagementsystem.util.CurrentUserAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class LoginFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();
    private ProgressBar progressLogin;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = fireStore.collection("Users");
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    ArrayList<String> mobileNoArray = new ArrayList<>();
    private View loginView;
    private EditText etLoginPhNo, etLoginOtp;
    private Button btnLoginOtp, btnLogin;
    private TextView tvSignUp;
    private ActionListener actionListener;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        collectionReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    if (documentSnapshot.exists()) {
                        mobileNoArray.add((String) documentSnapshot.get("userPhoneNumber"));
                    }
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this `fragment`
        loginView = inflater.inflate(R.layout.fragment_login, container, false);
        btnLogin = loginView.findViewById(R.id.btn_login);
        btnLoginOtp = loginView.findViewById(R.id.btn_login_otp);
        etLoginOtp = loginView.findViewById(R.id.et_login_otp);
        etLoginPhNo = loginView.findViewById(R.id.et_login_phno);
        tvSignUp = loginView.findViewById(R.id.tv_signUp);
        progressLogin=loginView.findViewById(R.id.login_progress);
        progressLogin.setVisibility(View.INVISIBLE);
        tvSignUp.setOnClickListener(v -> {
            actionListener.onSignUpClicked();
        });
        btnLoginOtp.setOnClickListener(v -> {
            String phNo = etLoginPhNo.getText().toString().trim();
            if (phNo.length() == 10) {
                sentVerificationCode(phNo);
            } else {
                Toast.makeText(getContext(), "Enter the valid number", Toast.LENGTH_SHORT).show();
            }
        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d(TAG, "onVerificationFailed: " + e.getMessage());
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mVerificationId = s;

            }

        };
        btnLogin.setOnClickListener(v -> {
            String otp = etLoginOtp.getText().toString().trim();
            if (otp.length() == 6) {
                progressLogin.setVisibility(View.VISIBLE);
                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId, otp);
                signIn(phoneAuthCredential);
            } else {
                Toast.makeText(getContext(), "Enter the correct otp number", Toast.LENGTH_SHORT).show();
            }
        });
        return loginView;
    }

    public void setActionListener(Context context) {
        this.actionListener = (ActionListener) context;
    }

    private void sentVerificationCode(String phNo) {
//        Log.d(TAG, "sentVerificationCode: "+mobileNoArray.size());
        if (mobileNoArray.contains(phNo)) {
            PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                    .setPhoneNumber("+91" + phNo)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(getActivity())
                    .setCallbacks(mCallbacks)
                    .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        } else {
            Toast.makeText(getContext(), "Create new account", Toast.LENGTH_SHORT).show();
        }
    }

    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    currentUser = task.getResult().getUser();
                    String user=currentUser.getUid();
                    collectionReference.document(user).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "onEvent: " + task.getResult().get("userName"));
                                CurrentUserAPI currentUserAPI = CurrentUserAPI.getInstance();
                                currentUserAPI.setUserId(String.valueOf(Objects.requireNonNull(task.getResult()).get("userId")));
                                currentUserAPI.setUserName(String.valueOf(Objects.requireNonNull(task.getResult()).get("userName")));
                                currentUserAPI.setUserStoreName(String.valueOf(Objects.requireNonNull(task.getResult()).get("userStoreName")));
                                currentUserAPI.setUserPhoneNumber(String.valueOf(Objects.requireNonNull(task.getResult().get("userPhoneNumber"))));
                                currentUserAPI.setUserEmailAddress(String.valueOf(Objects.requireNonNull(task.getResult().get("userMailID"))));
                                progressLogin.setVisibility(View.INVISIBLE);
                                startActivity(new Intent(getActivity(), HomeScreenActivity.class));
                            }
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Error in db task successful", Toast.LENGTH_SHORT).show();
                    });
//              Querying using full collections
//                    collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()){
//                                for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
//                                    if (documentSnapshot.exists() && documentSnapshot.get("userId").equals(user)) {
//                                        Log.d(TAG, "onEvent: "+documentSnapshot.get("userName"));
//                                        CurrentUserAPI currentUserAPI= CurrentUserAPI.getInstance();
//                                        currentUserAPI.setUserId(String.valueOf(documentSnapshot.get("userId")));
//                                        currentUserAPI.setUserName(String.valueOf(documentSnapshot.get("userName")));
//                                        currentUserAPI.setUserStoreName(String.valueOf(documentSnapshot.get("userStoreName")));
//                                        progressLogin.setVisibility(View.INVISIBLE);
//                                        Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();
//                                        startActivity(new Intent(getActivity(), HomeScreenActivity.class));
//
//                                    }
//                                }
//                            }
//                        }
//                    }).addOnFailureListener(e -> {
//                        Toast.makeText(getContext(), "Error in db task successful", Toast.LENGTH_SHORT).show();
//
//                    });
                } else {
                    Toast.makeText(getContext(), "Error in task successful", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: error in failure of sign in" + e.getMessage());
            }
        });
    }

}