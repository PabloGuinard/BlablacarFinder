package com.dut.blablacarfinder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;


public class PopupFragment extends Fragment {
    public MainActivity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }

    public PopupFragment(){
        super(R.layout.popup_fragment);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    public PopupFragment setPopup(Point point, Context context, MainActivity activity) {
        View view = setPopupContent(point, context);

        int color;
        int mode;
        if(SettingsActivity.isDarkMode){
            color = view.getContext().getColor(R.color.white);
            mode = AlertDialog.THEME_HOLO_DARK;
        } else {
            color = view.getContext().getColor(R.color.black);
            mode = AlertDialog.THEME_HOLO_LIGHT;
        }

        AlertDialog alertDialog = new AlertDialog.Builder(context, mode)
                .setIcon(R.drawable.ic_marker_icon)
                .setTitle(point.placeName)
                .setView(view)
                .setPositiveButton(R.string.view, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(activity, MapActivity.class);
                        intent.putExtra(MainActivity.INTENT_POINTSLIST, activity.getPointsList());
                        intent.putExtra(MainActivity.INTENT_LOCATION, activity.getArea());
                        intent.putExtra(MainActivity.INTENT_SELECTED_POINT, point);
                        activity.startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    public static View setPopupContent(Point point, Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.popup_fragment, null);
        TextView tvAddress1 = view.findViewById(R.id.tv_address_1);
        SettingsActivity.setWithDarkMode(tvAddress1);
        tvAddress1.setText(point.address);
        TextView tvAddress2 = view.findViewById(R.id.tv_address_2);
        SettingsActivity.setWithDarkMode(tvAddress2);
        tvAddress2.setText(point.city + " " + point.code);
        TextView tvDistance = view.findViewById(R.id.tv_distance);
        String text;
        if(SettingsActivity.isDistanceMeters) {
            text = point.distanceFromUser + " " + view.getContext().getString(R.string.meters);
        } else {
            text = MainActivity.meterToKilometer(point.distanceFromUser + "") + " "
                    + view.getContext().getString(R.string.kilometers);
        }
        SettingsActivity.setWithDarkMode(tvDistance);
        tvDistance.setText(text);
        TextView tvNbPlaces = view.findViewById(R.id.tv_nb_place);
        SettingsActivity.setWithDarkMode(tvNbPlaces);
        if(point.nbPlaces == 0){
            tvNbPlaces.setText(context.getString(R.string.unknown));
        } else {
            tvNbPlaces.setText(point.nbPlaces + "");
        }
        if(SettingsActivity.isDarkMode){
            view.setBackgroundColor(view.getContext().getColor(R.color.black));
        }
        return view;
    }
}
