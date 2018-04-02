package com.ilyakamar.my_contactslist_23318.utils;



import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ilyakamar.my_contactslist_23318.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by User on 23/03/2018.
 */

public class UniversalImageLoader {
    private static final int defaultImage = R.drawable.ic_android;

    private Context mContext;

    public UniversalImageLoader(Context context) {
        mContext = context;
    }

    public ImageLoaderConfiguration getConfig(){
        // universal Image Loader Setup
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultImage)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .cacheOnDisk(true).cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
//                .defaultDisplayImageOptions(defaultOptions)
//                .memoryCache(new WeakMemoryCache())
//                .diskCacheSize(100*1024*1024)
//                .build();


        return new ImageLoaderConfiguration.Builder(mContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100*1024*1024)
                .build();

    }

    public static void setImage(String imgURL, ImageView imageView, final ProgressBar mProgressBar,String append){

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(append + imgURL, imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {// 1
                if (mProgressBar!=null){
                    mProgressBar.setVisibility(View.VISIBLE);
                }

            }// end 1

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {// 2

                if (mProgressBar!=null){
                    mProgressBar.setVisibility(View.GONE);
                }
            }// end 2

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {// 3

                if (mProgressBar!=null){
                    mProgressBar.setVisibility(View.GONE);
                };
            }// end 3

            @Override
            public void onLoadingCancelled(String imageUri, View view) {// 4

                if (mProgressBar!=null){
                    mProgressBar.setVisibility(View.GONE);
                }
            }// end 4
        });
    }
}

