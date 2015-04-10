package mooklabs.ivdconnect;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.chiralcode.colorpicker.ColorPicker;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final int REQUEST_ENABLE_BT = 1;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private static MainActivity thisact;
    public BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisact = this;
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null)
            System.out.println("no bluetoth");
            //System.exit(1);            // Device does not support Bluetooth

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.PREFERENCE_FILE_KEY), Context.MODE_PRIVATE);

        ((ColorPicker) (this.getParent().findViewById(R.id.colorPickerLeft))).setColor (sharedPref.getInt("leftcolor",  0));
        ((ColorPicker) (this.getParent().findViewById(R.id.colorPickerRight))).setColor(sharedPref.getInt("rightcolor", 0));


    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        position++;
        switch (position) {
            case 1:
                System.out.println("1");
                fragmentManager.beginTransaction().replace(R.id.container, MapFragment.newInstance(position)).commit();
                break;
            case 2:
                System.out.println("2");
                fragmentManager.beginTransaction().replace(R.id.container, StatsFragment.newInstance(position)).commit();
                break;
            case 3:
                System.out.println("3");
                fragmentManager.beginTransaction().replace(R.id.container, HudFragment.newInstance(position)).commit();
                break;
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
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        try {
            this.findViewById(R.id.colorPickerLeft).setBackgroundColor(((ColorPicker) (this.findViewById(R.id.colorPickerLeft))).getColor());
            this.findViewById(R.id.colorPickerRight).setBackgroundColor(((ColorPicker) (this.findViewById(R.id.colorPickerRight))).getColor());
            SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.PREFERENCE_FILE_KEY), Context.MODE_PRIVATE);
            sharedPref.edit().putInt("leftcolor",((ColorPicker) (this.findViewById(R.id.colorPickerLeft))).getColor());
            sharedPref.edit().putInt("rightcolor",((ColorPicker) (this.findViewById(R.id.colorPickerRight))).getColor());
            sharedPref.edit().apply();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }
    public void updateColors(){
        this.findViewById(R.id.colorPickerLeft).setBackgroundColor(((ColorPicker) (this.findViewById(R.id.colorPickerLeft))).getColor());
        this.findViewById(R.id.colorPickerRight).setBackgroundColor(((ColorPicker) (this.findViewById(R.id.colorPickerRight))).getColor());
    }

    /**
     * Map fragment, shows/sets home and such??
     */
    public static class MapFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static MapFragment newInstance(int sectionNumber) {
            MapFragment fragment = new MapFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public MapFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_map, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    /**
     * stats fragment
     */
    public static class StatsFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static StatsFragment newInstance(int sectionNumber) {
            StatsFragment fragment = new StatsFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public StatsFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_stats, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    /**
     * Hud settings fragment
     */
    public static class HudFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static HudFragment newInstance(int sectionNumber) {
            HudFragment fragment = new HudFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public HudFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_hud, container, false);
            return rootView;
        }


        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
            ((MainActivity) activity).updateColors();

        }
    }
}
