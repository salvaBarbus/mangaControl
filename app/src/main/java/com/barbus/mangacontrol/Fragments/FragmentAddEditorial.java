package com.barbus.mangacontrol.Fragments;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.barbus.mangacontrol.R;

/**
 * Created by SalvadorGiralt on 3/07/14.
 */
public class FragmentAddEditorial extends Fragment {

    private EditText edtNombreEditorial;
    private Button btnConfirmAddEditorial;
    private Bundle args;
    private Uri url;
    private ContentResolver cr;
    private long idUpdate;
    private String txtMensajeResultado;
    private String txtBotonEnvio;

    public FragmentAddEditorial(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_editoriales, container, false);
        edtNombreEditorial = (EditText) rootView.findViewById(R.id.edtNombreEditorial);
        btnConfirmAddEditorial = (Button) rootView.findViewById(R.id.btnConfirmAddEditorial);
        args = this.getArguments();
        cr = getActivity().getContentResolver();

        if(args != null)
        {
            idUpdate = args.getLong("idUpdate");
            url = Uri.parse("content://com.barbus.controlmanga.contentproviders/editoriales/"+idUpdate);
            Cursor editorial = cr.query(url, new String[]{"_id","nombreEditorial"}, null, null, null);
            if(editorial.moveToFirst())
            {
                int indexNombreEditorial = editorial.getColumnIndex("nombreEditorial");
                do {
                    String nombreEditorial = editorial.getString(indexNombreEditorial);
                    edtNombreEditorial.setText(nombreEditorial);
                }while(editorial.moveToNext());
            }
            editorial.close();
            txtBotonEnvio = getString(R.string.strEditEditorial);
        }
        else
        {
            txtBotonEnvio = getString(R.string.strAddEditorial);
        }

        btnConfirmAddEditorial.setText(txtBotonEnvio);

        btnConfirmAddEditorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues cv = new ContentValues();
                String nombreEditorial = edtNombreEditorial.getText().toString();
                if (nombreEditorial != null && !"".equals(nombreEditorial)) {
                    cv.put("nombreEditorial", nombreEditorial);

                    //llamamos a la Uri para añadir o updatear la editorial
                    if (args != null) {
                        url = Uri.parse("content://com.barbus.controlmanga.contentproviders/editoriales/" + idUpdate);
                        int resultado = cr.update(url, cv, null, null);
                        txtMensajeResultado = getString(R.string.txtEditorialModificadaOK);
                    } else {
                        url = Uri.parse("content://com.barbus.controlmanga.contentproviders/editoriales/1");
                        Uri resultado = cr.insert(url, cv);
                        String lastPath = resultado.getLastPathSegment();
                        txtMensajeResultado = getString(R.string.txtEditorialModificadaOK);
                    }
                    edtNombreEditorial.setText("");
                    Toast.makeText(getActivity(), txtMensajeResultado, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.txtErrorNombreEditorialNulo), Toast.LENGTH_LONG).show();
                }

            }
        });

        return rootView;
    }
}
