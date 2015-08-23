package com.barbus.mangacontrol.DAOs;

import android.net.Uri;

/**
 * Created by SalvadorGiralt on 8/07/14 for Manga Control
 */
public class Serie {

    //definicion de los nombres de las columnas
    public static final String _ID = "_id";
    public static final String NOMBRE_SERIE = "nombreSerie";
    public static final String ID_ESTADO_SERIE = "idEstadoSerie";
    public static final String ID_ESTADO_COLECCION = "idEstadoColeccion";
    public static final String NUM_VOLUMENES = "numVolumenes";
    public static final String ID_EDITORIAL = "idEditorial";
    public static final String ID_GENERO = "idGenero";

    //Definición de la Uri para consultas
    public static final String URI = "content://com.barbus.controlmanga.contentproviders/series/";
    public static final Uri CONTENT_URI = Uri.parse(URI);

    //variables privadas para almacenar los datos del elemento
    private long id;
    private String nombreSerie;
    private String idEditorial;
    private long idGenero;
    private long idEstadoSerie;
    private long idEstadoColeccion;
    private long numVolumenesTotales;
    private long numVolumenesActuales;

    public Serie(){}

    public Serie(long id, String nombreSerie, long idEstadoSerie, long idGenero,
                 long idEstadoColeccion, long numVolumenesTotales,
                 long numVolumenesActuales, String idEditorial)
    {
        this.id = id;
        this.nombreSerie = nombreSerie;
        this.idEstadoSerie = idEstadoSerie;
        this.idEstadoColeccion = idEstadoColeccion;
        this.numVolumenesTotales = numVolumenesTotales;
        this.numVolumenesActuales = numVolumenesActuales;
        this.idEditorial = idEditorial;
        this.idGenero = idGenero;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombreSerie() {
        return nombreSerie;
    }

    public void setNombreSerie(String nombreSerie) {
        this.nombreSerie = nombreSerie;
    }

    public long getIdEstadoSerie() {
        return idEstadoSerie;
    }

    public void setIdEstadoSerie(long idEstadoSerie) {
        this.idEstadoSerie = idEstadoSerie;
    }

    public long getIdEstadoColeccion() {
        return idEstadoColeccion;
    }

    public void setIdEstadoColeccion(long idEstadoColeccion) {
        this.idEstadoColeccion = idEstadoColeccion;
    }

    public long getNumVolumenesTotales() {
        return numVolumenesTotales;
    }

    public void setNumVolumenesTotales(long numVolumenesTotales) {
        this.numVolumenesTotales = numVolumenesTotales;
    }

    public long getNumVolumenesActuales() {
        return numVolumenesActuales;
    }

    public void setNumVolumenesActuales(long numVolumenesActuales) {
        this.numVolumenesActuales = numVolumenesActuales;
    }

    public String getIdEditorial() {
        return idEditorial;
    }

    public void setIdEditorial(String idEditorial) {
        this.idEditorial = idEditorial;
    }

    public long getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(long idGenero) {
        this.idGenero = idGenero;
    }
}
