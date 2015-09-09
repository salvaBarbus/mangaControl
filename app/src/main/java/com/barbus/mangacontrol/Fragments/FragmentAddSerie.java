package com.barbus.mangacontrol.Fragments;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.Long;

import com.barbus.mangacontrol.DAOs.Editorial;
import com.barbus.mangacontrol.DAOs.Genero;
import com.barbus.mangacontrol.DAOs.Serie;
import com.barbus.mangacontrol.DAOs.Volumen;
import com.barbus.mangacontrol.R;

/**
 * Created by SalvadorGiralt on 7/07/14 for ${PROJECT_NAME}
 */
public class FragmentAddSerie extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        ConfirmFragment.NoticeDialogListener{

    private EditText edtNombreSerie;
    private EditText edtNombreOriginalSerie;
    private Spinner spnEditorial;
    private RadioGroup radGroupSerie;
    private EditText edtNumeroVolumenes;
    private Spinner spnEstadoCol;
    private Button btnAddSerie;
    private Spinner spnGenero;
    private static final int URL_EDITORIALES = 0;
    private static final int URL_ESTADO_COLECCION = 1;
    private static final int URL_EDIT_SERIE = 2;
    private static final int URL_GENEROS = 3;
    private SimpleCursorAdapter adapterEditoriales;
    private SimpleCursorAdapter adapterEstadoColeccion;
    private SimpleCursorAdapter adapterGenero;
    private String mensajeError = "";
    private Uri url;
    private ContentResolver cr;
    private Bundle args;
    private long numeroOriginalVolumenes;
    private long diferenciaVolumenes;
    private long idSerie;

    public FragmentAddSerie(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_serie, container, false);

        edtNombreSerie = (EditText) rootView.findViewById(R.id.edtNombreSerie);
        edtNombreOriginalSerie = (EditText) rootView.findViewById(R.id.edtNombreOriginalSerie);
        spnEditorial = (Spinner) rootView.findViewById(R.id.spnEditorial);
        radGroupSerie = (RadioGroup) rootView.findViewById(R.id.radGroupSerie);
        edtNumeroVolumenes = (EditText) rootView.findViewById(R.id.edtNumeroVol);
        spnEstadoCol = (Spinner) rootView.findViewById(R.id.spnEstadoCol);
        btnAddSerie = (Button) rootView.findViewById(R.id.btnAddSerie);
        spnGenero = (Spinner) rootView.findViewById(R.id.spnGenero);
        cr = getActivity().getContentResolver();

        //Inicializamos los loaders necesarios
        getLoaderManager().initLoader(URL_EDITORIALES, null, this);
        getLoaderManager().initLoader(URL_ESTADO_COLECCION, null, this);
        getLoaderManager().initLoader(URL_GENEROS, null, this);

        args = this.getArguments();

        if(args != null)
        {
            idSerie = args.getLong("idSerie");
            args.clear();
            args.putLong(Serie._ID, idSerie);
            getLoaderManager().initLoader(URL_EDIT_SERIE, args, this);
            btnAddSerie.setText(getString(R.string.editar_serie));
        }

