package cl.iism.alertaparadero.utilidades;

import android.app.AlertDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import cl.iism.alertaparadero.Home;

/**
 * Created by gvalles on 30-12-2015.
 */
public class myLocationListener implements LocationListener{

    @Override
    public void onLocationChanged(Location location) {
        double latitud = location.getLatitude();
        double longitud = location.getLongitude();

        Log.d("AlertaParadero GPS", "Latitud: " + latitud + " / Longitud: " + longitud);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        AlertDialog alerta = new AlertDialog.Builder(null)
                .setTitle("Error de localización")
                .setMessage("Debes activar tu GPS para poder prestarte un mejor servicio.")
                .setPositiveButton("Aceptar", null).create();
        alerta.show();
    }
}
