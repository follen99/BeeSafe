package com.example.beesafe.utils;

import com.example.beesafe.model.User;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.digest.DigestUtils;

/**
 * classe usata per gestire il Digest
 */
public class DigestHelper {

    /**
     * metodo statico che contatta il database e genera il digest
     * @return il digest generato a partire dalla password dell'utente ed un segreto comune tra client e server
     */
    public static String getDigestFromCurrentUser(){
        FirebaseUserHelper helper = FirebaseUserHelper.getInstance();

        User currentUser = helper.getUserReference();

        return DigestUtils.md5Hex( currentUser.getPassword() + Constants.SECRET).toUpperCase();
    }
}
