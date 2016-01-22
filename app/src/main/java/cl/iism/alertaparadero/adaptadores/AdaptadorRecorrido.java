package cl.iism.alertaparadero.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cl.iism.alertaparadero.R;
import cl.iism.alertaparadero.clases.Recorrido;

/**
 * Created by gvalles on 16-12-2015.
 */
public class AdaptadorRecorrido extends BaseAdapter{
    private Context context;
    private ArrayList<Recorrido> recorridos;

    public AdaptadorRecorrido(Context context, ArrayList<Recorrido> recorridos){
        this.context = context;
        this.recorridos = recorridos;
    }

    public int getCount(){
        return recorridos.size();
    }

    public Recorrido getItem(int position){
        return recorridos.get(position);
    }

    public long getItemId(int position){
        Recorrido recorrido = (Recorrido)recorridos.get(position);
        return recorrido.getId();
    }

    public View getView(int position, View view, ViewGroup viewGroup){
        if(view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.paradero_grid_layout, viewGroup, false);
        }

        TextView txt_codigo_recorrido = (TextView) view.findViewById(R.id.txt_codigo_recorrido);
        TextView txt_direccion_recorrido = (TextView) view.findViewById(R.id.txt_direccion_recorrido);
        ImageView img_color_recorrido = (ImageView) view.findViewById(R.id.img_esquina_recorrido);

        final Recorrido item = (Recorrido)recorridos.get(position);
        txt_codigo_recorrido.setText(item.getCodigo());
        txt_direccion_recorrido.setText(item.getDireccion());

        if(item.getColor().equals("CE")){
            img_color_recorrido.setImageResource(R.mipmap.ic_esquina_celeste);
        }if(item.getColor().equals("RO")){
            img_color_recorrido.setImageResource(R.mipmap.ic_esquina_roja);
        }if(item.getColor().equals("AZ")){
            img_color_recorrido.setImageResource(R.mipmap.ic_esquina_azul);
        }if(item.getColor().equals("AM")){
            img_color_recorrido.setImageResource(R.mipmap.ic_esquina_amarilla);
        }if(item.getColor().equals("NA")){
            img_color_recorrido.setImageResource(R.mipmap.ic_esquina_naranja);
        }if(item.getColor().equals("VE")){
            img_color_recorrido.setImageResource(R.mipmap.ic_esquina_verde);
        }if(item.getColor().equals("XX")){
            img_color_recorrido.setImageResource(R.mipmap.ic_esquina_xx);
        }

        return view;
    }
}
