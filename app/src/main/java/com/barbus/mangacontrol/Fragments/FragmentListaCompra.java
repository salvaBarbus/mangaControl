package com.barbus.mangacontrol.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.barbus.mangacontrol.DAOs.CompoundCursorAdapter;
import com.barbus.mangacontrol.DAOs.ElementoListaCompra;
import com.barbus.mangacontrol.DAOs.Serie;
import com.barbus.mangacontrol.DAOs.Volumen;
import com.barbus.mangacontrol.MainActivity;
import com.barbus.mangacontrol.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by SalvadorGiralt on 3/07/14 for ${PROJECT_NAME}
 */
public class FragmentListaCompra extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private Button btnComprar;
    private ListView lstTomosCompra;
    private SimpleCursorAdapter adapterCompras;
    private ContentResolver cr;
    private Cursor seriesIncompletas;
    private Uri url;
    private FragmentListaCompra fragmentListaCompra;
    private static final int URL_SERIES = 0;
    private static final int URL_TOMOS = 1;
    private static final int SECTION_NUMBER = 4;
    private long[] arraySeries;
    private LinkedList<CheckBox> listaChecks;
    private ActionMode mActionMode;

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_ctx_series, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.ctxOptionEditSerie:
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };

    public FragmentListaCompra(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lista_compra, container, false);

        fragmentListaCompra = this;
        btnComprar = (Button) rootView.findViewById(R.id.btnComprar);
        lstTomosCompra = (ListView) rootView.findViewById(R.id.lstTomosCompra);
        listaChecks = new LinkedList<CheckBox>();
        getLoaderManager().initLoader(URL_SERIES, null, this);

        adapterCompras = new CompoundCursorAdapter(getActivity(),
                R.layout.fragment_elemento_lista_compra,
                null,
                new String[]{Volumen._ID, Volumen.NOMBRE, Volumen.NUMERO},
                new int[]{R.id.txtNombreSerieListaCompra, R.id.txtNumeroVolListaCompra},
                0);
//
//        adapterCompras = new SimpleCursorAdapter(getActivity(),
//                android.R.layout.simple_list_item_2,
//                null,
//                new String[]{Volumen.NOMBRE, Volumen.NUMERO},
//                new int[]{android.R.id.text1, android.R.id.text2},
//                0);

        lstTomosCompra.setAdapter(adapterCompras);
        lstTomosCompra.setLongClickable(true);

        lstTomosCompra.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("com.barbus.mangacontrol", "estem dins del long click");
                if (mActionMode != null) {
                    return false;
                }

                // Start the CAB using the ActionMode.Callback defined above
                mActionMode = getActivity().startActionMode(mActionModeCallback);
                view.setSelected(true);
                lstTomosCompra.setItemChecked(i, true);
                Log.d("com.barbus.mangacontrol", "Seleccionado item en posicion "+i+" con id "+l);
                return false;
            }
        });

        lstTomosCompra.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                // Here you can do something when items are selected/de-selected,
                // such as update the title in the CAB
                Log.d("com.barbus.mangacontrol", "Recibido evento itemcheckedstatechanged para posicion "+position+", id "+id+" y checkeado "+checked);
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // Respond to clicks on the actions in the CAB
                StringBuilder sb = new StringBuilder();
                long[] checkedIds = lstTomosCompra.getCheckedItemIds();
                String checker = "";
                for(long l : checkedIds)
                {
                    checker+=l+",";
                }
                switch (item.getItemId()) {
                    case R.id.ctxOptionEditSerie:
//                        deleteSelectedItems();
                        Log.d("com.barbus.mangacontrol", "CheckedIds="+checker);
                        mode.finish(); // Action picked, so close the CAB
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate the menu for the CAB
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_ctx_series, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Here you can make any necessary updates to the activity when
                // the CAB is removed. By default, selected items are deselected/unchecked.
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // Here you can perform updates to the CAB due to
                // an invalidate() request
                return false;
            }
        });

        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int counter = 0;
                int provisionalResult;
                CompoundCursorAdapter adapter = (CompoundCursorAdapter) lstTomosCompra.getAdapter();
                if(adapter != null)
                {
                    ArrayList<ElementoListaCompra> listaElementos = adapter.getElementosCheckeados();
                    if(listaElementos != null)
                    {
                        /*
                        Prodecemos a Updatear los tomos concretos. Tenemos sus ids, así que no tenemos que
                        hacer ninguna guarrada, podemos acceder a ellos con la Uri directamente.
                        La modificación consiste en poner comprado = 1. De esta manera, los tomos ya no
                        saldrán en la lista de la compra.
                         */
                        for(ElementoListaCompra e : listaElementos)
                        {
                            ContentValues cv = new ContentValues();
                            cv.put(Volumen.COMPRADO, 1);

                            url = ContentUris.withAppendedId(Volumen.CONTENT_URI, e.getId());

                            provisionalResult = cr.update(url, cv, null, null);

                            //sumamos el resultado al contador de operaciones
                            counter += provisionalResult;

                            //limpiamos el CV para no liarla
                            cv.clear();
                        }
                    }
                }

                Toast.makeText(getActivity(), counter+" filas modificadas satisfactoriamente", Toast.LENGTH_LONG).show();
            }
        });

        cr = getActivity().getContentResolver();

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(SECTION_NUMBER);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        switch (id)
        {
            case URL_SERIES:
                url = Serie.CONTENT_URI;
                return new CursorLoader(getActivity(),
                        url,
                        new String[]{Serie._ID},
                        Serie.ID_ESTADO_COLECCION+" = ?",//selection
                        new String[]{"2"},//selectionArgs
                        null);//orderBy

            case URL_TOMOS:
                url = Volumen.CONTENT_URI;
                return new CursorLoader(getActivity(),
                        url,
                        new String[]{Volumen._ID, Volumen.ID_SERIE, Volumen.ID_EDITORIAL, Volumen.NOMBRE, Volumen.NUMERO},
                        Volumen.COMPRADO+" = ?",//selection
                        new String[]{"0"},//selectionArgs
                        Volumen.NOMBRE+","+Volumen.NUMERO);//orderBy

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        int idCursor = cursorLoader.getId();
        switch (idCursor)
        {
            case URL_SERIES:
                seriesIncompletas = cursor;
                arraySeries = new long[seriesIncompletas.getCount()];
                if(seriesIncompletas.moveToFirst())
                {
                    int columnaId = seriesIncompletas.getColumnIndex(Serie._ID);
                    int i=0;
                    do {
                        arraySeries[i] = seriesIncompletas.getLong(columnaId);
                        i++;
                    }while(seriesIncompletas.moveToNext());
                }
                Bundle args = new Bundle();
                args.putLongArray("arrayIds", arraySeries);
                getLoaderManager().initLoader(URL_TOMOS, args, this);
                break;

            case URL_TOMOS:
                adapterCompras.swapCursor(cursor);
                break;

            default:
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        int idCursor = cursorLoader.getId();
        switch (idCursor)
        {
            case URL_TOMOS:
                adapterCompras.swapCursor(null);
                break;

            default:
                break;
        }
    }
}
