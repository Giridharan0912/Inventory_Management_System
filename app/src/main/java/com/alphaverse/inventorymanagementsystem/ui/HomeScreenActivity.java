package com.alphaverse.inventorymanagementsystem.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import com.alphaverse.inventorymanagementsystem.R;
import com.alphaverse.inventorymanagementsystem.ui.auth.AuthenticationActivity;
import com.alphaverse.inventorymanagementsystem.ui.invoice.InvoiceActivity;
import com.alphaverse.inventorymanagementsystem.util.CurrentUserAPI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeScreenActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    public static final int REQ_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Button btn=findViewById(R.id.btn_logout);
        AppCompatButton btnInvoice=findViewById(R.id.btn_invoice);
        Button button=findViewById(R.id.btn_stock);

        firebaseAuth=FirebaseAuth.getInstance();
        currentUser=firebaseAuth.getCurrentUser();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentUserAPI currentUserAPI=CurrentUserAPI.getInstance();
                Toast.makeText(getApplicationContext(),currentUserAPI.getUserName(), Toast.LENGTH_SHORT).show();
            }
        });
        if (ActivityCompat.checkSelfPermission(HomeScreenActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeScreenActivity.this, new String[]{Manifest.permission.CAMERA}, REQ_CODE);
        }
        btn.setOnClickListener(v -> {
            if (currentUser != null && firebaseAuth != null) {
                firebaseAuth.signOut();
                startActivity(new Intent(HomeScreenActivity.this, AuthenticationActivity.class));
                finish();
            }
        });
        btnInvoice.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), InvoiceActivity.class));
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(HomeScreenActivity.this, "permitted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(HomeScreenActivity.this, "not permitted", Toast.LENGTH_SHORT).show();
        }
    }
}