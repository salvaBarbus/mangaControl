package com.barbus.mangacontrol;

import android.app.Activity;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.barbus.mangacontrol.Fragments.ConfirmDeleteSerie;
import com.barbus.mangacontrol.Fragments.ConfirmRemoveVolumenesFragment;
import com.barbus.mangacontrol.Fragments.FragmentAddSerie;
import com.barbus.mangacontrol.Fragments.FragmentControlEditoriales;
import com.barbus.mangacontrol.Fragments.FragmentControlSeries;
import com.barbus.mangacontrol.Fragments.FragmentControlTomos;
import com.barbus.mangacontrol.Fragments.FragmentDbBackup;
import com.barbus.mangacontrol.Fragments.FragmentEstadisticas;
import com.barbus.mangacontrol.Fragments.FragmentListaCompra;
import com.barbus.mangacontrol.Fragments.FragmentPruebasListas;
import com.barbus.mangacontrol.Fragments.PruebaListasFragment;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, ConfirmRemoveVolumenesFragment.NoticeDialogListener,
        ConfirmDeleteSerie.DeleteSerieListener, PruebaListasFragment.OnFragmentInteractionListener, FragmentPruebasListas.OnFragmentInteractionListener{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        clearBackStack();
        switch(position)
        {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new FragmentControlSeries(), "controlSeries")
                        .commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, FragmentControlTomos.newInstance("hola", "Manola"), "controlTomos")
                        .commit();
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new FragmentControlEditoriales())
                        .commit();
                break;
            case 3:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new FragmentListaCompra(), "listaCompra")
                        .commit();
                break;
            case 4:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, FragmentEstadisticas.newInstance("hola","Manola"), "estadisticas")
                        .commit();
                break;
            case 5:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new FragmentDbBackup())
                        .commit();
                break;

            default:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                        .commit();
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section6);
                break;
            case 6:
                mTitle = getString(R.string.title_section5);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this, getString(R.string.notYetImplemented), Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
//        Toast.makeText(this, "El usuario ha dicho que sí.", Toast.LENGTH_SHORT).show();
        FragmentAddSerie editSerie = (FragmentAddSerie) getFragmentManager().findFragmentByTag("editSerieFragment");
        editSerie.onDialogPositiveClick(dialog);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
//        Toast.makeText(this, "El usuario ha dicho que no.", Toast.LENGTH_SHORT).show();
        FragmentAddSerie editSerie = (FragmentAddSerie) getFragmentManager().findFragmentByTag("editSerieFragment");
        editSerie.onDialogNegativeClick(dialog);
    }

    @Override
    public void onDeleteDialogPositiveClick(DialogFragment dialog) {
        FragmentControlSeries controlSeries = (FragmentControlSeries) getFragmentManager().findFragmentByTag("controlSeries");
        controlSeries.onDeleteDialogPositiveClick(dialog);
    }

    @Override
    public void onDeleteDialogNegativeClick(DialogFragment dialog) {
        FragmentControlSeries controlSeries = (FragmentControlSeries) getFragmentManager().findFragmentByTag("controlSeries");
        controlSeries.onDeleteDialogNegativeClick(dialog);
    }

    @Override
    public void onFragmentInteraction(String id) {
//        Log.d("com.barbus.mangacontrol", "Interacted with fragment prueba listas and received the event here");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    private void clearBackStack()
    {
        FragmentManager fm = getFragmentManager();
        fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

}
