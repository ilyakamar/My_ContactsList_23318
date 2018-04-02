package com.ilyakamar.my_contactslist_23318.utils;



import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ilyakamar.my_contactslist_23318.MainActivity;
import com.ilyakamar.my_contactslist_23318.R;
import com.ilyakamar.my_contactslist_23318.models.Contact;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 23/03/2018.
 */

public class ContactPropertyListAdapter extends ArrayAdapter<String>{

    private static final String TAG = "ContactPropertyListAdap";

    private LayoutInflater mInflater;
    private List<String> mProperties = null;
    private int layoutResource;
    private Context mContext;
    private String mAppend;

    public ContactPropertyListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> properties) {// constructor ContactListAdapter
        super(context, resource, properties);


        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutResource = resource;
        this.mContext = context;
        this.mProperties = properties;



    }// end constructor ContactListAdapter




    // ****************** Stuff to change *****************
    private static class ViewHolder{// ViewHolder
        TextView property;
        ImageView rightIcon;
        ImageView leftIcon;
    }// end ViewHolder
    //*****************************************************

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // ViewHolder Build Pattern Start
        final ViewHolder holder;

        if (convertView == null){
            convertView = mInflater.inflate(layoutResource,parent,false);
            holder = new ViewHolder();

            // ****************** Stuff to change *****************
            holder.property = (TextView)convertView.findViewById(R.id.tvMiddleCardView);
            holder.rightIcon = (ImageView)convertView.findViewById(R.id.iconRightCardView);
            holder.leftIcon = (ImageView)convertView.findViewById(R.id.iconLeftCardView);
            //*****************************************************



            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        // ****************** Stuff to change *****************
        final String property = getItem(position);
        holder.property.setText(property);

        // check if it's an email or a phone number
        // email
        if (property.contains("@")){

            holder.leftIcon.setImageResource(mContext.getResources().getIdentifier("@drawable/ic_email",null,mContext.getPackageName()));
            holder.leftIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: opening email.");

                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("plan/text");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL,new  String []{property});
                    mContext.startActivity(emailIntent);

                    /** optional settings for sending emails
                     *
                     * >>>>>>replase:
                     * emailIntent.setType("plan/text");
                     emailIntent.putExtra(Intent.EXTRA_EMAIL,new  String []{property});
                     mContext.startActivity(emailIntent);
                     * ---------------------------------------------------------------
                     *
                     * String email = property;
                     * String subject = "subject";
                     * String body = "body....";
                     *
                     * String uriText = "mailto: + Uri.encode(email) + "?subject =" + Uri.encode(subject)+
                     * "&body=" + Uri.encode(body);
                     * Uri uri = Uri.parse(uriText);
                     *
                     * emailIntent.setData(uri);
                     * mContext.startActivity(emailIntent);
                     */



                }
            });
        }
        else if ((property.length()) != 0){

            // Phone call
            holder.leftIcon.setImageResource(mContext.getResources().getIdentifier("@drawable/ic_phone",null,mContext.getPackageName()));
            holder.leftIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((MainActivity)mContext).checkPermission(Init.PHONE_PERMISSIONS)){
                        Log.d(TAG, "onClick: initiating phone call...");
                        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel",property,null));
                        mContext.startActivity(callIntent);
                    }else {
                        ((MainActivity)mContext).verifyPermissions(Init.PHONE_PERMISSIONS);
                    }
                }
            });

            // setup the icon for sending text messages
            holder.rightIcon.setImageResource(mContext.getResources().getIdentifier("@drawable/ic_message",null,mContext.getPackageName()));
            holder.rightIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: initiating text message...");

                    // The number that we want to send SMS
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW,Uri.fromParts("sms",property,null));
                    mContext.startActivity(smsIntent);

                }
            });
        }

        //*****************************************************
        return convertView;
    }

}
