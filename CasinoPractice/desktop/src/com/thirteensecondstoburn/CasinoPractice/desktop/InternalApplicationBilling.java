package com.thirteensecondstoburn.CasinoPractice.desktop;

import com.thirteensecondstoburn.CasinoPractice.GooglePlay.BillingException;
import com.thirteensecondstoburn.CasinoPractice.GooglePlay.IInternalApplicationBilling;
import com.thirteensecondstoburn.CasinoPractice.GooglePlay.ProductDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 3/23/2016.
 */
public class InternalApplicationBilling implements IInternalApplicationBilling {
    @Override
    public void create() {
        System.out.println("FAKING CREATING THE INTERNAL APPLICATION BILLING");
    }

    @Override
    public void dispose() {
        System.out.println("FAKING DISPOSING THE INTERNAL APPLICATION BILLING");
    }

    @Override
    public boolean isSetup() {
        return true; // FAKING THAT THE BILLING IS SET UP
    }

    @Override
    public List<ProductDetails> listProductDetails() throws BillingException {
        // ok, no billing in the desktop, just fake our products here
        ArrayList<ProductDetails> products = new ArrayList<>();

        products.add(new ProductDetails(BillingProduct.SKU_CHIPS_5000.toString(), "inapp", "$1.00", "5,000 chips (Casino Practice)", "Fake 5,000 chip description"));
        products.add(new ProductDetails(BillingProduct.SKU_CHIPS_25000.toString(), "inapp", "$3.00", "25,000 chips (Casino Practice)", "Fake 25,000 chip description"));
        products.add(new ProductDetails(BillingProduct.SKU_CHIPS_100000.toString(), "inapp", "$10.00", "100,000 chips (Casino Practice)", "Fake 10,000 chip description"));
        products.add(new ProductDetails(BillingProduct.SKU_CHIPS_500000.toString(), "inapp", "$25.00", "500,000 chips (Casino Practice)", "Fake 500,000 chip description"));
        products.add(new ProductDetails(BillingProduct.SKU_CHIPS_5000000.toString(), "inapp", "$100.00", "5,000,000 chips (Casino Practice)", "Fake 5,000,000 chip description"));

        return products;
    }
}
