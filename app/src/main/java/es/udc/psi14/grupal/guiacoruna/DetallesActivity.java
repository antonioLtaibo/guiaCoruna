package es.udc.psi14.grupal.guiacoruna;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import modelo.PuntoInteres;
import modelo.SQLModel;
import util.util;


public class DetallesActivity extends Activity {

    SQLModel model;
    int id;
    PuntoInteres puntoInteres;
    boolean loadImageFailed = false;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        if (getIntent().getExtras()!=null) {
            model = new SQLModel(this);
            Bundle extra = getIntent().getExtras(); // check if not null
            id = extra.getInt(util.TAG_ID,0);
            puntoInteres = model.findByID(String.valueOf(id));
            ActionBar actionBar;
            actionBar = getActionBar();
            actionBar.setIcon(R.drawable.ic_launcher);
            actionBar.setTitle(puntoInteres.getNombre());
            ((TextView) findViewById(R.id.tv_name)).setText(puntoInteres.getNombre());
            ((TextView)findViewById(R.id.tv_dir)).setText(puntoInteres.getDireccion());
            ((TextView)findViewById(R.id.tv_phone)).setText(puntoInteres.getTelefono().toString());

            String imagenString = puntoInteres.getImageString();

            ImageView imagen = (ImageView) findViewById(R.id.imagen);
            InputStream is = null;
            try {
                //is = this.getResources().getAssets().open("torre_hercules.png");
                is = this.getResources().getAssets().open(imagenString);
            } catch (IOException e) {
                e.printStackTrace();
                loadImageFailed = true;
            }

            if(loadImageFailed){
                try {
                    is = this.getResources().getAssets().open("noPhoto.png");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(is !=null){
                image = BitmapFactory.decodeStream(is);
            }

            imagen.setImageBitmap(image);

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detalles, menu);
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
