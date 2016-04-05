package com.thirteensecondstoburn.CasinoPractice.GooglePlay;

/**
 * Created by Nick on 4/4/2016.
 */
public interface IPurchaseListener {
    void onPurchased(PurchaseDetails details);
    void onConsumed(PurchaseDetails details);
}
