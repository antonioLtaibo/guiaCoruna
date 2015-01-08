package es.udc.psi14.grupal.guiacoruna;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import java.util.LinkedList;
import modelo.PuntoInteres;
import modelo.SQLModel;
import util.util;

public class NotificationService extends Service implements LocationListener {

    public static final int NOTIFICATION_FLAG = 2;
    private Thread workerThread = null;
    private NotificationManager mManager;
    public static final int APP_ID_NOTIFICATION = 1;
    private SQLModel model;
    private LinkedList<PuntoInteres> items;
    private LocationManager locateManager;
    private String[] coord;
    private double lat;
    private double lon;
    private Location posicion = null;
    int distancia;
    String puntoAnterior = " ";





    public NotificationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        model = new SQLModel(getApplicationContext());
        items = ((LinkedList) model.getAll());
        locateManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        locateManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        locateManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,  this);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (workerThread == null || !workerThread.isAlive()) {
            workerThread = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        //Aquí se realiza el trabajo del hilo secundario
                        for (int i = 0; i < items.size(); i++) {
                            if(items.get(i).getCoordenadas().compareTo("")==0){
                            }else {
                                if (posicion!=null) {
                                    coord = items.get(i).getCoordenadas().split(",");
                                    lat = Double.valueOf(coord[0]);
                                    lon = Double.valueOf(coord[1]);
                                    distancia = getDistance(lat, lon, posicion.getLatitude(), posicion.getLongitude());
                                    if (distancia < 250) {
                                        if (!puntoAnterior.equals(items.get(i).getCoordenadas())) {
                                            notificar(items.get(i), distancia);
                                            puntoAnterior = items.get(i).getCoordenadas();
                                        }

                                    }
                                }
                            }
                        }
                        //espera 2 minutos entre accion
                        try {
                            Thread.sleep(1000 * 2 * 60);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            workerThread.start();
        }
        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        workerThread.interrupt();
        locateManager.removeUpdates(this);

    }


    private void notificar(PuntoInteres puntoInteres, int distance) {

        mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //Prepara la actividad que se abrira cuando el usuario pulse la notificacion
        Intent intentNot = new Intent(this, DetallesActivity.class);
        intentNot.getIntExtra(util.TAG_ID,puntoInteres.getId());
        PendingIntent pendingIntent = PendingIntent.getActivity(this.getBaseContext(), NOTIFICATION_FLAG, intentNot, PendingIntent.FLAG_UPDATE_CURRENT);


        //Prepara la notificacion
        Notification.Builder mBuilder = new Notification.Builder(this)
                                        .setSmallIcon(R.drawable.icon_not)
                                        .setContentTitle(puntoInteres.getNombre())
                                        .setContentText(getString(R.string.distance_notific) + " " + String.valueOf(distance) + "m")
                                        .setTicker(puntoInteres.getNombre())
                                        .setContentIntent(pendingIntent);


        //Lanza la notificación
        mManager.notify(APP_ID_NOTIFICATION, mBuilder.build());

    }

    private static int getDistance(double lat1, double lon1, double lat2, double lon2){
        double Radius = 6371000; //Aprox Radio de la tierra
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon /2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return (int) (Radius * c);

    }

    @Override
    public void onLocationChanged(Location location) {
        posicion = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
