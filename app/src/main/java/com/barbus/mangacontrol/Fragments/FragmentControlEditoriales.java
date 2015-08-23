package com.barbus.mangacontrol.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
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

import com.barbus.mangacontrol.MainActivity;
import com.barbus.mangacontrol.R;

/**
 * Created by SalvadorGiralt on 3/07/14.
 */
public class FragmentControlEditoriales extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private Button btnAddEditorial;
    private ListView lstEditoriales;
    private Bundle args;
    private SimpleCursorAdapter mAdapter;
    private static final int URL_LOADER = 0;
    private static final int SECTION_NUMBER = 3;

    public FragmentControlEditoriales(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_control_editoriales, container, false);
        btnAddEditorial = (Button) rootView.findViewById(R.id.btnAddEditorial);
        args = this.getArguments();
        lstEditoriales = (ListView) rootView.findViewById(R.id.lstEditoriales);

        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1,
                null,
                new String[]{"nombreEditorial"},
                new int[]{android.R.id.text1},
                0);

        lstEditoriales.setAdapter(mAdapter);

        registerForContextMenu(lstEditoriales);

        btnAddEditorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //Cargamos el fragment de añadir Editorial
                FragmentManager fm = getFragmentManager();
                FragmentAddEditorial fragmentAddEditorial = new FragmentAddEditorial();
                fm.beginTransaction()
                        .replace(R.id.container, fragmentAddEditorial)
                        .addToBackStack("controlEditorial")
                        .commit();
            }
        });

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
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_ctx_editoriales, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo adapter = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId())
        {
            case R.id.ctxOptionEditEditorial:
                FragmentAddEditorial fragmentAddEditorial = new FragmentAddEditorial();
                FragmentManager fm = getFragmentManager();
                Bundle args = new Bundle();
                args.putLong("idUpdate", adapter.id);
                fragmentAddEditorial.setArguments(args);
                fm.beginTransaction()
                        .replace(R.id.container, fragmentAddEditorial, null)
                        .addToBackStack("listaEditoriales")
                        .commit();
                return true;
            case R.id.ctxOptionDeleteEditorial:
                ContentResolver cr = getActivity().getContentResolver();
                Uri url = Uri.parse("content://com.barbus.controlmanga.contentproviders/editoriales/"+adapter.id);
                int rowsDeleted = cr.delete(url, null, null);
                Toast.makeText(getActivity(), rowsDeleted+getString(R.string.filas_borradas_ok), Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri url = Uri.parse("content://com.barbus.controlmanga.contentproviders/editoriales/");
        switch(i)
        {
            case URL_LOADER:
                return new CursorLoader(getActivity(),
                        url,
                        new String[]{"_id","nombreEditorial"},
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
}
