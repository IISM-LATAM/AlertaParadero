package cl.iism.alertaparadero.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gvalles on 18-01-2016.
 */
public class ParaderoSQLiteHelper extends SQLiteOpenHelper {
    String sqlCreate = "CREATE TABLE IF NOT EXISTS paradero (codigo VARCHAR, nombre VARCHAR, parada VARCHAR, latitud DOUBLE, longitud DOUBLE, fecha_actualizacion VARCHAR);";

    public ParaderoSQLiteHelper(Context context, String nombre, SQLiteDatabase.CursorFactory cursorFactory, int version){
        super(context, nombre, cursorFactory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS paradero");

        db.execSQL(sqlCreate);
    }
}
