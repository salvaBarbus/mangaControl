package com.barbus.mangacontrol.Database;

/**
 * Created by SalvadorGiralt on 25/07/14 for Manga Control
 */
public class NamedQueries {

    //clase para contener las namedqueries necesarias para el programa
    public static final String editorialMaxTomos = "select nombreEditorial, numVol " +
            "from(" +
            "select edi.nombreEditorial as nombreEditorial, count(*) as numVol " +
            "from serie ser, genero ger, volumen vol, editorial edi " +
            "where ser._id = vol.idSerie " +
            "and ser.idGenero = ger._id " +
            "and vol.comprado = 1 " +
            "and ser.idEditorial = edi._id " +
            "group by edi.nombreEditorial " +
            ") " +
            "where numvol = (select max(counter) from(select count(*) as counter " +
            "from serie ser, genero ger, volumen vol, editorial edi " +
            "where ser._id = vol.idSerie " +
            "and ser.idGenero = ger._id " +
            "and vol.comprado = 1 " +
            "and ser.idEditorial = edi._id " +
            "group by edi.nombreEditorial))";
    public static final String generoMaxTomos = "select nombreGenero, numVol " +
            "from( " +
            "select ger.nombreGenero as nombreGenero, count(*) as numVol " +
            "from serie ser, genero ger, volumen vol " +
            "where ser._id = vol.idSerie " +
            "and ser.idGenero = ger._id " +
            "and vol.comprado = 1 " +
            "group by ger.nombreGenero " +
            ") " +
            "where numvol = (select max(counter) from(select count(*) as counter " +
            "from serie ser, genero ger, volumen vol " +
            "where ser._id = vol.idSerie " +
            "and ser.idGenero = ger._id " +
            "and vol.comprado = 1 " +
            "group by ger.nombreGenero))";
    public static final String serieMaxTomos = "select nombreSerie, numVol " +
            "from( " +
            "select ser.nombreSerie as nombreSerie, count(*) as numVol " +
            "from serie ser, volumen vol " +
            "where ser._id = vol.idSerie " +
            "and vol.comprado = 1 " +
            "group by ser.nombreSerie " +
            ") " +
            "where numvol = (select max(counter) from(select count(*) as counter " +
            "from serie ser, volumen vol " +
            "where ser._id = vol.idSerie " +
            "and vol.comprado = 1 " +
            "group by ser.nombreSerie))";
}
