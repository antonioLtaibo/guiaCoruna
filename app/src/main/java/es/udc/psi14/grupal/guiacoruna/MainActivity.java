package es.udc.psi14.grupal.guiacoruna;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import modelo.SQLModel;
import modelo.testmodel;
import util.util;


public class MainActivity extends Activity implements View.OnClickListener {


    private Button butMus,butHot,butNoche,butTend,butRes,butMon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        butMus = (Button) findViewById(R.id.but_mus);
        butHot = (Button) findViewById(R.id.but_hot);
        butNoche = (Button) findViewById(R.id.but_noche);
        butTend = (Button) findViewById(R.id.but_tend);
        butRes = (Button) findViewById(R.id.but_res);
        butMon = (Button) findViewById(R.id.but_mon);

        butMus.setOnClickListener(this);
        butRes.setOnClickListener(this);
        butNoche.setOnClickListener(this);
        butTend.setOnClickListener(this);
        butMon.setOnClickListener(this);
        butHot.setOnClickListener(this);

        SQLModel model;
        model = new SQLModel(this);
        model.loadInitData();
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
    public void onClick(View view) {
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
    }
}
