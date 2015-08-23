package com.barbus.mangacontrol.Fragments;



import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.barbus.mangacontrol.DAOs.Editorial;
import com.barbus.mangacontrol.DAOs.Serie;
import com.barbus.mangacontrol.DAOs.Volumen;
import com.barbus.mangacontrol.Database.NamedQueries;
import com.barbus.mangacontrol.MainActivity;
import com.barbus.mangacontrol.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentEstadisticas#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class FragmentEstadisticas extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;
    private ContentResolver cr;
    private Context mContext;
    private static final int URL_NUM_SERIES=0;
    private static final int URL_NUM_EDITORIALES=1;
    private static final int URL_TOTAL_TOMOS=2;
    private static final int SECTION_NUMBER = 5;

    private TextView txtLitNumSeries;
    private TextView txtNumSeries;
    private TextView txtLitNumEditorial;
    private TextView txtNumEditorial;
    private TextView txtLitTotalTomos;
    private TextView txtTotalTomos;
    private TextView txtLitEditMasTomos;
    private TextView txtEditMasTomos;
    private TextView txtLitGeneroMasTomos;
    private TextView txtGeneroMasTomos;
    private TextView txtLitSerieMasLarga;
    private TextView txtSerieMasLarga;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2014-07-29 14:14:52 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        txtLitNumSeries = (TextView)rootView.findViewById(R.id.txtLitNumSeries);
        txtNumSeries = (TextView)rootView.findViewById(R.id.txtNumSeries);
        txtLitNumEditorial = (TextView)rootView.findViewById(R.id.txtLitNumEditorial);
        txtNumEditorial = (TextView)rootView.findViewById(R.id.txtNumEditorial);
        txtLitTotalTomos = (TextView)rootView.findViewById(R.id.txtLitTotalTomos);
        txtTotalTomos = (TextView)rootView.findViewById(R.id.txtTotalTomos);
        txtLitEditMasTomos = (TextView)rootView.findViewById(R.id.txtLitEditMasTomos);
        txtEditMasTomos = (TextView)rootView.findViewById(R.id.txtEditMasTomos);
        txtLitGeneroMasTomos = (TextView)rootView.findViewById(R.id.txtLitGeneroMasTomos);
        txtGeneroMasTomos = (TextView)rootView.findViewById(R.id.txtGeneroMasTomos);
        txtLitSerieMasLarga = (TextView)rootView.findViewById(R.id.txtLitSerieMasLarga);
        txtSerieMasLarga = (TextView)rootView.findViewById(R.id.txtSerieMasLarga);
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentEstadisticas.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentEstadisticas newInstance(String param1, String param2) {
        FragmentEstadisticas fragment = new FragmentEstadisticas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public FragmentEstadisticas() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            cr = getActivity().getContentResolver();
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
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_estadisticas, container, false);
        findViews();
        mContext = getActivity();

        //inicializamos los cursorLoader
        getLoaderManager().initLoader(URL_NUM_EDITORIALES, null, this);
        getLoaderManager().initLoader(URL_NUM_SERIES, null, this);
        getLoaderManager().initLoader(URL_TOTAL_TOMOS, null, this);

        //ejecutamos la tarea asíncrona para cargar estadísticas vía namedqueries
        new CargarEstadisticasAsyncTask().execute(NamedQueries.editorialMaxTomos, NamedQueries.generoMaxTomos, NamedQueries.serieMaxTomos);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        switch(i)
        {
            case URL_TOTAL_TOMOS:
                return new CursorLoader(mContext,
                        Volumen.CONTENT_URI,
                        new String[]{Volumen._ID},//projection
                        null,//selection
                        null,//selectionArgs
                        null);//sortOrder
            case URL_NUM_SERIES:
                return new CursorLoader(mContext,
                        Serie.CONTENT_URI,
                        new String[]{Volumen._ID},//projection
                        null,//selection
                        null,//selectionArgs
                        null);//sortOrder;
            case URL_NUM_EDITORIALES:
                return new CursorLoader(mContext,
                        Editorial.CONTENT_URI,
                        new String[]{Volumen._ID},//projection
                        null,//selection
                        null,//selectionArgs
                        null);//sortOrder;
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        int id = cursorLoader.getId();
        switch(id)
        {
            case URL_NUM_EDITORIALES:
                if(cursor.moveToFirst())
                {
                    //básicamente pillamos el número de resultados encontrados
                    txtNumEditorial.setText(String.valueOf(cursor.getCount()));
                }
                break;
            case URL_TOTAL_TOMOS:
                if(cursor.moveToFirst())
                {
                    //idem de antes
                    txtTotalTomos.setText(String.valueOf(cursor.getCount()));
                }
                break;
            case URL_NUM_SERIES:
                if(cursor.moveToFirst())
                {
                    //idem de idem
                    txtNumSeries.setText(String.valueOf(cursor.getCount()));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

    private class CargarEstadisticasAsyncTask extends AsyncTask<String, Integer, ArrayList<Bundle>>
    {
        @Override
        protected ArrayList<Bundle> doInBackground(String... strings) {
            int count = strings.length;
            ArrayList<Bundle> resultado = new ArrayList<Bundle>();
            for (int i = 0; i < count; i++) {
                //do something!!!
                resultado.add(cr.call(Serie.CONTENT_URI, "executeNamedQuery", strings[i], null));
                publishProgress((int) (((i+1) / (float) count) * 100));
            }
            return resultado;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(ArrayList<Bundle> resultado) {
            txtEditMasTomos.setText(resultado.get(0).getString("literalResultado")+" ("+resultado.get(0).getLong("numVols")+")");
            txtGeneroMasTomos.setText(resultado.get(1).getString("literalResultado")+" ("+resultado.get(1).getLong("numVols")+")");
            txtSerieMasLarga.setText(resultado.get(2).getString("literalResultado")+" ("+resultado.get(2).getLong("numVols")+")");
        }
    }

}
