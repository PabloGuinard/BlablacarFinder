package com.dut.blablacarfinder;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


public class PopupFragment {

    public PopupFragment(Point point, Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.popup_adapter, null);
        TextView tvAddress1 = view.findViewById(R.id.tv_address_1);
        tvAddress1.setText(point.address);
        TextView tvAddress2 = view.findViewById(R.id.tv_address_2);
        tvAddress2.setText(point.city + " " + point.code);
        TextView tvDistance = view.findViewById(R.id.tv_distance);
        tvDistance.setText(point.distanceFromUser + "");
        TextView tvNbPlaces = view.findViewById(R.id.tv_nb_place);
        if(point.nbPlaces == 0){
            tvNbPlaces.setText(context.getString(R.string.unknown));
        } else {
            tvNbPlaces.setText(point.nbPlaces + "");
        }
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(point.placeName)
                .setView(view)
                .setPositiveButton(R.string.view, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked
                    }
                })
                .show();
    }
}
