package com.thirteensecondstoburn.CasinoPractice.android;

import android.app.Activity;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.thirteensecondstoburn.CasinoPractice.GooglePlay.IGoogleServices;

/**
 * Created by Nick on 11/3/2015.
 */
public class GoogleServices implements IGoogleServices,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    /* Client for accessing Google APIs */
    private GoogleApiClient googleApiClient;
    /* Is there a ConnectionResult resolution in progress? */
    public boolean isResolving = false;
    /* Should we automatically resolve ConnectionResults when possible? */
    public boolean shouldResolve = false;
    /* RequestCode for resolutions involving sign-in */
    private static final int RC_SIGN_IN = 1;

    private static final String TAG = "GoogleServices";

    private Activity context;

    public GoogleServices(Activity context) {
        this.context = context;
        // Build GoogleApiClient with access to basic profile
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();
    }

    @Override
    public void signIn() {
        if(!isConnected() && !isConnecting()) {
            shouldResolve = true;
            googleApiClient.connect();
        }
    }

    public void signInNoResolve() {
        if(!isConnected() && !isConnecting()) {
            shouldResolve = false;
            googleApiClient.connect();
        }
    }

    @Override
    public void signOut() {
        if(isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void changeUser() {
        if(isConnected()) {
            Plus.AccountApi.clearDefaultAccount(googleApiClient);
            signOut();
            signIn();
        }
    }

    @Override
    public boolean isConnected() {
        return googleApiClient.isConnected();
    }

    @Override
    public boolean isConnecting() {
        return googleApiClient.isConnecting();
    }

    @Override
    public void onConnected(Bundle bundle) {
        // onConnected indicates that an account was selected on the device, that the selected
        // account has granted any requested permissions to our app and that we were able to
        // establish a service connection to Google Play services.
        Log.d(TAG, "onConnected:" + bundle);
        Person currentPerson = Plus.PeopleApi.getCurrentPerson(googleApiClient);

        if(currentPerson != null) {
            Log.d(TAG, currentPerson.getDisplayName());
            Log.d(TAG, Plus.AccountApi.getAccountName(googleApiClient));
        } else {
            Log.e(TAG, "Whoa, no Person??");
            Log.e(TAG, "Connected? " + isConnected());
        }
        shouldResolve = false;
    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost. The GoogleApiClient will automatically
        // attempt to re-connect. Any UI elements that depend on connection to Google APIs should
        // be hidden or disabled until onConnected is called again.
        Log.d(TAG, "onConnectionSuspended:" + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Could not connect to Google Play Services.  The user needs to select an account,
        // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
        // ConnectionResult to see possible error codes.
        if (!isResolving && shouldResolve) {
            if (connectionResult.hasResolution()) {

                Log.d(TAG, "HAS RESOLUTION");
                try {
                    connectionResult.startResolutionForResult(context, RC_SIGN_IN);
                    isResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.d(TAG, "Could not resolve ConnectionResult.", e);
                    isResolving = false;
                    signIn();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.
                //showErrorDialog(connectionResult);
                Log.d(TAG, "NO RESOLUTION: " + connectionResult);
            }
        } else {
            // Show the signed-out UI
            //showSignedOutUI();
            Log.d(TAG, "WHAT? DIDN'T EVEN TRY??");
        }
    }
}
