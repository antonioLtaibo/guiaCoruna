package es.udc.psi14.grupal.guiacoruna;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;

import modelo.PuntoInteres;
import modelo.SQLModel;
import util.util;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnInfoWindowClickListener{



    private SQLModel model;
    private static final LatLng CORUNA_POINT = new LatLng(43.36763,-8.40801);
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LinkedList<PuntoInteres> items;
    private int drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (getIntent().getExtras()!=null) {
            Bundle extra = getIntent().getExtras(); // check if not null
            String type = extra.getString(util.TAG_TYPE, "");
            drawable = extra.getInt(util.TAG_ICON,0);
            model = new SQLModel(this);
            items = ((LinkedList) model.findByType(type));
        }
        setUpMapIfNeeded();
        mMap.setOnInfoWindowClickListener(this);



    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }


    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));+
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(CORUNA_POINT)
                .zoom(13)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        String lat;
        String lon;
        int i=0;
        for (i=0;i<items.size();i++){
            String[] coord= items.get(i).getCoordenadas().split(",");
            lat = coord[0];
            lon = coord[1];
            Marker coruna = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.valueOf(lat),Double.valueOf(lon)))
                    .title(items.get(i).getNombre())
                    .icon(BitmapDescriptorFactory.fromResource(drawable))
                    .snippet(items.get(i).getDetalles()));
            Log.d("ID PUNTO INTERES ->>", String.valueOf(items.get(i).getId()));
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        PuntoInteres pi = model.findByName(marker.getTitle());
        Intent intent = new Intent(getApplicationContext(), DetallesActivity.class);
        intent.putExtra(util.TAG_ID,pi.getId());
        startActivity(intent);

    }
}
