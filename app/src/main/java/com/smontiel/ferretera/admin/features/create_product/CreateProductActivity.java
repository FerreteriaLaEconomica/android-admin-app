package com.smontiel.ferretera.admin.features.create_product;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.smontiel.ferretera.admin.Injector;
import com.smontiel.ferretera.admin.R;
import com.smontiel.ferretera.admin.base.RxImageView;
import com.smontiel.ferretera.admin.data.models.Categoria;
import com.smontiel.ferretera.admin.data.models.Producto;
import com.smontiel.ferretera.admin.utils.CustomEditText;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;

import static com.smontiel.ferretera.admin.utils.Preconditions.checkNotNull;

public class CreateProductActivity extends AppCompatActivity implements CreateProductContract.View {
    private static final String PRODUCT_ID = "PRODUCT_ID";
    private static final String PRODUCT_NAME = "PRODUCT_NAME";
    private static final String PRODUCT_BARCODE = "PRODUCT_BARCODE";
    private static final String PRODUCT_DESCRIPTION = "PRODUCT_DESCRIPTION";
    private static final String PRODUCT_URL_PHOTO = "PRODUCT_URL_PHOTO";
    private static final String PRODUCT_FORMAT = "PRODUCT_FORMAT";
    private static final String PRODUCT_CATEGORY = "PRODUCT_CATEGORY";
    private static final String PRODUCT_PRICE = "PRODUCT_PRICE";
    private static final String PRODUCT_DISCOUNT = "PRODUCT_DISCOUNT";

    public static Intent getStartIntent(Context activity) {
        return new Intent(activity, CreateProductActivity.class);
    }

    public static Intent getStartIntent(Context activity, Producto producto) {
        Intent i = new Intent(activity, CreateProductActivity.class);
        i.putExtra(PRODUCT_ID, producto.id);
        i.putExtra(PRODUCT_NAME, producto.nombre);
        i.putExtra(PRODUCT_BARCODE, producto.codigoBarras);
        i.putExtra(PRODUCT_DESCRIPTION, producto.descripcion);
        i.putExtra(PRODUCT_URL_PHOTO, producto.urlFoto);
        i.putExtra(PRODUCT_FORMAT, producto.formato);
        i.putExtra(PRODUCT_CATEGORY, producto.categoria);
        i.putExtra(PRODUCT_PRICE, producto.precioVenta);
        i.putExtra(PRODUCT_DISCOUNT, producto.descuento);
        return i;
    }

    private CreateProductContract.Presenter presenter = new CreateProductPresenter(
            this, Injector.provideApiClient(), FirebaseStorage.getInstance());

    private static final int REQUEST_CODE_CHOOSE = 23;
    private FloatingActionButton fab;
    private ProgressDialog progressDialog;
    private RxImageView imagenIV;
    private CustomEditText nombreET, codigoBarrasET, descripcionET, precioET, descuentoET;
    private MaterialSpinner formatoSpinner, categoriaSpinner;
    private File imageFile;
    private String formato = "";
    private String categoria = "";

