package mds.gpp.saudeemcasa.controller;

import android.content.Context;
import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import api.Dao.HospitalDao;
import api.Exception.ConnectionErrorException;
import api.Helper.JSONHelper;
import api.Request.HttpConnection;
import mds.gpp.saudeemcasa.helper.GPSTracker;
import mds.gpp.saudeemcasa.model.Hospital;
import mds.gpp.saudeemcasa.model.Stablishment;

import static java.util.Collections.sort;


/**
 * Created by freemanpivo on 9/20/15.
 */
public class HospitalController {

    private static HospitalController instance = null;
    private static Hospital hospital;
    private static List<Hospital> hospitalList = new ArrayList<Hospital>();
    private static HospitalDao hospitalDao;
    private static Context context;

    private HospitalController(Context context) {
        this.context = context;
        hospitalDao = HospitalDao.getInstance(context);
    }

    public static HospitalController getInstance(Context context) {
        if (instance == null) {
            instance = new HospitalController(context);
        } else {
			/* ! Nothing To Do. */
        }
        return instance;
    }
    public void setHospital( Hospital hospital ) {
        HospitalController.hospital = hospital;
    }

    public Hospital getHospital() {
        return hospital;
    }


    public static List<Hospital> getAllHospitals(){

        return hospitalList;
    }

    public void initControllerHospital() throws IOException, JSONException, ConnectionErrorException {

            if (hospitalDao.isDbEmpty()) {
                //creating
                HttpConnection httpConnection = new HttpConnection();
                //requesting
                String jsonHospital = httpConnection.newRequest("http://159.203.95.153:3000/habilitados");
                System.out.println(jsonHospital);
                JSONHelper jsonParser = new JSONHelper(context);

                if(jsonHospital !=null){
                    if(jsonParser.hospitalListFromJSON(jsonHospital)){
                        hospitalList = hospitalDao.getAllHospitals();
                    }else{/*error introducing to database*/}
                }else {/*error on connection*/}


            } else {
                //just setting hospitals to local list
                hospitalList = hospitalDao.getAllHospitals();

            }

    }

    public int[] setDistance(Context context, ArrayList<Hospital> list) {
        int[] results = new int[list.size()];
        GPSTracker gps = new GPSTracker(context);



        if(gps.canGetLocation()) {
            double userLongitude = gps.getLongitude();
            double userLatitude = gps.getLatitude();

            for (int i = 0; i < list.size(); i++) {
                String auxLatitude = list.get(i).getLatitude();
                String auxLongitude = list.get(i).getLongitude();
                float resultsadapter[] = new float[1];
                Double.parseDouble(auxLongitude);
                Location.distanceBetween(Double.parseDouble(list.get(i).getLatitude()),
                        Double.parseDouble(list.get(i).getLongitude()),
                        userLatitude,userLongitude,resultsadapter);
                list.get(i).setDistance(resultsadapter[0]);
            }

            sort(list, new DistanceComparator());
            return results;
        }else {
            return null;
        }

    }

    public void requestRating() {
        HttpConnection httpConnection = new HttpConnection();
        for(int i = 0;i<15;i++){
            try {
                hospitalList.get(i).setRate(Float.parseFloat(httpConnection.newRequest("ipAdress")));
            } catch (ConnectionErrorException e) {
                /*fail to request*/
            }
        }
    }

    public static class DistanceComparator implements Comparator<Stablishment>
    {


        public int compare(Stablishment stablishment1, Stablishment stablishment2) {
            return stablishment1.getDistance()<(stablishment2.getDistance())? -1 : 1;
        }

    }
    public String updateRate(float rate,String androidId,int hospitalId ){
        HttpConnection connection = new HttpConnection();

        String response = null;
        try {
            response = connection.newRequest("http://159.203.95.153:3000"+"/"+"rate"+"/"+"gid"+"/"+hospitalId+"/"+"aid"+"/"+androidId+"/"+"rating"+"/"+rate);
        } catch (ConnectionErrorException e) {
            e.printStackTrace();
        }

        return response;
    }

}
