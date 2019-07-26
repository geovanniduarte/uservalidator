package co.com.geo.uservalidator.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class GPSLocation {
    /**
     * Temporizador para detener búsqueda de la posición.
     */
    private Timer mTimer1;
    /**
     * Manager para GPS.
     */
    private LocationManager mLocationManager;
    /**
     * Clase para manejar el resultado.
     */
    private LocationResult mLocationResult;
    /**
     * Bandera que determina si el GPS está prendido.
     */
    private boolean mIsGpsEnabled = false;
    /**
     * Tiempo de espera del fix de coordenada
     */
    private int mIntTimeOut = 60000;
    /**
     * Atributo para manejar el contexto que hizo el llamado de localizacion.
     */
    private Context mContext;

    public GPSLocation(int mIntTimeOut) {
        super();
        this.mIntTimeOut = mIntTimeOut;
    }


    /**
     * Inicializa el proceso de captura.
     * @param context contexto desde el que se llama
     * @param result Clase que maneja resultados
     * @return Verdadero si se puede utilizar el GPS, falso en otro caso.
     */

    @SuppressLint("MissingPermission")
    public boolean getLocation(Context context, LocationResult result) {
        mContext = context;
        // I use LocationResult callback class to pass location value from MyLocation to user code.
        mLocationResult = result;
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }

        try {
            // Validar si el proveedor está disponible
            mIsGpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }


        // Si no hay proveedores disponibles no se inicia el proceso
        if (!mIsGpsEnabled) {
            mLocationResult.gotLocation(null, true);
            return false;
        }
        // si GPS_PROVIDER está disponible se inicia proceso de actualización de localización
        if (mIsGpsEnabled) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListenerGps);
        }

        mTimer1 = new Timer();
        mTimer1.schedule(new GetLastLocation(), mIntTimeOut);
        return true;
    }

    /**
     * Metodo que se llama cuando se obtiene una coordenada.
     * @param location Location con la coordenada.
     * @param isRef si es de referencia.
     */
    public void setLocation(Location location, boolean isRef) {
        Log.i("GPSLocation", " setted location "+ location);
        if (mLocationResult != null) {
            if (mTimer1 != null) {
                mTimer1.cancel();
                mLocationResult.gotLocation(location, isRef);
                mLocationManager.removeUpdates(mLocationListenerGps);
                mLocationResult = null; //Indico que ya no tengo locationResult
            }

        }
    }

    /**
     * Listener localización por GPS.
     */
    private LocationListener mLocationListenerGps = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            setLocation(location, false);
            Log.i("GPSLocation", "onLocationChanged");
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    private static final int TWO_MINUTES = 1000 * 60 * 5;


    /**
     * Tarea que trae la última lozalización.
     */

    private class GetLastLocation extends TimerTask {
        @SuppressLint("MissingPermission")
        @Override
        public void run() {
            Location gpsLoc = null;
            gpsLoc = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            setLocation(gpsLoc, true);
        }
    }


    /**
     * Interface para retorno de los datos de localización.
     *
     */
    public abstract static class LocationResult {
        /**
         *
         * @param location
         * @param isNew boolean indica que es coordenada nueva (fresca) y no de referencia (vieja o lastLocation).
         */
        public abstract void gotLocation(Location location, boolean isNew);
    }

}
