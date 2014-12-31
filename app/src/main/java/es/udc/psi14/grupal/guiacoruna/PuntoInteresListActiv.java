package es.udc.psi14.grupal.guiacoruna;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

import modelo.PuntoInteres;
import modelo.SQLModel;
import util.util;


public class PuntoInteresListActiv extends Activity {

    private ListView listView;
    PuntoInteresAdapter adapter;
    SQLModel model;
    PuntoInteres[] puntoInteresData;
    int drawable;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        if (getIntent().getExtras()!=null) {
            Bundle extra = getIntent().getExtras(); // check if not null
            type = extra.getString(util.TAG_TYPE, "");
            drawable = extra.getInt(util.TAG_ICON,0);
            ActionBar actionBar = getActionBar();
            actionBar.setIcon(drawable);
            actionBar.setTitle(type +  " List");
            model = new SQLModel(this);
            LinkedList<PuntoInteres> items = ((LinkedList) model.findByType(type));
            int size = items.size();
            puntoInteresData = new PuntoInteres[size];
            int index = 0;
            for (PuntoInteres pi : items) {
                puntoInteresData[index] = pi;
                index++;
            }
        }

        adapter = new PuntoInteresAdapter(this,
                R.layout.adapterlayout, puntoInteresData);

        listView = (ListView) findViewById(R.id.list_view);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DetallesActivity.class);
                intent.putExtra(util.TAG_ID,puntoInteresData[position].getId());
                startActivity(intent);
            }
        });
        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v == listView) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            //menu.setHeaderTitle();
            String[] menuItems = getResources().getStringArray(R.array.list_view_menu);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.list_view_menu);
        String menuItemName = menuItems[menuItemIndex];
        PuntoInteres pi = (PuntoInteres) listView.getItemAtPosition(info.position);
        String itemName = pi.getNombre();

        if(menuItemName.compareTo("EDIT")==0){
            Toast.makeText(this, "EDIT on "+itemName , Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AddPuntoInteres.class);
            Bundle extras = new Bundle();
            extras.putString(AddPuntoInteres.campoNombre, pi.getNombre());
            extras.putString(AddPuntoInteres.campoTelefono, pi.getTelefono());
            extras.putString(AddPuntoInteres.campoDireccion, pi.getDireccion());
            extras.putString(AddPuntoInteres.campoCoordenadas, pi.getCoordenadas());
            extras.putString(AddPuntoInteres.campoTipo, pi.getTipo());
            extras.putString(AddPuntoInteres.campoImagenNombre, pi.getImageString());

            extras.putString(AddPuntoInteres.activityMode, AddPuntoInteres.activityModeEdit);
            extras.putInt(AddPuntoInteres.idString, pi.getId());

            intent.putExtras(extras);

            startActivity(intent);

            refreshData();

        }else if(menuItemName.compareTo("REMOVE")==0){
            Toast.makeText(this, "REMOVE on "+itemName, Toast.LENGTH_SHORT).show();
            model.removePuntoInteres(pi.getId().toString());

            refreshData();
        }
        return true;
    }


    private void refreshData(){
        LinkedList<PuntoInteres> items = ((LinkedList) model.findByType(type));
        int size = items.size();
        puntoInteresData = new PuntoInteres[size];
        int index = 0;
        for (PuntoInteres pi : items) {
            puntoInteresData[index] = pi;
            index++;
        }
        adapter = new PuntoInteresAdapter(this,
                R.layout.adapterlayout, puntoInteresData);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.list, menu);
        getMenuInflater().inflate(R.menu.lista, menu);
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
        }
        return super.onOptionsItemSelected(item);
    }



}
