package com.ilyakamar.my_contactslist_23318.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ilyakamar.my_contactslist_23318.R;
import com.ilyakamar.my_contactslist_23318.models.Contact;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 23/03/2018.
 */

public class ContactListAdapter extends ArrayAdapter<Contact> {// ContactListAdapter

    private LayoutInflater mInflater;
    private List<Contact> mContacts = null;
    private ArrayList<Contact> arrayList; // need for the search bar
    private int layoutResource;
    private Context mContext;
    private String mAppend;

    public ContactListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Contact> contacts, String append) {// constructor ContactListAdapter
        super(context, resource, contacts);


        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutResource = resource;
        this.mContext = context;
        mAppend = append;
        this.mContacts = contacts;
        arrayList = new ArrayList<>();
        this.arrayList.addAll(mContacts);

    }// end constructor ContactListAdapter


    private static class ViewHolder{// ViewHolder
        TextView name;
        CircleImageView contactImage;
        ProgressBar mProgressBar;
    }// end ViewHolder

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // ViewHolder Build Pattern Start
        final ViewHolder holder;

        if (convertView == null){
            convertView = mInflater.inflate(layoutResource,parent,false);
            holder = new ViewHolder();

            // ****************** Stuff to change *****************
            holder.name = (TextView)convertView.findViewById(R.id.contactName);
            holder.contactImage = (CircleImageView)convertView.findViewById(R.id.contactImage);
            //*****************************************************

            holder.mProgressBar = (ProgressBar)convertView.findViewById(R.id.contactProgressBar);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        // ****************** Stuff to change *****************
        String name_ = getItem(position).getName();
        String imagePath = getItem(position).getProfileImage();
        holder.name.setText(name_);

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(mAppend + imagePath, holder.contactImage, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {// 1
                holder.mProgressBar.setVisibility(View.VISIBLE);
            }// end 1

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {// 2

                holder.mProgressBar.setVisibility(View.GONE);
            }// end 2

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {// 3

                holder.mProgressBar.setVisibility(View.GONE);
            }// end 3

            @Override
            public void onLoadingCancelled(String imageUri, View view) {// 4

                holder.mProgressBar.setVisibility(View.GONE);
            }// end 4
        });


        //*****************************************************
        return convertView;
    }

    // Filter class
    public void filter (String characterText){
        characterText = characterText.toLowerCase(Locale.getDefault());
        mContacts.clear();
        if (characterText.length() == 0){
            mContacts.addAll(arrayList);
        }
        else {
            mContacts.clear();;
            for (Contact contact : arrayList){
                if (contact.getName().toLowerCase(Locale.getDefault()).contains(characterText)){
                    mContacts.add(contact);
                }
            }
        }
        notifyDataSetChanged();
    }

}// end ContactListAdapter

