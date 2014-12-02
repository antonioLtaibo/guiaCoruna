package es.udc.psi14.grupal.guiacoruna;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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


public class AddPuntoInteres extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    TextView numItems;

    EditText et_nombre;
    EditText et_direccion;
    EditText et_telefono;
    Spinner spinnerTipo;
    String tipo="";
    EditText et_cadena_buscar;

    Button butt_enviar;

    Button butt_photo;

    ImageView imagen;

    SQLModel model;

    String TAG ="AddPI";
    String imageFullString="";
    String imageThumbnailString="";

    static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_punto_interes);

        model = new SQLModel(this);

        numItems = (TextView) findViewById(R.id.numItems);

        et_nombre = (EditText) findViewById(R.id.et_nombre);
        et_direccion = (EditText) findViewById(R.id.et_direccion);
        et_telefono = (EditText) findViewById(R.id.et_telefono);
        et_cadena_buscar = (EditText) findViewById(R.id.et_cadena_buscar);

        butt_enviar = (Button) findViewById(R.id.butt_enviar);
        butt_enviar.setOnClickListener(this);

        butt_photo = (Button) findViewById(R.id.butt_photo);
        butt_photo.setOnClickListener(this);

        imagen = (ImageView) findViewById(R.id.imagen);

        spinnerTipo = (Spinner) findViewById(R.id.tipo_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapter);
        spinnerTipo.setOnItemSelectedListener(this);
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
                ImgManager.saveToExternalSorage(imageData, imageFullString);


                Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(imageData, 64, 64);
                ImgManager.saveToExternalSorage(ThumbImage,imageThumbnailString);

            }
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
            boolean exito = false;
            try {
                exito = model.addPuntoInteres(pi);
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
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_punto_interes, menu);
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
