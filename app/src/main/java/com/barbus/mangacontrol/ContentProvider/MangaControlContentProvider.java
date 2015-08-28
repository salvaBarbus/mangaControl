package com.barbus.mangacontrol.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.barbus.mangacontrol.Database.DatabaseSqliteHelper;
import com.barbus.mangacontrol.Database.NamedQueries;

/**
 * Created by SalvadorGiralt on 3/07/14.
 */
public class MangaControlContentProvider extends ContentProvider {

    //Definimos los CONTENT_URI
    private static final String uri =
            "content://com.barbus.controlmanga.contentproviders/";

    public static final Uri CONTENT_URI = Uri.parse(uri);

    //Bases de datos:
    private DatabaseSqliteHelper dbsqlhlpr;
    private static final String DB_NAME = "controlManga.db";
    private static final int DB_VERSION = 3;
    private static final String TABLE_SERIES = "serie";
    private static final String TABLE_ESTADOSERIES = "estadoSerie";
    private static final String TABLE_LNKSERIEVOLS = "lnkserievol";
    private static final String TABLE_TIPOSRELACION = "tiporelacion";
    private static final String TABLE_VOLUMENES = "volumen";
    private static final String TABLE_EDITORIALES = "editorial";
    private static final String TABLE_TIPOSVOLUMEN = "tipovolumen";
    private static final String TABLE_ESTADOCOLECCION = "estadoColeccion";
    private static final String TABLE_GENERO = "genero";

    //Definimos el UriMatcher
    private static final int SERIE = 1;
    private static final int SERIE_ID = 2;
    private static final int ESTADOSERIE = 3;
    private static final int ESTADOSERIE_ID = 4;
    private static final int LNKSERIEVOL = 5;
    private static final int LNKSERIEVOL_ID = 6;
    private static final int TIPORELACION = 7;
    private static final int TIPORELACION_ID = 8;
    private static final int VOLUMEN = 9;
    private static final int VOLUMEN_ID = 10;
    private static final int EDITORIAL = 11;
    private static final int EDITORIAL_ID = 12;
    private static final int TIPOVOLUMEN = 13;
    private static final int TIPOVOLUMEN_ID = 14;
    private static final int ESTADOCOLECCION = 15;
    private static final int ESTADOCOLECCION_ID = 16;
    private static final int GENERO = 17;
    private static final int GENERO_ID = 18;

    private static final UriMatcher uriMatcher;

