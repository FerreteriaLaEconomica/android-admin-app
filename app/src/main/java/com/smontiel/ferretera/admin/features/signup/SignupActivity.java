package com.smontiel.ferretera.admin.features.signup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.smontiel.ferretera.admin.Injector;
import com.smontiel.ferretera.admin.R;
import com.smontiel.ferretera.admin.utils.ActivityUtils;

/**
 * Created by Salvador Montiel on 27/10/18.
 */
public class SignupActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.sign_up);
        setSupportActionBar(toolbar);

        SignupFragment statisticsFragment = (SignupFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (statisticsFragment == null) {
            statisticsFragment = SignupFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    statisticsFragment, R.id.contentFrame);
        }

        new SignupPresenter(statisticsFragment, Injector.provideAuthClient(),
                Injector.provideSharedPrefs());
    }
}
