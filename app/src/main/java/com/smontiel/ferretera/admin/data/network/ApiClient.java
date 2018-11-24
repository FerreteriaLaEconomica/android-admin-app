package com.smontiel.ferretera.admin.data.network;

import com.smontiel.ferretera.admin.data.models.Categoria;
import com.smontiel.ferretera.admin.data.models.Producto;
import com.smontiel.ferretera.admin.data.models.Sucursal;
import com.smontiel.ferretera.admin.data.models.SucursalRequest;

import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Salvador Montiel on 4/11/18.
 */
public interface ApiClient {

    @GET("/productos")
    Maybe<Response<List<Producto>>> getAllProducts();

    @POST("/productos")
    Maybe<Producto> createProduct(@Body Producto producto);

    @PUT("/productos/{id}")
    Maybe<Producto> updateProduct(@Path("id") int id, @Body Producto producto);

    @GET("/categorias")
    Maybe<Response<List<Categoria>>> getAllCategories();

    @POST("/categorias")
    Maybe<Response<Categoria>> createCategory(@Body Map<String, Object> body);

    @POST("/sucursales")
    Maybe<Response<Sucursal>> createSucursal(@Body SucursalRequest body);
}
