package com.alphaverse.inventorymanagementsystem.controller;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.alphaverse.inventorymanagementsystem.model.Stocks;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Repository {
    private static final String TAG = "Repository";
    private final Firebase firebase;
    private MutableLiveData<List<Stocks>> stocksLiveData;

    public Repository() {
        firebase = Firebase.getInstance();
    }

    public void insertDataInvoice(String sendUrl, String customerNumber, int total, String name) {
        Completable.fromAction(() ->
                firebase.sendBillToDatabase(sendUrl, customerNumber, total, name)).observeOn(Schedulers.io()).subscribeOn(Schedulers.single())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: completed");
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                        Log.d(TAG, "onError: " + e.getMessage());
                    }
                });
    }

    public void insertStocks(String stockName, String stockNos) {
        Completable.fromAction(() ->
                firebase.insertStocks(stockName, stockNos)).observeOn(Schedulers.io()).subscribeOn(Schedulers.single())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }


}
