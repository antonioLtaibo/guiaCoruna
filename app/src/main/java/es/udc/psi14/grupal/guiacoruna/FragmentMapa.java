package es.udc.psi14.grupal.guiacoruna;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import modelo.SQLModel;
import util.util;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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
public class FragmentMapa extends Fragment implements GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapClickListener {
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
    private OnFragmentInteractionListener mListener;
    private SupportMapFragment fragment;

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        android.support.v4.app.FragmentManager fm = getChildFragmentManager();

        fragment = (SupportMapFragment) fm.findFragmentById(R.id.location_map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.location_map, fragment).commit();
        }
        model = new SQLModel(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_fragment_mapa, container, false);
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = fragment.getMap();
            if (mMap != null) {
                setUpMap();
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mMap.setOnInfoWindowClickListener(this);
            }
        }
    }

    private void setUpMap() {
        String lat;
        String lon;
        int i=0;
        int drawable;
        String[] coord;
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(CORUNA_POINT)
                .zoom(13)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.setOnMapClickListener(this);
        items = ((LinkedList) model.getAll());
        Log.d("Number of items to draw in map: ",String.valueOf(items.size()));
        for (i=0;i<items.size();i++){
            Log.d("ID PUNTO INTERES ->>", items.get(i).getCoordenadas());
            Log.d("ID PUNTO INTERES ->>", String.valueOf(items.get(i).getId()));
            drawable = util.findIconType(items.get(i).getTipo());
            if(items.get(i).getCoordenadas().compareTo("")==0){

            }else{
                coord= items.get(i).getCoordenadas().split(",");
                lat = coord[0];
                lon = coord[1];
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.valueOf(lat), Double.valueOf(lon)))
                        .title(items.get(i).getNombre())
                        .icon(BitmapDescriptorFactory.fromResource(drawable))
                        .snippet(items.get(i).getDetalles()));
            }
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

    @Override
    public void onMapClick(LatLng latLng) {

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
