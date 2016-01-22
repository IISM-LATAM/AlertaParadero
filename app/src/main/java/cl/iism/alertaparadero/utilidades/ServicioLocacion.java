package cl.iism.alertaparadero.utilidades;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import cl.iism.alertaparadero.Alarma_alerta_paradero;
import cl.iism.alertaparadero.Home;

/**
 * Created by gvalles on 19-01-2016.
 */
public class ServicioLocacion extends Service
{
    private static final String TAG = "BOOMBOOMTESTGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 100;
    private float LOCATION_DISTANCE = 30;

    double latitud;
    double longitud;
    String nombre;
    String codigo;

    public int etapa;

    private class LocationListener implements android.location.LocationListener{
        Location mLastLocation;
        int etapa;
        int[] etapas = new int[4];

        public LocationListener(String provider)
        {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);

            etapa = 0;
            etapas[1] = etapas[2] = etapas[3] = 0;
        }
        @Override
        public void onLocationChanged(Location location)
        {
            Log.e(TAG, "onLocationChanged: " + location);

            Log.e("GPS", String.valueOf(latitud));

            mLastLocation.set(location);

            double latitud_usuario = location.getLatitude();
            double longitud_usuario = location.getLongitude();

            SharedPreferences preferences = getSharedPreferences("AlertaParadero_Paradero", MODE_PRIVATE);
            SharedPreferences.Editor editor = getSharedPreferences("AlertaParadero_Paradero", MODE_PRIVATE).edit();
            editor.putString("mi_latitud", String.valueOf(location.getLatitude()));
            editor.putString("mi_longitud", String.valueOf(location.getLongitude()));
            editor.commit();

            Location location1 = new Location("Posicion usuario");
            location1.setLatitude(latitud_usuario);
            location1.setLongitude(longitud_usuario);

            Location location2 = new Location("Posicion paradero");
            location2.setLatitude(latitud);
            location2.setLongitude(longitud);

            float distancia = location1.distanceTo(location2);

            etapa = 0;
            if(distancia <= 800){
                if(distancia <= 200){
                    etapa = 3;
                }
                if((distancia > 200) && (distancia < 500)){
                    etapa = 2;
                }
                if((distancia >= 500)){
                    etapa = 1;
                }

                if((etapa == 1) && (preferences.getBoolean("alarma1", false) == false)){
                    editor.putBoolean("alarma1", true);
                    editor.commit();

                    Intent intent = new Intent(ServicioLocacion.this, Alarma_alerta_paradero.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("tipo", 1);
                    startActivity(intent);

                    Notification notif  = new Notification.Builder(ServicioLocacion.this)
                    .setContentTitle("D E S P I E R T A !!!")
                    .setContentText("Estás a menos de 8 cuadras de bajarte. Ya es hora de despertar y prepararte para pararte.").build();

                    NotificationManager notifManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    int notif_ref = 1;

                    notifManager.notify(notif_ref, notif);
                }

                if((etapa == 2) && (preferences.getBoolean("alarma2", false) == false)){
                    // alarma de pararse
                    editor.putBoolean("alarma2", true);
                    editor.commit();

                    Intent intent = new Intent(ServicioLocacion.this, Alarma_alerta_paradero.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("tipo", 2);
                    startActivity(intent);
                }

                if((etapa == 3) && (preferences.getBoolean("alarma3", false) == false)){
                    // alarma de tocar timbre
                    editor.putBoolean("alarma3", true);
                    editor.commit();

                    Intent intent = new Intent(ServicioLocacion.this, Alarma_alerta_paradero.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("tipo", 3);
                    startActivity(intent);
                    stopSelf();
                }
            }

            Log.d("GPS Result", String.valueOf(distancia));
        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }
        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }
    @Override
    public void onCreate()
    {
        Log.e(TAG, "onCreate");

        SharedPreferences sharedPreferences = getSharedPreferences("AlertaParadero_Paradero", MODE_PRIVATE);
        nombre = sharedPreferences.getString("nombre", "");
        codigo = sharedPreferences.getString("codigo", "");
        latitud = Double.valueOf(sharedPreferences.getString("latitud", ""));
        longitud = Double.valueOf(sharedPreferences.getString("longitud", ""));

        this.etapa = 0;


        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }
    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }
    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}
