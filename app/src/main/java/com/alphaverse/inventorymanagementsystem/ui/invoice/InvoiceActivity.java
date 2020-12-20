package com.alphaverse.inventorymanagementsystem.ui.invoice;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alphaverse.inventorymanagementsystem.R;

/**
 * This method provide the all the fragments related to the invoice generation
 */
public class InvoiceActivity extends AppCompatActivity implements InvoiceActionListener {
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        fragmentManager = getSupportFragmentManager();
        initInvoice();
    }

    /**
     * This method will call invoice fragment
     */
    private void initInvoice() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ScannerFragment scannerFragment = new ScannerFragment();
        scannerFragment.setInvoiceActionListener(this);
        fragmentTransaction.add(R.id.frame_invoice, scannerFragment).commit();
    }

    /**
     * This method will call bill fragment
     *
     * @param bundle
     */
    private void initBill(Bundle bundle) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        BillFragment billFragment = new BillFragment();
        billFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame_invoice, billFragment).commit();
    }

    /**
     * @param bundle
     */

    @Override
    public void onInvoiceCreated(Bundle bundle) {
        int actionPerformed = bundle.getInt(InvoiceActionListener.ACTION_KEY);
        if (actionPerformed == InvoiceActionListener.ACTION_VALUE) {
            initBill(bundle);
        }
    }
}