    //Inicializamos el UriMatcher
    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.barbus.controlmanga.contentproviders", "series", SERIE);
        uriMatcher.addURI("com.barbus.controlmanga.contentproviders", "series/#", SERIE_ID);
        uriMatcher.addURI("com.barbus.controlmanga.contentproviders", "estadoSeries", ESTADOSERIE);
        uriMatcher.addURI("com.barbus.controlmanga.contentproviders", "estadoSeries/#", ESTADOSERIE_ID);
        uriMatcher.addURI("com.barbus.controlmanga.contentproviders", "lnkSerieVols", LNKSERIEVOL);
        uriMatcher.addURI("com.barbus.controlmanga.contentproviders", "lnkSerieVols/#", LNKSERIEVOL_ID);
        uriMatcher.addURI("com.barbus.controlmanga.contentproviders", "tiposRelacion", TIPORELACION);
        uriMatcher.addURI("com.barbus.controlmanga.contentproviders", "tiposRelacion/#", TIPORELACION_ID);
        uriMatcher.addURI("com.barbus.controlmanga.contentproviders", "volumenes", VOLUMEN);
        uriMatcher.addURI("com.barbus.controlmanga.contentproviders", "volumenes/#", VOLUMEN_ID);
        uriMatcher.addURI("com.barbus.controlmanga.contentproviders", "editoriales", EDITORIAL);
        uriMatcher.addURI("com.barbus.controlmanga.contentproviders", "editoriales/#", EDITORIAL_ID);
        uriMatcher.addURI("com.barbus.controlmanga.contentproviders", "tiposVolumen", TIPOVOLUMEN);
        uriMatcher.addURI("com.barbus.controlmanga.contentproviders", "tiposVolumen/#", TIPOVOLUMEN_ID);
        uriMatcher.addURI("com.barbus.controlmanga.contentproviders", "estadoColeccion", ESTADOCOLECCION);
        uriMatcher.addURI("com.barbus.controlmanga.contentproviders", "estadoColeccion/#", ESTADOCOLECCION_ID);
        uriMatcher.addURI("com.barbus.controlmanga.contentproviders", "genero", GENERO);
        uriMatcher.addURI("com.barbus.controlmanga.contentproviders", "genero/#", GENERO_ID);
    }

    @Override
    public boolean onCreate() {
        dbsqlhlpr = new DatabaseSqliteHelper(getContext(), DB_NAME, null, DB_VERSION);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        //Montamos el SWITCH OF DOOOOOOOM
        String where = selection;
        String table = "";
        int match = uriMatcher.match(uri);
        switch (match) {
            case SERIE:
                table = TABLE_SERIES;
                break;
            case SERIE_ID:
                table = TABLE_SERIES;
                where = "_id="+uri.getLastPathSegment();
                break;
            case ESTADOSERIE:
                table = TABLE_ESTADOSERIES;
                break;
            case ESTADOSERIE_ID:
                table = TABLE_ESTADOSERIES;
                where = "_id="+uri.getLastPathSegment();
                break;
            case LNKSERIEVOL:
                table = TABLE_LNKSERIEVOLS;
                break;
            case LNKSERIEVOL_ID:
                table = TABLE_LNKSERIEVOLS;
                where = "_id="+uri.getLastPathSegment();
                break;
            case TIPORELACION:
                table = TABLE_TIPOSRELACION;
                break;
            case TIPORELACION_ID:
                table = TABLE_TIPOSRELACION;
                where = "_id="+uri.getLastPathSegment();
                break;
            case VOLUMEN:
                table = TABLE_VOLUMENES;
                break;
            case VOLUMEN_ID:
                table = TABLE_VOLUMENES;
                where = "_id="+uri.getLastPathSegment();
                break;
            case EDITORIAL:
                table = TABLE_EDITORIALES;
                break;
            case EDITORIAL_ID:
                table = TABLE_EDITORIALES;
                where = "_id="+uri.getLastPathSegment();
                break;
            case TIPOVOLUMEN:
                table = TABLE_TIPOSVOLUMEN;
                break;
            case TIPOVOLUMEN_ID:
                table = TABLE_TIPOSVOLUMEN;
                where = "_id="+uri.getLastPathSegment();
                break;
            case ESTADOCOLECCION:
                table = TABLE_ESTADOCOLECCION;
                break;
            case ESTADOCOLECCION_ID:
                table = TABLE_ESTADOCOLECCION;
                where = "_id="+uri.getLastPathSegment();
                break;
            case GENERO:
                table = TABLE_GENERO;
                break;
            case GENERO_ID:
                table = TABLE_GENERO;
                where = "_id="+uri.getLastPathSegment();
                break;
            default:
                break;
        }

        SQLiteDatabase db = dbsqlhlpr.getWritableDatabase();
        Cursor c = db.query(table, projection, where, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        if("executeNamedQuery".equals(method))
        {
            Bundle returner = new Bundle();

            if(arg != null)
            {
                Cursor aux = executeNamedQuery(arg);
                //Verifiquem que la query estigui controlada
                if(NamedQueries.editorialMaxTomos.equals(arg)
                   ||NamedQueries.serieMaxTomos.equals(arg)
                   ||NamedQueries.generoMaxTomos.equals(arg))
                {
                    if(aux.moveToFirst())
                    {
                        String literalResultado = aux.getString(0);
                        Long numVols = aux.getLong(1);
                        returner.putString("literalResultado", literalResultado);
                        returner.putLong("numVols", numVols);
                    }
                }
            }

            return returner;
        }
        return super.call(method, arg, extras);
    }

    public Cursor executeNamedQuery(String namedQuery)
    {
        SQLiteDatabase db = dbsqlhlpr.getReadableDatabase();
        Cursor c = db.rawQuery(namedQuery, null);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        //Montamos el tipeador.
        int match = uriMatcher.match(uri);

        //Montamos el SWITCH OF TYPE OF DOOOOOOOOOOOOM
        switch (match) {
            case SERIE:
                return "vnd.android.cursor.dir/vnd.barbus.serie";
            case SERIE_ID:
                return "vnd.android.cursor.item/vnd.barbus.serie";
            case ESTADOSERIE:
                return "vnd.android.cursor.dir/vnd.barbus.estadoserie";
            case ESTADOSERIE_ID:
                return "vnd.android.cursor.item/vnd.barbus.estadoserie";
            case LNKSERIEVOL:
                return "vnd.android.cursor.dir/vnd.barbus.lnkserievol";
            case LNKSERIEVOL_ID:
                return "vnd.android.cursor.item/vnd.barbus.lnkserievol";
            case TIPORELACION:
                return "vnd.android.cursor.dir/vnd.barbus.tiporelacion";
            case TIPORELACION_ID:
                return "vnd.android.cursor.item/vnd.barbus.tiporelacion";
            case VOLUMEN:
                return "vnd.android.cursor.dir/vnd.barbus.volumen";
            case VOLUMEN_ID:
                return "vnd.android.cursor.item/vnd.barbus.volumen";
            case EDITORIAL:
                return "vnd.android.cursor.dir/vnd.barbus.editorial";
            case EDITORIAL_ID:
                return "vnd.android.cursor.item/vnd.barbus.editorial";
            case TIPOVOLUMEN:
                return "vnd.android.cursor.dir/vnd.barbus.tipovolumen";
            case TIPOVOLUMEN_ID:
                return "vnd.android.cursor.item/vnd.barbus.tipovolumen";
            case ESTADOCOLECCION:
                return "vnd.android.cursor.dir/vnd.barbus.estadocoleccion";
            case ESTADOCOLECCION_ID:
                return "vnd.android.cursor.item/vnd.barbus.estadocoleccion";
            case GENERO:
                return "vnd.android.cursor.dir/vnd.barbus.genero";
            case GENERO_ID:
                return "vnd.android.cursor.item/vnd.barbus.genero";

            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long regId = 1;
        String table = "";
        SQLiteDatabase db = dbsqlhlpr.getWritableDatabase();

        //Montamos el SWITCH OF INSERTING DOOOOOOOOOOOOM
        int match = uriMatcher.match(uri);
        switch (match) {

            case SERIE_ID:
                table = TABLE_SERIES;
                break;
            case ESTADOSERIE_ID:
                table = TABLE_ESTADOSERIES;
                break;
            case LNKSERIEVOL_ID:
                table = TABLE_LNKSERIEVOLS;
                break;
            case TIPORELACION_ID:
                table = TABLE_TIPOSRELACION;
                break;
            case VOLUMEN_ID:
                table = TABLE_VOLUMENES;
                break;
            case EDITORIAL_ID:
                table = TABLE_EDITORIALES;
                break;
            case TIPOVOLUMEN_ID:
                table = TABLE_TIPOSVOLUMEN;
                break;
            case ESTADOCOLECCION_ID:
                table = TABLE_ESTADOCOLECCION;
                break;
            case GENERO_ID:
                table = TABLE_GENERO;
                break;

            default:
                break;
        }
        regId = db.insert(table, null, contentValues);
        Uri newUri = ContentUris.withAppendedId(CONTENT_URI, regId);
        getContext().getContentResolver().notifyChange(uri, null);
        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //Montamos el SWITCH OF DELETING DOOOOOOOM
        String where = selection;
        String table = "";
        int cont;
        int match = uriMatcher.match(uri);
        switch (match) {

            case SERIE_ID:
                table = TABLE_SERIES;
                where = "_id="+uri.getLastPathSegment();
                break;
            case ESTADOSERIE_ID:
                table = TABLE_ESTADOSERIES;
                where = "_id="+uri.getLastPathSegment();
                break;
            case LNKSERIEVOL_ID:
                table = TABLE_LNKSERIEVOLS;
                where = "_id="+uri.getLastPathSegment();
                break;
            case TIPORELACION_ID:
                table = TABLE_TIPOSRELACION;
                where = "_id="+uri.getLastPathSegment();
                break;
            case VOLUMEN:
                table = TABLE_VOLUMENES;
                break;
            case VOLUMEN_ID:
                table = TABLE_VOLUMENES;
                where = "_id="+uri.getLastPathSegment();
                break;
            case EDITORIAL_ID:
                table = TABLE_EDITORIALES;
                where = "_id="+uri.getLastPathSegment();
                break;
            case TIPOVOLUMEN_ID:
                table = TABLE_TIPOSVOLUMEN;
                where = "_id="+uri.getLastPathSegment();
                break;
            case ESTADOCOLECCION_ID:
                table = TABLE_ESTADOCOLECCION;
                where = "_id="+uri.getLastPathSegment();
                break;
            case GENERO_ID:
                table = TABLE_GENERO;
                where = "_id="+uri.getLastPathSegment();
                break;

            default:
                break;
        }

        SQLiteDatabase db = dbsqlhlpr.getWritableDatabase();
        cont = db.delete(table, where, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cont;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        //Montamos el SWITCH OF UPDATING DOOOOOOOM
        String where = selection;
        String table = "";
        int cont;
        int match = uriMatcher.match(uri);
        switch (match) {

            case SERIE_ID:
                table = TABLE_SERIES;
                where = "_id="+uri.getLastPathSegment();
                break;
            case ESTADOSERIE_ID:
                table = TABLE_ESTADOSERIES;
                where = "_id="+uri.getLastPathSegment();
                break;
            case LNKSERIEVOL_ID:
                table = TABLE_LNKSERIEVOLS;
                where = "_id="+uri.getLastPathSegment();
                break;
            case TIPORELACION_ID:
                table = TABLE_TIPOSRELACION;
                where = "_id="+uri.getLastPathSegment();
                break;
            case VOLUMEN_ID:
                table = TABLE_VOLUMENES;
                where = "_id="+uri.getLastPathSegment();
                break;
            case EDITORIAL_ID:
                table = TABLE_EDITORIALES;
                where = "_id="+uri.getLastPathSegment();
                break;
            case TIPOVOLUMEN_ID:
                table = TABLE_TIPOSVOLUMEN;
                where = "_id="+uri.getLastPathSegment();
                break;
            case ESTADOCOLECCION_ID:
                table = TABLE_ESTADOCOLECCION;
                where = "_id="+uri.getLastPathSegment();
                break;
            case GENERO_ID:
                table = TABLE_GENERO;
                where = "_id="+uri.getLastPathSegment();
                break;

            default:
                break;
        }
        SQLiteDatabase db = dbsqlhlpr.getWritableDatabase();
        cont = db.update(table, contentValues, where, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cont;
    }
}
