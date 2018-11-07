package com.smontiel.ferretera.admin.features.auth;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.smontiel.ferretera.admin.R;
import com.smontiel.ferretera.admin.data.models.User;
import com.smontiel.ferretera.admin.data.network.NetworkUtils;
import com.smontiel.ferretera.admin.features.dashboard.DashboardActivity;
import com.smontiel.ferretera.admin.utils.CustomEditText;

import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static com.smontiel.ferretera.admin.utils.Preconditions.checkNotNull;

public class SignupFragment extends Fragment implements SignupContract.View {
    public static SignupFragment newInstance() {
        return new SignupFragment();
    }

    private SignupContract.Presenter presenter;

    private CustomEditText nombreET, apellidosET, emailET, telefonoET, passwordET, passwordCheckET;
    private FloatingActionButton fab;
    private ProgressDialog progressDialog;

    private Disposable disposable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signup, container, false);
        initializeViews(rootView);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Espera un momento estamos creando tu cuenta...");
        progressDialog.setCancelable(false);
        return rootView;
    }

    private void initializeViews(View v) {
        fab = v.findViewById(R.id.signup_fab);
        fab.setImageDrawable(new IconicsDrawable(getActivity(), MaterialDesignIconic.Icon.gmi_check)
                .colorRes(android.R.color.white));
        fab.setOnClickListener(view -> {
            if (NetworkUtils.isInternetAvailable(getActivity())) {
                progressDialog.show();
                presenter.signUp(nombreET.getText(), apellidosET.getText(), emailET.getText(),
                        passwordET.getText(), telefonoET.getText());
            }
            else showDialog("Sin conexión",
                    "No hay conexión a Internet. Por favor activa el Wi-Fi o los datos.",
                    MaterialDesignIconic.Icon.gmi_network_wifi_off);
        });
        nombreET = v.findViewById(R.id.nombreET);
        nombreET.requestFocus();
        Pattern nombrePattern = Pattern.compile("^\\w+(?:(?:\\s\\w+)*)$");
        Observable<String> nombreObservable = RxTextView.textChanges(nombreET.getEditText())
                .map(CharSequence::toString)
                .filter(text -> {
                    boolean result = nombrePattern.matcher(text).matches();
                    if (!result) {
                        nombreET.setError("Nombre inválido");
                        fab.setEnabled(false);
                    } else nombreET.setError(null);
                    return result;
                });
        apellidosET = v.findViewById(R.id.apellidosET);
        Observable<String> apellidosObservable = RxTextView.textChanges(apellidosET.getEditText())
                .map(CharSequence::toString)
                .filter(text -> {
                    boolean result = nombrePattern.matcher(text).matches();
                    if (!result) {
                        apellidosET.setError("Apellido(s) inválido(s)");
                        fab.setEnabled(false);
                    } else apellidosET.setError(null);
                    return result;
                });
        emailET = v.findViewById(R.id.emailET);
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Observable<String> emailObservable = RxTextView.textChanges(emailET.getEditText())
                .map(CharSequence::toString)
                .filter(text -> {
                    boolean result = emailPattern.matcher(text).matches();
                    if (!result) {
                        emailET.setError("Correo electrónico inválido");
                        fab.setEnabled(false);
                    } else emailET.setError(null);
                    return result;
                });
        passwordET = v.findViewById(R.id.passwordET);
        Observable<String> passwordObservable = RxTextView.textChanges(passwordET.getEditText())
                .map(CharSequence::toString)
                .filter(text -> {
                    boolean result = text.length() >= 5;
                    if (!result) {
                        passwordET.setError("La contraseña debe tener al menos 5 caracteres");
                        fab.setEnabled(false);
                    } else passwordET.setError(null);
                    return result;
                });
        passwordCheckET = v.findViewById(R.id.passwordCheckET);
        Observable<String> passwordCheckObservable = RxTextView.textChanges(passwordCheckET.getEditText())
                .map(CharSequence::toString)
                .filter(text -> {
                    boolean result = text.equals(passwordET.getText());
                    if (!result) {
                        passwordCheckET.setError("Las contraseñas no coinciden");
                        fab.setEnabled(false);
                    } else if (text.length() < 5) {
                        passwordCheckET.setError("La contraseña debe tener al menos 5 caracteres");
                        fab.setEnabled(false);
                        result = false;
                    } else passwordCheckET.setError(null);
                    return result;
                });
        telefonoET = v.findViewById(R.id.telefonoET);
        telefonoET.setCounterEnabled(true);
        telefonoET.setCounterMaxLength(10);
        Pattern telefonoPattern = Pattern.compile("\\d{10}");
        Observable<String> telefonoObservable = RxTextView.textChanges(telefonoET.getEditText())
                .map(CharSequence::toString)
                .filter(text -> {
                    boolean result = telefonoPattern.matcher(text).matches();
                    if (!result) {
                        telefonoET.setError("Teléfono debe ser un número de 10 dígitos");
                        fab.setEnabled(false);
                    } else telefonoET.setError(null);
                    return result;
                });
        disposable = Observable.combineLatest(nombreObservable, apellidosObservable, emailObservable,
                passwordObservable, passwordCheckObservable, telefonoObservable,
                (s1, s2, s3, s4, s5, s6) -> Void.TYPE)
                .subscribe(ignored -> fab.setEnabled(true));
    }

    @Override
    public void setPresenter(SignupContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showInfoDialog(String message) {
        showDialog("Ocurrió un error", message, GoogleMaterial.Icon.gmd_error);
    }

    @Override
    public void onSignupSuccess(User user) {
        progressDialog.dismiss();
        getActivity().startActivity(DashboardActivity.getIntent(getActivity(), user));
        getActivity().finish();
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
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setIcon(new IconicsDrawable(getActivity(), icon)
                        .colorRes(android.R.color.black))
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .create();
        dialog.show();
    }
}
