package com.smontiel.ferretera.admin.features.dashboard;

import com.smontiel.ferretera.admin.BasePresenter;
import com.smontiel.ferretera.admin.BaseView;

/**
 * Created by Salvador Montiel on 27/10/18.
 */
public interface DashboardContract {
    interface View extends BaseView<Presenter> {

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void logOut();
    }
}
