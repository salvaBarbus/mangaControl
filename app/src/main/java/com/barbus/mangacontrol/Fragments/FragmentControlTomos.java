package com.barbus.mangacontrol.Fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.barbus.mangacontrol.DAOs.CompundTomoCursorAdapter;
import com.barbus.mangacontrol.DAOs.Serie;
import com.barbus.mangacontrol.DAOs.Volumen;
import com.barbus.mangacontrol.MainActivity;
import com.barbus.mangacontrol.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentControlTomos#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class FragmentControlTomos extends Fragment
            implements LoaderManager.LoaderCallbacks<Cursor>,
            ConfirmDeleteSerie.DeleteSerieListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Spinner spnSerie;
    private ListView lstTomos;
    private Uri url;
    private ContentResolver cr;
    private static final int URL_SERIES = 0;
    private static final int URL_TOMOS = 1;
    private SimpleCursorAdapter adapterSeries;
    private SimpleCursorAdapter adapterTomos;
    private FragmentControlTomos mFragment = this;
    private static final int SECTION_NUMBER = 2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentControlTomos.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentControlTomos newInstance(String param1, String param2) {
        FragmentControlTomos fragment = new FragmentControlTomos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public FragmentControlTomos() {
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(SECTION_NUMBER);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_control_tomos, container, false);

        spnSerie = (Spinner) rootView.findViewById(R.id.spnNombreSerieControlTomos);
        lstTomos = (ListView) rootView.findViewById(R.id.lstTomosControlTomos);

        getLoaderManager().initLoader(URL_SERIES, null, mFragment);
        Bundle wtf = new Bundle();
        wtf.putLong(Volumen.ID_SERIE, 1);
        getLoaderManager().initLoader(URL_TOMOS, wtf, mFragment);

        adapterSeries = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_spinner_item,
                null,
                new String[]{Serie.NOMBRE_SERIE},
                new int[]{android.R.id.text1},
                0);
        adapterSeries.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSerie.setAdapter(adapterSeries);

//        adapterTomos = new SimpleCursorAdapter(getActivity(),
//                android.R.layout.simple_list_item_2,
//                null,
//                new String[]{Volumen.NOMBRE, Volumen.NUMERO},
//                new int[]{android.R.id.text1, android.R.id.text2},
//                0);

        adapterTomos = new CompundTomoCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_2,
                null,
                new String[]{Volumen._ID, Volumen.NUMERO, Volumen.NOMBRE, Volumen.ID_SERIE,
                             Volumen.ID_TIPO_VOLUMEN, Volumen.ID_EDITORIAL, Volumen.COMPRADO},
                new int[]{R.id.txtNumeroTomoListaTomos, R.id.btnEstadoCompraListaTomos},
                0);
        lstTomos.setAdapter(adapterTomos);

        spnSerie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getActivity(), "Seleccionado elemento en posicion "+i+ " con id "+l, Toast.LENGTH_LONG).show();
                Bundle args = new Bundle();
                args.putLong(Volumen.ID_SERIE, l);
                getLoaderManager().restartLoader(URL_TOMOS, args, mFragment);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
//                Toast.makeText(getActivity(), "Ningún elemento seleccionado", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        switch (i)
        {
            case URL_SERIES:
                url = Serie.CONTENT_URI;
                return new CursorLoader(getActivity(),
                        url,
                        new String[]{Serie._ID, Serie.NOMBRE_SERIE},
                        null,
                        null,
                        Serie.NOMBRE_SERIE);

            case URL_TOMOS:
                url = Volumen.CONTENT_URI;
                long idSerie = (Long) bundle.get(Volumen.ID_SERIE);
                return new CursorLoader(getActivity(),
                        url,
                        new String[]{Volumen._ID, Volumen.NUMERO, Volumen.NOMBRE, Volumen.ID_SERIE,
                                Volumen.ID_TIPO_VOLUMEN, Volumen.ID_EDITORIAL, Volumen.COMPRADO},
                        Volumen.ID_SERIE +" = ?",
                        new String[]{String.valueOf(idSerie)},
                        Volumen.NUMERO);

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        int id = cursorLoader.getId();
        switch(id)
        {
            case URL_SERIES:
                adapterSeries.swapCursor(cursor);
                break;

            case URL_TOMOS:
                adapterTomos.swapCursor(cursor);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        int id = cursorLoader.getId();
        switch(id)
        {
            case URL_SERIES:
                adapterSeries.swapCursor(null);
                break;

            case URL_TOMOS:
                adapterTomos.swapCursor(null);
                break;
        }
    }

    @Override
    public void onDeleteDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDeleteDialogNegativeClick(DialogFragment dialog) {

    }
}
