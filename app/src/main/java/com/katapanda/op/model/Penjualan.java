package com.katapanda.op.model;

public class Penjualan {
    String namaBrg, kategoriBrg, tglBrg;
    double hargaBrg, jmlBrg, totalBrg;

    public Penjualan() {
    }

    public Penjualan(String namaBrg, String kategoriBrg, String tglBrg, double hargaBrg, double jmlBrg, double totalBrg) {
        this.namaBrg = namaBrg;
        this.kategoriBrg = kategoriBrg;
        this.tglBrg = tglBrg;
        this.hargaBrg = hargaBrg;
        this.jmlBrg = jmlBrg;
        this.totalBrg = totalBrg;
    }

    public String getNamaBrg() {
        return namaBrg;
    }

    public void setNamaBrg(String namaBrg) {
        this.namaBrg = namaBrg;
    }

    public String getKategoriBrg() {
        return kategoriBrg;
    }

    public void setKategoriBrg(String kategoriBrg) {
        this.kategoriBrg = kategoriBrg;
    }

    public String getTglBrg() {
        return tglBrg;
    }

    public void setTglBrg(String tglBrg) {
        this.tglBrg = tglBrg;
    }

    public double getHargaBrg() {
        return hargaBrg;
    }

    public void setHargaBrg(double hargaBrg) {
        this.hargaBrg = hargaBrg;
    }

    public double getJmlBrg() {
        return jmlBrg;
    }

    public void setJmlBrg(double jmlBrg) {
        this.jmlBrg = jmlBrg;
    }

    public double getTotalBrg() {
        return totalBrg;
    }

    public void setTotalBrg(double totalBrg) {
        this.totalBrg = totalBrg;
    }
}
