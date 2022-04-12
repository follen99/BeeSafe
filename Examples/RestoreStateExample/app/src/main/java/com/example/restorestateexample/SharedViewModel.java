package com.example.restorestateexample;

import android.content.ClipData;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    public SharedViewModel(){
        set(new Item(0));
    }

    private final MutableLiveData<Item> selected = new MutableLiveData<Item>();

    public void set(Item item) {
        selected.setValue(item);
    }

    public MutableLiveData<Item> get() {
        return selected;
    }
}
