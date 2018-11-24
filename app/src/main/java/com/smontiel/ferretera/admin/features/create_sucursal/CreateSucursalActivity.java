package com.smontiel.ferretera.admin.features.create_sucursal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.smontiel.ferretera.admin.Injector;
import com.smontiel.ferretera.admin.R;
import com.smontiel.ferretera.admin.utils.CustomEditText;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static com.smontiel.ferretera.admin.utils.Preconditions.checkNotNull;

/**
 * Created by Salvador Montiel on 23/11/18.
 */

public class CreateSucursalActivity extends AppCompatActivity implements CreateSucursalContract.View {
    public static Intent getStartIntent(Context activity) {
        return new Intent(activity, CreateSucursalActivity.class);
    }

    private CreateSucursalContract.Presenter presenter = new CreateSucursalPresenter(
            this, Injector.provideApiClient());

    private FloatingActionButton fab;
    private ProgressBar progressBar;
    private CustomEditText nombreET, calleET, numeroET, coloniaET, codigoPostalET;
    private CustomEditText localidadET, municipioET, estadoET, emailAdminET;

    private Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sucursal);

        initializeViews();
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
        Observable<String> calleObservable = RxTextView.textChanges(calleET.getEditText())
                .map(CharSequence::toString)
                .filter(charSequence -> {
                    boolean result = charSequence.length() > 1;
                    if (!result) {
                        calleET.setError("Calle debe tener más de 1 caracteres");
                        fab.setEnabled(false);
                    } else calleET.setError(null);
                    return result;
                });
        Observable<String> numeroObservable = RxTextView.textChanges(numeroET.getEditText())
                .map(CharSequence::toString)
                .filter(charSequence -> {
                    boolean result = charSequence.length() > 1;
                    if (!result) {
                        numeroET.setError("Número exterior");
                        fab.setEnabled(false);
                    } else numeroET.setError(null);
                    return result;
                });
        Observable<String> coloniaObservable = RxTextView.textChanges(coloniaET.getEditText())
                .map(CharSequence::toString)
                .filter(charSequence -> {
                    boolean result = charSequence.length() > 1;
                    if (!result) {
                        coloniaET.setError("Colonia debe tener más de 1 caracteres");
                        fab.setEnabled(false);
                    } else coloniaET.setError(null);
                    return result;
                });
        Observable<String> codigoPostalObservable = RxTextView.textChanges(codigoPostalET.getEditText())
                .map(CharSequence::toString)
                .filter(charSequence -> {
                    boolean result = charSequence.length() == 5;
                    if (!result) {
                        codigoPostalET.setError("Código postal");
                        fab.setEnabled(false);
                    } else codigoPostalET.setError(null);
                    return result;
                });
        Observable<String> first = Observable.combineLatest(nombreObservable, calleObservable, numeroObservable, coloniaObservable,
                codigoPostalObservable,
                (s1, s2, s3, s4, s5) -> s2);
        Observable<String> localidadObservable = RxTextView.textChanges(localidadET.getEditText())
                .map(CharSequence::toString)
                .filter(charSequence -> {
                    boolean result = charSequence.length() > 1;
                    if (!result) {
                        localidadET.setError("Localidad debe tener más de 1 caracteres");
                        fab.setEnabled(false);
                    } else localidadET.setError(null);
                    return result;
                });
        Observable<String> municipioObservable = RxTextView.textChanges(municipioET.getEditText())
                .map(CharSequence::toString)
                .filter(charSequence -> {
                    boolean result = charSequence.length() > 1;
                    if (!result) {
                        municipioET.setError("Municipio debe tener más de 1 caracteres");
                        fab.setEnabled(false);
                    } else municipioET.setError(null);
                    return result;
                });
        Observable<String> estadoObservable = RxTextView.textChanges(estadoET.getEditText())
                .map(CharSequence::toString)
                .filter(charSequence -> {
                    boolean result = charSequence.length() > 1;
                    if (!result) {
                        estadoET.setError("Estado debe tener más de 1 caracteres");
                        fab.setEnabled(false);
                    } else estadoET.setError(null);
                    return result;
                });
        Observable<String> emailAdminObservable = RxTextView.textChanges(emailAdminET.getEditText())
                .map(CharSequence::toString)
                .filter(charSequence -> {
                    boolean result = charSequence.length() > 1;
                    if (!result) {
                        emailAdminET.setError("Email debe tener más de 1 caracteres");
                        fab.setEnabled(false);
                    } else emailAdminET.setError(null);
                    return result;
                });
        Observable<String> second = Observable.combineLatest(localidadObservable, municipioObservable,
                estadoObservable, emailAdminObservable,
                (s1, s2, s3, s4) -> s2);
        disposable = Observable.combineLatest(first, second, (s1, s2) -> s2)
                .subscribe(objects -> fab.setEnabled(true));

    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.add_sucursal);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = findViewById(R.id.fab);
        fab.setImageDrawable(new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_content_save)
                .colorRes(android.R.color.white));
        fab.setOnClickListener(view -> {
            presenter.createSucursal(nombreET.getText(), calleET.getText(), numeroET.getText(),
                    coloniaET.getText(), codigoPostalET.getText(), localidadET.getText(),
                    municipioET.getText(), estadoET.getText(), emailAdminET.getText());
        });
        fab.setEnabled(false);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        nombreET = findViewById(R.id.nombreET);
        calleET = findViewById(R.id.calleET);
        numeroET = findViewById(R.id.numeroET);
        coloniaET = findViewById(R.id.coloniaET);
        codigoPostalET = findViewById(R.id.codigoPostalET);
        localidadET = findViewById(R.id.localidadET);
        municipioET = findViewById(R.id.municipioET);
        municipioET.getEditText().setText("Tantoyuca");
        estadoET = findViewById(R.id.estadoET);
        estadoET.getEditText().setText("Veracruz");
        emailAdminET = findViewById(R.id.emailAdminET);
    }

    @Override
    public void setPresenter(CreateSucursalContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public void setLoadingIndicator(boolean isLoading) {
        if (isLoading) progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        progressBar.setVisibility(View.GONE);
        showDialog("Ocurrió un error", message, GoogleMaterial.Icon.gmd_error);
    }

    @Override
    public void onSaveSucces() {
        nombreET.getEditText().setText("");
        calleET.getEditText().setText("");
        numeroET.getEditText().setText("");
        coloniaET.getEditText().setText("");
        codigoPostalET.getEditText().setText("");
        localidadET.getEditText().setText("");
        municipioET.getEditText().setText("");
        estadoET.getEditText().setText("");
        emailAdminET.getEditText().setText("");
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, "Sucursal creada exitosamente", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return true;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog(String title, String message, IIcon icon) {
        progressBar.setVisibility(View.GONE);
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
