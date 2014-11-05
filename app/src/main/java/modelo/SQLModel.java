package modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by tributo on 1/11/14.
 */
public class SQLModel implements ModelInterface {
    Context context;
    private SQLiteDatabase db;

    public SQLModel(Context context){

        this.context=context;
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
                    PuntoInteres.COLUMN_NAME_DIRECCION,
                    PuntoInteres.COLUMN_NAME_NOMBRE,
                    PuntoInteres.COLUMN_NAME_TIPO,
                    PuntoInteres.COLUMN_NAME_TELEFONO,
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

                PuntoInteres pi = new PuntoInteres();
                pi.setDireccion(direccion);
                pi.setNombre(nombre);
                pi.setTelefono(telefono);
                pi.setTipo(tipo);
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

                PuntoInteres pi = new PuntoInteres();
                pi.setDireccion(direccion);
                pi.setNombre(nombre);
                pi.setTelefono(telefono);
                pi.setTipo(tipo);

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
}
