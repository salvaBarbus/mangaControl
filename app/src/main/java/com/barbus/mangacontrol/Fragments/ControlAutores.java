package com.barbus.mangacontrol.Fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.barbus.mangacontrol.MainActivity;
import com.barbus.mangacontrol.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ControlAutores#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ControlAutores extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button btnAddAutor;
    private ListView lstAutores;
    private SimpleCursorAdapter mAdapter;
    private static final int URL_LOADER = 0;
    private ContentResolver cr;
    private static final int SECTION_NUMBER = 4;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ControlAutores.
     */
    // TODO: Rename and change types and number of parameters
    public static ControlAutores newInstance(String param1, String param2) {
        ControlAutores fragment = new ControlAutores();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ControlAutores() {
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
        View rootView = inflater.inflate(R.layout.fragment_control_autores, container, false);

        btnAddAutor = (Button) rootView.findViewById(R.id.btnAddAutor);
        lstAutores = (ListView) rootView.findViewById(R.id.lstAutores);
        cr = getActivity().getContentResolver();

        btnAddAutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Autor autor = new Autor();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.container, autor, "autor")
                        .addToBackStack("controlAutores")
                        .commit();
            }
        });

        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1,
                null,
                new String[]{"nombre"},
                new int[]{android.R.id.text1},
                0);

        lstAutores.setAdapter(mAdapter);

        getLoaderManager().initLoader(URL_LOADER, null, this);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(SECTION_NUMBER);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri url = Uri.parse("content://com.barbus.controlmanga.contentproviders/autor/");
        switch(i)
        {
            case URL_LOADER:
                return new CursorLoader(getActivity(),
                        url,
                        new String[]{"_id","nombre"},
                        null, //selection clause
                        null, //selection arguments
                        null); //order by

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

}
