package com.barbus.mangacontrol.DAOs;

/**
 * Created by SalvadorGiralt on 18/07/14 for Manga Control
 */
public class ElementoListaCompra {

    private String nombreSerie;
    private String literalVol;
    private String numeroVol;
    private long id;
    private boolean checked;

    public ElementoListaCompra(long id, String nombreSerie, String literalVol, String numeroVol)
    {
        this.id = id;
        this.setNombreSerie(nombreSerie);
        this.setLiteralVol(literalVol);
        this.setNumeroVol(numeroVol);
        this.setChecked(false);
    }

    @Override
    public String toString() {
        String aux = "";
        aux+="Valores del elemento:\nId="+this.id+"\nNombre Serie="+this.nombreSerie+"\nNumero Volumen=" +
                this.numeroVol+"\nCheckeado="+this.isChecked();
        return aux;
    }

    public String getNombreSerie() {
        return nombreSerie;
    }

    public void setNombreSerie(String nombreSerie) {
        this.nombreSerie = nombreSerie;
    }

    public String getLiteralVol() {
        return literalVol;
    }

    public void setLiteralVol(String literalVol) {
        this.literalVol = literalVol;
    }

    public String getNumeroVol() {
        return numeroVol;
    }

    public void setNumeroVol(String numeroVol) {
        this.numeroVol = numeroVol;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
