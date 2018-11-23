package com.smontiel.ferretera.admin.features.auth;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.smontiel.ferretera.admin.R;
import com.smontiel.ferretera.admin.data.models.User;
import com.smontiel.ferretera.admin.data.network.NetworkUtils;
import com.smontiel.ferretera.admin.features.create_product.CreateProductActivity;
import com.smontiel.ferretera.admin.features.dashboard.DashboardActivity;
import com.smontiel.ferretera.admin.utils.CustomEditText;

import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static com.smontiel.ferretera.admin.utils.Preconditions.checkNotNull;

public class LoginFragment extends Fragment implements LoginContract.View {

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    private LoginContract.Presenter presenter;

    private CustomEditText emailET, passwordET;
    private FloatingActionButton fab;
    private ProgressDialog progressDialog;

    private Disposable disposable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        initializeViews(rootView);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Autenticando...");
        progressDialog.setCancelable(false);
        return rootView;
    }

    private void initializeViews(View v) {
        fab = v.findViewById(R.id.login_fab);
        fab.setImageDrawable(new IconicsDrawable(getActivity(), CommunityMaterial.Icon2.cmd_login_variant)
                .colorRes(android.R.color.white));
        fab.setOnClickListener(view -> {
            if (NetworkUtils.isInternetAvailable(getActivity())) {
                presenter.logIn(emailET.getText(), passwordET.getText());
            }
            else showDialog("Sin conexión",
                    "No hay conexión a Internet. Por favor activa el Wi-Fi o los datos.",
                    MaterialDesignIconic.Icon.gmi_network_wifi_off);
        });

        TextView createAccount = v.findViewById(R.id.createAccountTV);
        createAccount.setOnClickListener(view -> ((AuthActivity) getActivity()).startSignupFragment());

        emailET = v.findViewById(R.id.emailET);
        emailET.requestFocus();
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
        disposable = Observable.combineLatest(emailObservable, passwordObservable, Pair::new)
                .subscribe(pair -> fab.setEnabled(true));
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
        hideProgressDialog();
        showDialog("Ocurrió un error", message, GoogleMaterial.Icon.gmd_error);
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void onLoginSuccess(User user) {
        hideProgressDialog();
        getActivity().finish();
        getActivity().startActivity(DashboardActivity.getIntent(getActivity(), user));
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