    private Disposable disposable;
    private Producto producto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            int id = b.getInt(PRODUCT_ID);
            String nombre = b.getString(PRODUCT_NAME);
            String barcode = b.getString(PRODUCT_BARCODE);
            String descripcion = b.getString(PRODUCT_DESCRIPTION);
            String urlFoto = b.getString(PRODUCT_URL_PHOTO);
            String formato = b.getString(PRODUCT_FORMAT);
            String categoria = b.getString(PRODUCT_CATEGORY);
            double precio = b.getDouble(PRODUCT_PRICE);
            int descuento = b.getInt(PRODUCT_DISCOUNT);
            producto = new Producto(id, barcode, nombre, descripcion, urlFoto, formato, categoria, precio, descuento);
        }
        requestPermissions();
        initializeViews();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Guardando...");
        progressDialog.setCancelable(false);
        initViewBindings();
    }

    private void initViewBindings() {
        Observable<String> nombreObservable = RxTextView.textChanges(nombreET.getEditText())
                .map(CharSequence::toString)
                .filter(charSequence -> {
                    boolean result = charSequence.length() > 3;
                    if (!result) {
                        nombreET.setError("Nombre debe tener más de 3 caracteres");
                        fab.setEnabled(false);
                    } else nombreET.setError(null);
                    return result;
                });
        Observable<String> codigoBarrasObservable = RxTextView.textChanges(codigoBarrasET.getEditText())
                .map(CharSequence::toString)
                .filter(charSequence -> {
                    boolean result = charSequence.length() > 1;
                    if (!result) {
                        codigoBarrasET.setError("Código de barras debe tener más de 1 caracter");
                        fab.setEnabled(false);
                    } else codigoBarrasET.setError(null);
                    return result;
                });
        Observable<String> descripcionObservable = RxTextView.textChanges(descripcionET.getEditText())
                .map(CharSequence::toString)
                .filter(charSequence -> {
                    boolean result = charSequence.length() > 1;
                    if (!result) {
                        descripcionET.setError("Descripción debe tener más de 1 caracter");
                        fab.setEnabled(false);
                    } else descripcionET.setError(null);
                    return result;
                });
        Observable<String> precioObservable = RxTextView.textChanges(precioET.getEditText())
                .map(CharSequence::toString)
                .filter(charSequence -> {
                    boolean result = charSequence.length() >= 1;
                    if (!result) {
                        precioET.setError("Precio debe tener 1 dígito al menos");
                        fab.setEnabled(false);
                    } else precioET.setError(null);
                    return result;
                });
        Observable<String> descuentoObservable = RxTextView.textChanges(descuentoET.getEditText())
                .map(CharSequence::toString)
                .filter(charSequence -> {
                    int num = charSequence.equals("") ? 0 :  Integer.valueOf(charSequence);
                    boolean result = charSequence.length() >= 1 && num >= 0 && num <= 100;
                    if (!result) {
                        descuentoET.setError("Descuento debe tener 1 dígito al menos entre 0 - 100");
                        fab.setEnabled(false);
                    } else descuentoET.setError(null);
                    return result;
                });

        disposable = Observable.combineLatest(nombreObservable, codigoBarrasObservable, descripcionObservable, precioObservable, descuentoObservable, imagenIV.getPublishSubject(),
                (s, s2, s3, s4, s5, s6) -> s6)
                .filter(isImageSet -> isImageSet)
                .subscribe(objects -> fab.setEnabled(true));
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (producto != null) toolbar.setTitle(R.string.edit_product);
        else toolbar.setTitle(R.string.add_product);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = findViewById(R.id.fab);
        fab.setImageDrawable(new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_content_save)
                .colorRes(android.R.color.white));
        fab.setOnClickListener(view -> {
            double precio = Double.valueOf(precioET.getText());
            int descuento = Integer.valueOf(descuentoET.getText());
            if (producto != null) {
                presenter.updateProducto(producto.id, codigoBarrasET.getText(), nombreET.getText(),
                        descripcionET.getText(), formato, categoria, producto.urlFoto, precio, descuento);
            } else {
                presenter.createProducto(codigoBarrasET.getText(), nombreET.getText(),
                        descripcionET.getText(), formato, categoria, imageFile, precio, descuento);
            }
        });
        fab.setEnabled(false);

        ImageView cameraIV = findViewById(R.id.camera_icon);
        cameraIV.setImageDrawable(new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_camera)
                .colorRes(R.color.md_white_1000));

        nombreET = findViewById(R.id.nombreET);
        if (producto != null) nombreET.getEditText().setText(producto.nombre);
        codigoBarrasET = findViewById(R.id.codigoBarrasET);
        if (producto != null) codigoBarrasET.getEditText().setText(producto.codigoBarras);
        descripcionET = findViewById(R.id.descripcionET);
        if (producto != null) descripcionET.getEditText().setText(producto.descripcion);
        precioET = findViewById(R.id.precioET);
        if (producto != null) precioET.getEditText().setText(producto.precioVenta + "");
        else precioET.getEditText().setText("0.00");
        descuentoET = findViewById(R.id.descuentoET);
        if (producto != null) descuentoET.getEditText().setText(producto.descuento + "");
        else descuentoET.getEditText().setText(0 + "");

        categoriaSpinner = findViewById(R.id.categoriaSpinner);
        categoriaSpinner.setOnItemSelectedListener((view, position, id, item) -> categoria = item.toString());

        formatoSpinner = findViewById(R.id.formatoSpinner);
        List<String> formatos = Arrays.asList("PIEZA", "KILOGRAMO");
        formatoSpinner.setItems(formatos);
        formatoSpinner.setOnItemSelectedListener((view, position, id, item) -> formato = item.toString());
        if (producto != null) {
            int index = formatos.indexOf(producto.formato);
            formatoSpinner.setSelectedIndex(index);
            formato = formatos.get(index);
        }
        else formato = formatoSpinner.getItems().get(0).toString();

        imagenIV = findViewById(R.id.imagenIV);
        if (producto != null) {
            Glide.with(this)
                    .load(producto.urlFoto)
                    .error(R.mipmap.ic_launcher_round)
                    .into(imagenIV);
        }
        imagenIV.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions();
                    return;
                }
            }
            Matisse.from(this)
                    .choose(MimeType.ofImage())
                    .countable(false)
                    .maxSelectable(1)
                    .capture(true)
                    .captureStrategy(new CaptureStrategy(true, "com.smontiel.ferretera.admin.fileprovider"))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .imageEngine(new GlideEngine())
                    .forResult(REQUEST_CODE_CHOOSE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            String path = Matisse.obtainPathResult(data).get(0);
            imageFile = new File(path);
            imagenIV.setImageURI(Uri.fromFile(imageFile));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestPermissions() {
        PermissionListener dialogPermissionListener =
                DialogOnDeniedPermissionListener.Builder
                        .withContext(this)
                        .withTitle("Permiso de archivos")
                        .withMessage("Necesito el permiso de acceso a los archivos para acceder " +
                                "y tomar fotos de los productos")
                        .withButtonText(android.R.string.ok)
                        .withIcon(R.mipmap.ic_launcher)
                        .build();
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(dialogPermissionListener)
                .check();
    }

    @Override
    public void setPresenter(CreateProductContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public void setLoadingIndicator(boolean isLoading) {
        if (isLoading) progressDialog.show();
        else progressDialog.dismiss();
    }

    @Override
    public void showError(String message) {
        progressDialog.dismiss();
        showDialog("Ocurrió un error", message, GoogleMaterial.Icon.gmd_error);
    }

    @Override
    public void onSaveSucces() {
        if (producto == null) {
            nombreET.getEditText().setText("");
            codigoBarrasET.getEditText().setText("");
            descripcionET.getEditText().setText("");
            imagenIV.setImageURI(null);
            precioET.getEditText().setText("0.00");
            descuentoET.getEditText().setText("0");
        }
        Toast.makeText(this, "Producto guardado exitosamente", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void showCategorias(List<Categoria> categorias) {
        List<String> names = new ArrayList<>(categorias.size());
        for (Categoria c : categorias) names.add(c.nombre);
        categoriaSpinner.setItems(names);
        if (producto != null) {
            int index = names.indexOf(producto.categoria);
            categoriaSpinner.setSelectedIndex(index);
            categoria = names.get(index);
        }
        else categoria = names.get(0);
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
        if (disposable != null) disposable.dispose();
    }

    private void showDialog(String title, String message, IIcon icon) {
        progressDialog.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setIcon(new IconicsDrawable(this, icon)
                        .colorRes(android.R.color.black))
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .create();
        dialog.show();
    }
}
