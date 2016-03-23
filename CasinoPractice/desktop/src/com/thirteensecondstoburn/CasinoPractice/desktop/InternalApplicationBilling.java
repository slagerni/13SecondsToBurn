package com.thirteensecondstoburn.CasinoPractice.desktop;

import com.thirteensecondstoburn.CasinoPractice.GooglePlay.IInternalApplicationBilling;

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
}
