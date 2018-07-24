package br.com.projetofragmeto.clinup.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public interface OnGetDataListener {
    public void onStart();

    public void onSuccess(DataSnapshot data);

    public void onFailed(DatabaseError databaseError);
}
