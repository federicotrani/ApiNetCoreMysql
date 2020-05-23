package com.example.netcoremysql.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.netcoremysql.ApiService;
import com.example.netcoremysql.R;
import com.example.netcoremysql.RetrofitInstance;
import com.example.netcoremysql.models.Alumno;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String TOKEN = "";
    private ListView lsvAlumnos;
    private List<Alumno> listaAlumnos;
    private AlumnoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        this.TOKEN = getIntent().getStringExtra("EXTRA_TOKEN");

        getSupportActionBar().setTitle("Lista de Alumnos");

        listaAlumnos = new ArrayList<Alumno>();

        this.cargarListaCompletaDesdeApi();

        this.findViews();
    }

    private void findViews(){
        lsvAlumnos = findViewById(R.id.lsvAlumnos);

        lsvAlumnos.setOnItemClickListener(this);
    }

    private void cargarListaCompletaDesdeApi(){

        final List<Alumno> lista = new ArrayList<Alumno>();

        ApiService api = RetrofitInstance.getInstance().create(ApiService.class);
        Call<List<Alumno>> call = api.ListarTodos();

        call.enqueue(new Callback<List<Alumno>>() {
            @Override
            public void onResponse(Call<List<Alumno>> call, Response<List<Alumno>> response) {
                for(Alumno alumno : response.body()){
                    lista.add(alumno);
                    Log.i("response", "onResponse: "+response.body());
                }

                cargarVistaListaAlumnos(lista);
            }

            @Override
            public void onFailure(Call<List<Alumno>> call, Throwable t) {
                Toast.makeText(ListaActivity.this,"Error, algo salio mal",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void cargarListaFavoritosApi(){

        final List<Alumno> lista = new ArrayList<Alumno>();

        ApiService api = RetrofitInstance.getInstance().create(ApiService.class);
        Call<List<Alumno>> call = api.ListarFavoritos(this.TOKEN);

        call.enqueue(new Callback<List<Alumno>>() {
            @Override
            public void onResponse(Call<List<Alumno>> call, Response<List<Alumno>> response) {
                for(Alumno alumno : response.body()){
                    lista.add(alumno);
                    Log.i("response", "onResponse: "+response.body());
                }

                cargarVistaListaAlumnos(lista);
            }

            @Override
            public void onFailure(Call<List<Alumno>> call, Throwable t) {
                Toast.makeText(ListaActivity.this,"Error, algo salio mal",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void cargarVistaListaAlumnos(List<Alumno> lista){
        // asignamos lista al Adapter para armar la VISTA
        adapter = new AlumnoAdapter(lista);
        // asignamos el adapter al LISTVIEW
        lsvAlumnos.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(ListaActivity.this, DetalleActivity.class);

        //Toast.makeText(this, "ID: "+adapter.getItemId(position), Toast.LENGTH_SHORT).show();
        intent.putExtra("EXTRA_ID_ALUMNO", adapter.getItem(position).getIdalumno());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lista_alumnos,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_lista_actualizar:
                this.cargarListaCompletaDesdeApi();
                return true;
            case R.id.action_lista_agregar:
                this.agregarNuevoAlumno();
                return true;
            case R.id.action_lista_favoritos:
                this.cargarListaFavoritosApi();
                return true;
            case R.id.action_lista_salir:
                finish();
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void agregarNuevoAlumno() {
        Intent intent = new Intent(ListaActivity.this, DetalleActivity.class);
        intent.putExtra("EXTRA_TOKEN",this.TOKEN);
        startActivity(intent);
    }
}
