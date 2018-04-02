package com.ilyakamar.my_contactslist_23318;

/**
 * Created by User on 23/03/2018.
 */

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.ilyakamar.my_contactslist_23318.utils.ChangePhotoDialog;
import com.ilyakamar.my_contactslist_23318.utils.DatabaseHelper;
import com.ilyakamar.my_contactslist_23318.utils.Init;
import com.ilyakamar.my_contactslist_23318.utils.UniversalImageLoader;
import com.ilyakamar.my_contactslist_23318.models.Contact;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 03/03/2018.
 */

public class EdditContactFragment extends Fragment implements ChangePhotoDialog.OnPhotoReceivedListener{
    private static final String TAG = "EdditContactFragment";


    // This will evade the nullpointer exception whena adding to a new bundle from MainActivity
    public EdditContactFragment(){// EdditContactFragment
        super();
        setArguments(new Bundle());
    }// end EdditContactFragment

    private Contact mContact;
    private EditText mPhoneNumber,mName,mEmail;
    private CircleImageView mContactImage;
    private Spinner mSelectDevice;
    private Toolbar toolbar;
    private String mSelectedImagePath;
    private int mPreviousKeyStroke;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {// onCreateView
        View view = inflater.inflate(R.layout.fragment_editcontact,container,false);
        mPhoneNumber = (EditText)view.findViewById(R.id.etContactPhone);
        mName = (EditText)view.findViewById(R.id.etContactName);
        mEmail= (EditText)view.findViewById(R.id.etContactEmail);
        mContactImage = (CircleImageView)view.findViewById(R.id.contactImage);
        mSelectDevice = (Spinner)view.findViewById(R.id.selectDevice);
        toolbar = (Toolbar)view.findViewById(R.id.editContactToolbar);
        Log.d(TAG, "onCreateView: started.");

        mSelectedImagePath = null;

        // set the beading the for the toolbar
        TextView heading = (TextView)view.findViewById(R.id.textContactToolbar);
        heading.setText(getString(R.string.edit_contact));

        // required for setting up the toolbar
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        //get the contact from the bundle
        mContact = getContactFromBundle();

        if (mContact != null){
            init();
        }

        // navigation for the backArrow
        ImageView ivBackArrow = (ImageView)view.findViewById(R.id.ivBackArrow);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"onClick: clicked back arrow.");
                // remove previous fragment from the backstack (therefore vavigating back)
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // save shanges to the contact
        ImageView ivCheckMark = (ImageView) view.findViewById(R.id.ivCheckMark);
        ivCheckMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: saving the edited contact");
                // execute the save method for the database

                if (checkStringIfNull(mName.getText().toString())){
                    Log.d(TAG, "onClick: saving changes to the contact:"+ mName.getText().toString());

                    // get the database helper and save the contact
                    DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                    Cursor cursor = databaseHelper.getContactID(mContact);

                    int contactID = -1;
                    while (cursor.moveToNext()){
                        contactID = cursor.getInt(0);
                    }
                    if (contactID > -1 ){
                        if (mSelectedImagePath != null){
                            mContact.setProfileImage(mSelectedImagePath);
                        }
                        mContact.setName(mName.getText().toString());
                        mContact.setPhonenumber(mPhoneNumber.getText().toString());
                        mContact.setDevice(mSelectDevice.getSelectedItem().toString());
                        mContact.setEmail(mEmail.getText().toString());

                        databaseHelper.updateContact(mContact,contactID);
                        Toast.makeText(getActivity(),"Contact Updated",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getActivity(),"Database Error",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // initiate the dialog box for choosing an image
        ImageView ivCamera = (ImageView)view.findViewById(R.id.ivCamera);
        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                Make sure all permissions have been verified before opening the dialog
                */
                for (int i = 0;i<Init.PERMISSIONS.length;i++){
                    String [] permission = {Init.PERMISSIONS[i]};
                    if (((MainActivity)getActivity()).checkPermission(permission)){

                        if (i== Init.PERMISSIONS.length - 1 ){

                            Log.d(TAG,"onClick: open the 'image selection dialog box'.");
                            ChangePhotoDialog dialog = new ChangePhotoDialog();
                            dialog.show(getFragmentManager(),getString(R.string.change_photo_dialog));
                            dialog.setTargetFragment(EdditContactFragment.this,0);
                        }

                    }else {
                        ((MainActivity)getActivity()).verifyPermissions(permission);
                    }
                }


            }
        });

