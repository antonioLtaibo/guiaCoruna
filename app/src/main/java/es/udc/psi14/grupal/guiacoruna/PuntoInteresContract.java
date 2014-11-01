package es.udc.psi14.grupal.guiacoruna;

import android.provider.BaseColumns;

/**
 * Created by tributo on 27/10/14.
 */
public final class PuntoInteresContract {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PuntoInteres.TABLE_NAME + " (" +
                    PuntoInteres._ID + " INTEGER PRIMARY KEY," +
                    PuntoInteres.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    PuntoInteres.COLUMN_NAME_NOMBRE + TEXT_TYPE + COMMA_SEP +
                    PuntoInteres.COLUMN_NAME_DIRECCION + TEXT_TYPE + COMMA_SEP +
                    PuntoInteres.COLUMN_NAME_TELEFONO + TEXT_TYPE + COMMA_SEP +
                    PuntoInteres.COLUMN_NAME_TIPO + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PuntoInteres.TABLE_NAME;

        public PuntoInteresContract() {}

        /* Inner class that defines the table contents */
        public static abstract class PuntoInteres implements BaseColumns {
            public static final String TABLE_NAME = "puntosInteres";
            public static final String COLUMN_NAME_ENTRY_ID = "entryid";
            public static final String COLUMN_NAME_NOMBRE = "nombre";
            public static final String COLUMN_NAME_DIRECCION = "direccion";
            public static final String COLUMN_NAME_TELEFONO = "telefono";
            public static final String COLUMN_NAME_TIPO = "tipo";
        }

 }