        adapterEditoriales = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_spinner_item,
                null,
                new String[]{"nombreEditorial"},
                new int[]{android.R.id.text1},
                0);
        adapterEditoriales.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEditorial.setAdapter(adapterEditoriales);

        adapterEstadoColeccion = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_spinner_item,
                null,
                new String[]{"nombreEstadoColeccion"},
                new int[]{android.R.id.text1},
                0);
        adapterEstadoColeccion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEstadoCol.setAdapter(adapterEstadoColeccion);

        adapterGenero = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_spinner_item,
                null,
                new String[]{Genero.NOMBRE_GENERO},
                new int[]{android.R.id.text1},
                0);
        adapterGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGenero.setAdapter(adapterGenero);

        btnAddSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //efectuamos las comprovaciones antes de hacer nada más
                if(isValidForInsertion())
                {
                    //recogemos los valores introducidos por el usuario
                    String nombreSerie = edtNombreSerie.getText().toString();
                    String nombreOriginalSerie = edtNombreOriginalSerie.getText().toString();
                    long idEditorial = spnEditorial.getSelectedItemId();
                    long idGenero = spnGenero.getSelectedItemId();
                    int checkedRadioButtonId = radGroupSerie.getCheckedRadioButtonId();
                    long idEstadoSerie = Long.MIN_VALUE;
                    if(checkedRadioButtonId == R.id.rdbtnSerieAbierta)
                    {
                        idEstadoSerie = (long) 1;
                    }
                    if(checkedRadioButtonId == R.id.rdbtnSerieCerrada)
                    {
                        idEstadoSerie = (long) 2;
                    }
                    long numeroVol = Long.parseLong(edtNumeroVolumenes.getText().toString());
                    long estadoCol = spnEstadoCol.getSelectedItemId();
                    if(args != null)
                    {
                        //miramos cuantos volumenes había antes de editar, y obramos en consecuencia

                        diferenciaVolumenes = numeroVol - numeroOriginalVolumenes;

                        if(diferenciaVolumenes < 0)
                        {
                            /*
                            Se ha reducido el número de volumenes, tenemos que:
                            1.- Determinar cuantos volumenes hay de diferencia
                            2.- Avisar al usuario de qué significará este cambio (borrar volumenes)
                            3.- Si el usuario confirma, borrar los volumenes afectados
                             */
                            showNoticeDialog(getString(R.string.confirmacion_eliminar_tomos),
                                    getString(R.string.aceptar),
                                    getString(R.string.cancelar));
                        } else if (diferenciaVolumenes > 0){
                            /*
                            Este caso es más sencillo, ya que solo se debe:
                            1.- Determinar la diferencia de volumenes
                            2.- Avisar al usuario que se insertarán X volumenes (con confirmación)
                            3.- Insertar los volumenes nuevos.
                             */
                            showNoticeDialog(getString(R.string.confirmacion_insertar_tomos),
                                    getString(R.string.aceptar),
                                    getString(R.string.cancelar));
                            } else {
                                showNoticeDialog(getString(R.string.confirmacion_editar_a_pelo),
                                        getString(R.string.aceptar),
                                        getString(R.string.cancelar));
                            }
                        } else {
                            //copiamos los valores a un ContentValues para insertar
                            ContentValues cv = new ContentValues();
                            cv.put(Serie.NOMBRE_SERIE,nombreSerie);
                            cv.put(Serie.NOMBRE_ORIGINAL_SERIE, nombreOriginalSerie);
                            cv.put(Serie.ID_ESTADO_SERIE,idEstadoSerie);
                            cv.put(Serie.ID_ESTADO_COLECCION,estadoCol);
                            cv.put(Serie.NUM_VOLUMENES, numeroVol);
                            cv.put(Serie.ID_EDITORIAL, idEditorial);
                            cv.put(Serie.ID_GENERO, idGenero);

                            //insertamos los valores en base de datos
                            url = ContentUris.withAppendedId(Serie.CONTENT_URI, 1);
                            Uri resultado = cr.insert(url, cv);
                            String idSerieInsertada = resultado.getLastPathSegment();
                            Toast.makeText(getActivity(), getString(R.string.serie_creada_ok)+nombreSerie, Toast.LENGTH_LONG).show();

                            cv.clear();
                            //insertamos los volumenes
                            url = ContentUris.withAppendedId(Uri.parse(Volumen.URI), 1);
                            for (int i=1; i<= numeroVol; i++)
                            {
                                cv.put(Volumen.NUMERO, i);
                                cv.put(Volumen.NOMBRE, nombreSerie);
                                cv.put(Volumen.ID_TIPO_VOLUMEN, 0);
                                cv.put(Volumen.ID_SERIE,idSerieInsertada);
                                cv.put(Volumen.ID_EDITORIAL, idEditorial);
                                if(estadoCol == 1)
                                {
                                    //serie Completa, insertamos los tomos con comprado = 1
                                    cv.put(Volumen.COMPRADO, 1);
                                }
                                else
                                {
                                    //serie Incompleta o Abandonada, insertamos los tomos con comprado = 0
                                    cv.put(Volumen.COMPRADO, 0);
                                }
                                resultado = cr.insert(url, cv);
                                cv.clear();
                            }
                            Toast.makeText(getActivity(), "Insertados "+numeroVol+" tomos satisfactoriamente", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(mensajeError)
                                .setTitle("Error")
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });
                        builder.show();
                    }
                }
            });
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Uri url_estado_coleccion = Uri.parse("content://com.barbus.controlmanga.contentproviders/estadoColeccion/");
        Uri url_edit_serie = Serie.CONTENT_URI;
        if(args != null)
        {
            url_edit_serie = ContentUris.withAppendedId(Serie.CONTENT_URI, bundle.getLong(Serie._ID));
        }
        switch (id)
        {
            case URL_EDITORIALES:
                return new CursorLoader(getActivity(),
                        Editorial.CONTENT_URI,
                        new String[]{"_id","nombreEditorial"},
                        null,//selection
                        null,//selectionArgs
                        null);//orderBy
            case URL_ESTADO_COLECCION:
                return new CursorLoader(getActivity(),
                        url_estado_coleccion,
                        new String[]{"_id","nombreEstadoColeccion"},
                        null,//selection
                        null,//selectionArgs
                        null);//orderBy
            case URL_EDIT_SERIE:
                return new CursorLoader(getActivity(),
                        url_edit_serie,
                        new String[]{Serie._ID,Serie.NOMBRE_SERIE, Serie.NOMBRE_ORIGINAL_SERIE,
                                     Serie.ID_ESTADO_SERIE, Serie.ID_EDITORIAL,
                                     Serie.ID_ESTADO_COLECCION, Serie.NUM_VOLUMENES, Serie.ID_GENERO},
                        null,//selection
                        null,//selectionArgs
                        null);//orderBy
            case URL_GENEROS:
                return new CursorLoader(getActivity(),
                        Genero.CONTENT_URI,
                        new String[]{Genero._ID, Genero.NOMBRE_GENERO},
                        null,//selection
                        null,//selectionArgs
                        null);//orderBy
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        int id_loader = cursorLoader.getId();
        switch(id_loader)
        {
            case URL_EDITORIALES:
                adapterEditoriales.swapCursor(cursor);
                break;
            case URL_ESTADO_COLECCION:
                adapterEstadoColeccion.swapCursor(cursor);
                break;
            case URL_GENEROS:
                adapterGenero.swapCursor(cursor);
                break;
            case URL_EDIT_SERIE:
                copyDataFromCursor(cursor);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        int id_loader = cursorLoader.getId();
        switch(id_loader)
        {
            case URL_EDITORIALES:
                adapterEditoriales.swapCursor(null);
                break;
            case URL_ESTADO_COLECCION:
                adapterEstadoColeccion.swapCursor(null);
                break;
            case URL_GENEROS:
                adapterGenero.swapCursor(null);
            default:
                break;
        }
    }

    private void copyDataFromCursor(Cursor cursor)
    {
        if(cursor.moveToFirst())
        {
            int nombreSerie = cursor.getColumnIndex(Serie.NOMBRE_SERIE);
            int nombreOriginalSerie = cursor.getColumnIndex(Serie.NOMBRE_ORIGINAL_SERIE);
            int numeroVol = cursor.getColumnIndex(Serie.NUM_VOLUMENES);
            int idEstColeccion = cursor.getColumnIndex(Serie.ID_ESTADO_COLECCION);
            int idEditorial = cursor.getColumnIndex(Serie.ID_EDITORIAL);
            int idEstadoSerie = cursor.getColumnIndex(Serie.ID_ESTADO_SERIE);
            int idGenero = cursor.getColumnIndex(Serie.ID_GENERO);
            do {
                edtNombreSerie.setText(cursor.getString(nombreSerie));
                edtNombreOriginalSerie.setText(cursor.getString(nombreOriginalSerie));
                spnEditorial.setSelection(cursor.getInt(idEditorial)-1);
                spnGenero.setSelection(cursor.getInt(idGenero)-1);
                int checkeado = cursor.getInt(idEstadoSerie);
                switch(checkeado)
                {
                    case 1:
                        radGroupSerie.check(R.id.rdbtnSerieAbierta);
                        break;
                    case 2:
                        radGroupSerie.check(R.id.rdbtnSerieCerrada);
                        break;
                    default:
                        break;
                }
                numeroOriginalVolumenes = cursor.getLong(numeroVol);
                edtNumeroVolumenes.setText(String.valueOf(numeroOriginalVolumenes));
                spnEstadoCol.setSelection(cursor.getInt(idEstColeccion)-1);
            }while(cursor.moveToNext());
        }
    }

    private boolean isValidForInsertion()
    {
        boolean result = true;
        //limpiamos el mensaje de error
        mensajeError = "";

        if(edtNombreSerie == null || "".equals(edtNombreSerie.getText().toString()))
        {
            //recogemos el mensaje de error de la base de datos
            mensajeError += "- "+getString(R.string.errSinNombreSerie) + "\n";
            result = false;
        }
        if(spnEditorial == null || spnEditorial.getSelectedItem() == null)
        {
            //recogemos el mensaje de error de la base de datos
            mensajeError += "- "+getString(R.string.errSinEditorial) + "\n";
            result = false;
        }
        if(radGroupSerie.getCheckedRadioButtonId() < 0)
        {
            //recogemos el mensaje de error de la base de datos
            mensajeError += "- "+getString(R.string.errSinEstadoSerie) + "\n";
            result = false;
        }
        if(edtNumeroVolumenes == null || "".equals(edtNumeroVolumenes.getText().toString()))
        {
            //recogemos el mensaje de error de la base de datos
            mensajeError += "- "+getString(R.string.errSinNumVol)+"\n";
            result = false;
        }
        //esta comprobación no debería ser necesaria peeeero...
        if(spnEstadoCol == null || spnEstadoCol.getSelectedItem() == null)
        {
            //recogemos el mensaje de error de la base de datos
            mensajeError += "- "+getString(R.string.errSinEstadoColeccion)+"\n";
            result = false;
        }
        if(spnGenero == null || spnGenero.getSelectedItem() == null)
        {
            //recogemos el mensaje de error
            mensajeError += "- "+getString(R.string.errSinGenero);
            result = false;
        }

        return result;
    }

    public void showNoticeDialog(String mensaje, String botonPositivo, String botonNegativo) {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = ConfirmFragment.newInstance(mensaje, botonPositivo, botonNegativo);
        dialog.show(getFragmentManager(), "ConfirmFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
//        Toast.makeText(getActivity(), "El usuario ha dicho que sí in da fragment", Toast.LENGTH_SHORT).show();

        //recogemos los valores introducidos por el usuario
        String nombreSerie = edtNombreSerie.getText().toString();
        String nombreOriginalSerie = edtNombreOriginalSerie.getText().toString();
        long idEditorial = spnEditorial.getSelectedItemId();
        int checkedRadioButtonId = radGroupSerie.getCheckedRadioButtonId();
        long idEstadoSerie = Long.MIN_VALUE;
        if(checkedRadioButtonId == R.id.rdbtnSerieAbierta)
        {
            idEstadoSerie = (long) 1;
        }
        if(checkedRadioButtonId == R.id.rdbtnSerieCerrada)
        {
            idEstadoSerie = (long) 2;
        }
        long numeroVol = Long.parseLong(edtNumeroVolumenes.getText().toString());
        long estadoCol = spnEstadoCol.getSelectedItemId();
        long genero = spnGenero.getSelectedItemId();

        //Lo primero es updatear la serie con los nuevos datos
        //copiamos los valores a un ContentValues para insertar
        ContentValues cv = new ContentValues();
        cv.put(Serie.NOMBRE_SERIE,nombreSerie);
        cv.put(Serie.NOMBRE_ORIGINAL_SERIE, nombreOriginalSerie);
        cv.put(Serie.ID_ESTADO_SERIE,idEstadoSerie);
        cv.put(Serie.ID_ESTADO_COLECCION,estadoCol);
        cv.put(Serie.NUM_VOLUMENES, numeroVol);
        cv.put(Serie.ID_EDITORIAL, idEditorial);
        cv.put(Serie.ID_GENERO, genero);

        //insertamos los valores en base de datos
        url = ContentUris.withAppendedId(Serie.CONTENT_URI, idSerie);
        int resultadoUpdate = cr.update(url, cv, null, null);
        Toast.makeText(getActivity(), getString(R.string.serie_modificada_ok)+nombreSerie, Toast.LENGTH_LONG).show();

        cv.clear();

        long suffix = numeroOriginalVolumenes;
        //procedemos a efectuar la operación necesaria según si la diferencia de tomos es mayor o menor que 0
        if(diferenciaVolumenes > 0)
        {

            //eliminamos volumenes
            while(diferenciaVolumenes > 0)
            {
                url = ContentUris.withAppendedId(Uri.parse(Volumen.URI),  1);
                suffix++;

                cv.clear();
                cv.put(Volumen.NUMERO, suffix);
                cv.put(Volumen.NOMBRE, nombreSerie);
                cv.put(Volumen.ID_TIPO_VOLUMEN, 0);
                cv.put(Volumen.ID_SERIE,idSerie);
                cv.put(Volumen.ID_EDITORIAL, idEditorial);
                if(estadoCol == 1)
                {
                    //serie Completa, insertamos los tomos con comprado = 1
                    cv.put(Volumen.COMPRADO, 1);
                }
                else
                {
                    //serie Incompleta o Abandonada, insertamos los tomos con comprado = 0
                    cv.put(Volumen.COMPRADO, 0);
                }
                Uri resultado = cr.insert(url, cv);
                cv.clear();
                diferenciaVolumenes--;
            }
        }
        else
        {
            //en teoría no podemos llegar con diferencia = 0 pero por si acaso lo controlamos
            if(diferenciaVolumenes < 0)
            {
                //añadimos los volúmenes que hagan falta
                while (diferenciaVolumenes < 0) {
                    url = ContentUris.withAppendedId(Uri.parse(Volumen.URI), suffix);
                    int filasDeleteadas = cr.delete(url, null, null);
                    suffix--;
                    diferenciaVolumenes++;
                }
            }
        }
        /*
        Una vez hemos acabado, miramos si ha cambiado el número de volumenes (suffix != numeroOriginalVolumenes)
        Si ocurre esto, updateamos el numero de volumenes de la serie
         */
        if(suffix != numeroOriginalVolumenes)
        {
            //suffix contiene el número del último volumen, actualizamos la serie con ello
            cv.clear();
            cv.put(Serie.NUM_VOLUMENES, suffix);
            url = ContentUris.withAppendedId(Serie.CONTENT_URI, idSerie);
            int filasUpdateadas = cr.update(url, cv, null, null);
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
//        Toast.makeText(getActivity(), "El usuario ha dicho que no in da fragment", Toast.LENGTH_SHORT).show();
    }
}
