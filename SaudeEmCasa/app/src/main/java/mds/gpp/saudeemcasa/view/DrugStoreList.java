package mds.gpp.saudeemcasa.view;

import android.app.ActionBar;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.*;

import mds.gpp.saudeemcasa.R;
import mds.gpp.saudeemcasa.adapter.DrugStoreAdapter;
import mds.gpp.saudeemcasa.adapter.HospitalAdapter;
import mds.gpp.saudeemcasa.helper.GPSTracker;
import mds.gpp.saudeemcasa.model.DrugStore;
import mds.gpp.saudeemcasa.model.Hospital;
import mds.gpp.saudeemcasa.controller.DrugStoreController;
import mds.gpp.saudeemcasa.model.DrugStore;


public class DrugStoreList extends Activity {

    ListView listView;
    ArrayList<DrugStore> list;
    GPSTracker gps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drugstore_list_screen);

        // Initializing list view
        listView = (ListView) findViewById(R.id.listView);
        gps = new GPSTracker(this);

        // Instancing controller
        DrugStoreController drugStoreController = DrugStoreController.getInstance(this);

        // Initialize and fill list of drugstore
        list = (ArrayList<DrugStore>) drugStoreController.getAllDrugstores();

        if(gps.canGetLocation()) {
            drugStoreController.setDistance(this, list);
            // Initializing new DrugStoreAdapter with list of drugstore
            DrugStoreAdapter adapter = new DrugStoreAdapter(this, list);

            // Setting adapter to listView
            listView.setAdapter(adapter);
        }else {
            Toast.makeText(this, "Voce nao esta conectado ao gps ou a internet!\n Concecte-se para prosseguir.",Toast.LENGTH_LONG);
        }

    }
}
