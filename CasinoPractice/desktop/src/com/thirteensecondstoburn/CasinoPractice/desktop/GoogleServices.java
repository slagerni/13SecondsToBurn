package com.thirteensecondstoburn.CasinoPractice.desktop;

import com.badlogic.gdx.Gdx;
import com.thirteensecondstoburn.CasinoPractice.GooglePlay.IGoogleServices;

/**
 * Created by Nick on 11/6/2015.
 */
public class GoogleServices implements IGoogleServices {
    private final String TAG = "DesktopGoogleServices";
    @Override
    public void signIn() {
        Gdx.app.log(TAG, "WOULD SIGN IN HERE");
    }

    @Override
    public void signOut() {
        // stub
        Gdx.app.log(TAG, "WOULD SIGN OUT HERE");
    }

    @Override
    public void changeUser() {
        // stub
        Gdx.app.log(TAG, "WOULD SIGN-OUT, CLEAR, SIGN-IN");
    }

    @Override
    public boolean isConnected() {
        // stub
        Gdx.app.log(TAG, "CHECKING IF WE'RE CONNECTED");
        return true;
    }

    @Override
    public boolean isConnecting() {
        // stub
        return false;
    }
}
