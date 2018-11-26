package com.smontiel.ferretera.admin.features.dashboard;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.smontiel.ferretera.admin.R;
import com.smontiel.ferretera.admin.data.models.Sucursal;

import java.util.List;

import timber.log.Timber;

import static com.smontiel.ferretera.admin.utils.Preconditions.checkNotNull;

/**
 * Created by Salvador Montiel on 27/10/18.
 */
public class DashboardFragment extends Fragment implements DashboardContract.View {
    private DashboardContract.Presenter presenter;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    private FastItemAdapter<InventarioItem> itemAdapter = new FastItemAdapter<>();

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private View noProductsView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dashboard_fragment, container, false);
        initializeViews(root);
        return root;
    }

    private void initializeViews(View v) {
        recyclerView = v.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(itemAdapter);
        itemAdapter.withOnClickListener((view, adapter, item, position) -> {
            new MaterialDialog.Builder(getActivity())
                    .title("Actualizar inventario")
                    .content(item.inventario.producto.nombre)
                    .inputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED)
                    .inputRange(1, 5)
                    .input("Cantidad disponible", "" + item.inventario.cantidad, false, (dialog, input) -> {})
                    .positiveText("Actualizar")
                    .onPositive((dialog, which) -> {
                        Timber.e(item.inventario.toString());
                        presenter.updateInventory(item.inventario.idSucursal, item.inventario.producto.id,
                                Integer.valueOf(dialog.getInputEditText().getText().toString()));
                    })
                    .negativeText("Cancelar")
                    .show();
            return true;
        });
        noProductsView = v.findViewById(R.id.no_products);
        progressBar = v.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
    }

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
    public void setLoadingIndicator(boolean isLoading) {
        if (isLoading) progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showInfoDialog(String message) {
        showDialog("Ocurrió un error", message, GoogleMaterial.Icon.gmd_error, false, null);
    }

    @Override
    public void updatedInventorySuccessfully() {
        Toast.makeText(getActivity(), "Inventario actualizado correctamente", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void showInventory(List<InventarioItem> inventarioItems) {
        if (inventarioItems.isEmpty()) showNoProductsView();
        else {
            itemAdapter.setNewList(inventarioItems);
            recyclerView.setVisibility(View.VISIBLE);
            noProductsView.setVisibility(View.GONE);
        }
    }

    private void showNoProductsView() {
        recyclerView.setVisibility(View.GONE);
        noProductsView.setVisibility(View.VISIBLE);
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
