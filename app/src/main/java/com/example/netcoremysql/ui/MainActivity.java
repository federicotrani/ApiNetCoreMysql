package com.example.netcoremysql.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLoginIngresar;
    private EditText edtLoginEmail, edtLoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        this.findViews();
    }

    private void findViews(){
        btnLoginIngresar = findViewById(R.id.btnLoginIngresar);
        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);

        btnLoginIngresar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btnLoginIngresar){
            if(this.validarDatos()){
                String email=edtLoginEmail.getText().toString();
                String password=edtLoginPassword.getText().toString();
                this.verificarLoginApi(email, password);
            }
        }
    }


    private boolean validarDatos(){

        if(edtLoginEmail.getText().toString().isEmpty()){
            edtLoginEmail.setError("Esta dato es obligatorio");
            return false;
        }

        if(edtLoginPassword.getText().toString().isEmpty()){
            edtLoginPassword.setError("Este dato es obligatorio");
            return false;
        }

        return true;
    }

    private void verificarLoginApi(String email, String password){

        ApiService api = RetrofitInstance.getInstance().create(ApiService.class);
        Call<String> call = api.Login(email,password);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.code()==200 && !response.body().isEmpty()){
                    loginAprobado(response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("API", "onFailure: error interno de validación de API" );
            }
        });

    }

    private void loginAprobado(String token){
        this.limpiarControles();
        Intent intent = new Intent(MainActivity.this, ListaActivity.class);
        intent.putExtra("EXTRA_TOKEN", token);
        startActivity(intent);
    }

    private void limpiarControles(){
        edtLoginEmail.setText("");
        edtLoginPassword.setText("");
    }
}
