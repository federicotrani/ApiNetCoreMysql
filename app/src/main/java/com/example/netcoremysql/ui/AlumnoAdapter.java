package com.example.netcoremysql.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.netcoremysql.R;
import com.example.netcoremysql.models.Alumno;
import java.util.List;

public class AlumnoAdapter extends BaseAdapter {

    List<Alumno> listaAlumnos;

    public AlumnoAdapter(List<Alumno> listaAlumnos) {
        this.listaAlumnos = listaAlumnos;
    }

    @Override
    public int getCount() {
        return listaAlumnos.size();
    }

    @Override
    public Alumno getItem(int position) {
        return listaAlumnos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getIdalumno();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View layout;

        if(convertView==null){
            layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_alumnos,null);
        }else{
            layout=convertView;
        }

        Alumno alumno = getItem(position);

        TextView txtDetalleNombre = layout.findViewById(R.id.txtDetalleNombre);
        TextView txtDetalleEmail = layout.findViewById(R.id.txtDetalleEmail);
        TextView txtDetalleDomicilio = layout.findViewById(R.id.txtDetalleDomicilio);
        ImageView imgDetalleFavorito = layout.findViewById(R.id.imgDetalleFavorito);

        txtDetalleNombre.setText(alumno.getNombre());
        txtDetalleEmail.setText(alumno.getEmail());
        txtDetalleDomicilio.setText(alumno.getDomicilio());
        imgDetalleFavorito.setImageResource(alumno.getFavorito()==1?R.drawable.ic_star_black_24dp:R.drawable.ic_star_border_black_24dp);

        return layout;
    }
}
