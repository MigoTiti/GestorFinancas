package com.titi.migo.gestordefinanca.telas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.titi.migo.gestordefinanca.R;
import com.titi.migo.gestordefinanca.util.AdministradorBD;

import java.util.HashMap;

public class Atividades extends AppCompatActivity {

    private void popularDetalhes(int id, View v) {
        Cursor detalhesAtividade = adminBD.getAtividadePorIDGeral(id);
        detalhesAtividade.moveToNext();

        TextView nomeTexto = (TextView) v.findViewById(R.id.nomeTextoDiag);
        TextView tipoTexto = (TextView) v.findViewById(R.id.tipoTextoDiag);
        TextView anoInicioTexto = (TextView) v.findViewById(R.id.anoInicioTextoDiag);
        TextView mesInicioTexto = (TextView) v.findViewById(R.id.mesInicioTextoDiag);
        TextView anoFimTexto = (TextView) v.findViewById(R.id.anoFimTextoDiag);
        TextView mesFimTexto = (TextView) v.findViewById(R.id.mesFimTextoDiag);
        TextView valorTexto = (TextView) v.findViewById(R.id.valorTextoDiag);
        TextView parcelasTexto = (TextView) v.findViewById(R.id.parcelasTextoDiag);

        nomeAtividadeAux = detalhesAtividade.getString(0);

        nomeTexto.setText(nomeAtividadeAux);
        tipoTexto.setText(detalhesAtividade.getString(1));
        anoInicioTexto.setText(detalhesAtividade.getString(2));
        mesInicioTexto.setText(detalhesAtividade.getString(3));
        anoFimTexto.setText(detalhesAtividade.getString(4));
        mesFimTexto.setText(detalhesAtividade.getString(5));
        valorTexto.setText(detalhesAtividade.getString(6));
        parcelasTexto.setText(Integer.toString(adminBD.getContagemRegistrosPorNome(nomeAtividadeAux)));
    }

