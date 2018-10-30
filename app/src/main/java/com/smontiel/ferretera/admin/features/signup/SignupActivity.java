package com.smontiel.ferretera.admin.features.signup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.smontiel.ferretera.admin.Injector;
import com.smontiel.ferretera.admin.R;
import com.smontiel.ferretera.admin.features.login.LoginActivity;
import com.smontiel.ferretera.admin.utils.ActivityUtils;

/**
 * Created by Salvador Montiel on 27/10/18.
 */
public class SignupActivity extends AppCompatActivity {
    public static Intent getStartIntent(Context activity) {
        return new Intent(activity, SignupActivity.class);
    }

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.sign_up);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(LoginActivity.getStartIntent(this));
        finish();
    }
}
