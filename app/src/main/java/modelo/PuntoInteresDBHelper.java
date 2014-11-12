package modelo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.MediaStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

/**
 * Created by tributo on 27/10/14.
 */
public class PuntoInteresDBHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PuntoInteres.TABLE_NAME + " (" +
                    PuntoInteres.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    PuntoInteres.COLUMN_NAME_NOMBRE + TEXT_TYPE + COMMA_SEP +
                    PuntoInteres.COLUMN_NAME_DIRECCION + TEXT_TYPE + COMMA_SEP +
                    PuntoInteres.COLUMN_NAME_TELEFONO + TEXT_TYPE + COMMA_SEP +
                    PuntoInteres.COLUMN_NAME_TIPO + TEXT_TYPE + COMMA_SEP +
                    PuntoInteres.COLUMN_NAME_IMAGEN + TEXT_TYPE + COMMA_SEP +
                    PuntoInteres.COLUMN_NAME_COORDENADAS + TEXT_TYPE + COMMA_SEP +
                    PuntoInteres.COLUMN_NAME_DETALLES + TEXT_TYPE + COMMA_SEP +
                    PuntoInteres.COLUMN_NAME_URL + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PuntoInteres.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PuntosInteres.db";

    public PuntoInteresDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
