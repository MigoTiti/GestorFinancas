package com.titi.migo.gestordefinanca.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Formatador {

    DecimalFormat d;

    public Formatador(Locale locale) {
        NumberFormat f = NumberFormat.getNumberInstance(locale);
        d = (DecimalFormat) f;
        d.applyPattern("#0.00");
    }

    public String formatar(double numero) {
        return d.format(numero);
    }
}
