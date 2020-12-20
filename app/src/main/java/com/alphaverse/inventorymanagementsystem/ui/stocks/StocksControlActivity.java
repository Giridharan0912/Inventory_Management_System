package com.alphaverse.inventorymanagementsystem.ui.stocks;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alphaverse.inventorymanagementsystem.R;

public class StocksControlActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks_control);
        fragmentManager = getSupportFragmentManager();
        initStocks();
    }

    private void initStocks() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        StocksFragment stocksFragment = new StocksFragment(getApplicationContext());
        fragmentTransaction.add(R.id.frame_stocks, stocksFragment).commit();
    }
}