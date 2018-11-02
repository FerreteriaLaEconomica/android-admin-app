package com.smontiel.ferretera.admin.features.dashboard;

import com.smontiel.ferretera.admin.BasePresenter;
import com.smontiel.ferretera.admin.BaseView;
import com.smontiel.ferretera.admin.data.Sucursal;

import java.util.List;

/**
 * Created by Salvador Montiel on 27/10/18.
 */
public interface DashboardContract {
    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean isLoading);

        void showInfoDialog(String message);

        void showSucursales(List<Sucursal> sucursales);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void logOut();
    }
}
