package com.thirteensecondstoburn.CasinoPractice.GooglePlay;

import java.util.List;

/**
 * Created by Nick on 3/23/2016.
 */
public interface IInternalApplicationBilling {
    public enum BillingProduct {
        SKU_CHIPS_5000 ("chips5000", 5000),
        SKU_CHIPS_25000 ("chips25000", 25000),
        SKU_CHIPS_100000 ("chips100000", 100000),
        SKU_CHIPS_500000 ("chips500000", 500000),
        SKU_CHIPS_5000000 ("chips5000000", 5000000);

        private String sku;
        private double amount;

        BillingProduct(String sku, double amount) {
            this.sku = sku;
            this.amount = amount;
        }

        @Override
        public String toString() { return sku; }
        public double getAmount() {return amount; }
    }
    void create();
    void dispose();
    boolean isSetup();
    void beginPurchase(String sku, IPurchaseListener purchaseListener);

    /** Don't call this directly from a UI thread as everything will hang while it's getting the list */
    List<ProductDetails> listProductDetails() throws BillingException;
}
