package es.udc.psi14.grupal.guiacoruna;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import modelo.PuntoInteres;
import modelo.SQLModel;
import util.*;

import static android.content.Intent.ACTION_DIAL;


public class DetallesActivity extends Activity implements View.OnClickListener {

    SQLModel model;
    int id;
    PuntoInteres puntoInteres;
    String TAG = "DetallesActivity";

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
                ((LinearLayout)findViewById(R.id.ll3)).setVisibility(View.GONE);
                ((View)findViewById(R.id.lblack3)).setVisibility(View.GONE);
            }else {
                tvPhone.setText(puntoInteres.getTelefono().toString());
            }
            tvDir.setText(puntoInteres.getDireccion());
            tvDetail = (TextView) findViewById(R.id.tv_detalles);
            if (puntoInteres.getDetalles().isEmpty()){
                tvDetail.setVisibility(View.GONE);
            }else {
                tvDetail.setText(puntoInteres.getDetalles());
            }

            tvWeb = (TextView) findViewById(R.id.tv_url);
            if (puntoInteres.getUrl().isEmpty()){
                ((LinearLayout)findViewById(R.id.ll4)).setVisibility(View.GONE);
                ((View)findViewById(R.id.lblack4)).setVisibility(View.GONE);
            }else {
                tvWeb.setText(puntoInteres.getUrl());
            }



            String imagenString = puntoInteres.getImageString();
            if (imagenString.isEmpty()){
                imagenString = "noPhoto.png";
            }
            ImageView imagenView = (ImageView) findViewById(R.id.imagen);

            ImageManagerInternal ImgManagerInt = new ImageManagerInternal(this);
            ImageManagerExternal ImgManagerExt= new ImageManagerExternal(this);


            Bitmap image = ImgManagerInt.getImage(imagenString);
            if(image == null){
                image = ImgManagerExt.getImage(imagenString);
            }

            if(image !=null) imagenView.setImageBitmap(image);

            tvDir.setOnClickListener(this);
            tvPhone.setOnClickListener(this);
            tvWeb.setOnClickListener(this);

        }

    }

    public void galleryAddPic(String path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
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
        if (id == R.id.editar) {
            Intent intent = new Intent(this, AddPuntoInteresActivity.class);
            Bundle extras = new Bundle();
            extras.putString(AddPuntoInteresActivity.campoNombre, puntoInteres.getNombre());
            extras.putString(AddPuntoInteresActivity.campoTelefono, puntoInteres.getTelefono());
            extras.putString(AddPuntoInteresActivity.campoDireccion, puntoInteres.getDireccion());
            extras.putString(AddPuntoInteresActivity.campoCoordenadas, puntoInteres.getCoordenadas());
            extras.putString(AddPuntoInteresActivity.campoTipo, puntoInteres.getTipo());
            extras.putString(AddPuntoInteresActivity.campoImagenNombre, puntoInteres.getImageString());

            extras.putString(AddPuntoInteresActivity.activityMode, AddPuntoInteresActivity.activityModeEdit);
            extras.putInt(AddPuntoInteresActivity.idString, puntoInteres.getId());

            intent.putExtras(extras);

            startActivity(intent);
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
