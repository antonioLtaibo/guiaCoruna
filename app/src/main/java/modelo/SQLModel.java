package modelo;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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

import util.util;
import util.InvalidPIException;

/**
 * Created by tributo on 1/11/14.
 */
public class SQLModel implements ModelInterface {
    Context context;
    String TAG = "SQLModel";

    public SQLModel(Context context){
        this.context=context;
    }

    String filenameLock;

    private void setFilenameLock(){
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;
        filenameLock = version+".lock";
    }

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

    public void createLockData(){
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(filenameLock, Context.MODE_PRIVATE);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void validatePI(PuntoInteres pi) throws InvalidPIException {
        String t = pi.getTipo();

        if(t.compareTo(util.TYPE_HOTEL)!=0 && t.compareTo(util.TYPE_SHOP)!=0 && t.compareTo(util.TYPE_RESTAURANT)!=0 &&
                t.compareTo(util.TYPE_NIGHT)!=0 && t.compareTo(util.TYPE_MUSEUM)!=0 && t.compareTo(util.TYPE_MONUMENT)!=0)
            throw new InvalidPIException("Invalid type.");

        if (pi.getDireccion().isEmpty() || pi.getNombre().isEmpty() || pi.getTelefono().isEmpty()) {
            throw new InvalidPIException("Some field was left blank.");
        }
    }


    @Override
    public boolean removePuntoInteres(String id) {
        boolean status;
        SQLiteOpenHelper helper = new PuntoInteresDBHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        if(this.findByID(id)==null){
            status = false;
        }else{
            status = db.delete(PuntoInteres.TABLE_NAME, PuntoInteres.COL_ID + "=" + id, null) > 0;
        }
        db.close();
        return status;
    }


    @Override
    public boolean addPuntoInteres(PuntoInteres pi) throws InvalidPIException {
        this.validatePI(pi);

        SQLiteOpenHelper helper = new PuntoInteresDBHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        try {
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

            db.insert(
                    PuntoInteres.TABLE_NAME,
                    null,
                    values);

            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }finally {
            db.close();
        }
    }


    private Cursor findByField(String selection, String[] selectionArgs) {
        SQLiteOpenHelper helper = new PuntoInteresDBHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor returnCursor;
        try {
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
            returnCursor = cursor;

        }catch (Exception ex){
            ex.printStackTrace();
            returnCursor = null;
        }
        return returnCursor;
    }


    private PuntoInteres parseSingleResult(Cursor cursor){
        try{
            boolean hasData = cursor.moveToFirst();
            if(hasData) {
                Integer id = cursor.getInt(
                        cursor.getColumnIndex(PuntoInteres.COL_ID));
                String telefono = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_TELEFONO));
                String tipo = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_TIPO));
                String direccion = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_DIRECCION));
                String nombre = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_NOMBRE));
                String imagen = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_IMAGEN));
                String coordenadas = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_COORDENADAS));
                String detalles = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_DETALLES));
                String url = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_URL));

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
        }
    }

    private List<PuntoInteres> parseMultipleResults(Cursor cursor){
        try{
            List results = new LinkedList <PuntoInteres>();
            boolean moreRows = cursor.moveToFirst();

            while(moreRows){

                Integer id = cursor.getInt(
                        cursor.getColumnIndex(PuntoInteres.COL_ID));
                String telefono = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_TELEFONO));
                String tipo = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_TIPO));
                String direccion = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_DIRECCION));
                String nombre = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_NOMBRE));
                String imagen = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_IMAGEN));
                String coordenadas = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_COORDENADAS));
                String detalles = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_DETALLES));
                String url = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_URL));

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
        }
    }

    @Override
    public PuntoInteres findByName(String nombre) {
        String[] selectionArgs = {
                nombre
        };
        Cursor cursor =
                this.findByField(PuntoInteres.COLUMN_NAME_NOMBRE,selectionArgs);
        PuntoInteres pi = this.parseSingleResult(cursor);
        cursor.close();
        return pi;
    }

    @Override
    public PuntoInteres findByID(String id) {
        String[] selectionArgs = {
                id
        };
        Cursor cursor =
                this.findByField(PuntoInteres.COL_ID,selectionArgs);
        PuntoInteres pi = this.parseSingleResult(cursor);
        cursor.close();
        return pi;
    }


    @Override
    public List<PuntoInteres> findByType(String tipo) {
        String[] selectionArgs = {
                tipo
        };
        Cursor cursor =
                this.findByField(PuntoInteres.COLUMN_NAME_TIPO,selectionArgs);
        List<PuntoInteres> puntos = this.parseMultipleResults(cursor);
        cursor.close();
        return puntos;
    }

    @Override
    public List<PuntoInteres> getAll() {
        Cursor cursor =
                this.findByField(null,null);
        List<PuntoInteres> puntos = this.parseMultipleResults(cursor);
        cursor.close();
        return puntos;
    }

    @Override
    public boolean updatePuntoInteres(PuntoInteres pi) throws InvalidPIException {
        SQLiteOpenHelper helper = new PuntoInteresDBHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        boolean status = false;
        this.validatePI(pi);

        try {
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

            String strFilter = "_id=" + pi.getId();

            db.update(
                    PuntoInteres.TABLE_NAME,
                    values,
                    strFilter,
                    null
            );

            status = true;
        }catch (Exception ex){
            ex.printStackTrace();
            status = false;
        }finally {
            db.close();
        }
        return status;
    }


    public void loadInitData(){
        setFilenameLock();
        Log.d(TAG, "Using lock file: "+filenameLock);
        if(this.DefaultDataExists()){
           Log.d(TAG, "Data Already Loaded");
           return;
        }
        createLockData();

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

        ImageManagerInternal ImgManager = new ImageManagerInternal(context);
        ImageManagerAssets assets = new ImageManagerAssets(context);
        Bitmap image;
        String imageString;

        JSONObject obj;
        PuntoInteres pi;
        try {
            obj = new JSONObject(fileString);
            JSONArray arr = obj.getJSONArray("data");
            for (int i = 0; i < arr.length(); i++)
            {
                String nombre = arr.getJSONObject(i).getString("nombre");
                pi = this.findByName(nombre);
                if (pi != null){
                    //ACTUALIZAR
                    pi.setNombre(nombre);
                    pi.setDireccion(arr.getJSONObject(i).getString("direccion"));
                    pi.setTelefono(arr.getJSONObject(i).getString("telefono"));
                    pi.setTipo(arr.getJSONObject(i).getString("tipo"));
                    pi.setImageString(arr.getJSONObject(i).getString("imagen"));
                    pi.setCoordenadas(arr.getJSONObject(i).getString("coordenadas"));
                    pi.setDetalles(arr.getJSONObject(i).getString("detalles"));
                    pi.setUrl(arr.getJSONObject(i).getString("url"));
                    try {
                        this.updatePuntoInteres(pi);
                    } catch (InvalidPIException e) {
                        e.printStackTrace();
                    }
                }else{
                    //CREAR
                    pi = new PuntoInteres();
                    pi.setNombre(nombre);
                    pi.setDireccion(arr.getJSONObject(i).getString("direccion"));
                    pi.setTelefono(arr.getJSONObject(i).getString("telefono"));
                    pi.setTipo(arr.getJSONObject(i).getString("tipo"));
                    pi.setImageString(arr.getJSONObject(i).getString("imagen"));
                    pi.setCoordenadas(arr.getJSONObject(i).getString("coordenadas"));
                    pi.setDetalles(arr.getJSONObject(i).getString("detalles"));
                    pi.setUrl(arr.getJSONObject(i).getString("url"));

                    try {
                        this.addPuntoInteres(pi);
                    } catch (InvalidPIException e) {
                        e.printStackTrace();
                        continue;
                    }
                }

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
