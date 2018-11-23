package com.smontiel.ferretera.admin.features.create_product;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
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
    public static Intent getStartIntent(Context activity) {
        return new Intent(activity, CreateProductActivity.class);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

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

        Observable.combineLatest(nombreObservable, codigoBarrasObservable, descripcionObservable, precioObservable, descuentoObservable, imagenIV.getPublishSubject(),
                (s, s2, s3, s4, s5, s6) -> s6)
                .filter(isImageSet -> isImageSet)
                .subscribe(objects -> fab.setEnabled(true));
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.add_product);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = findViewById(R.id.fab);
        fab.setImageDrawable(new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_content_save)
                .colorRes(android.R.color.white));
        fab.setOnClickListener(view -> {
            double precio = Double.valueOf(precioET.getText());
            int descuento = Integer.valueOf(descuentoET.getText());
            presenter.createProducto(codigoBarrasET.getText(), nombreET.getText(),
                    descripcionET.getText(), formato, categoria, imageFile, precio, descuento);
        });
        fab.setEnabled(false);

        ImageView cameraIV = findViewById(R.id.camera_icon);
        cameraIV.setImageDrawable(new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_camera)
                .colorRes(R.color.md_white_1000));

        nombreET = findViewById(R.id.nombreET);
        codigoBarrasET = findViewById(R.id.codigoBarrasET);
        descripcionET = findViewById(R.id.descripcionET);
        precioET = findViewById(R.id.precioET);
        precioET.getEditText().setText("0.00");
        descuentoET = findViewById(R.id.descuentoET);
        descuentoET.getEditText().setText(0 + "");

        categoriaSpinner = findViewById(R.id.categoriaSpinner);
        categoriaSpinner.setOnItemSelectedListener((view, position, id, item) -> categoria = item.toString());

        formatoSpinner = findViewById(R.id.formatoSpinner);
        formatoSpinner.setItems("PIEZA", "KILOGRAMO");
        formatoSpinner.setOnItemSelectedListener((view, position, id, item) -> formato = item.toString());
        formato = formatoSpinner.getItems().get(0).toString();

        imagenIV = findViewById(R.id.imagenIV);
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
        nombreET.getEditText().setText("");
        codigoBarrasET.getEditText().setText("");
        descripcionET.getEditText().setText("");
        imagenIV.setImageURI(null);
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
        categoria = names.get(0);
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
