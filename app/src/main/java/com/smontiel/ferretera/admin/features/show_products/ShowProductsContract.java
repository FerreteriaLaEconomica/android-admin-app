package com.smontiel.ferretera.admin.features.show_products;

import com.smontiel.ferretera.admin.base.BasePresenter;
import com.smontiel.ferretera.admin.base.BaseView;

import java.util.List;

/**
 * Created by Salvador Montiel on 4/11/18.
 */
public interface ShowProductsContract {
    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean isLoading);

        void showError(String message);

        void showProductos(List<ProductItem> productos);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void loadAllProducts();
    }
}
