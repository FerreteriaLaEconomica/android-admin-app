package com.smontiel.ferretera.admin.features.create_sucursal;

import com.smontiel.ferretera.admin.base.BasePresenter;
import com.smontiel.ferretera.admin.base.BaseView;

/**
 * Created by Salvador Montiel on 23/11/18.
 */

public interface CreateSucursalContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean isLoading);

        void showError(String message);

        void onSaveSucces();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void createSucursal(String nombre, String calle, String numExterior, String colonia,
                            String codigoPostal, String localidad, String municipio, String estado,
                            String emailAdmin);
    }
}
