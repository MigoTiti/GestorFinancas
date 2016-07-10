package com.titi.migo.gestordefinanca.telas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.titi.migo.gestordefinanca.R;
import com.titi.migo.gestordefinanca.util.AdministradorBD;

public class Home extends AppCompatActivity {

    LinearLayout conf;
    private AdministradorBD adminBD;
    private Spinner spinnerAnos;
    private Spinner spinnerMeses;
    private LinearLayout adicionar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        adminBD = new AdministradorBD(this);

        String[] meses = new String[]{"Janeiro", "Fevereiro", "Março", "Abril", "Maio",
                "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        String[] anos = new String[]{"2016", "2017", "2018", "2019",
                "2020", "2021", "2022"};

        ArrayAdapter<String> adaptadorMeses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, meses);
        adaptadorMeses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adaptadorAnos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, anos);
        adaptadorMeses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerMeses = (Spinner) findViewById(R.id.spinnerMes);
        spinnerMeses.setAdapter(adaptadorMeses);

        spinnerAnos = (Spinner) findViewById(R.id.spinnerAno);
        spinnerAnos.setAdapter(adaptadorAnos);

        adicionar = (LinearLayout) findViewById(R.id.botaoAdicionar);
        adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent atividade1 = new Intent(Home.this, Atividades.class);
                startActivity(atividade1);
                finish();
            }
        });

        spinnerMeses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView displayGanhos = (TextView) findViewById(R.id.quantiaGanho);
                displayGanhos.setText(Double.toString(adminBD.getSomatoriaAtividade(spinnerMeses.getSelectedItem().toString(),
                        spinnerAnos.getSelectedItem().toString(), "Ganho")));

                TextView displayPerdas = (TextView) findViewById(R.id.quantiaPerda);
                displayPerdas.setText(Double.toString(adminBD.getSomatoriaAtividade(spinnerMeses.getSelectedItem().toString(),
                        spinnerAnos.getSelectedItem().toString(), "Perda")));

                TextView displayTotal = (TextView) findViewById(R.id.quantiaDisponivel);
                displayTotal.setText(Double.toString(adminBD.getQuantia(spinnerMeses.getSelectedItem().toString(),
                        spinnerAnos.getSelectedItem().toString())));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                TextView displayGanhos = (TextView) findViewById(R.id.quantiaGanho);
                displayGanhos.setText("0,00");

                TextView displayPerdas = (TextView) findViewById(R.id.quantiaPerda);
                displayPerdas.setText("0,00");

                TextView displayTotal = (TextView) findViewById(R.id.quantiaDisponivel);
                displayTotal.setText("0,00");
            }
        });

        spinnerAnos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView displayGanhos = (TextView) findViewById(R.id.quantiaGanho);
                displayGanhos.setText(Double.toString(adminBD.getSomatoriaAtividade(spinnerMeses.getSelectedItem().toString(),
                        spinnerAnos.getSelectedItem().toString(), "Ganho")));

                TextView displayPerdas = (TextView) findViewById(R.id.quantiaPerda);
                displayPerdas.setText(Double.toString(adminBD.getSomatoriaAtividade(spinnerMeses.getSelectedItem().toString(),
                        spinnerAnos.getSelectedItem().toString(), "Perda")));

                TextView displayTotal = (TextView) findViewById(R.id.quantiaDisponivel);
                displayTotal.setText(Double.toString(adminBD.getQuantia(spinnerMeses.getSelectedItem().toString(),
                        spinnerAnos.getSelectedItem().toString())));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                TextView displayGanhos = (TextView) findViewById(R.id.quantiaGanho);
                displayGanhos.setText("0,00");

                TextView displayPerdas = (TextView) findViewById(R.id.quantiaPerda);
                displayPerdas.setText("0,00");

                TextView displayTotal = (TextView) findViewById(R.id.quantiaDisponivel);
                displayTotal.setText("0,00");
            }
        });

        TextView displayGanhos = (TextView) findViewById(R.id.quantiaGanho);
        displayGanhos.setText(Double.toString(adminBD.getSomatoriaAtividade(spinnerMeses.getSelectedItem().toString(),
                spinnerAnos.getSelectedItem().toString(), "Ganho")));

        TextView displayPerdas = (TextView) findViewById(R.id.quantiaPerda);
        displayPerdas.setText(Double.toString(adminBD.getSomatoriaAtividade(spinnerMeses.getSelectedItem().toString(),
                spinnerAnos.getSelectedItem().toString(), "Perda")));

        TextView displayTotal = (TextView) findViewById(R.id.quantiaDisponivel);
        displayTotal.setText(Double.toString(adminBD.getQuantia(spinnerMeses.getSelectedItem().toString(),
                spinnerAnos.getSelectedItem().toString())));
    }


}
