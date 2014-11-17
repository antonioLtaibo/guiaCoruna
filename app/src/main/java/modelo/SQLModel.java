package modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import es.udc.psi14.grupal.guiacoruna.R;
import util.ImageManagerAssets;
import util.ImageManagerInternal;

/**
 * Created by tributo on 1/11/14.
 */
public class SQLModel implements ModelInterface {
    Context context;
    private SQLiteDatabase db;
    String TAG = "SQLModel";

    public SQLModel(Context context){

        this.context=context;
    }

    String filenameLock = "defaultDataLoaded.lock";


    public boolean DefaultDataExists(){
        FileInputStream inputStream;
        try {
            inputStream = context.openFileInput(filenameLock);
            inputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void createLockDefaultData(){
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(filenameLock, Context.MODE_PRIVATE);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public boolean addPuntoInteres(PuntoInteres pi) {

        try {
            SQLiteOpenHelper helper = new PuntoInteresDBHelper(context);

            // Gets the data repository in write mode
            db = helper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(PuntoInteres.COLUMN_NAME_DIRECCION, pi.getDireccion());
            values.put(PuntoInteres.COLUMN_NAME_NOMBRE, pi.getNombre());
            values.put(PuntoInteres.COLUMN_NAME_TIPO, pi.getTipo());
            values.put(PuntoInteres.COLUMN_NAME_TELEFONO, pi.getTelefono());
            values.put(PuntoInteres.COLUMN_NAME_IMAGEN,pi.getImageString());
            values.put(PuntoInteres.COLUMN_NAME_COORDENADAS,pi.getCoordenadas());
            values.put(PuntoInteres.COLUMN_NAME_DETALLES,pi.getDetalles());
            values.put(PuntoInteres.COLUMN_NAME_URL,pi.getUrl());

            // Insert the new row, returning the primary key value of the new row
            long newRowId;
            newRowId = db.insert(
                    PuntoInteres.TABLE_NAME,
                    null,
                    values);

            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }


    private Cursor findByField(String selection, String[] selectionArgs) {
        try {
            SQLiteOpenHelper helper = new PuntoInteresDBHelper(context);

            db = helper.getReadableDatabase();

            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            String[] projection = {
                    PuntoInteres.COL_ID,
                    PuntoInteres.COLUMN_NAME_DIRECCION,
                    PuntoInteres.COLUMN_NAME_NOMBRE,
                    PuntoInteres.COLUMN_NAME_TIPO,
                    PuntoInteres.COLUMN_NAME_TELEFONO,
                    PuntoInteres.COLUMN_NAME_IMAGEN,
                    PuntoInteres.COLUMN_NAME_COORDENADAS,
                    PuntoInteres.COLUMN_NAME_DETALLES,
                    PuntoInteres.COLUMN_NAME_URL

            };

            //String selection = PuntoInteresContract.PuntoInteres.COLUMN_NAME_NOMBRE;

            /*String[] selectionArgs = {
                    value
            };*/

            String sortOrder = PuntoInteres.COLUMN_NAME_NOMBRE + " DESC";

            if(selection!=null) selection = selection + "=?";

            Cursor cursor = db.query(
                    PuntoInteres.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );
            return cursor;

        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }


    private PuntoInteres parseSingleResult(Cursor cursor){
        try{
            boolean hasData = cursor.moveToFirst();
            if(hasData) {

                Integer id = cursor.getInt(
                        cursor.getColumnIndex(PuntoInteres.COL_ID)
                );

                String telefono = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_TELEFONO)
                );
                String tipo = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_TIPO)
                );
                String direccion = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_DIRECCION)
                );
                String nombre = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_NOMBRE)
                );
                String imagen = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_IMAGEN)
                );

                String coordenadas = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_COORDENADAS)
                );

                String detalles = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_DETALLES)
                );

                String url = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_URL)
                );

                PuntoInteres pi = new PuntoInteres();
                pi.setId(id);
                pi.setDireccion(direccion);
                pi.setNombre(nombre);
                pi.setTelefono(telefono);
                pi.setTipo(tipo);
                pi.setImageString(imagen);
                pi.setCoordenadas(coordenadas);
                pi.setDetalles(detalles);
                pi.setUrl(url);

                return pi;
            }else{
                return null;
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            db.close();
        }
    }

    private List<PuntoInteres> parseMultipleResults(Cursor cursor){
        try{
            List results = new LinkedList <PuntoInteres>();
            boolean moreRows = cursor.moveToFirst();

            while(moreRows){

                Integer id = cursor.getInt(
                        cursor.getColumnIndex(PuntoInteres.COL_ID)
                );

                String telefono = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_TELEFONO)
                );
                String tipo = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_TIPO)
                );
                String direccion = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_DIRECCION)
                );
                String nombre = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_NOMBRE)
                );
                String imagen = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_IMAGEN)
                );

                String coordenadas = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_COORDENADAS)
                );

                String detalles = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_DETALLES)
                );

                String url = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_URL)
                );

                PuntoInteres pi = new PuntoInteres();
                pi.setId(id);
                pi.setDireccion(direccion);
                pi.setNombre(nombre);
                pi.setTelefono(telefono);
                pi.setTipo(tipo);
                pi.setImageString(imagen);
                pi.setCoordenadas(coordenadas);
                pi.setDetalles(detalles);
                pi.setUrl(url);

                results.add(pi);

                moreRows = cursor.moveToNext();
            }
            return results;
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            db.close();
        }
    }

    @Override
    public PuntoInteres findByName(String nombre) {
        String[] selectionArgs = {
                nombre
        };
        Cursor cursor =
                this.findByField(PuntoInteres.COLUMN_NAME_NOMBRE,selectionArgs);
        return this.parseSingleResult(cursor);
    }

    @Override
    public PuntoInteres findByID(String id) {
        String[] selectionArgs = {
                id
        };
        Cursor cursor =
                this.findByField(PuntoInteres.COL_ID,selectionArgs);
        return this.parseSingleResult(cursor);
    }


    @Override
    public List<PuntoInteres> findByType(String tipo) {
        String[] selectionArgs = {
                tipo
        };
        Cursor cursor =
                this.findByField(PuntoInteres.COLUMN_NAME_TIPO,selectionArgs);
        return this.parseMultipleResults(cursor);
    }

    @Override
    public List<PuntoInteres> getAll() {
        String[] selectionArgs = null;
        Cursor cursor =
                this.findByField(null,null);
        return this.parseMultipleResults(cursor);
    }


    public void loadInitData(){

        if(this.DefaultDataExists()){
           Log.d(TAG, "Default Data Already Loaded");
            return;
        }

        this.createLockDefaultData();

        InputStream is = context.getResources().openRawResource(R.raw.first_data);
        final char[] buffer = new char[512];
        final StringBuilder out = new StringBuilder();
        try {
            final Reader in = new InputStreamReader(is, "UTF-8");
            try {
                for (;;) {
                    int rsz = in.read(buffer, 0, buffer.length);
                    if (rsz < 0)
                        break;
                    out.append(buffer, 0, rsz);
                }
            }
            finally {
                in.close();
            }
        } catch (UnsupportedEncodingException ex) {

        } catch (IOException ex) {

        }
        String fileString = out.toString();

        Log.d(TAG, "Saving image to gallery");
        ImageManagerInternal ImgManager = new ImageManagerInternal(context);
        ImageManagerAssets assets = new ImageManagerAssets(context);
        Bitmap image;
        String imageString;

        JSONObject obj = null;
        try {
            obj = new JSONObject(fileString);
            JSONArray arr = obj.getJSONArray("data");
            for (int i = 0; i < arr.length(); i++)
            {
                PuntoInteres pi = new PuntoInteres();

                pi.setNombre(arr.getJSONObject(i).getString("nombre"));
                pi.setDireccion(arr.getJSONObject(i).getString("direccion"));
                pi.setTelefono(arr.getJSONObject(i).getString("telefono"));
                pi.setTipo(arr.getJSONObject(i).getString("tipo"));
                pi.setImageString(arr.getJSONObject(i).getString("imagen"));
                pi.setCoordenadas(arr.getJSONObject(i).getString("coordenadas"));
                pi.setDetalles(arr.getJSONObject(i).getString("detalles"));
                pi.setUrl(arr.getJSONObject(i).getString("url"));

                this.addPuntoInteres(pi);

                imageString = pi.getImageString();

                if (!imageString.isEmpty()) {
                    String imageStringIcon = imageString.substring(0, imageString.lastIndexOf(".")) + "_icon.png";
                    image = assets.getImage(imageString);
                    if (image != null) ImgManager.saveToInternalSorage(image, imageString);
                    image = assets.getImageIcon(imageString);
                    if (image != null) ImgManager.saveToInternalSorage(image, imageStringIcon);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        image = assets.getImage("noPhoto.png");
        ImgManager.saveToInternalSorage(image,"noPhoto.png");
        image = assets.getImageIcon("noPhoto.png");
        ImgManager.saveToInternalSorage(image,"noPhoto_icon.png");

    }

}
