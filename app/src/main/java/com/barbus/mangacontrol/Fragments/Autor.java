package com.barbus.mangacontrol.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
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
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.barbus.mangacontrol.DAOs.AutorDAO;
import com.barbus.mangacontrol.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Autor#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Autor extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> ,
        ConfirmFragment.NoticeDialogListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText edtNombreAutor;
    private Button btnAddAutor;
    private static final int URL_AUTORES = 0;
    private static final int URL_EDIT_AUTORES = 1;
    private String mensajeError = "";
    private Uri url;
    private ContentResolver cr;
    private Bundle args;
    private long idAutor;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Autor.
     */
    // TODO: Rename and change types and number of parameters
    public static Autor newInstance(String param1, String param2) {
        Autor fragment = new Autor();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Autor() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_autor, container, false);

        edtNombreAutor = (EditText) rootView.findViewById(R.id.edtNombreAutor);
        btnAddAutor = (Button) rootView.findViewById(R.id.btnAcceptAutor);
        cr = getActivity().getContentResolver();
        args = this.getArguments();

        if(args!=null) {
            idAutor = args.getLong("idAutor");
            args.clear();
            args.putLong(AutorDAO._ID, idAutor);
            getLoaderManager().initLoader(URL_AUTORES, args, this);
            btnAddAutor.setText(R.string.txtEditAutor);
        }

        btnAddAutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidForInsertion()) {
                    String nombreAutor = edtNombreAutor.getText().toString();
                    //copiamos los valores a un contentValues para Insertar/editar
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(AutorDAO.NOMBRE_AUTOR, nombreAutor);

                    if (args != null) {
                        //update case
                        showNoticeDialog(getString(R.string.updateAuthorMessage),
                                getString(R.string.aceptar),
                                getString(R.string.cancelar));
                    } else {
                        //insert case
                        //hacemos las operaciones necesarias en base de datos
                        url = ContentUris.withAppendedId(AutorDAO.CONTENT_URI, 1);
                        Uri resultado = cr.insert(url, contentValues);
                        String idAutorInsertado = resultado.getLastPathSegment();
                        Toast.makeText(getActivity(), "Creado satisfactoriamente el autor " + nombreAutor, Toast.LENGTH_LONG).show();
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

    private boolean isValidForInsertion() {
        return true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Uri urlEditAutor = AutorDAO.CONTENT_URI;
        if(args!= null) {
            urlEditAutor = ContentUris.withAppendedId(AutorDAO.CONTENT_URI, bundle.getLong(AutorDAO._ID));
        }
        switch(id) {
            case URL_AUTORES:
                return new CursorLoader(getActivity(),
                        urlEditAutor,
                        new String[]{AutorDAO._ID, AutorDAO.NOMBRE_AUTOR},
                        null,
                        null,
                        null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int loaderId = loader.getId();
        switch (loaderId) {
            case URL_AUTORES:
                copyDataFromCursor(cursor);
                break;
            default:
                break;
        }
    }

    private void copyDataFromCursor(Cursor cursor) {
        if(cursor.moveToFirst()) {
            int nombreAutor = cursor.getColumnIndex(AutorDAO.NOMBRE_AUTOR);
            do {
                edtNombreAutor.setText(cursor.getString(nombreAutor));
            } while(cursor.moveToNext());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void showNoticeDialog(String mensaje, String botonPositivo, String botonNegativo) {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = ConfirmFragment.newInstance(mensaje, botonPositivo, botonNegativo);
        dialog.show(getFragmentManager(), "ConfirmEditAuthor");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        //get the field values and put them in a ContentValues to do the update
        //at this point, the values are already validates by the isValidForInsertion method
        String authorName = edtNombreAutor.getText().toString();
        ContentValues contentValues = new ContentValues();
        contentValues.put(AutorDAO.NOMBRE_AUTOR, authorName);

        //update the values in the database
        url = AutorDAO.getContentUriWithAppendedId(idAutor);
        int updateResult = cr.update(url, contentValues, null, null);
        if(updateResult > 0) {
            //all went well
            Toast.makeText(getActivity(), getString(R.string.authorUpdateSuccess)+authorName,
                    Toast.LENGTH_LONG)
                    .show();
        } else {
            //something went wrong
            Toast.makeText(getActivity(), getString(R.string.authorUpdateFailure)+authorName,
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
