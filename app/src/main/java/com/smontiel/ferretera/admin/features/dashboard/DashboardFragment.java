package com.smontiel.ferretera.admin.features.dashboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smontiel.ferretera.admin.R;

import static com.smontiel.ferretera.admin.utils.Preconditions.checkNotNull;

/**
 * Created by Salvador Montiel on 27/10/18.
 */
public class DashboardFragment extends Fragment implements DashboardContract.View {
    private DashboardContract.Presenter presenter;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dashboard_fragment, container, false);
        initializeViews(root);
        return root;
    }

    private void initializeViews(View view) {}

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
    public void setPresenter(DashboardContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
