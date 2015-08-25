package com.barbus.mangacontrol.DAOs;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.barbus.mangacontrol.R;

import java.util.ArrayList;

/**
 * Created by SalvadorGiralt on 21/07/14 for Manga Control
 */
public class CompundTomoCursorAdapter extends SimpleCursorAdapter{

    private ArrayList<Volumen> listaTomos;
    private Context mContext;
    private int[] to;
    private String[] from;
    private ContentResolver cr;

    public CompundTomoCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.mContext = context;
        this.to = to;
        this.from = from;
        this.cr = mContext.getContentResolver();
        setListaTomos(new ArrayList<Volumen>());
    }

    @Override
    public Cursor swapCursor(Cursor c) {
        getListaTomos().removeAll(getListaTomos());
        if(c != null && c.moveToFirst())
        {
            Volumen volAux;
            //definimos los ints que apuntan a los nombres de las columnas
            int id = c.getColumnIndex(from[0]);
            int numero = c.getColumnIndex(from[1]);
            int nombre = c.getColumnIndex(from[2]);
            int idSerie = c.getColumnIndex(from[3]);
            int idTipoVolumen = c.getColumnIndex(from[4]);
            int idEditorial = c.getColumnIndex(from[5]);
            int comprado = c.getColumnIndex(from[6]);
            do {
                volAux = new Volumen();
                volAux.setId(c.getLong(id));
                volAux.setNumero(c.getLong(numero));
                volAux.setNombre(c.getString(nombre));
                volAux.setIdSerie(c.getLong(idSerie));
                volAux.setIdTipoVolumen(c.getLong(idTipoVolumen));
                volAux.setIdEditorial(c.getLong(idEditorial));
                volAux.setComprado(c.getLong(comprado));
                listaTomos.add(volAux);
            }while(c.moveToNext());
        }
        return super.swapCursor(c);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if(getListaTomos().size() > 0)
        {
            if(convertView == null)
            {
                //la vista se está creando por primera vez, definimos sus elementos y seteamos el tag
                //inflamos el layout concreto que queremos
                LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflator.inflate(R.layout.fragment_elemento_lista_tomos, null);
                //creamos un nuevo elemento de tipo ViewHolder
                final ViewHolder viewHolder = new ViewHolder();
                //obtenemos las referencias a los elementos del layout
                viewHolder.numeroTomo = (TextView) view.findViewById(to[0]);
                viewHolder.btnComprado = (Button) view.findViewById(to[1]);
                //una vez estamos aquí, ya tenemos la referencia a los elementos que necesitamos
                //asignamos el clicklistener al botón
                viewHolder.btnComprado.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Volumen volActual = (Volumen) viewHolder.btnComprado.getTag();
                        new ComprarTomosTask().execute(volActual);
                    }
                });
                //seteamos el tag
                view.setTag(viewHolder);
                //asignamos el elemento actual el elemento clickable (es decir, el botón)
                viewHolder.btnComprado.setTag(getListaTomos().get(position));
            }
            else
            {
                //el elemento ya existía, simplemente lo reaprovechamos
                view = convertView;
                ((ViewHolder) view.getTag()).btnComprado.setTag(getListaTomos().get(position));
            }
            //Recuperamos el tag del elemento actual y le seteamos valores a los elementos de la UI
            ViewHolder holder = (ViewHolder) view.getTag();
            //seteamos el texto del botón comprado/comprar
            long comprado = ((Volumen)getListaTomos().get(position)).getComprado();
            if(comprado == 1)
            {
                holder.btnComprado.setText(mContext.getString(R.string.txtTomoComprado));
            }
            else
            {
                holder.btnComprado.setText(mContext.getString(R.string.txtTomoPorComprar));
            }
            holder.numeroTomo.setText(String.valueOf(getListaTomos().get(position).getNumero()));
        }
        return view;
    }

    public ArrayList<Volumen> getListaTomos() {
        return listaTomos;
    }

    public void setListaTomos(ArrayList<Volumen> listaTomos) {
        this.listaTomos = listaTomos;
    }

    static class ViewHolder{
        TextView numeroTomo;
        Button btnComprado;
    }

    private class ComprarTomosTask extends AsyncTask<Volumen, Integer, Long> {
        // Do the long-running work in here
        protected Long doInBackground(Volumen... volumenes) {
            int count = volumenes.length;
            long updateados = 0;
            for (int i = 0; i < count; i++) {
                //copiamos el volumen que nos ha llegado a una variable auxiliar
                Volumen volActual = volumenes[i];
                //construimos el contentvalues con los valores actuales del volumen + toggle comprado
                ContentValues cv = new ContentValues();
                cv.put(Volumen.NUMERO, volActual.getNumero());
                cv.put(Volumen.NOMBRE, volActual.getNombre());
                cv.put(Volumen.ID_SERIE, volActual.getIdSerie());
                cv.put(Volumen.ID_EDITORIAL, volActual.getIdEditorial());
                cv.put(Volumen.ID_TIPO_VOLUMEN, volActual.getIdTipoVolumen());
                if (volActual.getComprado() == 1)
                {
                    cv.put(Volumen.COMPRADO, 0);
                }
                else
                {
                    cv.put(Volumen.COMPRADO, 1);
                }
                //lanzamos el update
                Uri url = ContentUris.withAppendedId(Uri.parse(Volumen.URI), volActual.getId());
                updateados = cr.update(url, cv, null, null);
                //publishProgress((int) ((i / (float) count) * 100));
                // Escape early if cancel() is called
                if (isCancelled()) break;
            }
            return updateados;
        }

        // This is called each time you call publishProgress()
        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        // This is called when doInBackground() is finished
        protected void onPostExecute(Long result) {
            Toast.makeText(mContext, result+mContext.getString(R.string.tomo_modificado_ok), Toast.LENGTH_SHORT).show();
        }
    }
}
