package mds.gpp.saudeemcasa.view;

import android.Manifest;
import android.app.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import android.text.Html;
import android.view.View;
import android.widget.ImageButton;

import android.widget.RatingBar;

import android.widget.TextView;

import java.util.UUID;

import mds.gpp.saudeemcasa.R;
import mds.gpp.saudeemcasa.controller.DrugStoreController;

import android.provider.Settings.Secure;

import static java.security.AccessController.getContext;

/**
 * Created by lucas on 10/21/15.
 */
public class DrugstoreScreen extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        System.out.println("ANDROID ID >>> "+androidId);

        /*
        * if (requestedId.equals(androidId)){
        *   print "VOCE JA VOTOU!"
        * else
        *   
        * */

        setContentView(R.layout.drugstore_screen);
        DrugStoreController controller = DrugStoreController.getInstance(this);
        // setting name
        TextView nameTextView = (TextView)findViewById(R.id.textViewDrugName);
        nameTextView.setText(controller.getDrugstore().getName());
        // Address
        TextView addressTextView = (TextView)findViewById(R.id.textViewAddress);
        addressTextView.setText(Html.fromHtml(controller.getDrugstore().getAddress() + " - " + controller.getDrugstore().getCity() + " - " + controller.getDrugstore().getState()));
        //CEP
        TextView cepTextView = (TextView)findViewById(R.id.textViewCep);
        cepTextView.setText("Cep: "+controller.getDrugstore().getPostalCode());
        // setting telephone
        if(controller.getDrugstore().getType().equals("FARMACIAPOPULAR")){
            TextView telephoneTextView = (TextView) findViewById(R.id.textViewDrugTel);
            telephoneTextView.setText("Não possui telefone");
        }else {
            TextView telephoneTextView = (TextView) findViewById(R.id.textViewDrugTel);
            telephoneTextView.setText("Tel: " + controller.getDrugstore().getTelephone());
            setPhoneCallListenner(controller.getDrugstore().getTelephone());
        }

        //set ratting for drugstore
        RatingBar ratingBarFinal = (RatingBar)findViewById(R.id.ratingBarFinalDrugstore);
        ratingBarFinal.setRating(controller.getDrugstore().getRate());

        TextView textViewRate = (TextView)findViewById(R.id.textViewRatingDrugstore);
        textViewRate.setText(""+controller.getDrugstore().getRate());

    }

    private void setPhoneCallListenner(final String telephone) {
        ImageButton phoneCallButton = (ImageButton) findViewById(R.id.phonecallButtonDrugstore);
        phoneCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws SecurityException {

                Intent phoneCall = new Intent(Intent.ACTION_CALL);
                phoneCall.setData(Uri.parse("tel:" + telephone));
                startActivity(phoneCall);

            }
        });
    }



}