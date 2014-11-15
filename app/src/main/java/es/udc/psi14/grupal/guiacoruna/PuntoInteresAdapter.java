package es.udc.psi14.grupal.guiacoruna;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import modelo.PuntoInteres;
import util.*;


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


        ImageManagerInternal ImgManagerInt = new ImageManagerInternal(this.getContext());
        ImageManagerExternal ImgManagerExt= new ImageManagerExternal(this.getContext());


        Bitmap image = ImgManagerInt.getImageIcon(puntointeres.getImageString());
        if(image == null){
            image = ImgManagerExt.getImageIcon(puntointeres.getImageString());
        }


        if(image !=null) holder.imgIcon.setImageBitmap(image);

        return row;
    }


    static class PuntoInteresHolder
    {
        ImageView imgIcon;
        TextView title;
        TextView detail;
    }

}