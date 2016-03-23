package com.thirteensecondstoburn.CasinoPractice.android;

import android.content.Context;
import android.util.Log;

import com.thirteensecondstoburn.CasinoPractice.GooglePlay.IInternalApplicationBilling;
import com.thirteensecondstoburn.CasinoPractice.android.util.IabHelper;
import com.thirteensecondstoburn.CasinoPractice.android.util.IabResult;

/**
 * Created by Nick on 3/23/2016.
 */
public class InternalApplicationBilling implements IInternalApplicationBilling {
    private IabHelper _iabHelper;
    private Context _context;
    private String TAG = "Internal Billing";
    private boolean _isSetup = false;
    private String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAh8a5M1+yC6ZBPXB/13Qgi7HxT6yL8Ny/mVgO5tuUdoPmAObMrC10W6ZW6hjQrO16CrbPpP8vGYIslVmniN8KLlqYmNIm4zHCU+u1KlOCY0NnTgtjOFRaJxspv/MXR6V9n0PGsh9bJxM90ToLdxKw5Y6MH4Qpa3/uWT6LQ8PuR6/dOakV5GOisiZ87Yr4Nz/WdpqVyWGWJ1PpmyM9HquTjyNcauQaS85dCe800+ivGii/dn7SusEdlV1ypou+niHBdMT2Tau3KkhZGdyvngdtDtazgD+L6JWXEyVmBgoSHii4iTFh9gG85qTKxZ1G0E70oIUCjamejJGlnbqeEBmuuwIDAQAB";

    public InternalApplicationBilling(Context context) {
        _context = context;
    }

    @Override
    public void create() {
        System.out.println("Creating In App Billing Helper");

//        _iabHelper = new IabHelper(_context, pubKey);
//        _iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
//            public void onIabSetupFinished(IabResult result) {
//                if (!result.isSuccess()) {
//                    // Oh noes, there was a problem.
//                    Log.d(TAG, "Problem setting up In-app Billing: " + result);
//                }
//                // Hooray, IAB is fully set up!
//                _isSetup = true;
//            }
//        });
    }

    @Override
    public void dispose() {
        System.out.println("Disposing In App Billing Helper");
//        if(_iabHelper != null) _iabHelper.dispose();
//        _iabHelper = null;
    }

    @Override
    public boolean isSetup() {
        return _isSetup;
    }
}
