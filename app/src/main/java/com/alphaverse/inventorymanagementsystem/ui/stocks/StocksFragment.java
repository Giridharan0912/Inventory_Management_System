package com.alphaverse.inventorymanagementsystem.ui.stocks;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alphaverse.inventorymanagementsystem.R;
import com.alphaverse.inventorymanagementsystem.model.Stocks;
import com.alphaverse.inventorymanagementsystem.util.CurrentUserAPI;
import com.alphaverse.inventorymanagementsystem.viewmodel.InventoryViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class StocksFragment extends Fragment implements StocksDialogFragment.CreateStock {
    public static final int REQ_CODE_WITH_STOCK = 32;
    public static final String TAG = "StocksFragment";
    private final Context context;
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private View stocksView;
    private RecyclerView stocksRecyclerView;
    private StocksRecyclerViewAdapter stocksRecyclerViewAdapter;
    private ImageButton addStocks;
    private InventoryViewModel inventoryViewModel;
    private ArrayList<Stocks> stocksArrayList;

    StocksFragment(Context context) {

        this.context = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        stocksView = inflater.inflate(R.layout.fragment_stocks, container, false);
        stocksRecyclerView = stocksView.findViewById(R.id.stocks_rv);
        stocksArrayList = new ArrayList<>();
//        TODO: initialize viewmodel
        inventoryViewModel = new ViewModelProvider(this).get(InventoryViewModel.class);
        stocksRecyclerView.setHasFixedSize(true);
        stocksRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        initUI();
        return stocksView;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        getStocks();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private void getStocks() {
        stocksArrayList.clear();
        CollectionReference collectionReference = firestore.collection("Users").document(CurrentUserAPI.getInstance().getUserId()).collection("Stocks");
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                Stocks stocks = snapshot.toObject(Stocks.class);
                                stocksArrayList.add(stocks);
                                Log.d(TAG, "onSuccess: " + stocks.getStockName());
                            }

                            stocksRecyclerViewAdapter = new StocksRecyclerViewAdapter(context, stocksArrayList);
                            stocksRecyclerView.setAdapter(stocksRecyclerViewAdapter);
                            stocksRecyclerViewAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, "empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void initUI() {
        addStocks = stocksView.findViewById(R.id.imageButton_stock);
        addStocks.setOnClickListener(v -> {
            StocksDialogFragment stocksDialogFragment = new StocksDialogFragment();
            stocksDialogFragment.setTargetFragment(StocksFragment.this, REQ_CODE_WITH_STOCK);
            stocksDialogFragment.show(getParentFragmentManager(), "create stocks");
        });
    }

    @Override
    public void createStock(Stocks stocks) {
        inventoryViewModel.insertStocks(stocks);
        stocksRecyclerViewAdapter.notifyDataSetChanged();
        getStocks();
    }

}