package com.r4sh33d.medmanager;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseFragment  extends Fragment{

    public void setToolbarTitle (String title){
        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        ab.setTitle(title);
    }

    public AlertDialog showAlertDialog(String message, DialogInterface.OnClickListener
            positiveClickListener , DialogInterface.OnClickListener negativeClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("Ok", positiveClickListener)
                .setNegativeButton("Cancel" ,negativeClickListener);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

}
