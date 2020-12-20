package com.alphaverse.inventorymanagementsystem.controller;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alphaverse.inventorymanagementsystem.util.CurrentUserAPI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Firebase extends Application {
    private static final String TAG = "Firebase";
    private static Firebase firebaseInstance;
    private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private final CurrentUserAPI currentUserAPI = CurrentUserAPI.getInstance();

    public static Firebase getInstance() {
        if (firebaseInstance == null) {
            firebaseInstance = new Firebase();
        }
        return firebaseInstance;
    }


    public void sendBillToDatabase(String sendUrl, String customerNumber, int total, String customerName) {
        Date currentTime = Calendar.getInstance().getTime();
        HashMap<String, String> customerDetails = new HashMap<>();
        customerDetails.put("CustomerName", customerName);
        customerDetails.put("CustomerNumber", customerNumber);
        CollectionReference invoiceCollectionReference = fireStore.collection("Users").document(currentUserAPI.getUserId()).collection("Customers").document(customerNumber).collection("Invoice");
        Task<Void> documentReference = fireStore.collection("Users").document(currentUserAPI.getUserId())
                .collection("Customers").document(customerNumber).set(customerDetails).addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "sendBillToDatabase: success ");
                    HashMap<String, String> addInvoice = new HashMap<>();
                    addInvoice.put("DayGenerated", currentTime.toString());
                    addInvoice.put("url", sendUrl);
                    addInvoice.put("BillAmount", String.valueOf(total));
                    invoiceCollectionReference.document(currentTime.toString()).set(addInvoice).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess:fully completed ");
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure:collection failed ");
                        }
                    });
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: document failed");
                    }
                });
    }

    public void insertStocks(String stockName, String stockNos) {
        CollectionReference collectionReference = fireStore.collection("Users").document(currentUserAPI.getUserId())
                .collection("Stocks");
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("StockName", stockName);
        hashMap.put("StockNos", stockNos);
        collectionReference.document(stockName).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: " + stockName);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

//    public MutableLiveData<List<Stocks>> getStocks() {
//        List<Stocks> list = null;
//        CollectionReference collectionReference = fireStore.collection("Users").document(currentUserAPI.getUserId())
//                .collection("Stocks");
//        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                        String name = String.valueOf(documentSnapshot.get("StockName"));
//                        String nos = String.valueOf(documentSnapshot.get("StockNos"));
//                        Stocks stocks = new Stocks(name, nos);
//                        list.add(stocks);
//                    }
//                    stocksLiveData.setValue(list);
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
//        return stocksLiveData;
//    }


}
