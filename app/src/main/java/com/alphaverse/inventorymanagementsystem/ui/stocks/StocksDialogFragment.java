package com.alphaverse.inventorymanagementsystem.ui.stocks;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.alphaverse.inventorymanagementsystem.R;
import com.alphaverse.inventorymanagementsystem.model.Stocks;

import static com.alphaverse.inventorymanagementsystem.ui.stocks.StocksFragment.REQ_CODE_WITH_STOCK;

public class StocksDialogFragment extends DialogFragment {
    private static final String TAG = "StockDialogFragment";
    public CreateStock createStock;
    private View view;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            createStock = (CreateStock) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage());
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.stocks_item, container, false);
        EditText etStock = view.findViewById(R.id.stocks_et);
        EditText etquantity = view.findViewById(R.id.et_stocks_quantity);
        Button btnOk = view.findViewById(R.id.btn_ok_stocks);
        Button btnDismiss = view.findViewById(R.id.btn_dismiss_stocks);
        if (getTargetRequestCode() == REQ_CODE_WITH_STOCK) {
            btnOk.setOnClickListener(v -> {
                String name = etStock.getText().toString();
                String nos = etquantity.getText().toString();
                Stocks stocks = new Stocks(name, nos);
                createStock.createStock(stocks);
                getDialog().dismiss();
            });
            btnDismiss.setOnClickListener(v -> {
                getDialog().dismiss();
            });
        }
        return view;
    }

    public interface CreateStock {
        void createStock(Stocks stocks);
    }
}
