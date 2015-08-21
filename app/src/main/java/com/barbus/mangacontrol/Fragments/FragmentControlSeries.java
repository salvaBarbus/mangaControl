package com.barbus.mangacontrol.Fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.barbus.mangacontrol.DAOs.Serie;
import com.barbus.mangacontrol.DAOs.Volumen;
import com.barbus.mangacontrol.Database.NamedQueries;
import com.barbus.mangacontrol.MainActivity;
import com.barbus.mangacontrol.R;

/**
 * Created by SalvadorGiralt on 3/07/14.
 */
public class FragmentControlSeries extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
                ConfirmDeleteSerie.DeleteSerieListener{

    private Button btnAddSerie;
    private ListView lstSeries;
    private SimpleCursorAdapter mAdapter;
    private static final int URL_LOADER = 0;
    private static final int SECTION_NUMBER = 1;
    private long idSerie;
    private ContentResolver cr;

    public FragmentControlSeries(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_control_series, container, false);

        btnAddSerie = (Button) rootView.findViewById(R.id.btnAddSerie);
        lstSeries = (ListView) rootView.findViewById(R.id.lstSeries);
        cr = getActivity().getContentResolver();

        cr.call(Serie.CONTENT_URI,"executeNamedQuery", NamedQueries.generoMaxTomos, null);

        btnAddSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentAddSerie fragmentAddSerie = new FragmentAddSerie();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.container, fragmentAddSerie, "addSerie")
                        .addToBackStack("controlSerie")
                        .commit();
            }
        });

        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1,
                null,
                new String[]{"nombreSerie"},
                new int[]{android.R.id.text1},
                0);

        lstSeries.setAdapter(mAdapter);

        registerForContextMenu(lstSeries);

        getLoaderManager().initLoader(URL_LOADER, null, this);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(SECTION_NUMBER);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_ctx_series, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId())
        {
            case R.id.ctxOptionEditSerie:
//                Toast.makeText(getActivity(), "Editando Serie "+info.position, Toast.LENGTH_SHORT).show();
                FragmentAddSerie fragmentAddSerie = new FragmentAddSerie();
                FragmentManager fragmentManager = getFragmentManager();
                Bundle argsUpdate = new Bundle();
                argsUpdate.putLong("idSerie", info.id);
                fragmentAddSerie.setArguments(argsUpdate);
                fragmentManager.beginTransaction()
                        .replace(R.id.container,fragmentAddSerie, "editSerieFragment")
                        .addToBackStack(null)
                        .commit();
                return true;

            case R.id.ctxOptionDeleteSerie:
//                Toast.makeText(getActivity(), "Deleteando Serie "+info.id, Toast.LENGTH_SHORT).show();
                ContentResolver cr = getActivity().getContentResolver();
                Uri url = ContentUris.withAppendedId(Serie.CONTENT_URI, info.id);
                showNoticeDialog("Se eliminará la serie y todos sus tomos al aceptar",
                        "Aceptar",
                        "Cancelar",
                        info.id);
//                int numberRowsDeleted = cr.delete(url, null, null);

//                Toast.makeText(getActivity(), numberRowsDeleted+" filas borradas satisfactoriamente", Toast.LENGTH_LONG).show();
                return true;

            default:
                break;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri url = Uri.parse("content://com.barbus.controlmanga.contentproviders/series/");
        switch(i)
        {
            case URL_LOADER:
                return new CursorLoader(getActivity(),
                        url,
                        new String[]{"_id","nombreSerie"},
                        null, //selection clause
                        null, //selection arguments
                        null); //order by

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

    public void showNoticeDialog(String mensaje, String botonPositivo, String botonNegativo, long idSerie) {
        // Create an instance of the dialog fragment and show it
        this.idSerie = idSerie;
        DialogFragment dialog = ConfirmDeleteSerie.newInstance(mensaje, botonPositivo, botonNegativo);
        dialog.show(getFragmentManager(), "deleteSerieFragment");
    }

    @Override
    public void onDeleteDialogPositiveClick(DialogFragment dialog) {
        Uri url = ContentUris.withAppendedId(Serie.CONTENT_URI, this.idSerie);
        int seriesDeleteadas = cr.delete(url, null, null);
        int tomosDeleteados = 0;
        if(seriesDeleteadas > 0)
        {
            url = Uri.parse(Volumen.URI);
            tomosDeleteados = cr.delete(url, Volumen.ID_SERIE+" = ?", new String[]{String.valueOf(this.idSerie)});
        }
        String mensaje = "Resultado final:\n "+seriesDeleteadas+" series Borradas.\n"+tomosDeleteados+" tomos borrados";
        Toast.makeText(getActivity(), mensaje, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeleteDialogNegativeClick(DialogFragment dialog) {
        Toast.makeText(getActivity(), "Borrado cancelado.", Toast.LENGTH_SHORT).show();
    }

}
