package es.udc.psi14.grupal.guiacoruna;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import modelo.PuntoInteres;
import modelo.SQLModel;
import util.util;

import static android.content.Intent.ACTION_DIAL;
import static android.view.View.INVISIBLE;


public class DetallesActivity extends Activity implements View.OnClickListener {

    SQLModel model;
    int id;
    PuntoInteres puntoInteres;
    boolean loadImageFailed = false;
    Bitmap image;

    View v1,v2,v3,v4;

    TextView tvName,tvDir,tvPhone,tvDetail, tvWeb;
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
            tvName = (TextView) findViewById(R.id.tv_name);
            tvName.setText(puntoInteres.getNombre());
            tvDir = (TextView) findViewById(R.id.tv_dir);
            tvPhone =(TextView) findViewById(R.id.tv_phone);

            if (puntoInteres.getTelefono().toString().isEmpty()){
                ((LinearLayout)findViewById(R.id.ll3)).setVisibility(INVISIBLE);
                ((View)findViewById(R.id.lblack3)).setVisibility(INVISIBLE);
            }else {
                tvPhone.setText(puntoInteres.getTelefono().toString());
            }
            tvDir.setText(puntoInteres.getDireccion());
            tvDetail = (TextView) findViewById(R.id.tv_detalles);
            if (puntoInteres.getDetalles().isEmpty()){
                tvDetail.setVisibility(INVISIBLE);
            }else {
                tvDetail.setText(puntoInteres.getDetalles());
            }

            tvWeb = (TextView) findViewById(R.id.tv_url);
            if (puntoInteres.getUrl().isEmpty()){
                ((LinearLayout)findViewById(R.id.ll4)).setVisibility(INVISIBLE);
                ((View)findViewById(R.id.lblack4)).setVisibility(INVISIBLE);
            }else {
                tvWeb.setText(puntoInteres.getUrl());
            }



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


            tvDir.setOnClickListener(this);
            tvPhone.setOnClickListener(this);
            tvWeb.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_phone:
                int phone = Integer.valueOf(tvPhone.getText().toString().replaceAll(" ",""));
                Intent intentPhone = new Intent(ACTION_DIAL,Uri.parse("tel:" +phone));
                startActivity(intentPhone);
                break;
            case R.id.tv_dir:
                String buscar = puntoInteres.getCoordenadas();
                Intent intentMap = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("geo:0,0?q="+buscar));
                startActivity(intentMap);
                break;
            case R.id.tv_url:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(puntoInteres.getUrl()));
                startActivity(i);
        }
    }
}
