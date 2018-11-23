package com.smontiel.ferretera.admin.data.network;

import com.smontiel.ferretera.admin.data.models.Categoria;
import com.smontiel.ferretera.admin.data.models.Producto;

import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Salvador Montiel on 4/11/18.
 */
public interface ApiClient {

    @GET("/productos")
    Maybe<Response<List<Producto>>> getAllProducts();

    @POST("/productos")
    Maybe<Producto> createProduct(@Body Producto producto);

    @GET("/categorias")
    Maybe<Response<List<Categoria>>> getAllCategories();

    @POST("/categorias")
    Maybe<Response<Categoria>> createCategory(@Body Map<String, Object> body);
}
