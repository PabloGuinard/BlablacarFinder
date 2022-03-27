package com.dut.blablacarfinder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListPointsAdapter extends BaseAdapter {
    private ArrayList<Point> pointsList;
    private double[] area;

    public ListPointsAdapter(ArrayList<Point> pointsList, double[] area) {
        this.pointsList = pointsList;
        this.area = area;
    }

    @Override
    public int getCount() {
        return pointsList.size();
    }

    @Override
    public Object getItem(int i) {
        return pointsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void refresh(ArrayList<Point> pointsList){
        this.pointsList = pointsList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        if(view == null){
            view = inflater.inflate(R.layout.list_points_adapter, viewGroup, false);
        }

        TextView tvPlaceName = view.findViewById(R.id.tv_place_name);
        tvPlaceName.setText(pointsList.get(i).placeName);
        TextView tvDistance = view.findViewById(R.id.tv_address_popup);
        String text = pointsList.get(i).distanceFromUser + " meters";
        tvDistance.setText(text);
        View finalView = view;
        view.setOnClickListener(see -> {
            new PopupFragment().setPopup(pointsList.get(i), finalView.getContext(), pointsList, area);
        });

        return view;
    }
}
