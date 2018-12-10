package com.smontiel.ferretera.admin.features.update_inventory;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.smontiel.ferretera.admin.Injector;
import com.smontiel.ferretera.admin.R;
import com.smontiel.ferretera.admin.data.models.ApiError;
import com.smontiel.ferretera.admin.data.models.Inventario;
import com.smontiel.ferretera.admin.data.models.Producto;
import com.smontiel.ferretera.admin.data.models.Sucursal;
import com.smontiel.ferretera.admin.data.network.ApiClient;
import com.smontiel.ferretera.admin.features.create_product.CreateProductActivity;
import com.smontiel.ferretera.admin.utils.CustomEditText;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class UpdateInventoryActivity extends AppCompatActivity {
    private static final String PRODUCT_ID = "PRODUCT_ID";
    private static final String PRODUCT_NAME = "PRODUCT_NAME";
    private static final String PRODUCT_BARCODE = "PRODUCT_BARCODE";
    private static final String PRODUCT_DESCRIPTION = "PRODUCT_DESCRIPTION";
    private static final String PRODUCT_URL_PHOTO = "PRODUCT_URL_PHOTO";
    private static final String PRODUCT_FORMAT = "PRODUCT_FORMAT";
    private static final String PRODUCT_CATEGORY = "PRODUCT_CATEGORY";
    private static final String PRODUCT_PRICE = "PRODUCT_PRICE";
    private static final String PRODUCT_DISCOUNT = "PRODUCT_DISCOUNT";
    private static final String PRODUCT_QUANTITY = "PRODUCT_QUANTITY";
    private static final String SUCURSAL_ID = "SUCURSAL_ID";
    private static final String SUCURSAL_NAME = "SUCURSAL_NAME";

    public static Intent getStartIntent(Context activity, Map<String, String> inventario, Sucursal sucursal) {
        Intent i = new Intent(activity, UpdateInventoryActivity.class);
        i.putExtra(PRODUCT_ID, Integer.valueOf(inventario.get("id_producto")));
        i.putExtra(PRODUCT_NAME, inventario.get("nombre"));
        i.putExtra(PRODUCT_BARCODE, inventario.get("codigo_barras"));
        i.putExtra(PRODUCT_DESCRIPTION, inventario.get("descripcion"));
        i.putExtra(PRODUCT_URL_PHOTO, inventario.get("url_foto"));
        i.putExtra(PRODUCT_FORMAT, inventario.get("formato"));
        i.putExtra(PRODUCT_CATEGORY, inventario.get("categoria"));
        i.putExtra(PRODUCT_PRICE, Double.valueOf(inventario.get("precio_venta")));
        i.putExtra(PRODUCT_DISCOUNT, Integer.valueOf(inventario.get("porcentaje_descuento")));
        i.putExtra(PRODUCT_QUANTITY, Integer.valueOf(inventario.get("cantidad")));
        i.putExtra(SUCURSAL_ID, sucursal.id);
        i.putExtra(SUCURSAL_NAME, sucursal.nombre);
        return i;
    }

    private ApiClient apiClient = Injector.provideApiClient();
    private Disposable disposable;

    private TextView nombreTV, cantidadTV, codigoBarrasTV, descripcionTV;
    private CustomEditText cantidadET;
    private Button actualizarBtn;
    private ImageView imagenIV;
    private TextView formatoTV;
    private TextView categoriaTV;
    private TextView precioTV;
    private TextView descuentoTV;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_inventory);

        initializeViews();
    }

    private void initializeViews() {
        Bundle b = getIntent().getExtras();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setSubtitle("Sucursal " + b.getString(SUCURSAL_NAME));
        nombreTV = findViewById(R.id.nombreTV);
        nombreTV.setText(b.getString(PRODUCT_NAME));
        imagenIV = findViewById(R.id.imagenIV);
        Glide.with(this)
                .load(b.getString(PRODUCT_URL_PHOTO))
                .error(R.mipmap.ic_launcher_round)
                .into(imagenIV);
        cantidadTV = findViewById(R.id.cantidadTV);
        cantidadTV.setText(b.getInt(PRODUCT_QUANTITY) + " unidades en inventario");
        cantidadET = findViewById(R.id.cantidadET);
        actualizarBtn = findViewById(R.id.actualizarBtn);
        actualizarBtn.setOnClickListener(view -> {
            int idSucursal = b.getInt(SUCURSAL_ID);
            int idProducto = b.getInt(PRODUCT_ID);
            Map<String, Object> map = new HashMap<>();
            map.put("cantidad", Integer.valueOf(cantidadET.getText()));
            setLoadingIndicator(true);
            disposable = apiClient.updateInventory(idSucursal, idProducto, map)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        setLoadingIndicator(false);
                        if (response.isSuccessful()) {
                            updatedInventorySuccessfully(response.body().cantidad);
                        } else {
                            String body = response.errorBody().string();
                            ApiError apiError = Injector.provideGson().fromJson(body, ApiError.class);
                            showInfoDialog(apiError.message);
                        }
                    }, throwable -> {
                        Timber.e(throwable);
                        setLoadingIndicator(false);
                        showInfoDialog("La aplicación no se pudo conectar al servidor");
                    });
        });

        codigoBarrasTV = findViewById(R.id.codigoBarrasTV);
        codigoBarrasTV.setText("Código de barras: \n" + b.getString(PRODUCT_BARCODE));
        descripcionTV = findViewById(R.id.descripcionTV);
        descripcionTV.setText("Descripción: \n" + b.getString(PRODUCT_DESCRIPTION));
        precioTV = findViewById(R.id.precioTV);
        precioTV.setText("Precio: \n$ " + b.getDouble(PRODUCT_PRICE));
        descuentoTV = findViewById(R.id.descuentoTV);
        descuentoTV.setText("Descuento: \n" + b.getInt(PRODUCT_DISCOUNT));
        formatoTV = findViewById(R.id.formatoTV);
        formatoTV.setText("Formato: \n" + b.getString(PRODUCT_FORMAT));
        categoriaTV = findViewById(R.id.categoriaTV);
        categoriaTV.setText("Categoría: \n" + b.getString(PRODUCT_CATEGORY));

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
    }

    private void showInfoDialog(String message) {
        showDialog("Ocurrió un error", message, GoogleMaterial.Icon.gmd_error, false, null);
    }

    private void updatedInventorySuccessfully(int newQuantity) {
        cantidadTV.setText(newQuantity + " unidades en inventario");
        Toast.makeText(this, "Inventario actualizado correctamente", Toast.LENGTH_LONG)
                .show();
    }

    private void setLoadingIndicator(boolean isLoading) {
        if (isLoading) progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
    }

    private void showDialog(String title, String message, IIcon icon, boolean cancelable,
                            DialogInterface.OnClickListener positiveListener) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(cancelable)
                .setIcon(new IconicsDrawable(this, icon)
                        .colorRes(android.R.color.black))
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", positiveListener)
                .create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        if (disposable != null) disposable.dispose();
        super.onDestroy();
    }
}
