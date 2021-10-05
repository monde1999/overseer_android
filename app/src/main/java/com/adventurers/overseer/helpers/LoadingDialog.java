package com.adventurers.overseer.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.adventurers.overseer.R;

public class LoadingDialog {
    private final Activity activity;
    private AlertDialog alertDialog;

    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_loading, null));
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void dismissDialog(){
        alertDialog.dismiss();
    }
}
