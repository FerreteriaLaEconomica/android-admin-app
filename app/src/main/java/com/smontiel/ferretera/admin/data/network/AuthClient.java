package com.smontiel.ferretera.admin.data.network;

import com.smontiel.ferretera.admin.data.User;
import com.smontiel.ferretera.admin.data.UserDto;

import java.util.Map;

import io.reactivex.Maybe;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Salvador Montiel on 28/10/18.
 */
public interface AuthClient {

    @GET("/users/me")
    Maybe<Response<User>> login();

    @POST("/users/login")
    Maybe<Response<User>> loginWithCredentials(@Body Map<String, String> credentials);

    @POST("/users")
    Maybe<Response<User>> signUp(@Body UserDto user);
}
