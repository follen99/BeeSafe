package com.example.imagecollectionmvvm.listeners;

import android.view.View;

import com.example.imagecollectionmvvm.ImageCollection;

public interface ImageCollectionListener {
    void onItemClicked(int position, View view, ImageCollection collection);
    void onItemLongPressed(int position, View view, ImageCollection collection);
}
