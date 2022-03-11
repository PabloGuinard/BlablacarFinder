package com.dut.blablacarfinder;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class APIAsyncTask extends AsyncTask<Object, Void, JSONArray> {
    private ArrayList<Point> pointsArray;

    @Override
    //params : double[] area
    protected JSONArray doInBackground(Object... params) {
        double[] area = (double[]) params[0];
        pointsArray = (ArrayList<Point>) params[1];
        JSONArray result =null;
        int nbHits;
        int nbRows = 50;

        String[] partsUrl = new String[4];
        partsUrl[0] = "https://public.opendatasoft.com/api/records/1.0/search/?dataset=aires-covoiturage&q=&rows="
                + nbRows + "&start=";
        partsUrl[1] = "&geofilter.distance=";
        partsUrl[2] = "%2C+";
        partsUrl[3] = "%2C+";

        try {
            URL url = new URL(partsUrl[0] + 0 + partsUrl[1] + area[0] + partsUrl[2] + area[1] + partsUrl[3] + area[2]);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            //get 1st page of result
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                JSONObject response = new JSONObject(in.readLine());
                in.close();
                nbHits = response.getInt("nhits");
                result = response.getJSONArray("records");

                //for each next page add to result
                for (int cpt = 50; cpt < nbHits; cpt +=nbRows){
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
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        super.onPostExecute(jsonArray);
        ArrayList<Point> pointsArray = new ArrayList<>();

        //set pointsArray
        for (int cpt = 0; cpt < jsonArray.length(); cpt++){
            try {
                JSONObject row = jsonArray.getJSONObject(cpt);
                JSONObject fields = (JSONObject) row.get("fields");
                JSONArray coordinates = (JSONArray) fields.get("coordonnees");
                Point point = new Point((double) coordinates.get(0), (double) coordinates.get(1),
                        (int) fields.getInt("dist"), fields.getString("nom_du_lieu"));
                pointsArray.add(point);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.pointsArray = pointsArray;
    }
}
