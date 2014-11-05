package es.udc.psi14.grupal.guiacoruna;

import android.app.Activity;
import android.os.Bundle;
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


public class PuntoInteresListActiv extends Activity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        /**
        PuntoInteresContainer a = new PuntoInteresContainer();
        a.setNombre("aaaaa");
        a.setDireccion("diraaaaaa");
        PuntoInteresContainer b = new PuntoInteresContainer();
        b.setNombre("bbbbb");
        b.setDireccion("diraabbbbb");
        PuntoInteresContainer c = new PuntoInteresContainer();
        c.setNombre("cccccc");
        c.setDireccion("diracccc");
        PuntoInteresContainer[] puntoInteresData = new PuntoInteresContainer[] {
            a,b,c
        };
        /**/

        // MOSTRAR TODO_
        /**/
        SQLModel model = new SQLModel(this);
        LinkedList<PuntoInteres> items = ((LinkedList)model.getAll());
        int size = items.size();
        PuntoInteres[] puntoInteresData = new  PuntoInteres[size];
        int index = 0;
        for (PuntoInteres pi : items){
            puntoInteresData[index] = pi;
            index ++;
        }
        /**/


        PuntoInteresAdapter adapter = new PuntoInteresAdapter(this,
                R.layout.adapterlayout, puntoInteresData);





        listView = (ListView) findViewById(R.id.list_view);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView title = (TextView)parent.findViewById(R.id.txt_title);
                TextView detail = (TextView)parent.findViewById(R.id.txt_detail);
                Toast.makeText(getApplicationContext(),
                        "Click ListItem: " + title.getText().toString()+ " " + detail.getText().toString(), Toast.LENGTH_LONG)
                        .show();
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
