package es.udc.psi14.grupal.guiacoruna;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

import modelo.PuntoInteres;
import modelo.SQLModel;
import util.util;


public class PuntoInteresListActiv extends Activity {

    private ListView listView;
    SQLModel model;
    PuntoInteres[] puntoInteresData;
    int drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        if (getIntent().getExtras()!=null) {
            Bundle extra = getIntent().getExtras(); // check if not null
            String type = extra.getString(util.TAG_TYPE, "");
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

        PuntoInteresAdapter adapter = new PuntoInteresAdapter(this,
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
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.list, menu);
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
        }
        return super.onOptionsItemSelected(item);
    }



}
