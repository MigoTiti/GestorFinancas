package com.titi.migo.gestordefinanca.telas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.titi.migo.gestordefinanca.R;
import com.titi.migo.gestordefinanca.util.AdministradorBD;

public class Home extends AppCompatActivity {

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
            }
        });
    }

    AdministradorBD adminBD;
    Spinner spinnerAnos;
    Spinner spinnerMeses;
    LinearLayout adicionar;
    LinearLayout conf;
}
