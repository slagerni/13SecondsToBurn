package com.thirteensecondstoburn.CasinoPractice.GooglePlay;

/**
 * Created by Nick on 11/3/2015.
 */
public interface IGoogleServices {
    void signIn();
    void signOut();
    void changeUser();
    boolean isConnected();
    boolean isConnecting();
}
