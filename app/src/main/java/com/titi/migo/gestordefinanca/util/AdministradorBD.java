package com.titi.migo.gestordefinanca.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class AdministradorBD extends SQLiteOpenHelper {

    private final static String NOME_BD = "gestorDB.db";
    private final static int VERSAO_BD = 1;

    public AdministradorBD(Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase BD) {
        BD.execSQL("CREATE TABLE atividade " +
                "(" +
                "_id INTEGER PRIMARY KEY NOT NULL UNIQUE, " +
                "nome VARCHAR(45) NOT NULL, " +
                "tipo VARCHAR(5), " +
                "ano VARCHAR(4), " +
                "mes VARCHAR(45), " +
                "valor REAL " +
                ");");

        BD.execSQL("CREATE TABLE nomesunicos " +
                "(" +
                "_id INTEGER PRIMARY KEY NOT NULL UNIQUE, " +
                "nome VARCHAR(45) NOT NULL, " +
                "anoinicio VARCHAR(4), " +
                "mesinicio VARCHAR(45), " +
                "anofim VARCHAR (4), " +
                "mesfim VARCHAR(45) " +
                ");");

        BD.execSQL("CREATE TABLE valoresmes " +
                "(" +
                "_id INTEGER PRIMARY KEY NOT NULL UNIQUE, " +
                "ano VARCHAR(4), " +
                "mes VARCHAR(45), " +
                "quantiadisponivel REAL DEFAULT 0.0" +
                ");");

        HashMap<Integer, String> meses = new HashMap<>();
        meses.put(1, "Janeiro");
        meses.put(2, "Fevereiro");
        meses.put(3, "Março");
        meses.put(4, "Abril");
        meses.put(5, "Maio");
        meses.put(6, "Junho");
        meses.put(7, "Julho");
        meses.put(8, "Agosto");
        meses.put(9, "Setembro");
        meses.put(10, "Outubro");
        meses.put(11, "Novembro");
        meses.put(12, "Dezembro");

        for (int i = 2016; i <= 2022; i++) {
            for (int j = 1; j <= 12; j++) {
                ContentValues c = new ContentValues();
                c.put("ano", Integer.toString(i));
                c.put("mes", meses.get(j));
                BD.insert("valoresmes", null, c);
            }
        }

    }

    public Cursor procurarRegistro(String mes, String ano) {
        return this.getReadableDatabase().rawQuery("SELECT _id FROM valoresmes WHERE mes = ? AND ano = ? ", new String[]{mes, ano});
    }

    public double getSomatoriaAtividade(String mes, String ano, String tipo) {
        Cursor aux = this.getReadableDatabase().rawQuery("SELECT (SELECT sum(valor) FROM atividade WHERE mes = ? AND ano = ? AND tipo = ?) soma " +
                "FROM atividade", new String[]{mes, ano, tipo});
        if (aux.moveToNext()) {
            double d = aux.getDouble(0);
            aux.close();
            close();
            return d;
        } else {
            aux.close();
            close();
            return 0;
        }
    }

    public void resetBanco() {
        SQLiteDatabase bd = this.getWritableDatabase();
        bd.execSQL("DELETE FROM atividade ");
        bd.execSQL("DELETE FROM nomesunicos ");
        bd.execSQL("DELETE FROM valoresmes ");

        HashMap<Integer, String> meses = new HashMap<>();
        meses.put(1, "Janeiro");
        meses.put(2, "Fevereiro");
        meses.put(3, "Março");
        meses.put(4, "Abril");
        meses.put(5, "Maio");
        meses.put(6, "Junho");
        meses.put(7, "Julho");
        meses.put(8, "Agosto");
        meses.put(9, "Setembro");
        meses.put(10, "Outubro");
        meses.put(11, "Novembro");
        meses.put(12, "Dezembro");

        for (int i = 2016; i <= 2022; i++) {
            for (int j = 1; j <= 12; j++) {
                ContentValues c = new ContentValues();
                c.put("ano", Integer.toString(i));
                c.put("mes", meses.get(j));
                bd.insert("valoresmes", null, c);
            }
        }

        bd.close();
    }

    public double getQuantia(String mes, String ano) {
        Cursor aux = this.getReadableDatabase().rawQuery("SELECT quantiadisponivel FROM valoresmes WHERE mes = ? AND ano = ? ",
                new String[]{mes, ano});
        aux.moveToNext();
        double d = aux.getDouble(0);
        aux.close();
        close();
        return d;
    }

    public void atualizarRegistroPorID(int id, double valor, boolean primeiraParcela) {
        Cursor aux = this.getReadableDatabase().rawQuery("SELECT quantiadisponivel FROM valoresmes WHERE _id = ? ",
                new String[]{Integer.toString(id)});
        aux.moveToNext();

        if (id == 1 || primeiraParcela) {
            double valorOriginal = aux.getDouble(0);
            aux.close();

            ContentValues c = new ContentValues();
            c.put("quantiadisponivel", valorOriginal + valor);

            this.getWritableDatabase().update("valoresmes", c, "_id = ?", new String[]{Integer.toString(id)});
            close();
        } else {
	        Cursor anoMes = this.getReadableDatabase().rawQuery("SELECT ano, mes FROM valoresmes WHERE _id = ?",
			        new String[]{Integer.toString(id)});

	        anoMes.moveToNext();
	        String ano = anoMes.getString(0);
	        String mes = anoMes.getString(1);

	        DatabaseUtils.dumpCursor(anoMes);

	        Cursor aux2 = this.getReadableDatabase().rawQuery("SELECT valor FROM atividade WHERE ano = ? AND mes = ?",
			        new String[]{ano, mes});

            double acc = 0;

	        while (aux2.moveToNext()) {
		        acc += aux2.getDouble(0);
	        }

            aux2.close();

            ContentValues c = new ContentValues();
	        c.put("quantiadisponivel", acc + valor);

            this.getWritableDatabase().update("valoresmes", c, "_id = ?", new String[]{Integer.toString(id)});
            close();
        }

    }

    public void atualizarRegistroAPartirDe(int id, double valor) {
        Cursor aux = this.getReadableDatabase().rawQuery("SELECT (SELECT max(_id) FROM valoresmes) id FROM valoresmes", null);
        aux.moveToNext();

        int maiorID = aux.getInt(0);
        aux.close();
        SQLiteDatabase b = getReadableDatabase();

        Cursor aux2 = b.rawQuery("SELECT quantiadisponivel FROM valoresmes WHERE _id = ?", new String[]{Integer.toString(id)});
        for (int i = id + 1; i <= maiorID; i++) {
            aux2 = b.rawQuery("SELECT quantiadisponivel FROM valoresmes WHERE _id = ?", new String[]{Integer.toString(i)});
            aux2.moveToNext();

            ContentValues c = new ContentValues();
            c.put("quantiadisponivel", valor + aux2.getDouble(0));
            b.update("valoresmes", c, "_id = ? ", new String[]{Integer.toString(i)});
        }
        aux2.close();
        b.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase BD, int versaoAntiga, int versaoNova) {
        BD.execSQL("DROP TABLE IF EXISTS atividade");
        onCreate(BD);
    }

    public int getContagemRegistrosPorNome(String nome) {
        Cursor aux = this.getReadableDatabase().rawQuery("SELECT count(*) FROM atividade WHERE nome = ? ", new String[]{nome});
        aux.moveToNext();
        int i = aux.getInt(0);
        aux.close();
        close();
        return i;
    }

    public Cursor getAtividadesDistintas() {
        return this.getReadableDatabase().rawQuery("SELECT * FROM atividade GROUP BY nome ORDER BY _id ASC ", null);
    }

    public Cursor getAtividadesPorNome(String nome) {
        return this.getReadableDatabase().rawQuery("SELECT * FROM atividade WHERE nome = ?  ORDER BY _id ASC", new String[]{nome});
    }

    public boolean isNomeUnico(String nome) {
        Cursor aux = this.getReadableDatabase().rawQuery("SELECT nome FROM nomesunicos WHERE nome = ? ", new String[]{nome});
        int i = aux.getCount();
        aux.close();
        close();
        return i > 0;
    }

    public Cursor getNomeUnico(String nome) {
        return this.getReadableDatabase().rawQuery("SELECT anoinicio, anofim, mesinicio, mesfim FROM nomesunicos WHERE nome = ? ", new String[]{nome});
    }

    public void adicionarNomeUnico(String nome, String anoInicio, String anoFim, String mesInicio, String mesFim) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("nome", nome);
        contentValues.put("anoinicio", anoInicio);
        contentValues.put("anofim", anoFim);
        contentValues.put("mesinicio", mesInicio);
        contentValues.put("mesfim", mesFim);
        this.getWritableDatabase().insert("nomesunicos", null, contentValues);
        close();
    }

    public void removerNomeUnico(String nome){
        this.getWritableDatabase().execSQL("DELETE FROM nomesunicos WHERE nome = ? ", new String[]{nome});
        close();
    }

    public void adicionarAtividade(String nome, String tipo, String ano, String mes, double valor) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("nome", nome);
        contentValues.put("tipo", tipo);
        contentValues.put("ano", ano);
        contentValues.put("mes", mes);
        contentValues.put("valor", valor);
        this.getWritableDatabase().insert("atividade", null, contentValues);
        close();
    }

    public void atualizarAtividadeGeral(String nomeOriginal, String nome, String tipo, double valor) {
        this.getWritableDatabase().execSQL("UPDATE atividade SET nome = ?, tipo = ?, valor = ? " +
                "WHERE nome = ? ", new String[]{nome, tipo, Double.toString(valor), nomeOriginal});
        this.getWritableDatabase().execSQL("UPDATE nomesunicos SET nome = ? WHERE nome = ? ",
                new String[]{nome, nomeOriginal});
        close();
    }

    public void deletarAtividadePorData(String nome, String ano, String mes){
        this.getWritableDatabase().execSQL("DELETE FROM atividade WHERE nome = ? AND ano = ? AND mes = ? ", new String[]{nome, ano, mes});
        close();
    }

    public void deletarTodasAtividades(String nome) {
        this.getWritableDatabase().execSQL("DELETE FROM atividade WHERE nome = ? ", new String[]{nome});
        close();
    }
}
