package com.smontiel.ferretera.admin.features.login;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.smontiel.ferretera.admin.Injector;
import com.smontiel.ferretera.admin.R;
import com.smontiel.ferretera.admin.utils.ActivityUtils;

/**
 * Created by Salvador Montiel on 29/10/18.
 */
public class LoginActivity extends AppCompatActivity {
    public static Intent getStartIntent(Context activity) {
        return new Intent(activity, LoginActivity.class);
    }

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.log_in);
        setSupportActionBar(toolbar);

        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    loginFragment, R.id.contentFrame);
        }

        new LoginPresenter(loginFragment, Injector.provideAuthClient(),
                Injector.provideSharedPrefs());
    }
}
