package com.thirteensecondstoburn.CasinoPractice.android;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.thirteensecondstoburn.CasinoPractice.GooglePlay.BillingException;
import com.thirteensecondstoburn.CasinoPractice.GooglePlay.IInternalApplicationBilling;
import com.thirteensecondstoburn.CasinoPractice.GooglePlay.IPurchaseListener;
import com.thirteensecondstoburn.CasinoPractice.GooglePlay.ProductDetails;
import com.thirteensecondstoburn.CasinoPractice.GooglePlay.PurchaseDetails;
import com.thirteensecondstoburn.CasinoPractice.android.util.IabException;
import com.thirteensecondstoburn.CasinoPractice.android.util.IabHelper;
import com.thirteensecondstoburn.CasinoPractice.android.util.IabResult;
import com.thirteensecondstoburn.CasinoPractice.android.util.Inventory;
import com.thirteensecondstoburn.CasinoPractice.android.util.Purchase;
import com.thirteensecondstoburn.CasinoPractice.android.util.SkuDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 3/23/2016.
 */
public class InternalApplicationBilling implements IInternalApplicationBilling {
    private IabHelper iabHelper;
    private Context context;
    private Activity activity;
    private String TAG = "Internal Billing";
    private boolean isSetup = false;
    private String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAh8a5M1+yC6ZBPXB/13Qgi7HxT6yL8Ny/mVgO5tuUdoPmAObMrC10W6ZW6hjQrO16CrbPpP8vGYIslVmniN8KLlqYmNIm4zHCU+u1KlOCY0NnTgtjOFRaJxspv/MXR6V9n0PGsh9bJxM90ToLdxKw5Y6MH4Qpa3/uWT6LQ8PuR6/dOakV5GOisiZ87Yr4Nz/WdpqVyWGWJ1PpmyM9HquTjyNcauQaS85dCe800+ivGii/dn7SusEdlV1ypou+niHBdMT2Tau3KkhZGdyvngdtDtazgD+L6JWXEyVmBgoSHii4iTFh9gG85qTKxZ1G0E70oIUCjamejJGlnbqeEBmuuwIDAQAB";

    public IabHelper getIabHelper() {return iabHelper;}

    public InternalApplicationBilling(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    public void create() {
        System.out.println("Creating In App Billing Helper");

        iabHelper = new IabHelper(context, pubKey);
        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    Log.d(TAG, "Problem setting up In-app Billing: " + result);
                }
                // Hooray, IAB is fully set up!
                isSetup = true;
            }
        });
    }

    @Override
    public void dispose() {
        System.out.println("Disposing In App Billing Helper");
        if(iabHelper != null) iabHelper.dispose();
        iabHelper = null;
        isSetup = false;
    }

    @Override
    public boolean isSetup() {
        return isSetup;
    }

    @Override
    public void beginPurchase(String sku, final IPurchaseListener purchaseListener) {
        if(iabHelper == null || !isSetup) return;

        iabHelper.launchPurchaseFlow(activity, sku, 42042, new IabHelper.OnIabPurchaseFinishedListener() {
            @Override
            public void onIabPurchaseFinished(IabResult result, Purchase info) {
                // at this point we've actually tried to purchase something
                if(result.isFailure()) {
                    PurchaseDetails details = new PurchaseDetails(info.getSku(), result.isSuccess(), result.getMessage(), false);
                    purchaseListener.onPurchased(details);
                }

                if(info.getSku().equals(BillingProduct.SKU_CHIPS_5000.toString())
                        || info.getSku().equals(BillingProduct.SKU_CHIPS_25000.toString())
                        || info.getSku().equals(BillingProduct.SKU_CHIPS_100000.toString())
                        || info.getSku().equals(BillingProduct.SKU_CHIPS_500000.toString())
                        || info.getSku().equals(BillingProduct.SKU_CHIPS_5000000.toString())
                ) {
                    // consumable product and the purchase was successful
                    iabHelper.consumeAsync(info, new IabHelper.OnConsumeFinishedListener() {
                        @Override
                        public void onConsumeFinished(Purchase purchase, IabResult result) {
                            PurchaseDetails consumedDetails = new PurchaseDetails(purchase.getSku(), result.isSuccess(), result.getMessage(), result.isSuccess());
                            purchaseListener.onConsumed(consumedDetails);
                        }
                    });
                } else {
                    PurchaseDetails details = new PurchaseDetails(info.getSku(), result.isSuccess(), result.getMessage(), false);
                    purchaseListener.onPurchased(details);
                }
            }
        });
    }

    @Override
    public List<ProductDetails> listProductDetails() throws BillingException {
        ArrayList<String> allProductList = new ArrayList<>();
        allProductList.add(BillingProduct.SKU_CHIPS_5000.toString());
        allProductList.add(BillingProduct.SKU_CHIPS_25000.toString());
        allProductList.add(BillingProduct.SKU_CHIPS_100000.toString());
        allProductList.add(BillingProduct.SKU_CHIPS_500000.toString());
        allProductList.add(BillingProduct.SKU_CHIPS_5000000.toString());

        try {
            Inventory inv = iabHelper.queryInventory(true, allProductList);
            ArrayList<ProductDetails> productDetails = new ArrayList<>();
            for (String sku : allProductList) {
                SkuDetails skuDetails = inv.getSkuDetails(sku);
                productDetails.add(new ProductDetails(skuDetails.getSku(), skuDetails.getType(), skuDetails.getPrice(), skuDetails.getTitle(), skuDetails.getDescription()));
            }
            return productDetails;
        } catch (IabException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new BillingException(e.getResult().getMessage());
        }
    }
}
