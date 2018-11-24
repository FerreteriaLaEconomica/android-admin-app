package com.smontiel.ferretera.admin.features.create_sucursal;

import com.smontiel.ferretera.admin.Injector;
import com.smontiel.ferretera.admin.data.models.ApiError;
import com.smontiel.ferretera.admin.data.models.Sucursal;
import com.smontiel.ferretera.admin.data.models.SucursalRequest;
import com.smontiel.ferretera.admin.data.network.ApiClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

import static com.smontiel.ferretera.admin.utils.Preconditions.checkNotNull;

/**
 * Created by Salvador Montiel on 23/11/18.
 */
public class CreateSucursalPresenter implements CreateSucursalContract.Presenter {
    private final CreateSucursalContract.View view;
    private final ApiClient apiClient;
    private final CompositeDisposable compositeDisposable;

    public CreateSucursalPresenter(CreateSucursalContract.View view, ApiClient apiClient) {
        this.view = checkNotNull(view);
        this.apiClient = checkNotNull(apiClient);
        this.compositeDisposable = new CompositeDisposable();
        view.setPresenter(this);
    }

    @Override
    public void createSucursal(String nombre, String calle, String numExterior, String colonia,
                               String codigoPostal, String localidad, String municipio, String estado,
                               String emailAdmin) {
        view.setLoadingIndicator(true);
        SucursalRequest s = new SucursalRequest(nombre, calle, numExterior, colonia, codigoPostal, localidad, municipio, estado, emailAdmin);
        Disposable disposable = apiClient.createSucursal(s)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    view.setLoadingIndicator(false);
                    Timber.i("Creating sucursal response: " + response);
                    if (response.isSuccessful()) {
                        view.onSaveSucces();
                    } else {
                        String body = response.errorBody().string();
                        ApiError apiError = Injector.provideGson().fromJson(body, ApiError.class);
                        view.showError(apiError.message);
                    }
                }, throwable -> {
                    Timber.e(throwable);
                    view.showError("La aplicación no se pudo conectar al servidor");
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void subscribe() {}

    @Override
    public void unsubscribe() {
        if (compositeDisposable != null) compositeDisposable.clear();
    }
}
