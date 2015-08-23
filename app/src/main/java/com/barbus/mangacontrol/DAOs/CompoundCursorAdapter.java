package com.barbus.mangacontrol.DAOs;

import android.content.Context;
import android.database.Cursor;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.barbus.mangacontrol.MainActivity;
import com.barbus.mangacontrol.R;

import java.util.ArrayList;

/**
 * Created by SalvadorGiralt on 18/07/14 for Manga Control
 */
public class CompoundCursorAdapter extends SimpleCursorAdapter{

    private ArrayList<ElementoListaCompra> listaElementos;
    private Context mContext;
    private int[] to;
    private String[] from;
    private ActionMode mActionMode;

    public CompoundCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        mContext = context;
        this.from = from;
        this.to = to;
        setListaElementos(new ArrayList<ElementoListaCompra>());
    }

    @Override
    public Cursor swapCursor(Cursor c) {
        getListaElementos().removeAll(getListaElementos());
        if(c != null)
        {
            ElementoListaCompra elementoActual;
            if(c.moveToFirst())
            {

                //definimos ints para indexar los nombres de las columnas
                int id = c.getColumnIndex(from[0]);
                int nombreSerie = c.getColumnIndex(from[1]);
                int numVol = c.getColumnIndex(from[2]);
                do {
                    elementoActual = new ElementoListaCompra(c.getLong(id),
                                                             c.getString(nombreSerie),
                                                             "Tomo: ",
                                                             c.getString(numVol));
                    getListaElementos().add(elementoActual);
                }while(c.moveToNext());
            }
        }
        return super.swapCursor(c);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (getListaElementos().size() > 0) {
            if(convertView == null)
            {
                LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflator.inflate(R.layout.fragment_elemento_lista_compra, null);
                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.nombreSerie = (TextView) view.findViewById(R.id.txtNombreSerieListaCompra);
                viewHolder.literalVols = (TextView) view.findViewById(R.id.txtVolumenListaCompra);
                viewHolder.numVol = (TextView) view.findViewById(R.id.txtNumeroVolListaCompra);
                viewHolder.chkComprar = (CheckBox) view.findViewById(R.id.chkComprar);
                viewHolder.chkComprar
                        .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(CompoundButton buttonView,
                                                         boolean isChecked) {
                                ElementoListaCompra element = (ElementoListaCompra) viewHolder.chkComprar
                                        .getTag();
                                element.setChecked(buttonView.isChecked());
                            }
                        });
                view.setTag(viewHolder);
                viewHolder.chkComprar.setTag(getListaElementos().get(position));

            }
            else
            {
                view = convertView;
                ((ViewHolder) view.getTag()).chkComprar.setTag(getListaElementos().get(position));
            }
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.nombreSerie.setText(getListaElementos().get(position).getNombreSerie());
            holder.literalVols.setText(getListaElementos().get(position).getLiteralVol());
            holder.numVol.setText(getListaElementos().get(position).getNumeroVol());
            holder.chkComprar.setChecked(getListaElementos().get(position).isChecked());
        }
        return view;
    }

    public ArrayList<ElementoListaCompra> getListaElementos() {
        return listaElementos;
    }

    public void setListaElementos(ArrayList<ElementoListaCompra> listaElementos) {
        this.listaElementos = listaElementos;
    }

    public ArrayList<ElementoListaCompra> getElementosCheckeados()
    {
        ArrayList<ElementoListaCompra> elementos = new ArrayList<ElementoListaCompra>();
        for(ElementoListaCompra e : listaElementos)
        {
            if(e.isChecked())
            {
                elementos.add(e);
            }
        }
        if(elementos.size() > 0)
        {
            return elementos;
        }
        else
        {
            return null;
        }
    }

    static class ViewHolder{
        TextView nombreSerie, literalVols, numVol;
        CheckBox chkComprar;
    }

    class ActionBarCallBack implements ActionMode.Callback {

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // TODO Auto-generated method stub
            mode.getMenuInflater().inflate(R.menu.menu_ctx_series, menu);
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            // TODO Auto-generated method stub

            mode.setTitle("CheckBox is Checked");
            return false;
        }

    }
}
