package com.barbus.mangacontrol.DAOs;

import android.net.Uri;

/**
 * Created by salva on 28/08/15.
 */
public class AutorDAO {

    //definición de los nombres de las columnas
    public static final String _ID = "_id";
    public static final String NOMBRE_AUTOR = "nombre";

    //Definición de la Uri para consultas
    public static final String URI = "content://com.barbus.controlmanga.contentproviders/autor/";
    public static final Uri CONTENT_URI = Uri.parse(URI);

    public static Uri getContentUriWithAppendedId(long id) {
        return Uri.withAppendedPath(CONTENT_URI, String.valueOf(id));
    }

}
