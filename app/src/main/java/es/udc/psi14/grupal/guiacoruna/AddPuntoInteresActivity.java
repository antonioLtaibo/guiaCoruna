package es.udc.psi14.grupal.guiacoruna;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import modelo.PuntoInteres;
import modelo.SQLModel;
import util.ImageManagerExternal;
import util.InvalidPIException;


public class AddPuntoInteresActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    static final String activityModeCreate = "CREATE";
    static final String activityModeEdit  = "EDIT";
    String mode = activityModeCreate;
    static final String activityMode = "ACTIVITY_MODE";
    static final String latitude = "LATITUDE";
    static final String longitude = "LONGITUDE";

    Integer updating_pi_ID;
    static final String idString ="ID";

    EditText et_nombre;
    static final String campoNombre = "NOMBRE";

    EditText et_direccion;
    static final String campoDireccion = "DIRECCION";

    EditText et_telefono;
    static final String campoTelefono= "TELEFONO";

    Spinner spinnerTipo;
    String tipo="";
    static final String campoTipo = "TIPO";

    String coordenadas = "";
    static final String campoCoordenadas = "COORDENADAS";
    TextView loc;

    Button butt_enviar;
    Button butt_photo;

    ImageView imagen;

    SQLModel model;

    String TAG ="AddPI";
    String imageFullString="";
    static final String campoImagenNombre = "IMAGE";
    String imageThumbnailString="";

    private LocationManager locateManager;
    Boolean maps = false;

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_SEND_COORD = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_punto_interes);

        model = new SQLModel(this);


        et_nombre = (EditText) findViewById(R.id.et_nombre);
        et_direccion = (EditText) findViewById(R.id.et_direccion);
        et_telefono = (EditText) findViewById(R.id.et_telefono);

        butt_enviar = (Button) findViewById(R.id.butt_enviar);
        butt_enviar.setOnClickListener(this);

        butt_photo = (Button) findViewById(R.id.butt_photo);
        butt_photo.setOnClickListener(this);

        loc = (TextView) findViewById(R.id.but_loc);
        loc.setOnClickListener(this);

        imagen = (ImageView) findViewById(R.id.imagen);

        spinnerTipo = (Spinner) findViewById(R.id.tipo_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapter);
        spinnerTipo.setOnItemSelectedListener(this);


        if(getIntent().getExtras() != null){
            mode = getIntent().getExtras().getString(activityMode);

            if (mode.equals(activityModeCreate)){
                loc.setVisibility(View.GONE);
                maps = true;
                coordenadas = String.valueOf(getIntent().getDoubleExtra(latitude,0))+","+ String.valueOf(getIntent().getDoubleExtra(longitude,0));
                et_direccion.setText(getIntent().getStringExtra(campoDireccion));
            }else {
                et_nombre.setText(getIntent().getExtras().getString(campoNombre));
                et_direccion.setText(getIntent().getExtras().getString(campoDireccion));
                et_telefono.setText(getIntent().getExtras().getString(campoTelefono));
                coordenadas = getIntent().getExtras().getString(campoCoordenadas);
                tipo = getIntent().getExtras().getString(campoTipo);
                imageFullString = getIntent().getExtras().getString(campoImagenNombre);

                updating_pi_ID = getIntent().getExtras().getInt(idString);

                int spinnerPostion = adapter.getPosition(tipo);
                spinnerTipo.setSelection(spinnerPostion);
                spinnerPostion = 0;
            }
            butt_enviar.setText(mode);
        }

    }


    String mCurrentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "APP_LA_CORUNNA_" +et_nombre.getText().toString();
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        File image = new File(storageDir,imageFileName+".png");
        imageFullString = imageFileName+".png";
        imageThumbnailString = imageFileName+"_icon.png";

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d(TAG, "Couldn't create image");
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT , Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            File f = new File(mCurrentPhotoPath);
            Bitmap imageData = null;
            try {
                imageData = BitmapFactory.decodeStream(new FileInputStream(f));
                Log.d(TAG, "Image was created successfully on: "+mCurrentPhotoPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (imageData != null) {
                imagen.setImageBitmap(imageData);
                Log.d(TAG, "Saving image to external");
                ImageManagerExternal ImgManager = new ImageManagerExternal(this);
                Bitmap ThumbImage1 = ThumbnailUtils.extractThumbnail(imageData, 1024, 1024);
                ImgManager.saveToExternalSorage(ThumbImage1, imageFullString);


                Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(imageData, 64, 64);
                ImgManager.saveToExternalSorage(ThumbImage,imageThumbnailString);
            }
        }
        if (requestCode == REQUEST_SEND_COORD && resultCode == RESULT_OK){
            //codigo de las coordenadas
            coordenadas = data.getStringExtra(campoCoordenadas);
            et_direccion.setText(data.getStringExtra(campoDireccion));
            Log.d("COORDENADAS",coordenadas);
        }

    }



    @Override
    public void onClick(View view) {
        if(view == butt_enviar){
            PuntoInteres pi = new PuntoInteres();
            pi.setTelefono(et_telefono.getText().toString());
            pi.setNombre(et_nombre.getText().toString());
            pi.setDireccion(et_direccion.getText().toString());
            pi.setTipo(tipo);
            pi.setImageString(imageFullString);
            pi.setCoordenadas(coordenadas);
            boolean exito = false;
            try {
                if(mode.compareTo(activityModeCreate)==0){
                    exito = model.addPuntoInteres(pi);
                    if(exito){
                        et_telefono.setText("");
                        et_nombre.setText("");
                        et_direccion.setText("");
                        imagen.setImageResource(0);
                        Toast.makeText(this, "Item insertado con éxito", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this,"Inserción Falló",Toast.LENGTH_SHORT).show();
                    }
                }else if (mode.compareTo(activityModeEdit)==0){
                    pi.setId(updating_pi_ID);
                    exito = model.updatePuntoInteres(pi);
                    if(exito){
                        et_telefono.setText("");
                        et_nombre.setText("");
                        et_direccion.setText("");
                        imagen.setImageResource(0);
                        Toast.makeText(this, "Item actualizado con éxito", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this,"Actualización Falló",Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }else{
                    finish();
                }
            } catch (InvalidPIException e) {
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
            if(exito){
                et_telefono.setText("");
                et_nombre.setText("");
                et_direccion.setText("");
                imagen.setImageResource(0);
                Toast.makeText(this, "Item insertado con éxito", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"Inserción Falló",Toast.LENGTH_SHORT).show();
            }
        }else if(view == butt_photo) {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                if (et_nombre.getText().toString().isEmpty()) {
                    Toast.makeText(this,"Especifica un nombre primero",Toast.LENGTH_SHORT).show();
                } else {
                    this.dispatchTakePictureIntent();

                }
            } else{

                Toast.makeText(this,"No hay una tarjeta SD",Toast.LENGTH_SHORT).show();
            }
        }else if (view == loc){
            startActivityForResult(new Intent(getApplicationContext(), selecPuntoActiv.class), REQUEST_SEND_COORD);

        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView == spinnerTipo){
            tipo = adapterView.getItemAtPosition(i).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        tipo = "";
    }



}
