package com.smontiel.ferretera.admin.features.dashboard;

import com.smontiel.ferretera.admin.base.BasePresenter;
import com.smontiel.ferretera.admin.base.BaseView;
import com.smontiel.ferretera.admin.data.models.Sucursal;

import java.util.List;

/**
 * Created by Salvador Montiel on 27/10/18.
 */
public interface DashboardContract {
    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean isLoading);

        void showInfoDialog(String message);

        void updatedInventorySuccessfully();

        void showInventory(List<InventarioItem> inventarioItems);

        void showSucursales(List<Sucursal> sucursales);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void loadInventoryBySucursalId(int id);

        void updateInventory(int idSucursal, int idProduct, int quantity);

        void logOut();
    }
}