    private void removerParcelas() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividades);

        adminBD = new AdministradorBD(this);

        atualizarLista();

        final ListView lista = (ListView) findViewById(R.id.listView);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final View detalhes = View.inflate(view.getContext(), R.layout.layout_detalhes, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                Cursor item = (Cursor) lista.getItemAtPosition(i);
                int id = item.getInt(0);

                builder.setTitle("Detalhes")
                        .setView(detalhes)
                        .setCancelable(true)
                        .create();

                popularDetalhes(id, detalhes);

                final Button removerAtividade = (Button) detalhes.findViewById(R.id.removerTudoB);
                Button removerParcela = (Button) detalhes.findViewById(R.id.removerParcelaB);
                Button editarTudo = (Button) detalhes.findViewById(R.id.editarTudoB);
                Button editarParcela = (Button) detalhes.findViewById(R.id.editarParcelaB);

                final AlertDialog a = builder.show();

                removerAtividade.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adminBD.deletarTodasAtividades(nomeAtividadeAux);
                        atualizarLista();
                        a.dismiss();
                    }
                });

                removerParcela.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final View removerParcela = View.inflate(view.getContext(), R.layout.layout_remover_parcela, null);
                        final AlertDialog.Builder construtor = new AlertDialog.Builder(view.getContext());

                        construtor.setTitle("Remover parcelas");
                        construtor.setMessage("Escolha uma ou mais parcelar para remoção: ");
                        construtor.setView(removerParcela)
                                .setCancelable(false)
                                .create();

                        ListView listaParcelas = (ListView) removerParcela.findViewById(R.id.listaParcela);
                        final Cursor atividades = adminBD.getAtividadesPorNome(nomeAtividadeAux);

                        String[] colunas = new String[]{"ano", "mes", "valor"};
                        int[] widgets = new int[]{R.id.anoListaParcela, R.id.mesListaParcela, R.id.valorListaParcela};

                        SimpleCursorAdapter adapter = new SimpleCursorAdapter(view.getContext(), R.layout.lista_parcelas,
                                atividades, colunas, widgets, 0);

                        listaParcelas.setAdapter(adapter);

                        final Button removerParcelas = (Button) removerParcela.findViewById(R.id.removerParcelaBotao);
                        final Button cancelar = (Button) removerParcela.findViewById(R.id.button);

                        final AlertDialog dialogo = construtor.show();

                        removerParcelas.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                removerParcelas();
                                dialogo.dismiss();
                            }
                        });

                        cancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogo.dismiss();
                            }
                        });
                    }
                });

                editarTudo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                editarParcela.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        });

        adicionarAtividade = (Button) findViewById(R.id.adicionarAtividade);
        adicionarAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final View dialogoAdicionar = View.inflate(view.getContext(), R.layout.layout_adicionar_atividade, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setTitle("Adicionar atividade")
                        .setMessage("Preencha todos os campos abaixo")
                        .setView(dialogoAdicionar)
                        .setCancelable(false);

                String[] meses = new String[]{"Janeiro", "Fevereiro", "Março", "Abril", "Maio",
                        "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
                String[] anos = new String[]{"2016", "2017", "2018", "2019",
                        "2020", "2021", "2022"};

                ArrayAdapter<String> adaptadorMeses = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, meses);
                adaptadorMeses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                ArrayAdapter<String> adaptadorAnos = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, anos);
                adaptadorMeses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                final Spinner anoInicio = (Spinner) dialogoAdicionar.findViewById(R.id.spinnerAnoInicioAdicionar);
                final Spinner anoFim = (Spinner) dialogoAdicionar.findViewById(R.id.spinnerAnoTerminoAdicionar);

                anoInicio.setAdapter(adaptadorAnos);
                anoFim.setAdapter(adaptadorAnos);

                final Spinner mesInicio = (Spinner) dialogoAdicionar.findViewById(R.id.spinnerMesInicioAdicionar);
                final Spinner mesFim = (Spinner) dialogoAdicionar.findViewById(R.id.spinnerMesTerminoAdicionar);

                mesInicio.setAdapter(adaptadorMeses);
                mesFim.setAdapter(adaptadorMeses);

                builder.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText nome = (EditText) dialogoAdicionar.findViewById(R.id.nome);
                        String nomeTexto = nome.getText().toString();

                        if (!checarNome(nomeTexto)) {
                            EditText valor = (EditText) dialogoAdicionar.findViewById(R.id.valor);
                            String valorTexto = valor.getText().toString();

                            try {
                                double valorAux = Double.parseDouble(valorTexto);
                                String anoInicioTexto = anoInicio.getSelectedItem().toString();
                                String anoFimTexto = anoFim.getSelectedItem().toString();
                                String mesFimTexto = mesFim.getSelectedItem().toString();
                                String mesInicioTexto = mesInicio.getSelectedItem().toString();
                                Switch s = (Switch) dialogoAdicionar.findViewById(R.id.switchGanho);

                                String tipo;
                                if (s.isChecked())
                                    tipo = "Ganho";
                                else
                                    tipo = "Perda";

                                adicionarAtividade(nomeTexto, tipo, anoInicioTexto, anoFimTexto,
                                        mesInicioTexto, mesFimTexto, valorAux);
                                atualizarLista();
                            } catch (NumberFormatException e) {
                                criarDialogoErro(dialogoAdicionar.getContext(), "Digite um valor válido.");
                                valor.setText("");
                            }
                        } else {
                            criarDialogoErro(dialogoAdicionar.getContext(), "Nome já existente no banco de dados.");
                            nome.setText("");
                            dialog.cancel();
                        }
                    }
                })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).show();
            }
        });
    }

    private void criarDialogoErro(Context c, String mensagem) {
        final AlertDialog.Builder dlg = new AlertDialog.Builder(c);
        dlg.setMessage(mensagem);
        dlg.setTitle("Erro");
        dlg.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        dlg.setCancelable(true);
        dlg.create().show();
    }

    private void adicionarAtividade(String nome, String tipo, String anoInicio, String anoFim,
                                    String mesInicio, String mesFim, double valor) {
        adminBD.adicionarNomeUnico(nome);

        HashMap<String, Integer> meses = new HashMap<>();
        meses.put("Janeiro", 1);
        meses.put("Fevereiro", 2);
        meses.put("Março", 3);
        meses.put("Abril", 4);
        meses.put("Maio", 5);
        meses.put("Junho", 6);
        meses.put("Julho", 7);
        meses.put("Agosto", 8);
        meses.put("Setembro", 9);
        meses.put("Outubro", 10);
        meses.put("Novembro", 11);
        meses.put("Dezembro", 12);

        if (anoInicio.equals(anoFim)) {
            if (mesInicio.equals(mesFim))
                adminBD.adicionarAtividade(nome, tipo, anoInicio, mesInicio, anoInicio, anoInicio, mesInicio, mesFim, valor);
            else {
                for (int i = meses.get(mesInicio); i <= meses.get(mesFim); i++) {
                    String mes = "";

                    for (String o : meses.keySet()) {
                        if (meses.get(o).equals(i)) {
                            mes = o;
                            break;
                        }
                    }

                    adminBD.adicionarAtividade(nome, tipo, anoInicio, mes, anoInicio, anoInicio, mesInicio, mesFim, valor);
                }
            }
        } else {
            int nVoltas = Integer.parseInt(anoFim) - Integer.parseInt(anoInicio);

            for (int i = meses.get(mesInicio); i <= 12; i++) {
                String mes = "";

                for (String o : meses.keySet())
                    if (meses.get(o).equals(i)) {
                        mes = o;
                        break;
                    }

                adminBD.adicionarAtividade(nome, tipo, anoInicio, mes, anoInicio, anoFim, mesInicio, mesFim, valor);
            }

            nVoltas--;
            int nVoltasInv = 1;

            while (nVoltas > 0) {
                for (int i = 1; i <= 12; i++) {
                    String mes = "";

                    for (String o : meses.keySet())
                        if (meses.get(o).equals(i)) {
                            mes = o;
                            break;
                        }

                    adminBD.adicionarAtividade(nome, tipo, Integer.toString(Integer.parseInt(anoInicio) + nVoltasInv), mes, anoInicio, anoFim, mesInicio, mesFim, valor);
                }
                nVoltas--;
                nVoltasInv++;
            }

            if (meses.get(mesFim) == 1) {
                adminBD.adicionarAtividade(nome, tipo, Integer.toString(Integer.parseInt(anoInicio) + nVoltasInv),
                        "Janeiro", anoInicio, anoFim, mesInicio, mesFim, valor);
            } else {
                for (int i = 1; i <= meses.get(mesFim); i++) {
                    String mes = "";

                    for (String o : meses.keySet())
                        if (meses.get(o).equals(i)) {
                            mes = o;
                            break;
                        }

                    adminBD.adicionarAtividade(nome, tipo, Integer.toString(Integer.parseInt(anoInicio) + nVoltasInv), mes, anoInicio, anoFim, mesInicio, mesFim, valor);
                }
            }
        }
    }

    private void atualizarLista() {
        final Cursor atividades = adminBD.getAtividadesDistintas();

        String[] colunas = new String[]{"_id", "valor", "tipo"};
        int[] widgets = new int[]{R.id.idTexto, R.id.valorTexto, R.id.tipoTexto};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.lista_atividade,
                atividades, colunas, widgets, 0);

        lista = (ListView) findViewById(R.id.listView);
        lista.setAdapter(adapter);
    }

    private boolean checarNome(String nome) {
        return adminBD.isNomeUnico(nome);
    }

    String nomeAtividadeAux;
    Button adicionarAtividade;
    ListView lista;
    AdministradorBD adminBD;
}
