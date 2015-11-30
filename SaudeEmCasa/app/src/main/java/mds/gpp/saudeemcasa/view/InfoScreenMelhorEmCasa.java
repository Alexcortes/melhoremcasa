package mds.gpp.saudeemcasa.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import mds.gpp.saudeemcasa.R;

/**
 * Created by iolane on 28/11/15.
 */
public class InfoScreenMelhorEmCasa extends FragmentActivity {

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.melhor_em_casa_info);

            TextView nameTextView = (TextView) findViewById(R.id.text_info_melhor_em_casa);
            nameTextView.setText("O programa Melhor em Casa, é um programa relacionado a área de saúde, criado pelo Governo Federal em 2013, com as seguintes propostas:\n" +
                    "\n" +
                    "Atendimento as pessoas com necessidade de reabilitação motora, idosos, pacientes crônicos sem agravamento ou em situação pós-cirúrgica\n" +
                    "\n" +
                    "Melhorar e ampliar a assistência do SUS a pacientes com agravos de saúde que possam receber atendimento em casa, e perto da família,\n" +
                    "\n" +
                    "Contribuir para a recuperação de doenças, visto que, carinho e atenção familiar aliados à adequada assistência em saúde se tornam elementos importantes para a recuperação do paciente.\n");
        }
}
