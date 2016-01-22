package cl.iism.alertaparadero.utilidades;

import android.app.Service;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

import cl.iism.alertaparadero.Alarma_alerta_paradero;
import cl.iism.alertaparadero.utilidades.myLocationListener;

/**
 * Created by gvalles on 18-12-2015.
 */
public class ServicioAlerta extends Service{
    private String codigo;
    private String nombre;
    private long latitud;
    private long longitud;

    public ServicioAlerta(){

    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public void onCreate(){
        Log.d("AlertaParadero Servicio", "onCreate()");

        LocationManager mlocManager = (LocationManager) getSystemService(getBaseContext().LOCATION_SERVICE);
        myLocationListener mlocListener = new myLocationListener();
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) mlocListener);

        Intent dialogIntent = new Intent(this, Alarma_alerta_paradero.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d("AlertaParadero Servicio", "onStartCommand()");

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy(){
        Log.d("AlertaParadero Servicio", "onDestroy()");
    }
}
