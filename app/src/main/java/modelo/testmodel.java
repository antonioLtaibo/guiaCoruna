package modelo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

import es.udc.psi14.grupal.guiacoruna.R;


public class testmodel extends Activity implements View.OnClickListener {

    TextView numItems;

    EditText et_nombre;
    EditText et_direccion;
    EditText et_telefono;
    EditText et_tipo;
    EditText et_cadena_buscar;

    Button butt_enviar;
    Button butt_contar;
    Button butt_buscar;

    RadioButton buscar_nombre;
    RadioButton buscar_tipo;
    RadioButton buscar_id;

    Button mostrar_todo;

    Button butt_init;

    SQLModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testmodel);

        model = new SQLModel(this);

        numItems = (TextView) findViewById(R.id.numItems);

        et_nombre = (EditText) findViewById(R.id.et_nombre);
        et_direccion = (EditText) findViewById(R.id.et_direccion);
        et_telefono = (EditText) findViewById(R.id.et_direccion);
        et_tipo = (EditText) findViewById(R.id.et_tipo);
        et_cadena_buscar = (EditText) findViewById(R.id.et_cadena_buscar);

        butt_enviar = (Button) findViewById(R.id.butt_enviar);
        butt_enviar.setOnClickListener(this);
        butt_contar = (Button) findViewById(R.id.butt_contar);
        butt_contar.setOnClickListener(this);
        butt_buscar = (Button) findViewById(R.id.butt_buscar);
        butt_buscar.setOnClickListener(this);

        buscar_nombre = (RadioButton) findViewById(R.id.buscar_nombre);
        buscar_tipo = (RadioButton) findViewById(R.id.buscar_tipo);
        buscar_id = (RadioButton) findViewById(R.id.buscar_id);

        mostrar_todo = (Button) findViewById(R.id.mostrar_todo);

        butt_init = (Button) findViewById(R.id.butt_init);
        butt_init.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if(view == butt_enviar){
            PuntoInteres pi = new PuntoInteres();
            pi.setTelefono(et_telefono.getText().toString());
            pi.setNombre(et_nombre.getText().toString());
            pi.setDireccion(et_direccion.getText().toString());
            pi.setTipo(et_tipo.getText().toString());
            boolean exito = model.addPuntoInteres(pi);

            if(exito){
                Toast.makeText(this,"Item insertado con éxito",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"Inserción Falló",Toast.LENGTH_SHORT).show();
            }
        }else if(view == butt_buscar){
            String string = et_cadena_buscar.getText().toString();
            LinkedList<PuntoInteres> pi = new LinkedList<PuntoInteres>();
            if(buscar_nombre.isChecked()){
                PuntoInteres c = model.findByName(string);
                if(c!=null) pi.add(c);
            }else if(buscar_id.isChecked()){
                PuntoInteres c = model.findByID(string);
                if(c!=null) pi.add(c);
            }else if(buscar_tipo.isChecked()){
                pi = (LinkedList<PuntoInteres>) model.findByType(string);
            }
            if(pi.isEmpty()){
                Toast.makeText(this,"Busqueda Falló",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"Item encontrado con éxito",Toast.LENGTH_SHORT).show();
            }

        }else if(view == butt_contar){
            Integer num = model.getAll().size();
            numItems.setText(num.toString());
        }else if(view == butt_init){
            model.loadInitData();
        }
    }
}