package com.barbus.mangacontrol.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SalvadorGiralt on 3/07/14.
 */
public class DatabaseSqliteHelper extends SQLiteOpenHelper {

    //Sentencias SQL para la creación de las tablas
    //serie:
    String sqlCreateSerie = "CREATE TABLE serie "+
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nombreSerie TEXT, "+
            "idEditorial INTEGER, "+
            "idGenero INTEGER, "+
            "numVolumenes INTEGER, "+
            "idEstadoSerie INTEGER, "+
            "idEstadoColeccion INTEGER )";
    //oferta
    String sqlCreateEstadoSerie = "CREATE TABLE estadoSerie "+
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "nombreEstadoSerie TEXT )";
    String sqlCreateLnkSerieVol = "CREATE TABLE lnkSerieVol "+
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "idSerie INTEGER, "+
            "idVolumen INTEGER, "+
            "idRelacion INTEGER )";
    String sqlCreateTiposRelacion = "CREATE TABLE tipoRelacion "+
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "tipoRelacion TEXT )";
    String sqlCreateVolumen = "CREATE TABLE volumen "+
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "numero INTEGER, "+
            "nombre TEXT, "+
            "idSerie INTEGER, "+
            "idTipoVolumen INTEGER, "+
            "comprado INTEGER, "+
            "idEditorial INTEGER )";
    String sqlCreateEditoriales = "CREATE TABLE editorial "+
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "nombreEditorial TEXT )";
    String sqlCreatetipoVolumen = "CREATE TABLE tipoVolumen "+
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "tipoVolumen INTEGER )";
    String sqlCreateEstadoColeccion = "CREATE TABLE estadoColeccion "+
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "nombreEstadoColeccion TEXT )";

    String sqlCreateGenero = "CREATE TABLE genero "+
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "nombreGenero TEXT )";

    String sqlCreateAutor = "CREATE TABLE autor ("+
            "_id	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "+
            "nombre	TEXT NOT NULL UNIQUE )";

    String sqlCreateLnkAutorSerie = "CREATE TABLE lnkAutorSerie "+
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "idSerie INTEGER, "+
            "idAutor INTEGER )";

    //STRINGS PARA UPDATEAR

    String sqlUpdateSerie_addTituloOriginal = "alter table serie add column tituloOriginal text";

    public DatabaseSqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseSqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Creamos las tablas
        sqLiteDatabase.execSQL(sqlCreateSerie);
        sqLiteDatabase.execSQL(sqlCreateEstadoSerie);
        sqLiteDatabase.execSQL(sqlCreateLnkSerieVol);
        sqLiteDatabase.execSQL(sqlCreateTiposRelacion);
        sqLiteDatabase.execSQL(sqlCreateVolumen);
        sqLiteDatabase.execSQL(sqlCreateEditoriales);
        sqLiteDatabase.execSQL(sqlCreatetipoVolumen);
        sqLiteDatabase.execSQL(sqlCreateEstadoColeccion);
        sqLiteDatabase.execSQL(sqlCreateGenero);

        //Insertamos los datos fijos en las tablas de estado de aplicación, estado de colección, género
        ContentValues cv = new ContentValues();
        cv.put("nombreEstadoSerie","Serie Abierta");
        sqLiteDatabase.insert("estadoSerie", null, cv);
        cv.clear();

        cv.put("nombreEstadoSerie","Serie Cerrada");
        sqLiteDatabase.insert("estadoSerie", null, cv);
        cv.clear();

        cv.put("nombreEstadoColeccion","Completa");
        sqLiteDatabase.insert("estadoColeccion", null, cv);
        cv.clear();

        cv.put("nombreEstadoColeccion","Incompleta");
        sqLiteDatabase.insert("estadoColeccion", null, cv);
        cv.clear();

        cv.put("nombreEstadoColeccion","Abandonada");
        sqLiteDatabase.insert("estadoColeccion", null, cv);
        cv.clear();

        //género
        cv.put("nombreGenero","Shonen");
        sqLiteDatabase.insert("genero", null, cv);
        cv.clear();

        cv.put("nombreGenero","Shojo");
        sqLiteDatabase.insert("genero", null, cv);
        cv.clear();

        cv.put("nombreGenero","Seinen");
        sqLiteDatabase.insert("genero", null, cv);
        cv.clear();

        cv.put("nombreGenero","Shojo Ai");
        sqLiteDatabase.insert("genero", null, cv);
        cv.clear();

        cv.put("nombreGenero","Shonen Ai");
        sqLiteDatabase.insert("genero", null, cv);
        cv.clear();

        cv.put("nombreGenero","Yaoi");
        sqLiteDatabase.insert("genero", null, cv);
        cv.clear();

        cv.put("nombreGenero","Yuri");
        sqLiteDatabase.insert("genero", null, cv);
        cv.clear();

        cv.put("nombreGenero","Hentai");
        sqLiteDatabase.insert("genero", null, cv);
        cv.clear();

        cv.put("nombreGenero","Josei");
        sqLiteDatabase.insert("genero", null, cv);
        cv.clear();

        cv.put("nombreGenero","Kodomo");
        sqLiteDatabase.insert("genero", null, cv);
        cv.clear();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        int upgradeTo = oldVersion + 1;
        while (upgradeTo <= newVersion)
        {
            switch (upgradeTo)
            {
                case 2:
                    sqLiteDatabase.execSQL(sqlUpdateSerie_addTituloOriginal);
                    break;
                case 3:
                    sqLiteDatabase.execSQL(sqlCreateAutor);
                    sqLiteDatabase.execSQL(sqlCreateLnkAutorSerie);
                    break;
            }
            upgradeTo++;
        }
    }
}
