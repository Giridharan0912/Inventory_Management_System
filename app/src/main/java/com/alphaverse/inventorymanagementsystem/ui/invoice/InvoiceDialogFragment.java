package com.alphaverse.inventorymanagementsystem.ui.invoice;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.alphaverse.inventorymanagementsystem.R;
import com.alphaverse.inventorymanagementsystem.model.Invoice;

import static com.alphaverse.inventorymanagementsystem.ui.invoice.ScannerFragment.REQ_CODE_FOR_CREATE_NEW;
import static com.alphaverse.inventorymanagementsystem.ui.invoice.ScannerFragment.REQ_CODE_WITH_PRODUCT_DETAILS;

/**
 * This dialog fragment box will be called during scanner fragment which gives the product name and product price so that user can customize
 */
public class InvoiceDialogFragment extends DialogFragment {
    private final String TAG = getClass().getSimpleName();
    private View view;
    public ResumeScanner resumeScanner;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            resumeScanner = (ResumeScanner) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage());
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.invoice_popup, container, false);
        EditText etProduct = view.findViewById(R.id.product_name);
        EditText etPrice = view.findViewById(R.id.product_price);
        Button btnDismiss = view.findViewById(R.id.btn_dismiss);
        Button btnOk = view.findViewById(R.id.btn_ok);
        TextView tvInvoice = view.findViewById(R.id.tv_invoice);
        Spinner quantitySpinner = view.findViewById(R.id.quantity_spinner);
        Integer[] quantityNo = {1, 2, 3, 4, 5, 6, 7, 8};
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, quantityNo);
        quantitySpinner.setAdapter(arrayAdapter);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        switch (getTargetRequestCode()) {
            case REQ_CODE_FOR_CREATE_NEW:
                Toast.makeText(getContext(), "Create", Toast.LENGTH_SHORT).show();
                btnOk.setOnClickListener(v -> {
                    String productName = etProduct.getText().toString();
                    int pricePerQuantity = Integer.parseInt(etPrice.getText().toString());
                    int quantity = Integer.parseInt(quantitySpinner.getItemAtPosition(quantitySpinner.getSelectedItemPosition()).toString());
                    int productPrice = pricePerQuantity * quantity;
                    Invoice invoice = new Invoice(productName, productPrice, quantity);
                    resumeScanner.resume(true, invoice);
                    getDialog().dismiss();
                });
                btnDismiss.setOnClickListener(v -> {
                    resumeScanner.resume(true);
                    getDialog().dismiss();
                });
                break;
            case REQ_CODE_WITH_PRODUCT_DETAILS:
                Bundle bundle = getArguments();
                String product = bundle.getString("ProductName");
                String price = bundle.getString("ProductPrice");
                etProduct.setText(product);
                etPrice.setText(price);
                Toast.makeText(getContext(), "update", Toast.LENGTH_SHORT).show();
                btnOk.setOnClickListener(v -> {
                    String productName = etProduct.getText().toString();
                    int pricePerQuantity = Integer.parseInt(etPrice.getText().toString());
                    int quantity = Integer.parseInt(quantitySpinner.getItemAtPosition(quantitySpinner.getSelectedItemPosition()).toString());
                    int productPrice = pricePerQuantity * quantity;
                    Invoice invoice = new Invoice(productName, productPrice, quantity);
                    resumeScanner.resume(true, invoice);
                    getDialog().dismiss();

                });
                btnDismiss.setOnClickListener(v -> {
                    resumeScanner.resume(true);
                    getDialog().dismiss();
                    bundle.clear();
                });
                break;

        }
        return view;
    }

    public interface ResumeScanner {
        void resume(boolean b);

        void resume(boolean b, Invoice invoice);
    }


}
