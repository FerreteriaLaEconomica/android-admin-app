package com.smontiel.ferretera.admin.features.auth;

import com.smontiel.ferretera.admin.base.BasePresenter;
import com.smontiel.ferretera.admin.base.BaseView;
import com.smontiel.ferretera.admin.data.models.User;

/**
 * Created by Salvador Montiel on 29/10/18.
 */
public interface LoginContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void showInfoDialog(String message);

        void showProgressDialog();

        void hideProgressDialog();

        void onLoginSuccess(User user);
    }

    interface Presenter extends BasePresenter {

        void logIn(String email, String password);
    }
}
