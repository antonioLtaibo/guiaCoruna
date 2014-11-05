package es.udc.psi14.grupal.guiacoruna;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import modelo.PuntoInteres;


public class PuntoInteresAdapter extends ArrayAdapter<PuntoInteres> {

    Context context;
    int resourceLayoutId;
    PuntoInteres data[]= null;

    public PuntoInteresAdapter(Context context, int resourceLayoutId, PuntoInteres[] data) {
        super(context, resourceLayoutId,data);
        this.resourceLayoutId = resourceLayoutId;
        this.context = context;
        this.data = data;

    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PuntoInteresHolder holder;


        if (row==null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resourceLayoutId,parent,false);

            holder = new PuntoInteresHolder();
            holder.imgIcon = (ImageView) row.findViewById(R.id.icon);
            holder.title = (TextView) row.findViewById(R.id.txt_title);
            holder.detail = (TextView) row.findViewById(R.id.txt_detail);
            row.setTag(holder);
        }else{
            holder = (PuntoInteresHolder)row.getTag();
        }

        PuntoInteres puntointeres = data[position];
        holder.title.setText(puntointeres.getNombre());
        holder.detail.setText(puntointeres.getDireccion());
        holder.imgIcon.setImageResource(R.drawable.ic_launcher);

        return row;
    }


    static class PuntoInteresHolder
    {
        ImageView imgIcon;
        TextView title;
        TextView detail;
    }

}