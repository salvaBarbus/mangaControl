package com.barbus.mangacontrol.DAOs;

import android.net.Uri;

/**
 * Created by SalvadorGiralt on 9/07/14 for Manga Control
 */
public class Volumen {

    //definición de los nombres de las columnas
    String sqlCreateVolumen = "CREATE TABLE volumen "+
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "numero INTEGER, "+
            "nombre TEXT, "+
            "idSerie INTEGER, "+
            "idTipoVolumen INTEGER, "+
            "idEditorial INTEGER )";

    public static final String _ID = "_id";
    public static final String NUMERO = "numero";
    public static final String NOMBRE = "nombre";
    public static final String ID_SERIE = "idSerie";
    public static final String ID_TIPO_VOLUMEN = "idTipoVolumen";
    public static final String ID_EDITORIAL = "idEditorial";
    public static final String COMPRADO = "comprado";

    //Definición de la Uri para consultas
    public static final String URI = "content://com.barbus.controlmanga.contentproviders/volumenes/";
    public static final Uri CONTENT_URI = Uri.parse(URI);

    //variables privadas para almacenar los valores del elemento
    private long id;
    private long numero;
    private String nombre;
    private long idSerie;
    private long idTipoVolumen;
    private long idEditorial;
    private long comprado;

    //constructores
    public Volumen(){}

    public Volumen(long id, long numero, String nombre, long idSerie,
                   long idTipoVolumen, long idEditorial, long comprado)
    {
        this.id = id;
        this.numero = numero;
        this.nombre = nombre;
        this.idSerie = idSerie;
        this.idTipoVolumen = idTipoVolumen;
        this.idEditorial = idEditorial;
        this.comprado = comprado;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getIdSerie() {
        return idSerie;
    }

    public void setIdSerie(long idSerie) {
        this.idSerie = idSerie;
    }

    public long getIdTipoVolumen() {
        return idTipoVolumen;
    }

    public void setIdTipoVolumen(long idTipoVolumen) {
        this.idTipoVolumen = idTipoVolumen;
    }

    public long getIdEditorial() {
        return idEditorial;
    }

    public void setIdEditorial(long idEditorial) {
        this.idEditorial = idEditorial;
    }

    public long getComprado() {
        return comprado;
    }

    public void setComprado(long comprado) {
        this.comprado = comprado;
    }
}
