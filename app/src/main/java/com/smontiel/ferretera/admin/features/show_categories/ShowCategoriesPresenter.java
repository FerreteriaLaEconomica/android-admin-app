package com.smontiel.ferretera.admin.features.show_categories;

import com.smontiel.ferretera.admin.Injector;
import com.smontiel.ferretera.admin.data.models.ApiError;
import com.smontiel.ferretera.admin.data.models.Categoria;
import com.smontiel.ferretera.admin.data.network.ApiClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

import static com.smontiel.ferretera.admin.utils.Preconditions.checkNotNull;

/**
 * Created by Salvador Montiel on 23/11/18.
 */
public class ShowCategoriesPresenter implements ShowCategoriesContract.Presenter {
    private final ShowCategoriesContract.View view;
    private final ApiClient apiClient;
    private final CompositeDisposable compositeDisposable;

    public ShowCategoriesPresenter(ShowCategoriesContract.View view, ApiClient apiClient) {
        this.view = checkNotNull(view);
        this.apiClient = checkNotNull(apiClient);
        this.compositeDisposable = new CompositeDisposable();
        view.setPresenter(this);
    }

    @Override
    public void loadAllCategories() {
        view.setLoadingIndicator(true);
        Disposable disposable = apiClient.getAllCategories()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    view.setLoadingIndicator(false);
                    if (response.isSuccessful()) {
                        showProductos(response.body());
                    } else {
                        String body = response.errorBody().string();
                        ApiError apiError = Injector.provideGson().fromJson(body, ApiError.class);
                        view.showError(apiError.message);
                    }
                }, throwable -> {
                    view.setLoadingIndicator(false);
                    view.showError("La aplicación no se pudo conectar al servidor");
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void createCategory(String name) {
        if (name.isEmpty()) return;
        view.setProgressBarIndicator(true);
        Map<String, Object> body = new HashMap<>();
        body.put("nombre", name);
        Disposable disposable = apiClient.createCategory(body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    view.setProgressBarIndicator(false);
                    if (response.isSuccessful()) {
                        Timber.i("Created category: " + response.body());
                        loadAllCategories();
                    } else {
                        String responseBody = response.errorBody().string();
                        ApiError apiError = Injector.provideGson().fromJson(responseBody, ApiError.class);
                        view.showError(apiError.message);
                    }
                }, throwable -> {
                    Timber.e(throwable);
                    view.setProgressBarIndicator(false);
                    view.showError("La aplicación no se pudo conectar al servidor");
                });
        compositeDisposable.add(disposable);
    }

    private void showProductos(List<Categoria> categorias) {
        List<CategoryItem> list = new ArrayList<>();
        for (Categoria c : categorias) list.add(new CategoryItem(c.nombre));
        view.showCategories(list);
    }

    @Override
    public void subscribe() {
        loadAllCategories();
    }

    @Override
    public void unsubscribe() {
        if (compositeDisposable != null) compositeDisposable.clear();
    }
}
