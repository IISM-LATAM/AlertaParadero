package cl.iism.alertaparadero;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import cl.iism.alertaparadero.utilidades.ServicioLocacion;

/**
 * Created by gvalles on 04-01-2016.
 */
public class VistaAlertaParadero extends FragmentActivity{

    GoogleMap map;

    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_alerta_paradero);

        final SharedPreferences sharedPreferences = getSharedPreferences("AlertaParadero_Paradero", MODE_PRIVATE);

        double latitud = Double.valueOf(sharedPreferences.getString("latitud", ""));
        double longitud = Double.valueOf(sharedPreferences.getString("longitud", ""));

        TextView txt_nombre_paradero = (TextView) findViewById(R.id.txt_nombre_paradero_vista_alerta);
        txt_nombre_paradero.setText(sharedPreferences.getString("nombre", ""));

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        final LatLng TutorialsPoint = new LatLng(latitud , longitud);
        Marker TP = map.addMarker(new MarkerOptions().position(TutorialsPoint).title(sharedPreferences.getString("nombre", "") + "\n" + sharedPreferences.getString("codigo", "")));
        TP.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_icono_paradero));
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(latitud, longitud));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        map.moveCamera(center);
        map.animateCamera(zoom);


    }

    @Override
    public void onRestart(){
        super.onRestart();

        int estado = 0;
        ActivityManager manager  = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if(ServicioLocacion.class.getName().equals(service.service.getClassName())){
                estado++;
            }
        }

        if(estado == 0){
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
            finish();
        }
    }
}
