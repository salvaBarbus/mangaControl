package com.barbus.mangacontrol.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.barbus.mangacontrol.MainActivity;
import com.barbus.mangacontrol.R;

/**
 * Created by SalvadorGiralt on 7/07/14.
 */
public class FragmentAddTomo extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private Spinner spnSerie;
    private EditText edtTomoUnico;
    private EditText edtVariosTomosMin;
    private EditText edtVariosTomosMax;
    private Button btnAddTomos;
    private SimpleCursorAdapter adapterSeries;
    private static final int URL_SERIES = 0;

    public FragmentAddTomo(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_tomo, container, false);

        spnSerie = (Spinner) rootView.findViewById(R.id.spnSerie);
        edtTomoUnico = (EditText) rootView.findViewById(R.id.edtTomoUnico);
        edtVariosTomosMin = (EditText) rootView.findViewById(R.id.edtVariosTomosMin);
        edtVariosTomosMax = (EditText) rootView.findViewById(R.id.edtVariosTomosMax);

        adapterSeries = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_spinner_item,
                null,
                new String[]{"nombreSerie"},
                new int[]{android.R.id.text1,},
                0);
        adapterSeries.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSerie.setAdapter(adapterSeries);

        getLoaderManager().initLoader(URL_SERIES, null, this);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri url = Uri.parse("content://com.barbus.controlmanga.contentproviders/series/");
        switch (i)
        {
            case URL_SERIES:
                return new CursorLoader(getActivity(),
                        url,
                        new String[]{"_id","nombreSerie"},
                        null,//selection
                        null,//selectionArgs
                        null);//orderBy
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        adapterSeries.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        adapterSeries.swapCursor(null);
    }
}
