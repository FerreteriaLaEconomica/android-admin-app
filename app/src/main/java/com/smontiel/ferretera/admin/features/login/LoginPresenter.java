package com.smontiel.ferretera.admin.features.login;

import android.util.Log;

import com.smontiel.ferretera.admin.Injector;
import com.smontiel.ferretera.admin.data.ApiError;
import com.smontiel.ferretera.admin.data.Constants;
import com.smontiel.ferretera.admin.data.SharedPrefs;
import com.smontiel.ferretera.admin.data.User;
import com.smontiel.ferretera.admin.data.network.AuthClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

import static com.smontiel.ferretera.admin.utils.Preconditions.checkNotNull;

/**
 * Created by Salvador Montiel on 29/10/18.
 */
public class LoginPresenter implements LoginContract.Presenter {
    private final LoginContract.View loginView;
    private final AuthClient authClient;
    private final SharedPrefs prefs;

    private final CompositeDisposable compositeDisposable;

    public LoginPresenter(LoginContract.View loginView, AuthClient authClient, SharedPrefs prefs) {
        this.loginView = checkNotNull(loginView);
        this.authClient = checkNotNull(authClient);
        this.prefs = checkNotNull(prefs);
        this.compositeDisposable = new CompositeDisposable();
        loginView.setPresenter(this);
    }

    @Override
    public void logIn(String email, String password) {
        Map<String, String> credentials = new HashMap<>(2);
        credentials.put("email", email);
        credentials.put("password", password);
        Maybe<Response<User>> call = authClient.loginWithCredentials(credentials);
        Disposable disposable = call.observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.isSuccessful()) {
                        prefs.saveString(Constants.AUTH_TOKEN, response.headers().get(Constants.AUTHORIZATION));
                        loginView.onSuccess(response.body());
                    } else {
                        String body = response.errorBody().string();
                        ApiError apiError = Injector.provideGson().fromJson(body, ApiError.class);
                        loginView.showInfoDialog(apiError.message);
                    }
                }, throwable -> loginView.showInfoDialog("La aplicación no se pudo conectar al servidor"));
        compositeDisposable.add(disposable);
    }

    @Override
    public void subscribe() {
        String token = prefs.getString(Constants.AUTH_TOKEN, "");
        if (!token.equals("")) {
            Maybe<Response<User>> call = authClient.login();
            Disposable disposable = call.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(userResponse -> {
                        if (userResponse.isSuccessful()) {
                            prefs.saveString(Constants.AUTH_TOKEN,
                                    userResponse.headers().get(Constants.AUTHORIZATION));
                            loginView.onSuccess(userResponse.body());
                        } else {
                            String body = userResponse.errorBody().string();
                            Log.i("aA", body);
                            ApiError apiError = Injector.provideGson().fromJson(body, ApiError.class);
                            loginView.showInfoDialog(apiError.message);
                        }
                    }, throwable -> loginView.showInfoDialog("La aplicación no se pudo conectar al servidor"));
            compositeDisposable.add(disposable);
        }
    }

    @Override
    public void unsubscribe() {
        if (compositeDisposable != null) compositeDisposable.clear();
    }
}
