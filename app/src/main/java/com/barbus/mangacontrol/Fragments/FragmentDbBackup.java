package com.barbus.mangacontrol.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.barbus.mangacontrol.MainActivity;
import com.barbus.mangacontrol.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by SalvadorGiralt on 7/07/14.
 */
public class FragmentDbBackup extends Fragment {

    private Button btnDoBackUp;
    private Button btnLoadBackup;

    private static final int READ_REQUEST_CODE = 0;
    private static final int SECTION_NUMBER = 6;

    public FragmentDbBackup(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_db_backup, container, false);

        btnDoBackUp = (Button) rootView.findViewById(R.id.btnDoBackup);
        btnLoadBackup = (Button) rootView.findViewById(R.id.btnLoadBackup);

        btnDoBackUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    backupDatabase();
                    Toast.makeText(getActivity(), "Copia de Seguridad realizada correctamente", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "No se ha podido realizar la copia de seguridad", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnLoadBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //performFileSearch();
                try {
                    loadDatabaseBackup();
                    Toast.makeText(getActivity(), "Copia de Seguridad cargada correctamente", Toast.LENGTH_LONG).show();
                }catch(FileNotFoundException ex)
                {
                    Toast.makeText(getActivity(), "No se ha encontrado el fichero de copia de seguridad", Toast.LENGTH_LONG).show();
                }
                catch(IOException ex)
                {
                    Toast.makeText(getActivity(), "No se ha podido cargar la copia de seguridad", Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(SECTION_NUMBER);
    }

    private void backupDatabase() throws IOException
    {
        String inFileName = "/data/data/com.barbus.mangacontrol/databases/controlManga.db";
        File dbFile = new File(inFileName);
        FileInputStream fis = new FileInputStream(dbFile);

        String outFileName = Environment.getExternalStorageDirectory()+"/controlManga.db";
        OutputStream output = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while((length = fis.read(buffer))>0){
            output.write(buffer, 0, length);
        }
        output.flush();
        output.close();
        fis.close();
    }

    private void loadDatabaseBackup() throws IOException
    {
//        String inFileName = "/data/data/com.barbus.mangacontrol/databases/controlManga.db";
        String inFileName = Environment.getExternalStorageDirectory()+"/controlManga.db";;
        File dbFile = new File(inFileName);
        FileInputStream fis = new FileInputStream(dbFile);

        String outFileName = "/data/data/com.barbus.mangacontrol/databases/controlManga.db";
        OutputStream output = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while((length = fis.read(buffer))>0){
            output.write(buffer, 0, length);
        }
        output.flush();
        output.close();
        fis.close();
    }

    /**
     * Fires an intent to spin up the "file chooser" UI and select an image.
     */
    private void performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("*/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                Log.d("com.barbus.mangacontrol", "Uri: " + uri.toString());
                //showImage(uri);
            }
        }
    }
}
