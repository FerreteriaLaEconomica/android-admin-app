package com.smontiel.ferretera.admin.features.dashboard;

import com.smontiel.ferretera.admin.data.Constants;
import com.smontiel.ferretera.admin.data.SharedPrefs;

import io.reactivex.disposables.CompositeDisposable;

import static com.smontiel.ferretera.admin.utils.Preconditions.checkNotNull;

/**
 * Created by Salvador Montiel on 27/10/18.
 */
public class DashboardPresenter implements DashboardContract.Presenter {
    private final DashboardContract.View dashboardView;
    private final SharedPrefs prefs;
    private final CompositeDisposable compositeDisposable;

    public DashboardPresenter(DashboardContract.View dashboardView, SharedPrefs prefs) {
        this.dashboardView = checkNotNull(dashboardView);
        this.prefs = checkNotNull(prefs);
        this.compositeDisposable = new CompositeDisposable();
        dashboardView.setPresenter(this);
    }

    @Override
    public void logOut() {
        prefs.saveString(Constants.AUTH_TOKEN, "");
    }

    @Override
    public void subscribe() {}

    @Override
    public void unsubscribe() {
        if (compositeDisposable != null) compositeDisposable.clear();
    }
}