        return view;
    }// end onCreateView


    // check if String is Null
    private boolean checkStringIfNull(String string){
        if (string.equals("")){
            return false;
        }
        else {
            return true;
        }
    }

    private void init(){
        mPhoneNumber.setText(mContact.getPhonenumber());
        mName.setText(mContact.getName());
        mEmail.setText(mContact.getEmail());
        UniversalImageLoader.setImage(mContact.getProfileImage(),mContactImage,null,""); //dell http://

        // Setting the selected device to the spinner -------------------
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.device_options,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSelectDevice.setAdapter(adapter);
        int position = adapter.getPosition(mContact.getDevice());
        mSelectDevice.setSelection(position);
        //----------------------------------------------------------------
    }


    /**
     * Retrieves the selected contact from the bundle(coming from Main activity)
     * @return
     */
    private Contact getContactFromBundle(){// getContactFromBundle
        Log.d(TAG, "getContactFromBundle: arguments "+ getArguments());

        Bundle bundle = this.getArguments();
        if (bundle!= null){
            return bundle.getParcelable(getString(R.string.contact));
        }else {
            return null;
        }

    }// end getContactFromBundle


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {// onCreateOptionsMenu
        inflater.inflate(R.menu.contact_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }// end onCreateOptionsMenu


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {// onOptionsItemSelected

        switch (item.getItemId()){
            case R.id.menuitem_delete:
                Log.d(TAG, "onOptionsItemSelected: deleting contact.");
                DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                Cursor cursor = databaseHelper.getContactID(mContact);

                int contactID = -1;
                while (cursor.moveToNext()){
                    contactID = cursor.getInt(0);
                }
                if (contactID > -1 ){
                    if (databaseHelper.deleteContact(contactID)> 0){
                        Toast.makeText(getActivity(),"Contact Deleted",Toast.LENGTH_SHORT).show();

                        // clear the arguments on the current bundle since the contact is deleted
                        this.getArguments().clear();

                        // remove previous fragment from the backstack (therefore navigating back)
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                    else {
                        Toast.makeText(getActivity(),"Database Error ",Toast.LENGTH_SHORT).show();
                    }
                }
        }

        return super.onOptionsItemSelected(item);
    }// end onOptionsItemSelected

    /**
     * Retrieves the selected image from the bundle (coming from ChangePhotoDialog)
     * @param bitmap
     */
    @Override
    public void getBitmapImage(Bitmap bitmap) {
        Log.d(TAG, "getBitmapImage: got the bitmap: "+bitmap);
        // get the bitmap from ChangePhotoDialog
        if (bitmap!=null){
            // compress the image (if you like )
            ((MainActivity)getActivity()).compressBitmap(bitmap,70);
            mContactImage.setImageBitmap(bitmap);
        }
    }

    @Override
    public void getImagePath(String imagePath) {
        Log.d(TAG, "getImagePath: got the image path: "+ imagePath);
        if (!imagePath.equals("")){
            imagePath = imagePath.replace(":/","://");
            mSelectedImagePath = imagePath;
            UniversalImageLoader.setImage(imagePath,mContactImage,null,"");
        }
    }

    /**
     * Initialize the onTextChangeListener for formatting the phonenumber
     */
    private void initOnTextChangeListener(){

        mPhoneNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                mPreviousKeyStroke = keyCode;
                return false;
            }
        });

        mPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String number = s.toString();
                Log.d(TAG, "afterTextChanged: "+number);

                if (number.length()== 3 && mPreviousKeyStroke!= KeyEvent.KEYCODE_DEL && !number.contains("(")){
                    number = String.format("(%s",s.toString().substring(0,3));
                    mPhoneNumber.setText(number);
                    mPhoneNumber.setSelection(number.length());

                }
                else if (number.length() == 5 && mPreviousKeyStroke != KeyEvent.KEYCODE_DEL && !number.contains(")")){
                    number = String.format("(%s) %s",
                            s.toString().substring(1,4),
                            s.toString().substring(4,5));
                    mPhoneNumber.setText(number);
                    mPhoneNumber.setSelection(number.length());

                }else if (number.length() == 10 && mPreviousKeyStroke != KeyEvent.KEYCODE_DEL  && !number.contains("-")){
                    number = String .format("(%s) %s-%s",
                            s.toString().substring(1,4),
                            s.toString().substring(6,9),
                            s.toString().substring(9,10));
                    mPhoneNumber.setText(number);
                    mPhoneNumber.setSelection(number.length());
                }

            }
        });
    }
}

