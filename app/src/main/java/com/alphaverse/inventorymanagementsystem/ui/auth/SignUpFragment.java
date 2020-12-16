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
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class SignUpFragment extends Fragment {
    private EditText etSignUpName, etSignUpStoreName, etSignUpPhNo, etSignUpEmail, etSignUpOtp;
    private TextView tvLogin;
    private Button btnSignUpOtp, btnSignUp;
    private View signUpView;
    private ProgressBar signUpProgress;
    private ActionListener actionListener;
    private final String TAG = getClass().getSimpleName();
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private CollectionReference collectionReference = fireStore.collection("Users");
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    ArrayList<String> mobileNoArray = new ArrayList<>();

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        fireStore.collection("Users").get().addOnCompleteListener(task -> {
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
        // Inflate the layout for this fragment
        signUpView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        etSignUpName = signUpView.findViewById(R.id.et_signup_name);
        etSignUpStoreName = signUpView.findViewById(R.id.et_signup_store_name);
        etSignUpPhNo = signUpView.findViewById(R.id.et_signup_phone);
        etSignUpEmail = signUpView.findViewById(R.id.et_signup_email);
        etSignUpOtp = signUpView.findViewById(R.id.et_signup_otp);
        btnSignUpOtp = signUpView.findViewById(R.id.btn_signup_otp);
        btnSignUp = signUpView.findViewById(R.id.btn_signup);
        tvLogin = signUpView.findViewById(R.id.tv_loginIn);
        signUpProgress=signUpView.findViewById(R.id.progress_signUp);
        btnSignUpOtp.setOnClickListener(v -> {
            String phNo = etSignUpPhNo.getText().toString().trim();
            if (phNo.length() == 10) {
                sentVerificationCode(phNo);
            } else {
                Toast.makeText(getContext(), "Enter the valid number", Toast.LENGTH_SHORT).show();
            }
        });
        btnSignUp.setOnClickListener(v -> {
            String otp = etSignUpOtp.getText().toString().trim();
            if (otp.length() == 6) {
                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId, otp);
                signUpProgress.setVisibility(View.VISIBLE);
                signIn(phoneAuthCredential);
            } else {
                Toast.makeText(getContext(), "Enter the correct otp number", Toast.LENGTH_SHORT).show();
            }
        });
        tvLogin.setOnClickListener(v -> {
            actionListener.onLoginClicked();
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
        return signUpView;

    }

    public void setActionListener(Context context) {
        this.actionListener = (ActionListener) context;
    }

    private void sentVerificationCode(String phNo) {
//       Log.d(TAG, "sentVerificationCode: "+mobileNoArray.size());
        if (!mobileNoArray.contains(phNo)) {
            PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                    .setPhoneNumber("+91" + phNo)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(getActivity())
                    .setCallbacks(mCallbacks)
                    .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        } else {
            Toast.makeText(getContext(), "Already existed", Toast.LENGTH_SHORT).show();
        }
    }

    private void signIn(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: " + task.getResult().getUser().getPhoneNumber());
                    currentUser = firebaseAuth.getCurrentUser();
                    final String currentUserId = currentUser.getUid();
                    HashMap<String, String> userDetails = new HashMap<>();
                    String name = etSignUpName.getText().toString().trim();
                    String userMailID = etSignUpEmail.getText().toString().trim();
                    String userMobile = etSignUpPhNo.getText().toString().trim();
                    String userStore=etSignUpStoreName.getText().toString().trim();
                    userDetails.put("userId", currentUserId);
                    userDetails.put("userName", name);
                    userDetails.put("userMailID", userMailID);
                    userDetails.put("userPhoneNumber", userMobile);
                    userDetails.put("userStoreName",userStore);

                    collectionReference.document(currentUserId).set(userDetails).addOnSuccessListener(documentReference -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: db entry success" + documentReference);
                            CurrentUserAPI currentUserAPI = CurrentUserAPI.getInstance();
                            currentUserAPI.setUserId(currentUserId);
                            currentUserAPI.setUserName(name);
                            currentUserAPI.setUserStoreName(userStore);
                            signUpProgress.setVisibility(View.INVISIBLE);
                            Toast.makeText(getContext(), "SignUp successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), HomeScreenActivity.class));
                        }
                    }).addOnFailureListener(e -> {
                        Log.d(TAG, "onComplete:db entry failure " + e.getMessage());
                    });
                } else {

                    Log.d(TAG, "onComplete: SignUp error");
                }

            }
        });
    }

}