package es.udc.psi14.grupal.guiacoruna;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import modelo.SQLModel;
import modelo.testmodel;
import util.util;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;

import modelo.PuntoInteres;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentMapa.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMapa#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class FragmentMapa extends SupportMapFragment implements GoogleMap.OnInfoWindowClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private SQLModel model;
    private static final LatLng CORUNA_POINT = new LatLng(43.36763,-8.40801);
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LinkedList<PuntoInteres> items;
    private int drawable;
    private MapView mapview;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMapa.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMapa newInstance(String param1, String param2) {
        FragmentMapa fragment = new FragmentMapa();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public FragmentMapa() {
        // Required empty public constructor
    }



    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.location_map)).getMap();
            //mMap = mapview.getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        setUpMapIfNeeded();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_fragment_mapa, container, false);
        //mapview = (MapView) view.findViewById(R.id.mapView_fragmento);

        /*
        if (getIntent().getExtras()!=null) {
            Bundle extra = getIntent().getExtras(); // check if not null
            String type = extra.getString(util.TAG_TYPE, "");
            drawable = extra.getInt(util.TAG_ICON,0);
            model = new SQLModel(this);
            items = ((LinkedList) model.findByType(type));
        }*/


        //setUpMapIfNeeded();
        return view;

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


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        PuntoInteres pi = model.findByName(marker.getTitle());
        Intent intent = new Intent(getActivity().getBaseContext(), DetallesActivity.class);
        intent.putExtra(util.TAG_ID,pi.getId());
        startActivity(intent);

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

}
