package com.thirteensecondstoburn.CasinoPractice.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;
import com.thirteensecondstoburn.CasinoPractice.GooglePlay.IGoogleServices;
import com.thirteensecondstoburn.CasinoPractice.android.util.IabHelper;

public class AndroidLauncher extends AndroidApplication {
    private GoogleServices googleServices;
    /* RequestCode for resolutions involving sign-in */
    private static final int RC_SIGN_IN = 1;
    private InternalApplicationBilling billing;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        googleServices = new GoogleServices(this);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        billing = new InternalApplicationBilling(this, this);
		initialize(new CasinoPracticeGame(googleServices, billing), config);
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("mGoogleApiClient.connect()");
        googleServices.signInNoResolve();
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("mGoogleApiClient.disconnect()");
        googleServices.signOut();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        // let the internal billing helper try to handle the intent
        if(billing != null && billing.getIabHelper() != null) {
            IabHelper iabHelper = billing.getIabHelper();
            if(!iabHelper.handleActivityResult(requestCode, resultCode, data)) {
                super.onActivityResult(requestCode, resultCode, data);
            } else {
                Log.d("AndroidLauncher", "onActivityResult handled by IABUtil.");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                googleServices.shouldResolve = false;
            }

            googleServices.isResolving = false;
            googleServices.signIn();
        }
    }
}
