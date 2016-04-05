package com.thirteensecondstoburn.CasinoPractice.GooglePlay;

/**
 * Created by Nick on 4/4/2016.
 */
public class PurchaseDetails {
    public boolean isSuccess() {
        return isSuccess;
    }

    public String getSku() {
        return sku;
    }

    public String getMessage() {
        return message;
    }

    public boolean isConsumed() {
        return isConsumed;
    }

    private boolean isSuccess = false;
    private String sku;
    private String message;
    private boolean isConsumed = false;

    public PurchaseDetails(String sku, boolean isSuccess, String message, boolean isConsumed) {
        this.sku = sku;
        this.isSuccess = isSuccess;
        this.message = message;
        this.isConsumed = isConsumed;
    }
}
