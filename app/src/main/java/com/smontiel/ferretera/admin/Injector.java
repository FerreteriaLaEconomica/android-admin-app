package com.smontiel.ferretera.admin;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smontiel.ferretera.admin.data.Constants;
import com.smontiel.ferretera.admin.data.SharedPrefs;
import com.smontiel.ferretera.admin.data.network.ApiClient;
import com.smontiel.ferretera.admin.data.network.AuthClient;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Salvador Montiel on 28/10/18.
 */
public class Injector {

    public static SharedPrefs provideSharedPrefs() {
        return SharedPrefs.getInstance(MyApp.getContext());
    }

    public static ApiClient provideApiClient() {
        return provideRetrofit().create(ApiClient.class);
    }

    public static AuthClient provideAuthClient() {
        return provideRetrofit().create(AuthClient.class);
    }

    private static Retrofit provideRetrofit() {
        RxJava2CallAdapterFactory callAdapterFactory = RxJava2CallAdapterFactory
                .createWithScheduler(Schedulers.io());
        return new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .client(provideOkHttpClient())
                .addCallAdapterFactory(callAdapterFactory)
                .addConverterFactory(GsonConverterFactory.create(provideGson()))
                .build();
    }

    private static OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        okhttpClientBuilder.addInterceptor(chain -> {
            Request request = chain.request();
            SharedPrefs prefs = provideSharedPrefs();
            String auth = prefs.getString(Constants.AUTH_TOKEN, "");
            if (!auth.equals("")) {
                Request newRequest = request.newBuilder()
                        .header(Constants.AUTHORIZATION, "Bearer " + auth)
                        .build();
                return chain.proceed(newRequest);
            }
            return chain.proceed(request);
        });
        return okhttpClientBuilder.build();
    }

    public static Gson provideGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }
}
