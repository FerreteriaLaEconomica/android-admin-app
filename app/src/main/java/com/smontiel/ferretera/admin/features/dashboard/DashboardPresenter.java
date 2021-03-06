package com.smontiel.ferretera.admin.features.dashboard;

import com.smontiel.ferretera.admin.Injector;
import com.smontiel.ferretera.admin.data.models.ApiError;
import com.smontiel.ferretera.admin.data.Constants;
import com.smontiel.ferretera.admin.data.SharedPrefs;
import com.smontiel.ferretera.admin.data.models.Inventario;
import com.smontiel.ferretera.admin.data.models.Sucursal;
import com.smontiel.ferretera.admin.data.network.ApiClient;
import com.smontiel.ferretera.admin.data.network.AuthClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;
import timber.log.Timber;

import static com.smontiel.ferretera.admin.utils.Preconditions.checkNotNull;

/**
 * Created by Salvador Montiel on 27/10/18.
 */
public class DashboardPresenter implements DashboardContract.Presenter {
    private final DashboardContract.View dashboardView;
    private final AuthClient authClient;
    private final ApiClient apiClient;
    private final SharedPrefs prefs;
    private final CompositeDisposable compositeDisposable;
    private int currentSucursal;

    public DashboardPresenter(DashboardContract.View dashboardView, AuthClient authClient,
                              ApiClient apiClient, SharedPrefs prefs) {
        this.dashboardView = checkNotNull(dashboardView);
        this.authClient = checkNotNull(authClient);
        this.apiClient = checkNotNull(apiClient);
        this.prefs = checkNotNull(prefs);
        this.compositeDisposable = new CompositeDisposable();
        dashboardView.setPresenter(this);
    }

    @Override
    public void loadInventoryBySucursalId(int id) {
        this.currentSucursal = id;
        dashboardView.setLoadingIndicator(true);
        Disposable disposable = apiClient.getInventarioBySucursal(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    dashboardView.setLoadingIndicator(false);
                    if (response.isSuccessful()) {
                        showInventory(response.body());
                    } else {
                        String body = response.errorBody().string();
                        ApiError apiError = Injector.provideGson().fromJson(body, ApiError.class);
                        dashboardView.showInfoDialog(apiError.message);
                    }
                }, throwable -> {
                    Timber.e(throwable);
                    dashboardView.setLoadingIndicator(false);
                    dashboardView.showInfoDialog("La aplicación no se pudo conectar al servidor");
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void updateInventory(int idSucursal, int idProduct, int quantity) {
        dashboardView.setLoadingIndicator(true);
        Map<String, Object> map = new HashMap<>();
        map.put("cantidad", quantity);
        Disposable disposable = apiClient.updateInventory(idSucursal, idProduct, map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    dashboardView.setLoadingIndicator(false);
                    if (response.isSuccessful()) {
                        dashboardView.updatedInventorySuccessfully();
                        loadInventoryBySucursalId(currentSucursal);
                    } else {
                        String body = response.errorBody().string();
                        ApiError apiError = Injector.provideGson().fromJson(body, ApiError.class);
                        dashboardView.showInfoDialog(apiError.message);
                    }
                }, throwable -> {
                    Timber.e(throwable);
                    dashboardView.setLoadingIndicator(false);
                    dashboardView.showInfoDialog("La aplicación no se pudo conectar al servidor");
                });
        compositeDisposable.add(disposable);
    }

    private void showInventory(List<Inventario> inventarios) {
        List<InventarioItem> list = new ArrayList<>();
        for (Inventario p : inventarios) list.add(new InventarioItem(p));
        dashboardView.showInventory(list);
    }

    @Override
    public void logOut() {
        prefs.saveString(Constants.AUTH_TOKEN, "");
    }

    @Override
    public void subscribe() {
        dashboardView.setLoadingIndicator(true);
        Maybe<Response<List<Sucursal>>> call = authClient.getAllSucursalesOfCurrentUser();
        Disposable disposable = call.observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    dashboardView.setLoadingIndicator(false);
                    if (response.isSuccessful()) {
                        dashboardView.showSucursales(response.body());
                    } else {
                        String body = response.errorBody().string();
                        ApiError apiError = Injector.provideGson().fromJson(body, ApiError.class);
                        dashboardView.showInfoDialog(apiError.message);
                    }
                }, throwable -> {
                    dashboardView.setLoadingIndicator(false);
                    dashboardView.showInfoDialog("La aplicación no se pudo conectar al servidor");
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void unsubscribe() {
        if (compositeDisposable != null) compositeDisposable.clear();
    }
}
