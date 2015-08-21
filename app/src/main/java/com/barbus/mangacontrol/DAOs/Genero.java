package com.barbus.mangacontrol.DAOs;

import android.net.Uri;

/**
 * Created by SalvadorGiralt on 25/07/14 for Manga Control
 */
public class Genero {

    //definición de los nombres de las columnas
    public static final String _ID = "_id";
    public static final String NOMBRE_GENERO = "nombreGenero";

    //Definición de la Uri para consultas
    public static final String URI = "content://com.barbus.controlmanga.contentproviders/genero/";
    public static final Uri CONTENT_URI = Uri.parse(URI);

    //variables privadas para almacenar los datos del elemento
    private long id;
    private String nombreGenero;

    public Genero(){}

    public Genero(long id, String nombreGenero)
    {
        this.id = id;
        this.nombreGenero = nombreGenero;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombreGenero() {
        return nombreGenero;
    }

    public void setNombreGenero(String nombreGenero) {
        this.nombreGenero = nombreGenero;
    }
}
