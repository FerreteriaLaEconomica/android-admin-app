package com.smontiel.ferretera.admin.features.login;

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
import android.widget.TextView;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.smontiel.ferretera.admin.R;
import com.smontiel.ferretera.admin.data.User;
import com.smontiel.ferretera.admin.data.network.NetworkUtils;
import com.smontiel.ferretera.admin.features.dashboard.DashboardActivity;
import com.smontiel.ferretera.admin.features.signup.SignupActivity;
import com.smontiel.ferretera.admin.utils.CustomEditText;

import static com.smontiel.ferretera.admin.utils.Preconditions.checkNotNull;

/**
 * Created by Salvador Montiel on 29/10/18.
 */
public class LoginFragment extends Fragment implements LoginContract.View {

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    private LoginContract.Presenter presenter;

    private CustomEditText emailET, passwordET;
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.login_fragment, container, false);
        initializeViews(root);
        return root;
    }

    private void initializeViews(View v) {
        TextView createAccount = v.findViewById(R.id.createAccountTV);
        createAccount.setOnClickListener(view -> {
            startActivity(SignupActivity.getStartIntent(getActivity()));
            getActivity().finish();
        });
        emailET = v.findViewById(R.id.emailET);
        emailET.validateWith(Patterns.EMAIL_ADDRESS);
        emailET.setErrorLabel("Correo electrónico inválido");
        passwordET = v.findViewById(R.id.passwordET);
        passwordET.canStartWithWhitespace(true);
        fab = v.findViewById(R.id.login_fab);
        fab.setImageDrawable(new IconicsDrawable(getActivity(), CommunityMaterial.Icon2.cmd_login_variant)
                .colorRes(android.R.color.white));
        fab.setOnClickListener(view -> {
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

            if (emailET.isValid() && passwordET.isValid()) {
                if (NetworkUtils.isInternetAvailable(getActivity()))
                    presenter.logIn(email, password);
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
    public void setPresenter(LoginContract.Presenter presenter) {
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
    public void onLoginSuccess(User user) {
        getActivity().startActivity(DashboardActivity.getIntent(getActivity(), user));
        getActivity().finish();
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

    public void onInternetUnavailable() {
        showDialog("Sin conexión", "No hay conexión a Internet. Por favor activa el Wi-Fi o los datos.",
                MaterialDesignIconic.Icon.gmi_network_wifi_off);
    }
}
