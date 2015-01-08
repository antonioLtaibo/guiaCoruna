package es.udc.psi14.grupal.guiacoruna;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class selecPuntoActiv extends Activity implements GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static final LatLng CORUNA_POINT = new LatLng(43.36763,-8.40801);
    private Marker markerSelect;
    private String coord;
    private String addressString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selec_punto);
        setUpMapIfNeeded();
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
                mMap.setOnMarkerDragListener(this);
            }
        }
    }

    private void setUpMap() {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(CORUNA_POINT)
                .zoom(13)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        markerSelect = mMap.addMarker(new MarkerOptions()
                .position(CORUNA_POINT)
                .title(getString(R.string.select))
                .draggable(true));
        Log.d("MARKER",String.valueOf(markerSelect.isDraggable()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.selec_punto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add_punto) {
            coord = new String(String.valueOf(markerSelect.getPosition().latitude) +" , "+ String.valueOf(markerSelect.getPosition().longitude));
            Intent returnIntent = new Intent();
            returnIntent.putExtra(AddPuntoInteresActivity.campoCoordenadas,coord);
            Geocoder gc = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses =gc.getFromLocation(markerSelect.getPosition().latitude, markerSelect.getPosition().longitude, 1);
// smaller numbers (1 to 5) are recommended for maxResults
                StringBuilder sb = new StringBuilder();
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);
                    sb.append(address.getAddressLine(0)).append(" ");
                    sb.append(address.getLocality());

                }
                addressString = sb.toString();
            } catch (IOException e) {}
            returnIntent.putExtra(AddPuntoInteresActivity.campoDireccion,addressString);
            setResult(RESULT_OK,returnIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

        Log.d("COOOR","Start");
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        Log.d("COOOR","MOVE");

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Log.d("COOOR",marker.getPosition().toString());
        markerSelect.setPosition(marker.getPosition());


    }
}
