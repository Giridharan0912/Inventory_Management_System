package com.alphaverse.inventorymanagementsystem.ui.stocks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alphaverse.inventorymanagementsystem.R;
import com.alphaverse.inventorymanagementsystem.model.Stocks;

import java.util.List;

public class StocksRecyclerViewAdapter extends RecyclerView.Adapter<StocksRecyclerViewAdapter.StocksViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private final List<Stocks> stocksList;

    public StocksRecyclerViewAdapter(Context context, List<Stocks> stocksList) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.stocksList = stocksList;
    }


    @NonNull
    @Override
    public StocksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.stocks_card_view, parent, false);
        return new StocksViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull StocksViewHolder holder, int position) {
        Stocks current = stocksList.get(position);
        holder.stockTv.setText(current.getStockName());
        holder.quantityTv.setText(current.getStockNos());

    }


    @Override
    public int getItemCount() {
        return stocksList.size();
    }

    public class StocksViewHolder extends RecyclerView.ViewHolder {
        private final TextView stockTv;
        private final TextView quantityTv;
        private final Context ctx;

        public StocksViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            ctx = context;
            stockTv = itemView.findViewById(R.id.stock_name);
            quantityTv = itemView.findViewById(R.id.stock_nos);
        }

    }
}
