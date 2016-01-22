package cl.iism.alertaparadero;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import cl.iism.alertaparadero.adaptadores.AdaptadorParadero;
import cl.iism.alertaparadero.clases.Paradero;
import cl.iism.alertaparadero.utilidades.Conexion;

/**
 * Created by gvalles on 28-12-2015.
 */
public class Lista_Paraderos_Cercanos extends Activity {
    private ArrayList<Paradero> listaParaderos;
    private GridView list_paraderos;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_paraderos_cercanos);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        list_paraderos = (GridView) findViewById(R.id.list_paraderos_cercanos);

        TextView txt_list_paraderos_cercanos = (TextView) findViewById(R.id.txt_list_paraderos_cercanos);

        setTitle("Lista de paraderos");

        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();

        if(bundle.getString("direccion") != null){
            txt_list_paraderos_cercanos.setText("Paraderos cercanos a " + bundle.getString("direccion"));
            Conexion conexion = new Conexion();
            conexion.setContext(this);

            try {
                SharedPreferences config = getSharedPreferences("AlertaParadero_Config", MODE_PRIVATE);

                String metros = config.getString("buscar.metros", "1000");
                JSONObject json = conexion.execute("ws_busca_paraderos_por_direccion", "direccion=" + URLEncoder.encode(bundle.getString("direccion")) + "&metros=" + metros).get();
                JSONObject header = json.getJSONObject("header");

                if(header.getString("coderr").equals("00")){
                    JSONArray output = json.getJSONArray("output");

                    listaParaderos = new ArrayList<Paradero>();
                    for(int i=0; i < output.length(); i++){
                        JSONObject out = output.getJSONObject(i);

                        listaParaderos.add(new Paradero(out.getString("codigo"), out.getString("nombre"), out.getString("distancia"), i));
                    }

                    AdaptadorParadero adapter = new AdaptadorParadero(Lista_Paraderos_Cercanos.this, listaParaderos);

                    list_paraderos.setAdapter(adapter);

                    list_paraderos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(Lista_Paraderos_Cercanos.this, Home.class);

                            Paradero paradero = listaParaderos.get(position);

                            intent.putExtra("codigoParadero", paradero.getCodigo());
                            startActivity(intent);
                        }
                    });
                }else{
                    AlertDialog alerta = new AlertDialog.Builder(this)
                            .setTitle("UPS! Esto es vergonzozo")
                            .setMessage("Se produjo un error inesperado al consultar los paraderos cercanos")
                            .setPositiveButton("Aceptar", new AlertDialog.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Lista_Paraderos_Cercanos.this, Home.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).create();
                    alerta.show();
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
