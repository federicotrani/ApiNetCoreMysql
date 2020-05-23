package com.example.netcoremysql;

import com.example.netcoremysql.models.Alumno;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // obtengo token de autenticación
    @GET("usuario/login")
    Call<String> Login(@Query("email") String email, @Query("password") String password);

    @GET("alumno/listartodos")
    Call<List<Alumno>> ListarTodos();

    @GET("alumno/listarfavoritos")
    Call<List<Alumno>> ListarFavoritos(@Header("Authorization") String token);

    @GET("alumno/listarPorId/{id}")
    Call<Alumno> ListarPorId(@Path("id") int id);

    @POST("alumno/agregar")
    Call<Integer> agregarAlumno(@Body Alumno alumno);

    @PUT("alumno/editar")
    Call<Integer> editarAlumno(@Body Alumno alumno);
}
