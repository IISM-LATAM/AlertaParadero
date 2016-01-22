package cl.iism.alertaparadero.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gvalles on 18-01-2016.
 */
public class BusquedaSQLiteHelper extends SQLiteOpenHelper {
    String sqlCreate = "CREATE TABLE IF NOT EXISTS busqueda (q VARCHAR, fecha_creacion VARCHAR);";

    public BusquedaSQLiteHelper(Context context, String nombre, SQLiteDatabase.CursorFactory factory, int flags){
        super(context, nombre, factory, flags);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS busqueda");

        db.execSQL(sqlCreate);
    }
}
