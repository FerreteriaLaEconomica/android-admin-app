package com.smontiel.ferretera.admin.features.auth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.smontiel.ferretera.admin.Injector;
import com.smontiel.ferretera.admin.R;
import com.smontiel.ferretera.admin.utils.ActivityUtils;

public class AuthActivity extends AppCompatActivity {
    private LoginFragment loginFragment;
    private SignupFragment signupFragment;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loginFragment = LoginFragment.newInstance();
        signupFragment = SignupFragment.newInstance();
        if (findViewById(R.id.contentFrame) != null) startLoginFragment();
    }

    protected void startLoginFragment() {
        getSupportActionBar().setTitle(R.string.log_in);
        ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                loginFragment, R.id.contentFrame);
        new LoginPresenter(loginFragment, Injector.provideAuthClient(),
                Injector.provideSharedPrefs());
    }

    protected void startSignupFragment() {
        toolbar.setTitle(R.string.sign_up);
        ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                signupFragment, R.id.contentFrame);
        new SignupPresenter(signupFragment, Injector.provideAuthClient(),
                Injector.provideSharedPrefs());
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.contentFrame) instanceof SignupFragment)
            startLoginFragment();
        else super.onBackPressed();
    }
}
