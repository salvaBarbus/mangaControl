package com.barbus.mangacontrol.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConfirmRemoveVolumenesFragment.NoticeDialogListener} interface
 * to handle interaction events.
 * Use the {@link ConfirmRemoveVolumenesFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ConfirmRemoveVolumenesFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_MENSAJE = "mensaje";
    private static final String ARG_POSITIVO = "positivo";
    private static final String ARG_NEGATIVO = "negativo";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    private NoticeDialogListener mListener;

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfirmRemoveVolumenesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfirmRemoveVolumenesFragment newInstance(String param1, String param2, String param3) {
        ConfirmRemoveVolumenesFragment fragment = new ConfirmRemoveVolumenesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MENSAJE, param1);
        args.putString(ARG_POSITIVO, param2);
        args.putString(ARG_NEGATIVO, param3);
        fragment.setArguments(args);
        return fragment;
    }
    public ConfirmRemoveVolumenesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_MENSAJE);
            mParam2 = getArguments().getString(ARG_POSITIVO);
            mParam3 = getArguments().getString(ARG_NEGATIVO);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mParam1)
                .setPositiveButton(mParam2, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(ConfirmRemoveVolumenesFragment.this);
                    }
                })
                .setNegativeButton(mParam3, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(ConfirmRemoveVolumenesFragment.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
