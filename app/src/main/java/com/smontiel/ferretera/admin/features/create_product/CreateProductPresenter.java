package com.smontiel.ferretera.admin.features.create_product;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smontiel.ferretera.admin.data.models.Producto;
import com.smontiel.ferretera.admin.data.network.ApiClient;

import java.io.File;

import durdinapps.rxfirebase2.RxFirebaseStorage;
import io.reactivex.CompletableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import timber.log.Timber;

import static com.smontiel.ferretera.admin.utils.Preconditions.checkNotNull;

/**
 * Created by Salvador Montiel on 9/11/18.
 */
public class CreateProductPresenter implements CreateProductContract.Presenter {
    private final CreateProductContract.View view;
    private final ApiClient apiClient;
    private final FirebaseStorage firebaseStorage;
    private final CompositeDisposable compositeDisposable;

    public CreateProductPresenter(CreateProductContract.View view, ApiClient apiClient, FirebaseStorage firebaseStorage) {
        this.view = checkNotNull(view);
        this.apiClient = checkNotNull(apiClient);
        this.firebaseStorage = checkNotNull(firebaseStorage);
        this.compositeDisposable = new CompositeDisposable();
        view.setPresenter(this);
    }

    @Override
    public void createProducto(String codigoBarras, String nombre, String descripcion, String formato,
                               String categoria, File image, double precio, int descuento) {
        StorageReference imageRef = firebaseStorage.getReference("products")
                .child(System.currentTimeMillis() + image.getName());

        view.setLoadingIndicator(true);
        Disposable disposable = RxFirebaseStorage.putFile(imageRef, Uri.fromFile(image))
                .flatMapMaybe(taskSnapshot -> RxFirebaseStorage.getDownloadUrl(imageRef))
                .flatMap(uri -> {
                    Producto     p = new Producto(0, codigoBarras, nombre, descripcion, uri.toString(), formato, categoria, precio, descuento);
                    return apiClient.createProduct(p);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(product -> {
                    Timber.d(product.toString());
                    view.setLoadingIndicator(false);
                    view.onSaveSucces();
                }, throwable -> {
                    Timber.e(throwable);
                    Log.e("aA", throwable.toString());
                    view.showError("Failure: " + throwable.getMessage());
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void updateProducto(int id, String codigoBarras, String nombre, String descripcion,
                               String formato, String categoria, String urlImage, double precio,
                               int descuento) {
        view.setLoadingIndicator(true);
        Producto p = new Producto(id, codigoBarras, nombre, descripcion, urlImage, formato, categoria, precio, descuento);
        Disposable disposable = apiClient.updateProduct(id, p)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(product -> {
                    Timber.d(product.toString());
                    view.setLoadingIndicator(false);
                    view.onSaveSucces();
                }, throwable -> {
                    Timber.e(throwable);
                    Log.e("aA", throwable.toString());
                    view.showError("Failure: " + throwable.getMessage());
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void subscribe() {
        loadCategorias();
    }

    private void loadCategorias() {
        Disposable disposable = apiClient.getAllCategories()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.isSuccessful()) {
                        view.showCategorias(response.body());
                    }
                }, throwable -> view.showError("La aplicación no se pudo conectar al servidor"));
        compositeDisposable.add(disposable);
    }

    @Override
    public void unsubscribe() {
        if (compositeDisposable != null) compositeDisposable.clear();
    }
}
