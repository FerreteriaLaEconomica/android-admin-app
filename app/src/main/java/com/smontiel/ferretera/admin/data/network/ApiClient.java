package com.smontiel.ferretera.admin.data.network;

import com.smontiel.ferretera.admin.data.models.Producto;

import java.util.List;

import io.reactivex.Maybe;
import retrofit2.Response;
import retrofit2.http.GET;

/**
 * Created by Salvador Montiel on 4/11/18.
 */
public interface ApiClient {

    @GET("/productos")
    Maybe<Response<List<Producto>>> getAllProducts();
}
