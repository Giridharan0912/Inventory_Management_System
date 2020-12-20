package com.alphaverse.inventorymanagementsystem.ui.invoice;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.print.PDFPrint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.alphaverse.inventorymanagementsystem.R;
import com.alphaverse.inventorymanagementsystem.controller.Repository;
import com.alphaverse.inventorymanagementsystem.model.Invoice;
import com.alphaverse.inventorymanagementsystem.util.CurrentUserAPI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tejpratapsingh.pdfcreator.utils.FileManager;
import com.tejpratapsingh.pdfcreator.utils.PDFUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//TODO: Open a dialog box and get customer details
public class BillFragment extends Fragment implements CustomerDialogFragment.GetCustomerDetails {
    public static final int REQ_CODE_FOR_CUSTOMER = 75;
    public final String TAG = getClass().getSimpleName();
    private final CurrentUserAPI currentUserAPI = CurrentUserAPI.getInstance();
    private final FirebaseStorage firebaseStorage;
    private final Repository repository;
    private View billView;
    private Bundle bundle;
    private ArrayList<Invoice> invoiceArrayList;
    private WebView billWebView;
    private StorageReference invoiceReference;
    private UploadTask invoiceUpload;
    private String sendUrl;
    private Button sendBtn;
    private String customerName;
    private String customerNumber;
    private int total;


    public BillFragment() {
        firebaseStorage = FirebaseStorage.getInstance();
        repository = new Repository();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        bundle = getArguments();
        invoiceArrayList = (ArrayList<Invoice>) bundle.getSerializable("InvoiceBill");
        for (Invoice invoice : invoiceArrayList) {
            total += invoice.getPrice();
        }
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        billView = inflater.inflate(R.layout.fragment_bill, container, false);
        billWebView = billView.findViewById(R.id.bill_web_view);
        sendBtn = billView.findViewById(R.id.btn_send_bill);
        Toolbar toolbar = billView.findViewById(R.id.toolbar_bill);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        new Handler(Looper.getMainLooper()).postDelayed(this::initBill, 500);
        return billView;
    }

    /**
     * initBill will initialize fragment with webView loaded with htmlContent which is a bill
     */
    private void initBill() {
        BillHtml billHtml = new BillHtml(invoiceArrayList);
        String html = billHtml.bill();
        billWebView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
    }

    @Override
    public void onResume() {
        super.onResume();
        webViewToPdf();
        /** Call repository and upload the bill to db
         * inside the user document ->INVOICE collection of user -> generate document with
         * CUSTOMER NAME&NUMBER -> generate document
         * with id=date&time with fields day generated,bill amt
         * */
        sendBtn.setOnClickListener(v -> {
            CustomerDialogFragment customerDialogFragment = new CustomerDialogFragment();
            customerDialogFragment.setTargetFragment(BillFragment.this, REQ_CODE_FOR_CUSTOMER);
            customerDialogFragment.show(getFragmentManager(), "create customer");
        });
    }

    /**
     * Convert webView to pdf and put in firebase storage then Get the url from the firebase storage and push to cloud fireStore
     */
    private void webViewToPdf() {
        final File savedPDFFile = FileManager.getInstance().createTempFile(getActivity(), "pdf", false);
        new Handler(Looper.getMainLooper()).postDelayed(() -> PDFUtil.generatePDFFromWebView(savedPDFFile, billWebView, new PDFPrint.OnPDFPrintListener() {
            @Override
            public void onSuccess(File file) {
                // Open Pdf Viewer
                Uri pdfUri = Uri.fromFile(savedPDFFile);
                Date currentTime = Calendar.getInstance().getTime();
                String ref = currentUserAPI.getUserPhoneNumber().concat(currentTime.toString());
                invoiceReference = firebaseStorage.getReference().child(currentUserAPI.getUserPhoneNumber()).child(ref);
                Log.d(TAG, "onSuccess: " + invoiceReference.getName());
                invoiceUpload = (UploadTask) invoiceReference.putFile(pdfUri).addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete()) ;
                    Uri url = uriTask.getResult();
                    Log.d(TAG, "onSuccess: " + url.toString());
                    sendUrl = url.toString();

                }).addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.getMessage())).addOnProgressListener(snapshot -> {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    Log.d(TAG, "onProgress: " + progress);
                });
            }

            @Override
            public void onError(Exception exception) {
                exception.printStackTrace();
            }
        }), 1500);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bill, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void getCustomerDetails(String name, String number) {
        customerNumber = number;
        repository.insertDataInvoice(sendUrl, customerNumber, total, name);
    }
}