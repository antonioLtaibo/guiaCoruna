package modelo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import es.udc.psi14.grupal.guiacoruna.R;
import util.ImageManagerAssets;
import util.ImageManagerExternal;
import util.ImageManagerInternal;


public class testmodel extends Activity implements View.OnClickListener {

    TextView numItems;

    EditText et_nombre;
    EditText et_direccion;
    EditText et_telefono;
    EditText et_tipo;
    EditText et_cadena_buscar;

    Button butt_enviar;
    Button butt_contar;
    Button butt_buscar;

    RadioButton buscar_nombre;
    RadioButton buscar_tipo;
    RadioButton buscar_id;

    Button butt_init;
    Button butt_photo;

    ImageView imagen;

    SQLModel model;

    String TAG ="TestModel";
    Bitmap bitmapImagen;
    String imageString;

    static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testmodel);

        model = new SQLModel(this);

        numItems = (TextView) findViewById(R.id.numItems);

        et_nombre = (EditText) findViewById(R.id.et_nombre);
        et_direccion = (EditText) findViewById(R.id.et_direccion);
        et_telefono = (EditText) findViewById(R.id.et_direccion);
        et_tipo = (EditText) findViewById(R.id.et_tipo);
        et_cadena_buscar = (EditText) findViewById(R.id.et_cadena_buscar);

        butt_enviar = (Button) findViewById(R.id.butt_enviar);
        butt_enviar.setOnClickListener(this);
        butt_contar = (Button) findViewById(R.id.butt_contar);
        butt_contar.setOnClickListener(this);
        butt_buscar = (Button) findViewById(R.id.butt_buscar);
        butt_buscar.setOnClickListener(this);

        buscar_nombre = (RadioButton) findViewById(R.id.buscar_nombre);
        buscar_tipo = (RadioButton) findViewById(R.id.buscar_tipo);
        buscar_id = (RadioButton) findViewById(R.id.buscar_id);

        butt_init = (Button) findViewById(R.id.butt_init);
        butt_init.setOnClickListener(this);

        butt_photo = (Button) findViewById(R.id.butt_photo);
        butt_photo.setOnClickListener(this);

        imagen = (ImageView) findViewById(R.id.imagen);
    }



    String mCurrentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "APP_LA_CORUNNA_" +et_nombre.getText().toString();
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        /*File image = File.createTempFile(
                imageFileName,  // prefix
                ".png",         // suffix
                storageDir      // directory
        );*/

        File image = new File(storageDir,imageFileName+".png");
        imageString = imageFileName+".png";

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
            Bitmap image = null;
            try {
                image = BitmapFactory.decodeStream(new FileInputStream(f));
                Log.d(TAG, "Image was created successfully on: "+mCurrentPhotoPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmapImagen = image;
        }
    }



    @Override
    public void onClick(View view) {
        if(view == butt_enviar){
            PuntoInteres pi = new PuntoInteres();
            pi.setTelefono(et_telefono.getText().toString());
            pi.setNombre(et_nombre.getText().toString());
            pi.setDireccion(et_direccion.getText().toString());
            pi.setTipo(et_tipo.getText().toString());
            pi.setImageString(imageString);
            boolean exito = model.addPuntoInteres(pi);

            if(exito){
                Toast.makeText(this,"Item insertado con éxito",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"Inserción Falló",Toast.LENGTH_SHORT).show();
            }
        }else if(view == butt_buscar){
            String string = et_cadena_buscar.getText().toString();
            LinkedList<PuntoInteres> pi = new LinkedList<PuntoInteres>();
            if(buscar_nombre.isChecked()){
                PuntoInteres c = model.findByName(string);
                if(c!=null) pi.add(c);
            }else if(buscar_id.isChecked()){
                PuntoInteres c = model.findByID(string);
                if(c!=null) pi.add(c);
            }else if(buscar_tipo.isChecked()){
                pi = (LinkedList<PuntoInteres>) model.findByType(string);
            }
            if(pi.isEmpty()){
                Toast.makeText(this,"Busqueda Falló",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"Item encontrado con éxito",Toast.LENGTH_SHORT).show();
            }

        }else if(view == butt_contar){
            Integer num = model.getAll().size();
            numItems.setText(num.toString());
        }else if(view == butt_init){
            model.loadInitData();
        }else if(view == butt_photo) {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                if (et_nombre.getText().toString().isEmpty()) {
                    Toast.makeText(this,"Especifica un nombre primero",Toast.LENGTH_SHORT).show();
                } else {
                    this.dispatchTakePictureIntent();
                    if (bitmapImagen != null) imagen.setImageBitmap(bitmapImagen);

                    Log.d(TAG, "Saving image to external");
                    ImageManagerExternal ImgManager = new ImageManagerExternal(this);
                    ImgManager.saveToExternalSorage(bitmapImagen,imageString);
                }
            } else{
                Toast.makeText(this,"No hay una tarjeta SD",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
