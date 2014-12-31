package es.udc.psi14.grupal.guiacoruna;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import util.util;
import modelo.SQLModel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentGrupos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentGrupos#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class FragmentGrupos extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private Button butMus,butHot,butNoche,butTend,butRes,butMon;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentGrupos.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentGrupos newInstance(String param1, String param2) {
        FragmentGrupos fragment = new FragmentGrupos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public FragmentGrupos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_grupos, container, false);


        butMus = (Button)  view.findViewById(R.id.but_mus);
        butHot = (Button) view.findViewById(R.id.but_hot);
        butNoche = (Button) view.findViewById(R.id.but_noche);
        butTend = (Button) view.findViewById(R.id.but_tend);
        butRes = (Button) view.findViewById(R.id.but_res);
        butMon = (Button) view.findViewById(R.id.but_mon);

        butMus.setOnClickListener(this);
        butRes.setOnClickListener(this);
        butNoche.setOnClickListener(this);
        butTend.setOnClickListener(this);
        butMon.setOnClickListener(this);
        butHot.setOnClickListener(this);

        /**
        SQLModel model;
        model = new SQLModel(getActivity().getBaseContext());
        model.loadInitData();
        /**/

        return view;

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
    public void onClick(View view) {
        //Intent i = new Intent(getActivity().getBaseContext(), MapsActivity.class);
        //descomentar la linea que nos interesa en cada caso hasta que se implemente el tab view
        Intent i = new Intent(getActivity().getBaseContext(), PuntoInteresListActiv.class);
        switch (view.getId()){
            case R.id.but_mus:
                i.putExtra(util.TAG_TYPE, util.TYPE_MUSEUM);
                i.putExtra(util.TAG_ICON,util.ICON_TYPE_MUSEUM);
                break;
            case R.id.but_hot:
                i.putExtra(util.TAG_TYPE, util.TYPE_HOTEL);
                i.putExtra(util.TAG_ICON,util.ICON_TYPE_HOTEL);
                break;
            case R.id.but_noche:
                i.putExtra(util.TAG_TYPE, util.TYPE_NIGHT);
                i.putExtra(util.TAG_ICON,util.ICON_TYPE_NIGHT);
                break;
            case R.id.but_tend:
                i.putExtra(util.TAG_TYPE, util.TYPE_SHOP);
                i.putExtra(util.TAG_ICON,util.ICON_TYPE_SHOP);
                break;
            case R.id.but_res:
                i.putExtra(util.TAG_TYPE, util.TYPE_RESTAURANT);
                i.putExtra(util.TAG_ICON,util.ICON_TYPE_RESTAURANT);
                break;
            case R.id.but_mon:
                i.putExtra(util.TAG_TYPE, util.TYPE_MONUMENT);
                i.putExtra(util.TAG_ICON,util.ICON_TYPE_MONUMENT);
                break;

        }
        startActivity(i);
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

}
