package com.example.netcoremysql.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.netcoremysql.ApiService;
import com.example.netcoremysql.R;
import com.example.netcoremysql.RetrofitInstance;
import com.example.netcoremysql.models.Alumno;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleActivity extends AppCompatActivity implements View.OnClickListener {

    private int ID_ACTUAL;
    private String TOKEN="";

    private Button btnDetalleGrabar, btnDetalleCancelar;
    private EditText edtDetalleNombre, edtDetalleEmail, edtDetalleDomicilio;
    private CheckBox chkDetalleFavorito, chkDetalleActivo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        this.obtenerParametros();

        if(this.ID_ACTUAL!=0){
            this.cargarDatosDesdeApi(this.ID_ACTUAL);
        }

        this.findViews();

    }

    private void findViews(){
        btnDetalleGrabar = findViewById(R.id.btnDetalleGrabar);
        btnDetalleCancelar = findViewById(R.id.btnDetalleCancelar);

        edtDetalleDomicilio = findViewById(R.id.edtDetalleDomicilio);
        edtDetalleEmail = findViewById(R.id.edtDetalleEmail);
        edtDetalleNombre = findViewById(R.id.edtDetalleNombre);

        chkDetalleActivo = findViewById(R.id.chkDetalleActivo);
        chkDetalleFavorito = findViewById(R.id.chkDetalleFavorito);

        btnDetalleCancelar.setOnClickListener(this);
        btnDetalleGrabar.setOnClickListener(this);
    }

    private void obtenerParametros(){
        Intent intent = getIntent();

        this.ID_ACTUAL = intent.getIntExtra("EXTRA_ID_ALUMNO",0);
        this.TOKEN = intent.getStringExtra("EXTRA_TOKEN");

        getSupportActionBar().setTitle("Detalle de Alumno #"+ this.ID_ACTUAL);
    }

    private void cargarDatosDesdeApi(int idAlumno){
        final Alumno item = new Alumno();

        ApiService api = RetrofitInstance.getInstance().create(ApiService.class);
        Call<Alumno> call = api.ListarPorId(idAlumno);

        call.enqueue(new Callback<Alumno>() {
            @Override
            public void onResponse(Call<Alumno> call, Response<Alumno> response) {
                edtDetalleDomicilio.setText(response.body().getDomicilio());
                edtDetalleEmail.setText(response.body().getEmail());
                edtDetalleNombre.setText(response.body().getNombre());

                chkDetalleActivo.setChecked(response.body().getActivo()==1?true:false);
                chkDetalleFavorito.setChecked(response.body().getFavorito()==1?true:false);
            }

            @Override
            public void onFailure(Call<Alumno> call, Throwable t) {

            }
        });
    }

    private void grabarDatos(){
        if(this.validarDatos()){
            // definimos llamada al API
            ApiService api = RetrofitInstance.getInstance().create(ApiService.class);

            Call<Integer> call = null;

            // asignamos alumno a editar/agregar
            Alumno alumno = new Alumno();
            alumno.setNombre(edtDetalleNombre.getText().toString());
            alumno.setDomicilio(edtDetalleDomicilio.getText().toString());
            alumno.setEmail(edtDetalleEmail.getText().toString());
            alumno.setFavorito(chkDetalleFavorito.isChecked()?1:0);

            if(this.ID_ACTUAL==0){
                // estamos agragando nuevo registro
                alumno.setActivo(1);
                call = api.agregarAlumno(alumno);

            }else{
                // estamos editando registro
                alumno.setIdalumno(this.ID_ACTUAL);
                alumno.setActivo(chkDetalleFavorito.isChecked()?1:0);
                call = api.editarAlumno(alumno);
            }

            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    Toast.makeText(DetalleActivity.this,"grabado: "+response.code(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.e("API", "onFailure: "+t.getMessage());
                    Toast.makeText(DetalleActivity.this, "Error, reintente", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validarDatos(){

        if(edtDetalleNombre.getText().toString().isEmpty()){
            edtDetalleNombre.setError("Este campo es obligatorio");
            return false;
        }

        if(edtDetalleEmail.getText().toString().isEmpty()){
            edtDetalleEmail.setError("Este campo es obligatorio");
            return false;
        }

        if(edtDetalleDomicilio.getText().toString().isEmpty()){
            edtDetalleDomicilio.setError("Este campo es obligatorio");
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnDetalleCancelar:
                finish();
                break;
            case R.id.btnDetalleGrabar:
                this.grabarDatos();
                break;
        }
    }
}
