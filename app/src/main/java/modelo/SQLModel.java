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
    public boolean addPuntoRuta(PuntoRuta nuevoPunto)  {
        SQLiteOpenHelper helper = new PuntoRutaDBHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        try {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(PuntoRuta.COLUMN_NAME_ID_PUNTO, nuevoPunto.getIdPuntoInteres());
            values.put(PuntoRuta.COLUMN_NAME_ID_RUTA, nuevoPunto.getIdRuta());
            values.put(PuntoRuta.COLUMN_NAME_ORDEN, nuevoPunto.getOrden());

            db.insert(
                    PuntoRuta.TABLE_NAME,
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



    @Override
    public boolean addRuta(Ruta ruta)  {
        SQLiteOpenHelper helper = new RutaDBHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        try {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(Ruta.COLUMN_NAME_NOMBRE, ruta.getNombre());
            values.put(Ruta.COLUMN_NAME_ID_RUTA, ruta.getIdRuta());

            db.insert(
                    Ruta.TABLE_NAME,
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
            values.put(PuntoInteres.COLUMN_NAME_IDENTIFICADOR, pi.getIdentificador());

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
                    PuntoInteres.COLUMN_NAME_URL,
                    PuntoInteres.COLUMN_NAME_IDENTIFICADOR

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
                String identificador = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_IDENTIFICADOR));

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
                pi.setIdentificador(identificador);

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
                String identificador = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoInteres.COLUMN_NAME_IDENTIFICADOR));

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
                pi.setIdentificador(identificador);

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
    public PuntoInteres findByIdentificador(String identificador) {
        String[] selectionArgs = {
                identificador
        };
        Cursor cursor =
                this.findByField(PuntoInteres.COLUMN_NAME_IDENTIFICADOR,selectionArgs);
        PuntoInteres pi = this.parseSingleResult(cursor);
        cursor.close();
        return pi;
    }


    @Override
    public Ruta findRutaByIdentificador(String identificador) {
        String[] selectionArgs = {
                identificador
        };
        String selection = Ruta.COLUMN_NAME_ID_RUTA;
        SQLiteOpenHelper helper = new RutaDBHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;
        try {
            String[] projection = {
                    Ruta.COL_ID,
                    Ruta.COLUMN_NAME_NOMBRE,
                    Ruta.COLUMN_NAME_ID_RUTA
            };
            String sortOrder = Ruta.COLUMN_NAME_NOMBRE + " DESC";
            if(selection!=null) selection = selection + "=?";

            cursor = db.query(
                    Ruta.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

        }catch (Exception ex){
            ex.printStackTrace();
        }

        try{
            boolean hasData = cursor.moveToFirst();
            if(hasData) {
                Integer id = cursor.getInt(
                        cursor.getColumnIndex(Ruta.COL_ID));
                String nombre = cursor.getString(
                        cursor.getColumnIndexOrThrow(Ruta.COLUMN_NAME_NOMBRE));
                String id_ruta = cursor.getString(
                        cursor.getColumnIndexOrThrow(Ruta.COLUMN_NAME_ID_RUTA));

                Ruta ruta = new Ruta();
                ruta.setId(id);
                ruta.setIdRuta(id_ruta);
                ruta.setNombre(nombre);

                return ruta;
            }else{
                return null;
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
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
    public List<Ruta> getAllRutas(){
        SQLiteOpenHelper helper = new RutaDBHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor returnCursor;
        try {
            String[] projection = {
                    Ruta.COL_ID,
                    Ruta.COLUMN_NAME_ID_RUTA,
                    Ruta.COLUMN_NAME_NOMBRE
            };
            String sortOrder = PuntoInteres.COLUMN_NAME_NOMBRE + " DESC";


            Cursor cursor = db.query(
                    Ruta.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );
            List results = new LinkedList <Ruta>();
            boolean moreRows = cursor.moveToFirst();

            while(moreRows){

                Integer id = cursor.getInt(
                        cursor.getColumnIndex(Ruta.COL_ID));
                String nombre = cursor.getString(
                        cursor.getColumnIndexOrThrow(Ruta.COLUMN_NAME_NOMBRE));
                String identificador = cursor.getString(
                        cursor.getColumnIndexOrThrow(Ruta.COLUMN_NAME_ID_RUTA));

                Ruta ruta = new Ruta();
                ruta.setId(id);
                ruta.setIdRuta(identificador);
                ruta.setNombre(nombre);

                results.add(ruta);

                moreRows = cursor.moveToNext();
            }
            return results;

        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PuntoRuta> findPuntosByRutaID(String identificador) {
        String[] selectionArgs = {
                identificador
        };
        String selection = PuntoRuta.COLUMN_NAME_ID_RUTA;
        SQLiteOpenHelper helper = new PuntoRutaDBHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;
        try {
            String[] projection = {
                    PuntoRuta.COLUMN_NAME_ID_PUNTO,
                    PuntoRuta.COLUMN_NAME_ORDEN
            };
            String sortOrder = PuntoRuta.COLUMN_NAME_ORDEN + " ASC";
            if(selection!=null) selection = selection + "=?";

            cursor = db.query(
                    PuntoRuta.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );
            List results = new LinkedList <PuntoRuta>();
            boolean moreRows = cursor.moveToFirst();

            while(moreRows){

                String identificadorPunto = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoRuta.COLUMN_NAME_ID_PUNTO));
                String orden = cursor.getString(
                        cursor.getColumnIndexOrThrow(PuntoRuta.COLUMN_NAME_ORDEN));

                PuntoRuta pr = new PuntoRuta();
                pr.setOrden(orden);
                pr.setIdPuntoInteres(identificadorPunto);
                results.add(pr);
                moreRows = cursor.moveToNext();
            }
            return results;

        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
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
        SQLiteDatabase db = helper.getReadableDatabase();
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
            values.put(PuntoInteres.COLUMN_NAME_IDENTIFICADOR, pi.getIdentificador());

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


    public void loadInitData() {
        setFilenameLock();
        Log.d(TAG, "Using lock file: " + filenameLock);
        if (this.DefaultDataExists()) {
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
                for (; ; ) {
                    int rsz = in.read(buffer, 0, buffer.length);
                    if (rsz < 0)
                        break;
                    out.append(buffer, 0, rsz);
                }
            } finally {
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
            for (int i = 0; i < arr.length(); i++) {
                String identificador = arr.getJSONObject(i).getString("identificador");
                pi = this.findByIdentificador(identificador);
                if (pi != null) {
                    //ACTUALIZAR
                    pi.setNombre(arr.getJSONObject(i).getString("nombre"));
                    pi.setDireccion(arr.getJSONObject(i).getString("direccion"));
                    pi.setTelefono(arr.getJSONObject(i).getString("telefono"));
                    pi.setTipo(arr.getJSONObject(i).getString("tipo"));
                    pi.setImageString(arr.getJSONObject(i).getString("imagen"));
                    pi.setCoordenadas(arr.getJSONObject(i).getString("coordenadas"));
                    pi.setDetalles(arr.getJSONObject(i).getString("detalles"));
                    pi.setUrl(arr.getJSONObject(i).getString("url"));
                    pi.setIdentificador(arr.getJSONObject(i).getString("identificador"));

                    try {
                        this.updatePuntoInteres(pi);
                    } catch (InvalidPIException e) {
                        e.printStackTrace();
                    }
                } else {
                    //CREAR
                    pi = new PuntoInteres();
                    pi.setNombre(arr.getJSONObject(i).getString("nombre"));
                    pi.setDireccion(arr.getJSONObject(i).getString("direccion"));
                    pi.setTelefono(arr.getJSONObject(i).getString("telefono"));
                    pi.setTipo(arr.getJSONObject(i).getString("tipo"));
                    pi.setImageString(arr.getJSONObject(i).getString("imagen"));
                    pi.setCoordenadas(arr.getJSONObject(i).getString("coordenadas"));
                    pi.setDetalles(arr.getJSONObject(i).getString("detalles"));
                    pi.setUrl(arr.getJSONObject(i).getString("url"));
                    pi.setIdentificador(arr.getJSONObject(i).getString("identificador"));

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

            arr = obj.getJSONArray("rutas");
            for (int i = 0; i < arr.length(); i++) {
                String identificador = arr.getJSONObject(i).getString("identificador");
                Ruta ruta = this.findRutaByIdentificador(identificador);
                if (ruta != null) {
                    //ACTUALIZAR


                } else {
                    //CREAR
                    Log.d(TAG, "Nueva ruta a crear.");
                    ruta = new Ruta();
                    ruta.setNombre(arr.getJSONObject(i).getString("nombre"));
                    String idRuta = arr.getJSONObject(i).getString("identificador");
                    ruta.setIdRuta(idRuta);
                    this.addRuta(ruta);

                    JSONArray puntosRuta = arr.getJSONObject(i).getJSONArray("puntos");
                    PuntoRuta nuevoPunto = null;
                    for (int j = 0; j < puntosRuta.length(); j++) {
                        Log.d(TAG, "Nuevo punto de ruta a crear.");
                        nuevoPunto = new PuntoRuta();
                        nuevoPunto.setIdRuta(idRuta);
                        nuevoPunto.setIdPuntoInteres(puntosRuta.getJSONObject(j).getString("idPuntoInteres"));
                        nuevoPunto.setOrden(puntosRuta.getJSONObject(j).getString("orden"));

                        this.addPuntoRuta(nuevoPunto);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        image = assets.getImage("noPhoto.png");
        ImgManager.saveToInternalSorage(image, "noPhoto.png");
        image = assets.getImageIcon("noPhoto.png");
        ImgManager.saveToInternalSorage(image, "noPhoto_icon.png");
    }
}
