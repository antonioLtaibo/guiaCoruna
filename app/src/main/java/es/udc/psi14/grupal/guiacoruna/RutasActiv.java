package es.udc.psi14.grupal.guiacoruna;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.LinkedList;

import modelo.PuntoInteres;
import modelo.PuntoRuta;
import modelo.Ruta;
import modelo.SQLModel;

public class RutasActiv extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private String TAG = "RutasMap";
    private static final LatLng CORUNA_POINT = new LatLng(43.36763,-8.40801);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutas);
        setUpMapIfNeeded();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(CORUNA_POINT)
                .zoom(13)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        SQLModel modelo = new SQLModel(this);
        LinkedList<Ruta> rutas = (LinkedList<Ruta>) modelo.getAllRutas();
        for(Ruta ruta : rutas){
            //Log.d(TAG, "Ruta encontrada: " + ruta.getNombre());
            LinkedList<PuntoRuta> puntos = (LinkedList<PuntoRuta>) modelo.findPuntosByRutaID(ruta.getIdRuta());

            PolylineOptions rectOptions = new PolylineOptions();


            for(PuntoRuta punto : puntos){
                //Log.d(TAG, "Punto contenido en ruta "+ruta.getId() + " con id de punto interes: " + punto.getIdPuntoInteres());
                PuntoInteres pi = modelo.findByID(punto.getIdPuntoInteres());
                if (pi != null) {
                    String coordenadas = pi.getCoordenadas();
                    //Log.d(TAG, "Las coordenadas del punto son: "+ coordenadas);

                    //Tener en cuenta los posibles puntos de interes borrados, devuelven un nulo.


                    int index = coordenadas.indexOf(",");
                    String lat = coordenadas.substring(0, index).trim();
                    String lng = coordenadas.substring(index + 1).trim();
                    double lati = Double.parseDouble(lat);
                    double lngi = Double.parseDouble(lng);
                    LatLng newLoc = new LatLng(lati, lngi);
                    rectOptions.add(newLoc);
                }
            }

            Polyline polyline = mMap.addPolyline(rectOptions);
        }
    }
}
