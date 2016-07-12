package com.titi.migo.gestordefinanca.telas;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.titi.migo.gestordefinanca.R;
import com.titi.migo.gestordefinanca.util.AdministradorBD;

import java.util.HashMap;
import java.util.Locale;

public class Opcoes extends AppCompatActivity {

    private AdministradorBD admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcoes);

        admin = new AdministradorBD(this);

        Button resetar = (Button) findViewById(R.id.botaoResetar);
        resetar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                admin.resetBanco();
            }
        });

        String[] idiomas = new String[]{"English", "Português"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, idiomas);

        final Spinner idiomaLista = (Spinner) findViewById(R.id.spinnerLinguagem);
        idiomaLista.setAdapter(adapter);

        final HashMap<String, String> map = new HashMap<>();
        map.put("Português", "pt");
        map.put("English", "en");

        final String idiomaAtual = PreferenceManager.getDefaultSharedPreferences(this).getString("LANG", "");
        String nome = null;

        for (String o : map.keySet())
            if (map.get(o).equals(idiomaAtual)) {
                nome = o;
                break;
            }

        int indice = 0;

        for (int i = 0; i < idiomas.length; i++)
            if (idiomas[i].equals(nome)) {
                indice = i;
                break;
            }

        idiomaLista.setSelection(indice, false);

        idiomaLista.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String idioma = map.get(idiomaLista.getSelectedItem().toString());
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", idioma).commit();

                setLangRecreate(idioma);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setLangRecreate(String langval) {
        Configuration config = getBaseContext().getResources().getConfiguration();
        Locale locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        recreate();
    }

    @Override
    public void onBackPressed() {
        Intent home = new Intent(this, Home.class);
        startActivity(home);
        finish();
    }
}
