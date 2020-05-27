package com.katapanda.op.library;

import android.content.Context;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class CusFormat {
    public static String namaBulan(String bulan){
        if (bulan.equals("01")){
            return "Januari";
        } else if (bulan.equals("02")){
            return "Febuari";
        } else  if (bulan.equals("03")){
            return "Maret";
        } else  if (bulan.equals("04")){
            return "April";
        } else  if (bulan.equals("05")){
            return "Mei";
        } else if (bulan.equals("06")){
            return "Juni";
        } else if (bulan.equals("07")){
            return "Juli";
        } else if (bulan.equals("08")){
            return "Agustus";
        } else if (bulan.equals("09")){
            return "September";
        } else if (bulan.equals("10")){
            return "Oktober";
        } else if (bulan.equals("11")){
            return "November";
        } else {
            return "Desember";
        }
    }
    public static String rupiah(double val){
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(val);
    }

    public static String tanggalFormat(String tanggal, String formatAsal, String formatTujuan) {
        try {
            //Format Asal
            DateFormat dateFormat = new SimpleDateFormat(formatAsal);
            //Jadi bentuk date
            Date dateAwal = dateFormat.parse(tanggal);
            //Format Tujuan
            DateFormat formats = new SimpleDateFormat(formatTujuan);

            return formats.format(dateAwal);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tanggal;
    }
    public static void toasMessage(Context context, String text, String color) {
        if (color.equals("success")) {
            Toasty.success(context, text, Toast.LENGTH_SHORT, true).show();
        } else if (color.equals("danger")) {
            Toasty.error(context, text, Toast.LENGTH_SHORT, true).show();
        } else if (color.equals("info")) {
            Toasty.info(context, text, Toast.LENGTH_SHORT, true).show();
        } else {
            Toasty.warning(context, text, Toast.LENGTH_SHORT, true).show();
        }
    }
}
