package com.titi.migo.gestordefinanca.telas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.titi.migo.gestordefinanca.R;
import com.titi.migo.gestordefinanca.util.AdministradorBD;

public class Opcoes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcoes);

        Button resetar = (Button) findViewById(R.id.botaoResetar);
        resetar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdministradorBD admin = new AdministradorBD(view.getContext());
                admin.resetBanco();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent home = new Intent(this, Home.class);
        startActivity(home);
        finish();
    }
}
