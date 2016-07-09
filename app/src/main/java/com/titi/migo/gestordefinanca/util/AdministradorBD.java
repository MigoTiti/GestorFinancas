package com.titi.migo.gestordefinanca.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                "anoinicio VARCHAR(4), " +
                "mesinicio VARCHAR(45), " +
                "anofim VARCHAR (4), " +
                "mesfim VARCHAR(45), " +
                "valor REAL " +
                ");");

        BD.execSQL("CREATE TABLE nomesunicos " +
                "(" +
                "_id INTEGER PRIMARY KEY NOT NULL UNIQUE, " +
                "nome VARCHAR(45) NOT NULL " +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase BD, int versaoAntiga, int versaoNova) {
        BD.execSQL("DROP TABLE IF EXISTS ano");
        BD.execSQL("DROP TABLE IF EXISTS mes");
        BD.execSQL("DROP TABLE IF EXISTS atividade");
        onCreate(BD);
    }

    public Cursor getAtividades() {
        return this.getReadableDatabase().rawQuery("SELECT * FROM atividade", null);
    }

    public Cursor getAtividadePorID(int id) {
        return this.getReadableDatabase().rawQuery("SELECT nome, tipo, ano, mes, anoinicio, mesinicio, anofim, mesfim, valor" +
                " FROM atividade WHERE _id = ?", new String[]{Integer.toString(id)});
    }

    public int getContagemRegistrosPorNome(String nome) {
        Cursor aux = this.getReadableDatabase().rawQuery("SELECT count(*) FROM atividade WHERE nome = ? ", new String[]{nome});
        aux.moveToNext();
        return aux.getInt(0);
    }

    public Cursor getAtividadesDistintas() {
        return this.getReadableDatabase().rawQuery("SELECT * FROM atividade GROUP BY nome ORDER BY _id ASC ", null);
    }

    public Cursor getAtividadesPorNome(String nome) {
        return this.getReadableDatabase().rawQuery("SELECT * FROM atividade WHERE nome = ?  ORDER BY _id ASC", new String[]{nome});
    }

    public boolean isNomeUnico(String nome) {
        Cursor aux = this.getReadableDatabase().rawQuery("SELECT nome FROM nomesunicos WHERE nome = ? ", new String[]{nome});
        return aux.getCount() > 0;
    }

    public void adicionarNomeUnico(String nome) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("nome", nome);
        this.getWritableDatabase().insert("nomesunicos", null, contentValues);
    }

    public void removerNomeUnico(String nome){
        this.getWritableDatabase().execSQL("DELETE FROM nomesunicos WHERE nome = ? ", new String[]{nome});
    }

    public void adicionarAtividade(String nome, String tipo, String ano, String mes, String anoInicio, String anoFim
            , String mesInicio, String mesFim, double valor) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("nome", nome);
        contentValues.put("tipo", tipo);
        contentValues.put("ano", ano);
        contentValues.put("mes", mes);
        contentValues.put("anoinicio", anoInicio);
        contentValues.put("anofim", anoFim);
        contentValues.put("mesinicio", mesInicio);
        contentValues.put("mesfim", mesFim);
        contentValues.put("valor", valor);
        this.getWritableDatabase().insert("atividade", null, contentValues);
    }

    public void atualizarAtividade(int id, String nome, String tipo, String ano, String mes, String anoInicio, String anoFim
            , String mesInicio, String mesFim, double valor) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("nome", nome);
        contentValues.put("tipo", tipo);
        contentValues.put("ano", ano);
        contentValues.put("mes", mes);
        contentValues.put("anoinicio", anoInicio);
        contentValues.put("anofim", anoFim);
        contentValues.put("mesinicio", mesInicio);
        contentValues.put("mesfim", mesFim);
        contentValues.put("valor", valor);
        this.getWritableDatabase().update("atividade", contentValues, "_id = ? ", new String[]{Integer.toString(id)});
    }

    public void deletarAtividade(int id) {
        this.getWritableDatabase().delete("atividade", "_id = ? ", new String[]{Integer.toString(id)});
    }

    public void deletarAtividadePorData(String nome, String ano, String mes){
        this.getWritableDatabase().execSQL("DELETE FROM atividade WHERE nome = ? AND ano = ? AND mes = ? ", new String[]{nome, ano, mes});
    }

    public void deletarTodasAtividades(String nome) {
        this.getWritableDatabase().execSQL("DELETE FROM atividade WHERE nome = ? ", new String[]{nome});
    }
}
