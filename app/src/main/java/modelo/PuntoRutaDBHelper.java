package modelo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tributo on 31/12/14.
 */
public class PuntoRutaDBHelper  extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    /*public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PuntoRuta.TABLE_NAME + " (" +
                    PuntoRuta.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    PuntoRuta.COLUMN_NAME_ID_PUNTO + TEXT_TYPE + COMMA_SEP +
                    PuntoRuta.COLUMN_NAME_ID_RUTA + TEXT_TYPE + COMMA_SEP +
                    PuntoRuta.COLUMN_NAME_ORDEN + TEXT_TYPE +
                    " )";*/

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PuntoRuta.TABLE_NAME + " (" +
                    PuntoRuta.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                    PuntoRuta.COLUMN_NAME_ID_PUNTO + TEXT_TYPE + COMMA_SEP +
                    PuntoRuta.COLUMN_NAME_ID_RUTA + TEXT_TYPE + COMMA_SEP +
                    PuntoRuta.COLUMN_NAME_ORDEN + TEXT_TYPE + COMMA_SEP +
                    " FOREIGN KEY ("+PuntoRuta.COLUMN_NAME_ID_PUNTO+") REFERENCES "+PuntoInteres.TABLE_NAME+" ("+PuntoInteres.COLUMN_NAME_IDENTIFICADOR+")" +
                    " );";



    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PuntoRuta.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PuntosRuta.db";

    public PuntoRutaDBHelper(Context context) {
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
