package com.alphaverse.inventorymanagementsystem.ui.invoice;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.alphaverse.inventorymanagementsystem.R;
import com.alphaverse.inventorymanagementsystem.model.Invoice;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.Objects;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * This scanner fragment will scan the barcode from the physical product of the users
 * if so will opted to bill fragment to get bill of the scanned product
 */
public class ScannerFragment extends Fragment implements ZXingScannerView.ResultHandler, InvoiceDialogFragment.ResumeScanner {
    public static final int REQ_CODE_WITH_PRODUCT_DETAILS = 40;
    private ZXingScannerView scannerView;
    public static final int REQ_CODE_FOR_CREATE_NEW = 20;
    private final String TAG = getClass().getSimpleName();
    private View scanView;
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final CollectionReference productCollection = firestore.collection("Products");
    private final ArrayList<Invoice> invoiceArrayList;
    private InvoiceActionListener invoiceActionListener;


    public ScannerFragment() {
        invoiceArrayList = new ArrayList<Invoice>();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * Will open the camera and initialize with scanner to scan the products
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        scanView = inflater.inflate(R.layout.fragment_scanner, container, false);
        Toolbar toolbar = scanView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ViewGroup frameScanner = scanView.findViewById(R.id.frame);
        scannerView = new ZXingScannerView(getContext());
        frameScanner.addView(scannerView);
        return scanView;
    }

    public void setInvoiceActionListener(Context context) {
        this.invoiceActionListener = (InvoiceActionListener) context;

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_scanner, menu);
        super.onCreateOptionsMenu(menu, inflater);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scanner_add:
                InvoiceDialogFragment dialogFragment = new InvoiceDialogFragment();
                dialogFragment.setTargetFragment(ScannerFragment.this, REQ_CODE_FOR_CREATE_NEW);
                dialogFragment.show(getFragmentManager(), "create product");
                scannerView.stopCamera();
                break;
            case R.id.scanner_tick:

                Bundle bundle = new Bundle();
                bundle.putInt(InvoiceActionListener.ACTION_KEY, InvoiceActionListener.ACTION_VALUE);
                bundle.putSerializable("InvoiceBill", invoiceArrayList);
                invoiceActionListener.onInvoiceCreated(bundle);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: Scanner fragment is paused");
    }

    @Override
    public void onStop() {
        super.onStop();
        scannerView.stopCamera();
        Log.d(TAG, "onStop: Scanner fragment is stopped");
    }

    /**
     * This method will handle the results of the scanner camera will return the value of the scanned code
     * once the value is obtained it automatically intents to @param rawResult to show the product name and price.
     */
    @Override
    public void handleResult(Result rawResult) {
        Log.d(TAG, rawResult.getText()); // Prints scan results
        Toast.makeText(getContext(), rawResult.getText(), Toast.LENGTH_SHORT).show();
        Log.d(TAG, rawResult.getBarcodeFormat().toString());
        String barcode = rawResult.getText();
        productCollection.document(barcode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String productName = (String) Objects.requireNonNull(task.getResult()).get("ProductName");
                    String productPrice = (String) Objects.requireNonNull(task.getResult()).get("Price");
                    InvoiceDialogFragment dialogFragment = new InvoiceDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("ProductName", productName);
                    bundle.putString("ProductPrice", productPrice);
                    dialogFragment.setTargetFragment(ScannerFragment.this, REQ_CODE_WITH_PRODUCT_DETAILS);
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getFragmentManager(), "show product");
                    scannerView.stopCamera();

                }
            }
        }).addOnFailureListener(e -> {
            Log.d(TAG, "handleResult: " + e.getMessage());
        });


    }


    @Override
    public void resume(boolean b) {
        if (b) {
            scannerView.setResultHandler(this);
            scannerView.startCamera();
        }
    }

    @Override
    public void resume(boolean b, Invoice invoice) {
        if (b) {
            invoiceArrayList.add(invoice);
            scannerView.setResultHandler(this);
            scannerView.startCamera();
        }
    }
}