package mds.gpp.saudeemcasa.controller;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
import mds.gpp.saudeemcasa.R;
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

    public static class DistanceComparator implements Comparator<Stablishment>
    {


        public int compare(Stablishment stablishment1, Stablishment stablishment2) {
            return stablishment1.getDistance()<(stablishment2.getDistance())? -1 : 1;
        }

    }
    public String updateRate(float rate,String androidId,int drugstoreId ){
        JSONObject json = new JSONObject();
        try{
            json.put("rate", rate);
            json.put("androidId", androidId);
            json.put("drugstoreId",drugstoreId);
        } catch (JSONException e) {
            /*Handle exception*/
        }
        HttpConnection connection = new HttpConnection();

        String response = connection.postRequest(json, "PUT THE IP HERE");

        return response;
    }

    public void updateGoogleMap() {

        GoogleMap googleMap = null;
        String latitude = hospital.getLatitude();
        String longitude = hospital.getLongitude();
        //convert string to double
        LatLng originLocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

        //BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.common_ic_googleplayservices);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        /*googleMap.addMarker(new MarkerOptions()
                .position(originLocation)
                //.icon(icon)
                .title(hospital.getName())
                .snippet(hospital.getCity()));*/

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(originLocation)
                .zoom(17)
                .bearing(90)
                .tilt(45)
                .build();
        //googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        googleMap.moveCamera(cameraUpdate);

    }

}
