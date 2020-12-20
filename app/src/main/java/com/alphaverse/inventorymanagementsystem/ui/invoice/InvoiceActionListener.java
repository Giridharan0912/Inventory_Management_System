package com.alphaverse.inventorymanagementsystem.ui.invoice;

import android.os.Bundle;

public interface InvoiceActionListener {
    String ACTION_KEY = "action_key";
    int ACTION_VALUE = 25;
    String INVOICE_KEY = "KEY_INVOICE";

    void onInvoiceCreated(Bundle bundle);
}
