package com.barbus.mangacontrol.DAOs;

import android.net.Uri;

/**
 * Created by SalvadorGiralt on 30/07/14 for Manga Control
 */
public class Editorial {

    String sqlCreateEditoriales = "CREATE TABLE editorial "+
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "nombreEditorial TEXT )";

    //definicion de los nombres de las columnas
    public static final String _ID = "_id";
    public static final String NOMBRE_EDITORIAL = "nombreEditorial";

    //Definición de la Uri para consultas
    public static final String URI = "content://com.barbus.controlmanga.contentproviders/editoriales/";
    public static final Uri CONTENT_URI = Uri.parse(URI);

    //variables privadas para almacenar los datos del elemento
    private Long id;
    private String nombreEditorial;

    //constructores
    public Editorial(){}

    public Editorial(Long id, String nombreEditorial)
    {
        this.id = id;
        this.nombreEditorial = nombreEditorial;
    }

    public String getNombreEditorial() {
        return nombreEditorial;
    }

    public void setNombreEditorial(String nombreEditorial) {
        this.nombreEditorial = nombreEditorial;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
