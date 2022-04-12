package it.ranauro.beesafe.backend.utils.listeners;

import com.google.firebase.database.DataSnapshot;

@Deprecated
public interface OnGetDataListener {
    void onSuccess(DataSnapshot snapshot);
    void onStart();
    void onFailure();
}
