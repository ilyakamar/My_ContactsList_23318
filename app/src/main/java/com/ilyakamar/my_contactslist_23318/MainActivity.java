package com.ilyakamar.my_contactslist_23318;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ilyakamar.my_contactslist_23318.utils.UniversalImageLoader;
import com.ilyakamar.my_contactslist_23318.models.Contact;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity implements
        ViewContactsFragment.OnContactSelectedListener,
        ContactFragment.OnEditContactListener,
        ViewContactsFragment.OnAddContactListener{// START MainActivity


    private static final String TAG = "MainActivity";

    private static final int REQUEST_CODE = 1;


    @Override
    public void onEditcontactSelected(Contact contact) {// onEditcontactSelected

        Log.d(TAG, "OnContactSelected: contact selected from "
                + getString(R.string.edit_contact_fragment)
                + " " + contact.getName());

        EdditContactFragment fragment = new EdditContactFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.contact),contact);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.addToBackStack(getString((R.string.edit_contact_fragment)));
        transaction.commit();
    }// end onEditcontactSelected


    @Override
    public void OnContactSelected(Contact contact) {// OnContactSelected

        Log.d(TAG, "OnContactSelected: contact selected from "
                + getString(R.string.view_contacts_fragment)
                + " " + contact.getName());

        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.contact),contact);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.addToBackStack(getString((R.string.contact_fragment)));
        transaction.commit();
    }// end OnContactSelected



    @Override
    public void onAddContact() {// onAddContact

        Log.d(TAG, "onAddContact: navigating to :"+getString(R.string.add_contact_fragment));

        AddContactFragment fragment = new AddContactFragment();


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.addToBackStack(getString((R.string.add_contact_fragment)));
        transaction.commit();
    }// end onAddContact


    @Override
    protected void onCreate(Bundle savedInstanceState) {// start onCreate
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        init();

        initImageLoader();

    }// end onCreate


    /*
    * initialize the first fragment (ViewContactsFragment)
    * */
    private void init(){// init
        ViewContactsFragment fragment = new ViewContactsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // reaplce whatever is in the fragment_container view with this fragment
        // and add the transaction to the back stack so the user can navigate back

        transaction.replace(R.id.fragment_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }// end init ()


    private void initImageLoader(){ // initImageLoader
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(MainActivity.this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }// end initImageLoader()


    /**
     * Compress a bitmap by the @parm "quality"
     * Quality can be anywhere from 1-100: 100 being the highest quality
     * @param bitmap
     * @param quality
     * @return
     */
    public Bitmap compressBitmap(Bitmap bitmap,int quality){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,quality,stream);
        return bitmap;
    }

    /**
     *Generalized method for asking permission.Can pass any array of permissions
     * @param permissions
     */
    public void verifyPermissions(String [] permissions){// verifyPermissions
        Log.d(TAG, "verifyPermissions: asking user for permissions. ");
        ActivityCompat.requestPermissions(
                MainActivity.this,
                permissions,
                REQUEST_CODE
        );
    }// end verifyPermissions


    /**
     * Checks to see if permission was granted for the passed parameters
     *
     * ONLY ONE PERMISION MAYT BE CHECKED AT A TIME
     * @param permissions
     * @return
     */
    public boolean checkPermission(String [] permissions){// checkPermission
        Log.d(TAG, "checkPermission: checking permissions for:"+permissions[0]);

        int permissionRequest = ActivityCompat.checkSelfPermission(
                MainActivity.this,
                permissions[0]);

        if (permissionRequest != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "checkPermission: \n Permissions was not granted for :" + permissions[0]);
            return false;
        }else {
            return true;
        }

    }//end checkPermission



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: requestCode: "+requestCode);

        switch (requestCode){
            case REQUEST_CODE:
                for (int i= 0 ;i< permissions.length;i++){
                    if (grantResults[i]== PackageManager.PERMISSION_GRANTED){
                        Log.d(TAG, "onRequestPermissionsResult: User has allowed permission to access: "+ permissions[i]);

                    }else {
                        break;
                    }
                }
                break;
        }

    }



}// END MainActivity
