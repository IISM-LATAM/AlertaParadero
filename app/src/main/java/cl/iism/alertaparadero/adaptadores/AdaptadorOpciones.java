package cl.iism.alertaparadero.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import cl.iism.alertaparadero.clases.Opciones;
import cl.iism.alertaparadero.R;

/**
 * Created by gvalles on 18-12-2015.
 */
public class AdaptadorOpciones extends BaseAdapter{
    private Context context;
    private ArrayList<Opciones> opciones;
    private String paradero;

    public AdaptadorOpciones(Context context, ArrayList<Opciones> opciones, String paradero){
        this.context = context;
        this.opciones = opciones;
        this.paradero = paradero;
    }

    public int getCount(){
        return this.opciones.size();
    }
    public Opciones getItem(int position){
        return (Opciones)this.opciones.get(position);
    }

    public long getItemId(int position){
        Opciones opcion = this.opciones.get(position);
        return opcion.getId();
    }

    public View getView(int position, View view, ViewGroup viewGroup){
        if(view == null){
            if(position != 4){
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.opciones_grid_layout, viewGroup, false);
            }else{
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.opciones_offline_grid_layout, viewGroup, false);
            }
        }


        TextView nombreOpcion = (TextView) view.findViewById(R.id.lista_paradero_codigo);
        Switch switchOffinle = (Switch) view.findViewById(R.id.switch_modo_offline);

        final Opciones opcion = (Opciones) this.opciones.get(position);
        nombreOpcion.setText(opcion.getOpcion());

        if(position == 4){
            /*SQLiteDatabase.CursorFactory cursorFactory = new SQLiteDatabase.CursorFactory() {
                @Override
                public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable, SQLiteQuery query) {
                    return null;
                }
            };


            if(cursor.getCount() > 0){
                switchOffinle.setChecked(true);
            }else {
                switchOffinle.setChecked(false);
            }*/
        }

        return view;
    }
}
