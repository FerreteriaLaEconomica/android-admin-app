package com.smontiel.ferretera.admin.features.show_products;

import com.smontiel.ferretera.admin.Injector;
import com.smontiel.ferretera.admin.data.models.ApiError;
import com.smontiel.ferretera.admin.data.models.Producto;
import com.smontiel.ferretera.admin.data.network.ApiClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.smontiel.ferretera.admin.utils.Preconditions.checkNotNull;

/**
 * Created by Salvador Montiel on 4/11/18.
 */
public class ShowProductsPresenter implements ShowProductsContract.Presenter {
    private final ShowProductsContract.View view;
    private final ApiClient productsRepo;
    private final CompositeDisposable compositeDisposable;

    public ShowProductsPresenter(ShowProductsContract.View view, ApiClient productsRepo) {
        this.view = checkNotNull(view);
        this.productsRepo = checkNotNull(productsRepo);
        this.compositeDisposable = new CompositeDisposable();
        view.setPresenter(this);
    }

    @Override
    public void loadAllProducts() {
        view.setLoadingIndicator(true);
        Disposable disposable = productsRepo.getAllProducts()
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

    private void showProductos(List<Producto> productos) {
        List<ProductItem> list = new ArrayList<>();
        for (Producto p : productos) list.add(new ProductItem(p));
        view.showProductos(list);
    }

    @Override
    public void subscribe() {
        loadAllProducts();
    }

    @Override
    public void unsubscribe() {
        if (compositeDisposable != null) compositeDisposable.clear();
    }
}
