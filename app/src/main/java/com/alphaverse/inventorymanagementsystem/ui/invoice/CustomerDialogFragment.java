package com.alphaverse.inventorymanagementsystem.ui.invoice;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.alphaverse.inventorymanagementsystem.R;

import static com.alphaverse.inventorymanagementsystem.ui.invoice.BillFragment.REQ_CODE_FOR_CUSTOMER;

/**
 * This customer dialog box will get the details of customer name and mobile number
 * and used to store the bill details
 */

public class CustomerDialogFragment extends DialogFragment {
    private View view;
    private GetCustomerDetails getCustomerDetails;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            getCustomerDetails = (GetCustomerDetails) getTargetFragment();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.create_customer_popup, container, false);
        EditText etCustomerName = view.findViewById(R.id.customer_name);
        EditText etCustomerNumber = view.findViewById(R.id.cutomer_number);
        Button btnOk = view.findViewById(R.id.btn_ok_customer);
        Button btnDismiss = view.findViewById(R.id.btn_dismiss_customer);
        if (getTargetRequestCode() == REQ_CODE_FOR_CUSTOMER) {
            btnOk.setOnClickListener(v -> {
                getCustomerDetails.getCustomerDetails(etCustomerName.getText().toString(), etCustomerNumber.getText().toString());
                getDialog().dismiss();
            });
            btnDismiss.setOnClickListener(v -> {
                getDialog().dismiss();
            });
        }
        return view;
    }

    public interface GetCustomerDetails {
        void getCustomerDetails(String name, String number);
    }
}
