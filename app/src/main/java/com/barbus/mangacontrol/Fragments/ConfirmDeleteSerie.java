package com.barbus.mangacontrol.Fragments;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.barbus.mangacontrol.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmDeleteSerie#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
//TODO: Think about removing this fragment now that we have reworked the confirmation fragment
public class ConfirmDeleteSerie extends DialogFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_MENSAJE = "mensaje";
    private static final String ARG_POSITIVO = "positivo";
    private static final String ARG_NEGATIVO = "negativo";

    private String mMensaje;
    private String mPositivo;
    private String mNegativo;
    private DeleteSerieListener mDeleteSerie;

    public interface DeleteSerieListener
    {
        public void onDeleteDialogPositiveClick(DialogFragment dialog);
        public void onDeleteDialogNegativeClick(DialogFragment dialog);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mensaje Mensaje a mostrar en el confirm.
     * @param positivo Texto del botón Positivo.
     * @param negativo Texto del botón Negativo.
     * @return A new instance of fragment ConfirmDeleteSerie.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfirmDeleteSerie newInstance(String mensaje, String positivo, String negativo) {
        ConfirmDeleteSerie fragment = new ConfirmDeleteSerie();
        Bundle args = new Bundle();
        args.putString(ARG_MENSAJE, mensaje);
        args.putString(ARG_POSITIVO, positivo);
        args.putString(ARG_NEGATIVO, negativo);
        fragment.setArguments(args);
        return fragment;
    }
    public ConfirmDeleteSerie() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mDeleteSerie = (DeleteSerieListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DeleteSerieListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDeleteSerie = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMensaje = getArguments().getString(ARG_MENSAJE);
            mPositivo = getArguments().getString(ARG_POSITIVO);
            mNegativo = getArguments().getString(ARG_NEGATIVO);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mMensaje)
                .setPositiveButton(mPositivo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mDeleteSerie.onDeleteDialogPositiveClick(ConfirmDeleteSerie.this);
                    }
                })
                .setNegativeButton(mNegativo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mDeleteSerie.onDeleteDialogNegativeClick(ConfirmDeleteSerie.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


}
