package com.dut.blablacarfinder;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;


public class PopupFragment extends Fragment {

    public PopupFragment(){
        super(R.layout.popup_fragment);
    }

    @SuppressLint("ResourceAsColor")
    public PopupFragment setPopup(Point point, Context context, ArrayList<Point> pointsList, double[] area) {
        View view = setPopupContent(point, context);

        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_marker_icon)
                .setTitle(point.placeName)
                .setView(view)
                .setPositiveButton(R.string.view, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(context, Map.class);
                        intent.putExtra(MainActivity.INTENT_POINTSLIST, pointsList);
                        intent.putExtra(MainActivity.INTENT_LOCATION, area);
                        intent.putExtra(MainActivity.INTENT_SELECTED_POINT, point);
                        startActivity(intent);
                    }
                })
                .show();
        return this;
    }

    @SuppressLint("SetTextI18n")
    public static View setPopupContent(Point point, Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.popup_fragment, null);
        TextView tvAddress1 = view.findViewById(R.id.tv_address_1);
        tvAddress1.setText(point.address);
        TextView tvAddress2 = view.findViewById(R.id.tv_address_2);
        tvAddress2.setText(point.city + " " + point.code);
        TextView tvDistance = view.findViewById(R.id.tv_distance);
        tvDistance.setText(point.distanceFromUser + " meters");
        TextView tvNbPlaces = view.findViewById(R.id.tv_nb_place);
        if(point.nbPlaces == 0){
            tvNbPlaces.setText(context.getString(R.string.unknown));
        } else {
            tvNbPlaces.setText(point.nbPlaces + "");
        }
        return view;
    }
}
