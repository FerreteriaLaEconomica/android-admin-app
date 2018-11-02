package com.smontiel.ferretera.admin.features.dashboard;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.smontiel.ferretera.admin.R;
import com.smontiel.ferretera.admin.data.Sucursal;

import java.util.List;

import static com.smontiel.ferretera.admin.utils.Preconditions.checkNotNull;

/**
 * Created by Salvador Montiel on 27/10/18.
 */
public class DashboardFragment extends Fragment implements DashboardContract.View {
    private DashboardContract.Presenter presenter;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dashboard_fragment, container, false);
        initializeViews(root);
        return root;
    }

    private void initializeViews(View view) {}

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    @Override
    public void setPresenter(DashboardContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public void setLoadingIndicator(boolean isLoading) {}

    @Override
    public void showInfoDialog(String message) {
        showDialog("Ocurrió un error", message, GoogleMaterial.Icon.gmd_error, false, null);
    }

    @Override
    public void showSucursales(List<Sucursal> sucursales) {
        ((DashboardActivity) getActivity()).updateDrawerItems(sucursales);
        if (!((DashboardActivity) getActivity()).currentUser.isSuperAdmin && sucursales.size() == 0) {
            showDialog("Sin permiso",
                    "No cuentas con los permisos necesarios para acceder a esta aplicacion",
                    GoogleMaterial.Icon.gmd_error,
                    false,
                    (dialogInterface, i) -> {
                        presenter.logOut();
                        getActivity().finish();
                    });
        }
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private void showDialog(String title, String message, IIcon icon, boolean cancelable,
                            DialogInterface.OnClickListener positiveListener) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setCancelable(cancelable)
                .setIcon(new IconicsDrawable(getActivity(), icon)
                        .colorRes(android.R.color.black))
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", positiveListener)
                .create();
        dialog.show();
    }
}
