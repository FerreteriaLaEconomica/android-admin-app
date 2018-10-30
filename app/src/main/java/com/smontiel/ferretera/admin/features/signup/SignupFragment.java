package com.smontiel.ferretera.admin.features.signup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.smontiel.ferretera.admin.R;
import com.smontiel.ferretera.admin.data.network.NetworkUtils;
import com.smontiel.ferretera.admin.features.dashboard.DashboardActivity;
import com.smontiel.ferretera.admin.utils.CustomEditText;

import java.util.regex.Pattern;

import static com.smontiel.ferretera.admin.utils.Preconditions.checkNotNull;

/**
 * Created by Salvador Montiel on 27/10/18.
 */
public class SignupFragment extends Fragment implements SignupContract.View {
    private SignupContract.Presenter presenter;

    private FloatingActionButton fab;
    private CustomEditText nombreET, apellidosET, emailET, telefonoET, passwordET, passwordCheckET;

    public static SignupFragment newInstance() {
        return new SignupFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.signup_fragment, container, false);
        initializeViews(root);
        return root;
    }

    private void initializeViews(View view) {
        nombreET = view.findViewById(R.id.nombreET);
        nombreET.validateWith(Pattern.compile("^[a-zA-Z]{1}[a-zA-Z ]*$"));
        nombreET.setErrorLabel("Nombre inválido");
        apellidosET = view.findViewById(R.id.apellidosET);
        apellidosET.validateWith(Pattern.compile("^[a-zA-Z]{1}[a-zA-Z ]*$"));
        apellidosET.setErrorLabel("Apellido(s) inválido(s)");
        emailET = view.findViewById(R.id.emailET);
        emailET.validateWith(Patterns.EMAIL_ADDRESS);
        emailET.setErrorLabel("Correo electrónico inválido");
        passwordET = view.findViewById(R.id.passwordET);
        passwordET.canStartWithWhitespace(true);
        passwordCheckET = view.findViewById(R.id.passwordCheckET);
        passwordCheckET.canStartWithWhitespace(true);
        telefonoET = view.findViewById(R.id.telefonoET);
        telefonoET.validateWith(Pattern.compile("\\d{10}"));
        telefonoET.setErrorLabel("Teléfono inválido");

        fab = view.findViewById(R.id.signup_fab);
        fab.setImageDrawable(new IconicsDrawable(getActivity(), MaterialDesignIconic.Icon.gmi_check)
                .colorRes(android.R.color.white));
        fab.setOnClickListener(v -> {
            String nombre = nombreET.getText();
            if (!nombreET.isValid() && nombre.equals("")) {
                nombreET.showError("Éste campo no puede estar vacío");
                return;
            }
            String apellidos = apellidosET.getText();
            if (!apellidosET.isValid() && apellidos.equals("")) {
                apellidosET.showError("Éste campo no puede estar vacío");
                return;
            }
            String email = emailET.getText();
            if (!emailET.isValid() && email.equals("")) {
                emailET.showError("Éste campo no puede estar vacío");
                return;
            }
            String password = passwordET.getText();
            if (password.equals("")) {
                passwordET.showError("Éste campo no puede estar vacío");
                return;
            } else passwordET.showError(null);
            if (!contraseñasSonIguales(password, passwordCheckET.getText())) {
                passwordCheckET.showError("La contraseña no coincide");
                return;
            } else passwordCheckET.showError(null);
            String telefono = telefonoET.getText();
            if (!telefonoET.isValid() && telefono.equals("")) {
                telefonoET.showError("Éste campo no puede estar vacío");
                return;
            }

            if (nombreET.isValid() && apellidosET.isValid() && emailET.isValid() && telefonoET.isValid()) {
                if (NetworkUtils.isInternetAvailable(getActivity()))
                    presenter.signUp(nombre, apellidos, email, password, telefono);
                else onInternetUnavailable();
            }
        });
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
    public void onSuccess() {
        getActivity().startActivity(DashboardActivity.getIntent(getActivity(), null));
    }

    private boolean contraseñasSonIguales(String a, String b) {
        return a.equals(b);
    }

    public void onInternetUnavailable() {
        showDialog("Sin conexión", "No hay conexión a Internet. Por favor activa el Wi-Fi o los datos.",
                MaterialDesignIconic.Icon.gmi_network_wifi_off);
    }

    private void showDialog(String title, String message, IIcon icon) {
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
