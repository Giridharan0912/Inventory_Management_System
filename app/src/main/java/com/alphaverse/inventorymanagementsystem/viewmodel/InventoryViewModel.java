package com.alphaverse.inventorymanagementsystem.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.alphaverse.inventorymanagementsystem.controller.Repository;
import com.alphaverse.inventorymanagementsystem.model.Stocks;

import java.util.ArrayList;

public class InventoryViewModel extends AndroidViewModel {
    private final Repository repository;
    MutableLiveData<ArrayList<Stocks>> stocksLiveData;

    public InventoryViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public void insertStocks(Stocks stocks) {
        repository.insertStocks(stocks.getStockName(), stocks.getStockNos());
    }

}
