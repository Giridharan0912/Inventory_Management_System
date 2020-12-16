package com.alphaverse.inventorymanagementsystem.ui.invoice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.alphaverse.inventorymanagementsystem.R;

public class InvoiceActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        fragmentManager=getSupportFragmentManager();
        initInvoice();
    }
    private void initInvoice(){
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        ScannerFragment scannerFragment=new ScannerFragment();
        fragmentTransaction.add(R.id.frame_invoice,scannerFragment).commit();
    }
}