package es.udc.psi14.grupal.guiacoruna;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import modelo.SQLModel;
import modelo.testmodel;
import util.util;


public class MainActivity extends FragmentActivity implements
        ActionBar.TabListener, FragmentGrupos.OnFragmentInteractionListener,FragmentMapa.OnFragmentInteractionListener {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "Grupos", "Mapa"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        SQLModel model;
        model = new SQLModel(this);
        model.loadInitData();

        /****/
        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.action_test_model){
            startActivity(new Intent(this, testmodel.class));
            return true;
        }
        else if(id == R.id.action_add_punto){
            startActivity(new Intent(this, AddPuntoInteres.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /*
    @Override
    public void onClick(View view) {
        //Intent i = new Intent(this, MapsActivity.class);
        //descomentar la linea que nos interesa en cada caso hasta que se implemente el tab view
        Intent i = new Intent(this, PuntoInteresListActiv.class);
        switch (view.getId()){
            case R.id.but_mus:
                i.putExtra(util.TAG_TYPE, util.TYPE_MUSEUM);
                i.putExtra(util.TAG_ICON,util.ICON_TYPE_MUSEUM);
                break;
            case R.id.but_hot:
                i.putExtra(util.TAG_TYPE, util.TYPE_HOTEL);
                i.putExtra(util.TAG_ICON,util.ICON_TYPE_HOTEL);
                break;
            case R.id.but_noche:
                i.putExtra(util.TAG_TYPE, util.TYPE_NIGHT);
                i.putExtra(util.TAG_ICON,util.ICON_TYPE_NIGHT);
                break;
            case R.id.but_tend:
                i.putExtra(util.TAG_TYPE, util.TYPE_SHOP);
                i.putExtra(util.TAG_ICON,util.ICON_TYPE_SHOP);
                break;
            case R.id.but_res:
                i.putExtra(util.TAG_TYPE, util.TYPE_RESTAURANT);
                i.putExtra(util.TAG_ICON,util.ICON_TYPE_RESTAURANT);
                break;
            case R.id.but_mon:
                i.putExtra(util.TAG_TYPE, util.TYPE_MONUMENT);
                i.putExtra(util.TAG_ICON,util.ICON_TYPE_MONUMENT);
                break;

        }
        startActivity(i);
    }*/
}
