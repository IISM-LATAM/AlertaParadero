package cl.iism.alertaparadero.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cl.iism.alertaparadero.clases.Paradero;
import cl.iism.alertaparadero.R;

import java.util.ArrayList;

/**
 * Created by gvalles on 24-12-2015.
 */
public class AdaptadorParadero extends BaseAdapter {
    private Context context;
    private ArrayList<Paradero> paraderos;

    public AdaptadorParadero(Context context, ArrayList<Paradero> paraderos){
        this.context = context;
        this.paraderos = paraderos;
    }

    public int getCount(){
        return this.paraderos.size();
    }

    public Paradero getItem(int position){
        return this.paraderos.get(position);
    }

    public long getItemId(int position){
        Paradero paradero = (Paradero)paraderos.get(position);
        return paradero.getId();
    }

    public View getView(int position, View view, ViewGroup viewGroup){
        if(view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lista_paraderos_grid_layout, viewGroup, false);
        }

        TextView codigoParadero = (TextView) view.findViewById(R.id.lista_paradero_codigo);
        TextView nombreParadero = (TextView) view.findViewById(R.id.lista_paradero_nombre);
        TextView distanciaParadero = (TextView) view.findViewById(R.id.lista_paradero_distancia);

        Paradero item = (Paradero) this.paraderos.get(position);

        codigoParadero.setText(item.getCodigo());
        nombreParadero.setText(item.getNombre());
        distanciaParadero.setText(item.getDistancia());

        return view;
    }
}
