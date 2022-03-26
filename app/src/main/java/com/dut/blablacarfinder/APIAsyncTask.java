package com.dut.blablacarfinder;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class APIAsyncTask extends AsyncTask<Object, Void, ArrayList<Point>> {
    /*GoogleMap googleMap;*/
    JSONArray result = null;
    int nbHits = -1;
    int nbRows = 50;
    ApiInterface apiInterface;
    ArrayList<Point> pointsList;
    Context context;

    @Override
    protected ArrayList<Point> doInBackground(Object... params) {
        double[] area = (double[]) params[0];
        apiInterface = (ApiInterface) params[1];
        pointsList = (ArrayList<Point>) params[2];
        context = (Context) params[3];

        String[] partsUrl = new String[5];
        partsUrl[0] = "https://public.opendatasoft.com/api/records/1.0/search/?dataset=aires-covoiturage&q=&rows=";
        partsUrl[1] = "&start=";
        partsUrl[2] = "&geofilter.distance=";
        partsUrl[3] = "%2C+";
        partsUrl[4] = "%2C+";

        try {
            URL url = new URL(partsUrl[0] + "50" + partsUrl[1] + 0 + partsUrl[2] + area[0]
                    + partsUrl[3] + area[1] + partsUrl[4] + area[2]);
            //if not first request in MainActivity
            if(pointsList.size() != 0 && apiInterface.getClass() != Map.class){
                url = new URL(partsUrl[0] + "10" + partsUrl[1] + pointsList.size() + partsUrl[2] + area[0]
                        + partsUrl[3] + area[1] + partsUrl[4] + (area[2] * 10));
            }
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            //get 1st page of result
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                JSONObject response = new JSONObject(in.readLine());
                in.close();
                nbHits = response.getInt("nhits");
                result = response.getJSONArray("records");
                //for each next page add to result if first request or in Map
                if(pointsList.size() != 0 && apiInterface.getClass() != Map.class){
                    for (int cpt = nbRows; cpt < nbHits; cpt +=nbRows){
                        url = new URL(partsUrl[0] + cpt + partsUrl[1] + area[0] + partsUrl[2] + area[1] + partsUrl[3] + area[2]);
                        connection = (HttpURLConnection) url.openConnection();

                        if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                            in = new BufferedReader(new InputStreamReader((connection.getInputStream())));
                            JSONArray tmp = new JSONObject(in.readLine()).getJSONArray("records");
                            for (int i = 0; i < tmp.length(); i++){
                                result.put(tmp.get(i));
                            }
                        }
                    }
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }

        if(nbHits == -1){
            return null;
        }

        ArrayList<Point> pointsArray = new ArrayList<>();

        //set pointsArray
        for (int cpt = 0; cpt < result.length(); cpt++){
            try {
                JSONObject row = result.getJSONObject(cpt);
                JSONObject fields = (JSONObject) row.get("fields");
                JSONArray coordinates = (JSONArray) fields.get("coordonnees");
                String placeName = context.getString(R.string.unknown);
                int places = 0;
                String address = context.getString(R.string.unknown);
                String city = context.getString(R.string.unknown);
                String code = context.getString(R.string.unknown);
                try{
                    places = fields.getInt("places");
                } catch (JSONException e){}
                try{
                    placeName = fields.getString("nom_du_lieu");
                } catch (JSONException e){}
                try{
                    address = fields.getString("adresse");
                } catch (JSONException e){}
                try{
                    city = fields.getString("ville");
                } catch (JSONException e){}
                try{
                    code = fields.getString("code_postal");
                } catch (JSONException e){ }
                Point point = new Point(
                        (double) coordinates.get(0),
                        (double) coordinates.get(1),
                        (int) fields.getInt("dist"),
                        placeName,
                        places,
                        address,
                        city,
                        code
                );
                pointsArray.add(point);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return pointsArray;
    }

    @Override
    protected void onPostExecute(ArrayList<Point> pointsList) {
        super.onPostExecute(pointsList);
        if(pointsList == null){
            return;
        }
        apiInterface.result(pointsList);
    }
}
