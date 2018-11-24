package com.smontiel.ferretera.admin.features.create_product;

import com.smontiel.ferretera.admin.base.BasePresenter;
import com.smontiel.ferretera.admin.base.BaseView;
import com.smontiel.ferretera.admin.data.models.Categoria;

import java.io.File;
import java.util.List;

/**
 * Created by Salvador Montiel on 9/11/18.
 */
public interface CreateProductContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean isLoading);

        void showError(String message);

        void onSaveSucces();

        boolean isActive();

        void showCategorias(List<Categoria> categorias);
    }

    interface Presenter extends BasePresenter {

        void createProducto(String codigoBarras, String nombre, String descripcion, String formato,
                            String categoria, File image, double precio, int descuento);

        void updateProducto(int id, String codigoBarras, String nombre, String descripcion, String formato,
                            String categoria, String urlImage, double precio, int descuento);
    }
}
