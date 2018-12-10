package com.smontiel.ferretera.admin.features.auth;

import com.smontiel.ferretera.admin.Injector;
import com.smontiel.ferretera.admin.data.models.ApiError;
import com.smontiel.ferretera.admin.data.Constants;
import com.smontiel.ferretera.admin.data.SharedPrefs;
import com.smontiel.ferretera.admin.data.models.User;
import com.smontiel.ferretera.admin.data.models.UserDto;
import com.smontiel.ferretera.admin.data.network.AuthClient;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

import static com.smontiel.ferretera.admin.utils.Preconditions.checkNotNull;

/**
 * Created by Salvador Montiel on 27/10/18.
 */
public class SignupPresenter implements SignupContract.Presenter {
    private final SignupContract.View signupView;
    private final AuthClient authClient;
    private final SharedPrefs prefs;

    private final CompositeDisposable compositeDisposable;

    public SignupPresenter(SignupContract.View signupView, AuthClient authClient, SharedPrefs prefs) {
        this.signupView = checkNotNull(signupView);
        this.authClient = checkNotNull(authClient);
        this.prefs = checkNotNull(prefs);
        this.compositeDisposable = new CompositeDisposable();
        signupView.setPresenter(this);
    }

    @Override
    public void signUp(String nombre, String apellidos, String email, String password, String direccion) {
        Maybe<Response<User>> call = authClient.signUp(new UserDto(nombre, apellidos, email, password, "", "7891015869", direccion));
        Disposable disposable = call.observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.isSuccessful()) {
                        prefs.saveString(Constants.AUTH_TOKEN, response.headers().get(Constants.AUTHORIZATION));
                        signupView.onSignupSuccess(response.body());
                    } else {
                        String body = response.errorBody().string();
                        ApiError apiError = Injector.provideGson().fromJson(body, ApiError.class);
                        signupView.showInfoDialog(apiError.message);
                    }
                }, throwable -> signupView.showInfoDialog("La aplicación no se pudo conectar al servidor"));
        compositeDisposable.add(disposable);
    }

    @Override
    public void subscribe() {}

    @Override
    public void unsubscribe() {
        if (compositeDisposable != null) compositeDisposable.clear();
    }
}
