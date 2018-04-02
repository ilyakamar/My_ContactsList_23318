package com.ilyakamar.my_contactslist_23318.utils;



import android.Manifest;

/**
 * Created by User on 23/03/2018.
 */

public class Init {

    public Init() {
    }


    // camera permissions
    public static final String [] PERMISSIONS= {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    // call permissions
    public static final String [] PHONE_PERMISSIONS = {Manifest.permission.CALL_PHONE};

    public static final int CAMERA_REQUEST_CODE = 5;
    public static final int PICKFILE_REQUEST_CODE = 8;
}